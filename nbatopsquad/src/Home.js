import React, { useContext, useEffect, useRef, useState } from "react";
import TeamView from "./Teams/TeamView";
import styles from "./Home.module.css";
import { TeamContext } from "./Context/TeamUpdate";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Client, Stomp } from "@stomp/stompjs";
import { useAuth } from "./Context/AuthContext";
import { NotificationContext } from "./Context/NotificationContext";
import { useDispatch, useSelector } from "react-redux";
import { addNotif } from "./redux/reducer/notifsReducer";
import { clearTeam, updateTeam } from "./redux/reducer/teamReducer";
import { useWebSocketService } from "./WebSocket";
import { toast } from "react-toastify";
import PlayerFilter from "./Players/PlayerFilter";
import ShowTransferPlayers from "./Players/ShowTransferPlayers";
import ViewPlayers from "./Players/ViewPlayers";



function Home(){
    const {token, userId} = useAuth();
    // const {notifications, addNotification, setNotifications} = useContext(NotificationContext);
    const dispatch = useDispatch();

    // const webSocketUrl = "http://localhost:8080/ws";

    // const{connect, subscribe, send, unsubscribe, disconnect} = useWebSocketService(
    //     webSocketUrl,
    //     () => console.log("Connected!"),
    //     (error) => console.error("Websocket Error: ", error)
    // );


    // useEffect(() => {
    //     connect();
    //     subscribe("/user/api/notifications/league", (message) => {
    //         dispatch(addNotif(message));
    //     });

    //     return () => {
    //         unsubscribe("/user/api/notifications/league")
    //         disconnect();
    //     }
    // }, []);

    const [menuState, setMenuState] =  useState("Leagues");

    const toggleLeagues =  () => {
        setMenuState("Leagues");
    }

    const toggleMatches = () => {
        setMenuState("Matches");
    }

    const togglePlayers = () => {
        setMenuState("Players");
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
                stompClient.subscribe("/user/api/notifications/league", (response) => {
                    dispatch(addNotif(JSON.parse(response.body)));
                    toast(JSON.parse(response.body).message, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                });

                stompClient.subscribe(`/api/general/${userId}`, (response) => {
                    dispatch(addNotif(JSON.parse(response.body)));
                    toast(JSON.parse(response.body).message, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
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

    
    

    const [teamCreated, setTeamCreated] = useState(true)
    const {team, setTeam} = useContext(TeamContext)

    const [create, isCreate] = useState(false)
    const [join, isJoin] = useState(false)
    const [createFormInputs, setCreateFormInputs] = useState()
    const [H2HSelected, setH2HSelected] = useState(false)
    const [searchedLeagueCode, setSearchedLeagueCode] = useState("")

    const [headToHead, showHeadToHead] = useState(true)
    const [roto, showRoto] = useState(false)
    const [created, showCreated] = useState(false)
    const [found, showFoundLeagues] = useState(false)
    const [isPublic, showPublicLeagues] = useState(false)

    const [selectedProLeague, setSelectedProLeague] = useState("CBA")
    const [topPerformers, setTopPerformers] = useState([])



    const [leagues, setLeagues] = useState([
        {id: 1000, name: "TheBestLeague", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
        {id: 2000,name: "TheGoldLeague", type:"H2H", matchmaking:"Random", users: 40, created: false},
        {id: 3000, name: "FarmersLeague👨🏾‍🌾", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
        {id: 4000, name: "TheGreatLeague", type:"Roto", users: 24, created: false},
        {id: 5000,name: "TheGrandLine", type:"Roto", users: 15, created: true},
        {id: 6000, name: "Sharingan", type:"Roto", users: 18, created: false}
    ])

    // const [leagues, setLeagues] = useState([])


    const [filteredLeagues, setFilteredLeagues] = useState([]);

    const [foundLeagues, setFoundLeagues] = useState([
        {id: 7000, name: "TheGoldLeague", type:"H2H", matchmaking:"Random", users: 40},
        {id: 90000, name: "TheGreatLeague", type:"Roto", users: 24},
        {id: 7960, name: "TheGrandLine", type:"Roto", users: 15},
        {id: 58565, name: "Sharingan", type:"Roto", users: 18}
    ]);

    const [publicLeagues, setPublicLeagues] = useState([
        {id: 373, name: "TheGoldLeague", type:"H2H", matchmaking:"Random", users: 40},
        {id: 4845, name: "FarmersLeague👨🏾‍🌾", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
        {id: 56788, name: "TheGrandLine", type:"Roto", users: 15},
        {id: 134555, name: "TheBestLeague", type:"H2H", matchmaking:"Round Robin", users: 10, created: false}
    ]);


    const [matches, setMatches] = useState([
        {id: 1, home: "Luffy", away: "Aokiji", homeScore: 20, awayScore: 30, league: "TheBestLeague", userHome: true, ongoing: true},
        {id: 2, home: "Luffy", away: "Kizaru", homeScore: 0, awayScore: 0, league: "TheGrandLine", userHome: true, ongoing: false},
        {id: 3, home: "Kaido", away: "Luffy", homeScore: 20, awayScore: 10, league: "TheGrandLine", userHome: false, ongoing: true},
        {id: 4, home: "Luffy", away: "Gorosei", homeScore: 20, awayScore: 10, league: "TheGrandLine", userHome: true, ongoing: false, completed: true},
    ])

    // const [matches, setMatches] = useState([]);

    const [onGoing, setOngoing] = useState(true);
    const [completed, setCompleted] = useState(false);
    const [displayedMatches, setDisplayedMatches] = useState(matches.filter(match => match.ongoing));


    const [currentPos, setCurrentPos] = useState('');
    const [currentLeague, setCurrentLeague] = useState('');
    const[search, setSearch] = useState('');

    const [currentlySelectedLeague, setCurrentlySelectedLeague] = useState("cba");

    const [topPerformingPlayers, setTopPerformingplayers] = useState([]);

    const showOnGoingMatches = () => {
        setOngoing(true);
        setCompleted(false);
        setDisplayedMatches(matches.filter(match => match.ongoing));
    }

    const showUpcomingMatches = () => {
        setOngoing(false);
        setCompleted(false);
        setDisplayedMatches(matches.filter(match => !match.ongoing && !match.completed));
    }

    const showPastMatches = () => {
        setOngoing(false);
        setCompleted(true);
        setDisplayedMatches(matches.filter(match => match.completed));
    }


    useEffect(() => {
        axios.get("http://localhost:8080/api/fantasy/team")
        .then((response => {
            setTeamCreated(true);
            // setTeam(response.data.players);
            dispatch(updateTeam(response.data.players));
            console.log("Team fetched success");
        })).catch((error) => {
            // setTeamCreated(false);
            dispatch(updateTeam(team));
            console.log(error);
        });


        axios.get("http://localhost:8080/api/fantasy/leagues/user")
        .then((response => {
            setLeagues(response.data);
            setFilteredLeagues(leagues.filter(league => league.type==="H2H"));
            console.log("Leagues fetchded success");
        })).catch((error) => {
            console.log(error);
        });

        axios.get("http://localhost:8080/api/fantasy/leagues/h2h/user/matches")
        .then((response) => {
            setMatches(response.data);
        })
        .catch((error) => {
            console.log(error);
        });

        axios.get("http://localhost:8080/api/players", {params:{sortBy: "fantasyPoints", league: currentlySelectedLeague, direction:"desc"}})
        .then((response) => {
            setTopPerformingplayers(response.data.content);
        })
        .catch((error) => {
            console.log(error);
            setTopPerformingplayers([]);
        });
    }, []);


    useEffect(() => {
        axios.get("http://localhost:8080/api/players", {params:{sortBy: "fantasyPoints", league: currentlySelectedLeague, direction:"desc"}})
        .then((response) => {
            setTopPerformingplayers(response.data.content);
        })
        .catch((error) => {
            console.log(error);
            setTopPerformingplayers([]);
        });
    }, [currentlySelectedLeague]);


    const showCreateForm = () => {
        isCreate(true);
        isJoin(false);
        showPublicLeagues(false);
        showFoundLeagues(false);
    }

    const showJoinForm = () => {
        showFoundLeagues(false);
        isJoin(true);
        isCreate(false);
        showPublicLeagues(false);
        showFoundLeagues(false);
    }

    const showH2HLeagues = () => {
        showHeadToHead(true);
        showRoto(false);
        showCreated(false);
        setFilteredLeagues(leagues.filter(league => league.type==="H2H"));
    }

    const showRotoLeagues = () => {
        showRoto(true);
        showHeadToHead(false);
        showCreated(false);
        setFilteredLeagues(leagues.filter(league => league.type==="Roto"));
    }

    const showCreatedLeagues = () => {
        showCreated(true);
        showHeadToHead(false);
        showRoto(false);
        setFilteredLeagues(leagues.filter(league => league.created));
    }

    const displayPublicLeagues = () => {
        isJoin(false);
        isCreate(false);
        showPublicLeagues(true);
        showFoundLeagues(false);
    }
    
    const handleCreateFormChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;

        setCreateFormInputs(prev => ({...prev, [name]:value}))
    }

    const handleJoinFormChange = (event) => {
        setSearchedLeagueCode(event.target.value);
    }

    const submitCreateForm = (e) => {
        e.preventDefault();

        if(createFormInputs){
            if(!createFormInputs.name){
                console.log("Please enter league name");
                toast.warn("Please enter the name of the league you want to create", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                return;
            }

            if(H2HSelected && (!createFormInputs.type || !createFormInputs.matchmaking)){
                console.log("Please enter both league type and matchmaking to create a Head to Head League");
                toast.warn("Please enter both league type and matchmaking to create a Head to Head league", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                return;
            }

            if(!H2HSelected && !createFormInputs.type){
                console.log("Please enter league type");
                toast.warn("Please enter the league type to create a league", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                return;
            }

            if(!createFormInputs.access){
                console.log("Please enter league access level");
                toast.warn("Please enter the access level of the league you want to create", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                return;
            }

            // const stompClient = stompClientRef.current;
            axios.post("http://localhost:8080/api/fantasy/leagues", createFormInputs)
            .then((response) => {
                console.log("League created!");
                setLeagues([...leagues, response.data]);
                setCreateFormInputs();
                isCreate(false);
                setH2HSelected(false);

                //Publish message to websocket
                // send("/app/league-created", createFormInputs);
                const stompClient = stompClientRef.current;

                if(stompClient && stompClient.connected){
                    console.log("Sending league created!")
                    stompClient.publish({
                        destination: "/app/league-created",
                        body: JSON.stringify(response.data)
                    })
                } else{
                    console.error('Stomp client is not connected');
                }
            })
            .catch((error) => {
                console.log(error);
                setCreateFormInputs();
                isCreate(false);
                setH2HSelected(false);
            })
        } else{
            console.log("Please enter league creation details");
        }
    }

    const submitJoinForm = (e) => {
        e.preventDefault();
        //API request to find leagues by the code.

        axios.get("http://localhost:8080/api/fantasy/leagues", {params:{code: searchedLeagueCode}})
        .then((response) => {
            setFoundLeagues(response.data);
            showFoundLeagues(true);
        })
        .catch((error) => {
            setFoundLeagues([]);
            showFoundLeagues(true);
            console.log(error);
        })

        // console.log(searchedLeagueCode);
    }

    const joinLeague = (id, type, name, league) => {
        axios.post(`http://localhost:8080/api/fantasy/leagues/${id}/member`)
        .then((response) => {
            toast("You joined a new " + type + " league with name: " + name, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true})
            setLeagues(prev => [...prev, league]);
            showFoundLeagues(false);
        })
        .catch((error) => {
            if(error.response.status === 409){
                toast.error("You have already joined this league!", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true})
            }
        })
    }

    const navigate = useNavigate();





    return (
        <>
            <div className={styles.home}>
                <div className={styles.teamView}>
                    {teamCreated ?
                    <> 
                    <div className={styles.team}>
                        <TeamView team={team}/>
                    </div>
                    <div className={styles.teamOptions}>
                        <button onClick={() => navigate("/team/edit")}>Edit Team</button>
                        <button onClick={() => navigate("/transfers")}>Make Transfers</button>
                    </div>
                    </>
                    : <button onClick={() => navigate("/team/create")}>Create Your Team</button>
                    //Send a request to API to see if user has a team, teamCreated is then set to true
                    }
                </div>
                <div className={styles.menu}>
                    <h1>Menu</h1>

                    <div className={styles.menuButtons}>
                        <button onClick={() => toggleLeagues()}>Leagues</button>
                        <button onClick={() => toggleMatches()}>Your Matches</button>
                        <button onClick={() => togglePlayers()}>Players</button>
                    </div>


                    {
                    menuState === "Leagues" &&
                    <>
                        <div className={styles.leaguesMenu}>
                            <div className={styles.leagueButtons}>
                                <button onClick={() => showCreateForm()}>Create League</button>
                                <button onClick={() => showJoinForm()}>Join League</button>
                                <button onClick={() => displayPublicLeagues()}>View Public Leagues</button>
                            </div>
                            {
                            (create || join) &&
                            <div className={styles.leagueForms}>
                                { create &&
                                (
                                <>
                                <div className={styles.leagueForm}>
                                    <p>Create a League</p>
                                    <form>
                                        <div>
                                            <label htmlFor="leagueName">League Name</label>
                                            <input required type="text" id="leagueName" placeholder="League Name" name="name" onChange={(e) => handleCreateFormChange(e)}/>
                                        </div>
                                        <div>
                                            <label htmlFor="leagueType">League Type</label>
                                            <select id="leagueType" name="type" onChange={(e) => {e.target.value === "H2H" ? setH2HSelected(true) : setH2HSelected(false); handleCreateFormChange(e)}}>
                                                <option>Type</option>
                                                <option value="H2H">Head To Head</option>
                                                <option value="Roto">Rotisserie</option>
                                            </select>
                                        </div>
                                        {
                                        H2HSelected &&
                                        <div>
                                            <label htmlFor="matchmaking">Matchmaking</label>
                                            <select id="matchmaking" name="matchmaking" onChange={(e) => handleCreateFormChange(e)}>
                                                <option>Matchmaking</option>
                                                <option value="RR">Round Robin</option>
                                                <option value="Random">Random</option>
                                            </select>
                                        </div>
                                        }
                                        <div>
                                            <label htmlFor="access">Access</label>
                                            <select id="access" name="access" onChange={(e) => handleCreateFormChange(e)}>
                                                <option>Access</option>
                                                <option value="Public">Public</option>
                                                <option value="Private">Private</option>
                                            </select>
                                        </div>
                                        <button type="submit" onClick={(e) => submitCreateForm(e)}>Create</button>
                                    </form>
                                    <button onClick={() => {isCreate(false)}} className={styles.close}>&times;</button>   
                                </div>  
                                </>             
                                )
                                }

                                {
                                join &&
                                (
                                <>
                                <div className={styles.leagueForm}>
                                    <p>Join a League (Enter the league's code to join)</p>
                                    <form>
                                        <div>
                                            <label htmlFor="leagueCode">League Code</label>
                                            <input type="text" placeholder="League Code" onChange={(e) =>  handleJoinFormChange(e)}/>
                                        </div>
                                        <button type="submit" onClick={(e) => submitJoinForm(e)}>Find League</button>
                                    </form>
                                    <button onClick={() => {isJoin(false)}} className={styles.close}>&times;</button>
                                </div>
                                </>
                                ) 
                                }
                            </div>
                            }
                            {
                            found && 
                            <>
                            <div className={styles.foundLeagues}>
                                {
                                foundLeagues.length !== 0 ?
                                <table>
                                    <thead>
                                        <tr>
                                            <th>League Name</th>
                                            <th>League Type</th>
                                            <th>Matchmaking</th>
                                            <th>Users</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                        foundLeagues.map(league => 
                                        <tr key={league.id}>
                                            <td>{league.name}</td>
                                            <td>{league.type}</td>
                                            <td>{league.matchmaking ? league.matchmaking : "None"}</td>
                                            <td>{league.users}</td>
                                            <td><button onClick={() => joinLeague(league.id, league.type, league.name, league)}>Join League</button></td>
                                        </tr>
                                        )
                                        }
                                    </tbody>
                                </table> : 
                                <p>No leagues found by this code</p>
                                }
                                <button onClick={() => showFoundLeagues(false)} className={styles.close}>&times;</button>
                            </div>
                            </>
                            }


                            <div className={styles.publicLeagues}>
                                {
                                isPublic && publicLeagues.length !== 0 ? 
                                <>
                                <p>Public Leagues</p>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>League Name</th>
                                            <th>League Type</th>
                                            <th>Matchmaking</th>
                                            <th>Users</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {
                                        publicLeagues.map(league => 
                                        <tr key={league.id}>
                                            <td>{league.name}</td>
                                            <td>{league.type}</td>
                                            <td>{league.matchmaking ? league.matchmaking : "None"}</td>
                                            <td>{league.users}</td>
                                            <td><button>Join League</button></td>
                                        </tr>
                                        )
                                        }
                                    </tbody>
                                </table>
                                <button onClick={() => showPublicLeagues(false)} className={styles.close}>&times;</button>
                                </> : 
                                <></>
                                }
                            </div>

                            <div className={`${styles.yourLeagues} ${styles.menuSection}`}>
                                <h3>Your Leagues</h3>
                                <div className={styles.menuButtons}>
                                    <button onClick={() => showH2HLeagues()}>H2H</button>
                                    <button onClick={() => showRotoLeagues()}>Roto</button>
                                    <button onClick={() => showCreatedLeagues()}>Created</button>
                                </div>

                                <div>
                                    {headToHead ? <h4>Head To Head Leagues</h4> : <></>}
                                    {roto ? <h4>Rotisserie Leagues</h4> : <></>}
                                    {created ? <h4>Created Leagues</h4> : <></>}
                                    <table>
                                        {
                                        filteredLeagues.length !== 0 &&
                                        <thead>
                                            <tr>
                                                <th>League Name</th>
                                                <th>League Type</th>
                                                {(headToHead || created) && <th>Matchmaking</th>}
                                                <th>Users</th>
                                                <th></th>

                                                {created && <th></th>}
                                                
                                            </tr>
                                        </thead>
                                        }
                                        
                                        <tbody>
                                            {
                                            headToHead ?
                                            (filteredLeagues.map((league) => 
                                            <tr key={league.id}>
                                                <td>{league.name}</td>
                                                <td>{league.type}</td>
                                                <td>{league.matchmaking}</td>
                                                <td>{league.users}</td>
                                                <td><button onClick={() => navigate(`/league/${league.id}`)}>View League</button></td>
                                            </tr>
                                            )) :

                                            roto ? 
                                            (filteredLeagues.map((league) => 
                                                <tr key={league.id}>
                                                    <td>{league.name}</td>
                                                    <td>{league.type}</td>
                                                    <td>{league.users}</td>
                                                    <td><button onClick={() => navigate(`/league/${league.id}`)}>View League</button></td>
                                                </tr>
                                            )) :

                                            created ?
                                            (filteredLeagues.map(league => 
                                                <tr key={league.id}>
                                                <td>{league.name}</td>
                                                <td>{league.type}</td>
                                                <td>{league.matchmaking ? league.matchmaking : "None"}</td>
                                                <td>{league.users}</td>
                                                <td><button onClick={() => navigate(`/league/${league.id}`)}>View League</button></td>
                                                <td><button>Manage League</button></td>
                                            </tr>
                                            )
                                            ) :
                                
                                            <></>
                                            }
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        </>
                        }

                        {
                        menuState === "Matches" &&
                        <>
                        <div className={`${styles.userMatches} ${styles.menuSection}`}>
                            <h3>Head To Head Matches</h3>
                            <div className={styles.menuButtons}>
                                <button onClick={() => showOnGoingMatches()}>Ongoing</button>
                                <button onClick={() => showUpcomingMatches()}>Upcoming</button>
                                <button onClick={() => showPastMatches()}>Past</button>
                            </div>
                            {
                            onGoing ? 
                            <p>Ongoing Matches</p> :
                            !onGoing && !completed ?
                            <p>Upcoming Matches</p> :
                            completed ? 
                            <p>Past Matches</p> :
                            <></>
                            }
                            <div className={styles.matches}>
                                <div className={`${styles.match} ${styles.matchHeader}`}>
                                    <div className={styles.matchParticipant}>
                                        <p>Home</p>
                                    </div>
                                    <div className={styles.matchParticipant}>
                                    </div>
                                    <div className={styles.matchParticipant}>
                                        <p>Away</p>
                                    </div>
                                    <div className={styles.matchParticipant}>
                                        <p>League</p>
                                    </div>
                                    <div className={styles.matchParticipant}>
                                        <p>Result</p>
                                    </div>
                                </div>
                                {
                                displayedMatches.map(match => 
                                <div className={styles.match} key={match.id}>
                                    <div className={`${styles.matchParticipant} ${match.userHome ? styles.loggedInUser : null}`}>
                                        <p>{match.home}</p>
                                        <p>{match.homeScore}</p>
                                    </div>
                                    <div>
                                        <p>VS</p>
                                    </div>
                                    <div className={`${styles.matchParticipant} ${!match.userHome ? styles.loggedInUser : null}`}>
                                        <p>{match.away}</p>
                                        <p>{match.awayScore}</p>
                                    </div>
                                    <div className={styles.matchLeague}>
                                        <p>{match.league}</p>
                                    </div>
                                    <div className={styles.matchResult}>
                                        {match.userHome && match.homeScore > match.awayScore ? <p className={styles.matchWin}>W</p> : match.userHome && match.homeScore < match.awayScore ? <p className={styles.matchLose}>L</p> 
                                        : match.userHome && match.homeScore === match.awayScore ? <p className={styles.matchDraw}>D</p>  : null}

                                        {!match.userHome && match.homeScore > match.awayScore ? <p className={styles.matchLose}>L</p>  : !match.userHome && match.homeScore < match.awayScore ? <p className={styles.matchWin}>W</p> 
                                        : !match.userHome && match.homeScore === match.awayScore ? <p className={styles.matchDraw}>D</p>  : null}
                                    </div>
                                </div>
                                )
                                }
                            </div>
                        </div>
                        </>
                        }

                        {
                        menuState === "Players" &&
                        <>
                        <div className={styles.players}>
                            <h3>Players</h3>

                            <PlayerFilter setCurrentPos={setCurrentPos} setCurrentLeague={setCurrentLeague} setSearch={setSearch}/>
                            <ViewPlayers currentPos={currentPos} currentLeague={currentLeague} search={search}/>

                            <h4>Top performing players</h4>
                            <div className={styles.menuButtons}>
                                <button onClick={() => setCurrentlySelectedLeague("cba")}>CBA</button>
                                <button onClick={() => setCurrentlySelectedLeague("nba")}>NBA</button>
                                <button onClick={() => setCurrentlySelectedLeague("wnba")}>WNBA</button>
                                <button onClick={() => setCurrentlySelectedLeague("euro")}>Euro League</button>
                            </div>
                            <p className={styles.currentlySelectedLeague}>{currentlySelectedLeague.toUpperCase()}</p>
                            <div className={styles.topPerformers}>
                                {
                                topPerformingPlayers.length > 0 ?
                                topPerformingPlayers.map(player=> 
                                <div key={player.id} className={styles.topPerformer}>
                                    <img src={player.image} alt={player.name}></img>
                                    <p>{player.name}</p>
                                    <p>Fantasy Points: {player.points}</p>
                                    <button>View {player.name}'s profile</button>
                                </div>) :
                                <p>No players</p>
                                }
                            </div>
                        </div>
                    </>
                    }
                </div>
            </div>
        </>
    );

}

export default Home