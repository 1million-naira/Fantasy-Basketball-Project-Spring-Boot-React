import React, { useEffect, useRef, useState } from "react";
import styles from '../Styles/AdminHome.module.css'
import Pagination from "../Pagination/Pagination";
import axios from "axios";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useAuth } from "../Context/AuthContext";
import { toast } from "react-toastify";
import Reportview from "./ReportView";
import { useNavigate } from "react-router-dom";

function AdminHome (){
    const {token} = useAuth();
    const navigate = useNavigate();

    const [status, setStatus] = useState({});

    const [reports, setReports] = useState([
        {id: 1000, user: 2, username: "Luffy", description: "Bad Language", against: 3, againstName: "Kizaru", message: "Shush", resolved: false},
        {id: 25566, user: 2, username: "Luffy", description: "Cheating", against: 8, againstName: "Aokiji", message: "Mate", resolved: false},
        {id: 334556, user: 5, username: "Sabo", description: "Bad Language", against: 9, againstName: "Gorosei", message: "Bad", removal: true, resolved: true}
    ])

    const [reportViewVisible, setReportViewVisible] = useState(false);
    const [currentlySelectedReport, setCurrentlySelectedReport] = useState(null);

    const toggleReportView = (report) => {
        setReportViewVisible(!reportViewVisible);
        setCurrentlySelectedReport(report);
    }

    // const [reports, setReports] = useState([]);

    const [displayedReports, setDisplayedReports] = useState([]);
    const [displayedReportState, setDisplayedReportState] = useState("Pending");
    const [isTransferMarketOpen, setTransferMarketStatus] = useState(false);

    // const displayAllReports = () => {
    //     setDisplayedReports(reports);
    //     setDisplayedReportState("All");
    // }

    const displayPendingReports = () =>{
        setDisplayedReports(reports.filter(report => !report.resolved));
        setDisplayedReportState("Pending");
    }

    const displayResolvedReports = () =>{
        setDisplayedReports(reports.filter(report => report.resolved));
        setDisplayedReportState("Resolved");
    }


    const [leagues, setLeagues] = useState([
        {id: 1, name: "TheBestLeague", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
        {id: 2,name: "TheGoldLeague", type:"H2H", matchmaking:"Random", users: 40, created: false},
        {id: 3, name: "FarmersLeague👨🏾‍🌾", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
        {id: 4, name: "TheGreatLeague", type:"Roto", users: 24, created: false},
        {id: 5,name: "TheGrandLine", type:"Roto", users: 15, created: true},
        {id: 6, name: "Sharingan", type:"Roto", users: 18, created: false}
    ])

    const [currentReportPage, setCurrentReportPage] = useState(1);
    const [currentLeaguePage, setCurrentLeaguePage] = useState(1);
    const [currentUserPage, setCurrentUserPage] = useState(1);

    const [currentReportTotal, setCurrentReportTotal] = useState(10);
    const [currentLeagueTotal, setCurrentLeagueTotal] = useState(10);
    const [currentUserTotal, setCurrentUserTotal] = useState(10);

    const [contentPerPage] = useState(5);



    const [headToHeadToggle, setHeadToHeadToggle] = useState(true);

    const updateLeagueToggle = () => {
        setHeadToHeadToggle(!headToHeadToggle);
    }

    const [users, setUsers] = useState([
        {id: 1, email: "Luffy@gmail.com", username: "Luffy"}
    ])

    const [isUsers, showUsers] = useState(true)
    const [isLeagues, showLeagues] = useState(false)

    const [reportByUserSearch, setReportByUserSearch] = useState("");
    const [userSearch, setUserSearch] = useState("");
    const [leagueSearch, setLeagueSearch] = useState("");

    const searchReport = (e) => {
        e.preventDefault();
        console.log(reportByUserSearch);
    }

    const searchUser = (e) => {
        e.preventDefault();
        console.log(userSearch);
    }

    const searchLeague = (e) => {
        e.preventDefault();
        console.log(leagueSearch);
    }


    const displayUsers = () =>{
        showUsers(true);
        showLeagues(false);
    }

    const displayLeagues = () =>{
        showUsers(false);
        showLeagues(true);
    }

    const loadBoxScores = () => {
        axios.post("http://localhost:8080/api/admin/boxscores")
        .then((response) => {
            console.log(response.data);
            toast.success(response.data, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        })
        .catch((error) => {
            console.log(error);
            toast.error("Box scores could not be read", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        })
    }

    const updateLeagues = () => {
        axios.put("http://localhost:8080/api/admin/leagues")
        .then((response) => {
            console.log(response.data);
            toast.success(response.data, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        })
        .catch((error) => {
            console.log(error);
            toast.error("Leagues could not be updated", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        })
    }

    useEffect(() => {
        axios.get("http://localhost:8080/api/admin/leagues", {params:{page: currentLeaguePage-1, size: contentPerPage, search: leagueSearch}})
        .then((response) => {
            setCurrentLeagueTotal(response.data.totalElements);
            setLeagues(response.data.content);
        })
        .catch((error) => {
            console.log(error);
        })
    }, [currentLeaguePage, leagueSearch])


    useEffect(() => {
        axios.get("http://localhost:8080/api/admin/users", {params:{page: currentUserPage-1, size: contentPerPage, search: userSearch}})
        .then((response) => {
            setCurrentUserTotal(response.data.totalElements);
            setUsers(response.data.content);
        })
        .catch((error) => {
            console.log(error);
        })
    }, [currentUserPage, userSearch])

    useEffect(() => {
        axios.get("http://localhost:8080/api/admin/reports", {params:{page: currentReportPage-1, size: contentPerPage, reportStatus: displayedReportState, search: reportByUserSearch}})
        .then((response) => {
            // console.log(response.data.content);

            if(reportByUserSearch !== ""){
                setDisplayedReportState("");
            }
            setCurrentReportTotal(response.data.totalElements);
            setReports(response.data.content);
        })
        .catch((error) => {
            console.log(error);
        })
    }, [currentReportPage, reportByUserSearch, displayedReportState])


    useEffect(() => {
        axios.get("http://localhost:8080/api/settings/transferMarket")
        .then((response) => {
            console.log("Transfer market open? " + response.data);
            if(response.data === true){
                setTransferMarketStatus(true);
            } else{
                setTransferMarketStatus(false);
            }
        })
        .catch((error) => {
            console.log(error);
        })

        axios.get("http://localhost:8080/api/admin/status")
        .then((response) => {
            console.log(response.data);
            setStatus(response.data);
        })
        .catch((error) => {
            console.log(error);
        });
    }, []);


    const updateTransferMarket = (status) => {
        axios.put("http://localhost:8080/api/admin/transferMarket", null, {params:{status: status}})
        .then((response) => {
            toast.success(response.data, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            if(status === "open"){
                setTransferMarketStatus(true);
            } else if(status === "closed"){
                setTransferMarketStatus(false);
            }
        })
        .catch((error) => {
            console.log(error);
            toast.error("The transfer market could not be updated", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        })
    }


    const stompClientRef = useRef(null);
    useEffect(() => {
        const socket = new SockJS("http://localhost:8080/ws");
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (str) => {
                console.log(str);
            },
            connectHeaders: {Authorization: `Bearer ${token}`},
            onConnect: () => {
                console.log("Connected to Websocket");
                stompClient.subscribe("/api/admin", (response) => {
                    console.log(JSON.parse(response.body));
                    setReports(prevReports => [...prevReports, JSON.parse(response.body)]);

                    if(displayedReportState === "Pending"){
                        setDisplayedReports(prev => [...prev, JSON.parse(response.body)]);
                    }
                    toast("New report received", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                });
            },
            onStompError: (frame) => {
                console.error("Broker reported error: " + frame.headers["message"]);
                console.error('Additional details: ' + frame.body);
            }
        });

        stompClient.activate();
        stompClientRef.current = stompClient;

        return () => {
            stompClient.deactivate();
        };
    }, []);



    


    return (
        <>
        <h1>Admin Home</h1>
        <div className={styles.appStatus}>
            <div>
                <p>Active Users: <span>{status.userCount}</span></p>
            </div>
            <div>
                <p>Active Leagues: <span>{status.leagueCount}</span></p>
            </div>
            <div>
                <p>Reports: <span>{status.reportsCount}</span></p>
            </div>
            <div>
                <p>Transfer Market Status: {isTransferMarketOpen ? "OPEN" : "CLOSED"}</p>
            </div>
        </div>

        <div className={styles.menuContent}>
            <div className={styles.moderate}>
                <div className={styles.reports}>
                    <h3>Reports</h3>
                    <button onClick={() => displayPendingReports()}>Pending</button>
                    <button onClick={() => displayResolvedReports()}>Resolved</button>
                    {/* <button onClick={() => displayAllReports()}>All</button> */}
                    <div>
                        <form>
                            <label htmlFor="searchReport">Search for a report by user ID</label>
                            <input id="searchUser" type="text" placeholder="Enter the user's ID" onChange={(e) => setReportByUserSearch(e.target.value)}></input>
                            {/* <div>
                                <button type="submit" onClick={(e) => searchReport(e)}>Search</button>
                            </div> */}
                        </form>

                        {displayedReportState === "Pending" && <h3>Pending reports</h3>}
                        {displayedReportState === "Resolved" && <h3>Resolved reports</h3>}

                        
                        {
                        reports.length !== 0 ? 
                        <>
                    
                        <table>
                            <thead>
                                <tr>
                                    <th>Reported By</th>
                                    <th>Reported Against</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                reports.map((report, index) =>
                                <tr key={report.id}>
                                    <td>{report.username}</td>
                                    <td>{report.againstName}</td>
                                    <td>{report.description}</td>
                                    <td><button onClick={() => toggleReportView(report)}>View Report</button></td>
                                </tr>
                                )
                                }
                            </tbody>
                        </table>
                        {
                        reportViewVisible && currentlySelectedReport &&
                        <Reportview report={currentlySelectedReport} closeReportView={toggleReportView} socketRef={stompClientRef.current}/>
                        }
                        <Pagination total={currentReportTotal} 
                        contentPerPage={contentPerPage} 
                        paginate={setCurrentReportPage}/>
                        </> :
                        <p>No available reports</p>
                        }
                    </div>
                </div>
                <div className={styles.usersAndLeagues}>
                    <button onClick={() => displayUsers()}>Users</button>
                    <button onClick={() => displayLeagues()}>Leagues</button>
                    <div className={styles.userAndLeagueContent}>
                        {
                            isUsers ? 
                            <>
                            <h3>Users</h3>
                            <form>
                                <label htmlFor="searchUser">Search for a user</label>
                                <input id="searchUser" type="text" placeholder="Enter the user's ID" onChange={(e) => setUserSearch(e.target.value)}></input>
                                {/* <div>
                                    <button type="submit" onClick={(e) => searchUser(e)}>Search</button>
                                </div> */}
                            </form>
                            {
                            users.length !== 0 ?
                            <>
                            <table>
                                <thead>
                                    <tr>
                                        <th>User ID</th>
                                        <th>Email</th>
                                        <th>Username</th>
                                    
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                    users.map(user =>
                                    <tr key={user.id}>
                                        <td>{user.id}</td>
                                        <td>{user.email}</td>
                                        <td>{user.username}</td>
                                        <td><button onClick={() => {
                                            axios.delete(`http://localhost:8080/api/admin/user/${user.id}`)
                                            .then((response) => {
                                                console.log(response.data);
                                                setUsers(users.filter(u => u.id !== user.id));
                                            })
                                            .catch((error) => {
                                                console.log(error);
                                            });
                                        }}>Remove User</button></td>
                                    </tr>
                                    )
                                    }
                                </tbody>
                            </table>
                            <Pagination total={currentUserTotal} 
                            contentPerPage={contentPerPage} 
                            paginate={setCurrentUserPage}/>
                            </> :
                            <p>No users</p>
                            }
                            </> :
                            isLeagues ?
                            <>
                            <h3>Leagues</h3>
                            {/* <button onClick={() => updateLeagueToggle()}>Head To Head</button>
                            <button onClick={() => updateLeagueToggle()}>Rotisserie</button>

                            <h4>{headToHeadToggle ? "Head To Head" : "Rotisserie"}</h4> */}
                            <form>
                                <label htmlFor="searchLeague">Search for a League</label>
                                <input id="searchLeague" type="text" placeholder="Enter the league's ID" onChange={(e) => setLeagueSearch(e.target.value)}></input>
                                {/* <div>
                                    <button type="submit" onClick={(e) => searchLeague(e)}>Search</button>
                                </div> */}
                            </form>
                            {
                            leagues.length !==0 ?
                            <>
                            <table>
                                <thead>
                                    <tr>
                                        <th>League ID</th>
                                        <th>League Name</th>
                                        <th>League Type</th>
                                        <th>Matchmaking</th>
                                        <th>Users</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                    leagues.map(league =>
                                    <tr key={league.id}>
                                        <td>{league.id}</td>
                                        <td>{league.name}</td>
                                        <td>{league.type}</td>
                                        <td>{league.matchmaking ? league.matchmaking : "None"}</td>
                                        <td>{league.users}</td>
                                        <td><button onClick={() => navigate(`/league/${league.id}`)}>View League</button></td>
                                        <td><button onClick={() => {
                                            axios.delete(`http://localhost:8080/api/admin/league/${league.id}`)
                                            .then((response) => {
                                                console.log(response.data);
                                                setLeagues(leagues.filter(l => l.id !== league.id));
                                            })
                                            .catch((error) => {
                                                console.log(error);
                                            });
                                        }}>Remove League</button></td>
                                    </tr>
                                    )
                                    }
                                </tbody>
                            </table>
                            <Pagination total={currentLeagueTotal} 
                            contentPerPage={contentPerPage} 
                            paginate={setCurrentLeaguePage}/>
                            </> :
                            <p>No leagues</p>
                            }
                            </> :
                            <></>
                        }
                    </div>
                </div>
            </div>
            <div className={styles.updates}>
                <div className={styles.updateOptions}>
                    <button onClick={() => loadBoxScores()}>Load In Box Scores</button>
                    <button onClick={() => updateLeagues()}>Update League Rounds</button>

                    {
                    isTransferMarketOpen ? 
                    <button onClick={() => updateTransferMarket("closed")}>Close Transfer Market</button> : 
                    <button onClick={() => updateTransferMarket("open")}>Open Transfer Market</button>
                    }
                </div>
                <div className={styles.contact}>
                    <button>Contact the developers</button>
                </div>
            </div>
        </div>
        </>
    );
}

export default AdminHome;