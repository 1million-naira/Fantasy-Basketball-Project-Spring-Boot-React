import react, { useContext, useEffect, useRef, useState } from "react";
import styles from "../Styles/LeagueView.module.css"
import { TeamContext } from "../Context/TeamUpdate";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useAuth } from "../Context/AuthContext";
import { toast } from "react-toastify";
import { useNavigateBack } from "../Router/NavigateBack";

function LeagueView(){
    //Use effect to fetch the league by ID

    const {leagueId} = useParams();
    const navigate = useNavigate();
    const {token, userId} = useAuth();
    // console.log(leagueId);

    const stompClientRef = useRef(null);

    useEffect(()=>{

        axios.get(`http://localhost:8080/api/fantasy/leagues/${leagueId}`)
        .then((response) => {
            setLeagueInfo(response.data);
        })
        .catch((error) => {
            console.log(error);
        })

        axios.get(`http://localhost:8080/api/fantasy/leagues/${leagueId}/members`)
        .then((response) => {
            setLeagueUsers(response.data);
            console.log("Success");
        }).catch((error) => {
            console.log(error);
        });

        axios.get(`http://localhost:8080/api/fantasy/leagues/${leagueId}/messages`)
        .then((response) => {
            setMessages(response.data);
        })
        .catch((error) => {
            console.log(error);
        });

        axios.get(`http://localhost:8080/api/fantasy/leagues/${leagueId}/topPlayers`)
        .then((response) => {
            setTopPerformingPlayers(response.data);
        })
        .catch((error) => {
            console.log(error);
            setTopPerformingPlayers([]);
        })
    }, []);

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
                stompClient.subscribe(`/api/chat/${leagueId}`, (response) => {
                    // addNotification(JSON.parse(response.body));
                    // setNotifications([...notifications, JSON.parse(response.body)]);
                    // dispatch(addNotif(JSON.parse(response.body)));
                    console.log(JSON.parse(response.body));
                    const message = JSON.parse(response.body);
                    
                    if(message.userId !== parseInt(userId)){
                        setMessages(prevMessages => [...prevMessages, {...message, user:false}]);
                    }

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

    const [topPerformingPlayers, setTopPerformingPlayers] = useState([]);

    const [leagueUsers, setLeagueUsers] = useState([
        {id: 1000, pos: 1, team: "NikaFc", name: "Luffy", round: 30, total: 1827, teamId: 1},
        {id: 2344, pos: 2, team: "KOHFC" , name: "Zoro", round: 35, total: 1826, teamId: 2},
        {id: 3345, pos: 3, team: "Black Leg", name: "Sanji", round: 24, total: 1825, teamId: 3},
        {id: 4323, pos: 4, team: "Knight of the sea", name: "Jinbei", round: 50, total: 1824, teamId: 4},
        {id: 5245, pos: 5, team: "COLAAAA", name: "Franky",round: 37, total: 1823, teamId: 5},
        {id: 65677, pos: 6, team: "First Point", name: "Chopper", round: 36, total: 1822, teamId: 6},
        {id: 76777, pos: 7, team: "Soul King FC", name: "Brook", round: 36, total: 1821, teamId: 7}
    ])

    const [leagueInfo, setLeagueInfo] = useState(
        {id: 4845, name: "FarmersLeague👨🏾‍🌾", type:"H2H", matchmaking:"Round Robin", users: 10, created: false, timeUntilNextRound: "00:00:00"});


    const[messages, setMessages] = useState([
        {id: 1, userId: 5, name:"Zoro", leagueId: 2, message:"Luffy beat Kaido", user: false},
        {id: 2, userId: 1, name:"Sabo", leagueId: 2, message:"Luffy is top 5", user: true},
        {id: 3, userId: 2, name:"Akainu", leagueId: 2, message:"We will capture Luffy", user: false},
        {id: 4, userId: 3, name:"Garp", leagueId: 2, message:"You will not repeat what happened with Ace", user: false},
        {id: 5, userId: 2, name:"Akainu", leagueId: 2, message:"Oh yes I will Garp", user: false}
    ])


    const [currentChat, setCurrentChat] = useState("")

    const [reportConfirmation, setReportConfirmation] = useState(false);

    const toggleReportConfirmation = (index) => {
        setReportedMessageIndex(index);
        setReportConfirmation(!reportConfirmation);
    }

    const [reportedMessageIndex, setReportedMessageIndex] = useState(-1);

    const reportMessage = (id) => {
        if(reportDescription === ""){
            toast.warn("Please enter a description for the report", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true})
            return;
        }
        
        axios.post(`http://localhost:8080/api/reports/message/${id}`, {description: reportDescription})
        .then((response) => {
            const stompClient = stompClientRef.current;

            if(stompClient && stompClient.connected){
                console.log("Reporting message!")
                stompClient.publish({
                    destination: "/app/admin/report",
                    body: JSON.stringify(response.data)
                })
            } else{
                console.error('Stomp client is not connected');
            }
            console.log(response.data);
            toast.success("Message reported successfully!", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            
        })
        .catch((error) => {
            console.log(error);
            toast.error("Message could not be reported", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        });

        setReportDescription("");
        toggleReportConfirmation(-1);
    }

    const [reportDescription, setReportDescription] = useState("");


    const sendMessage = (e) => {
        axios.post(`http://localhost:8080/api/fantasy/leagues/${leagueId}/message`, {message:currentChat})
        .then((response) => {

            // setMessages([
            //     ...messages,
            //     response.data
            // ]);

            setMessages(prevMessages => [...prevMessages, response.data]);
            // console.log("Message sent");

            const stompClient = stompClientRef.current;

            if(stompClient && stompClient.connected){
                console.log("Sending message!")
                stompClient.publish({
                    destination: `/app/league-chat/${leagueId}`,
                    body: JSON.stringify(response.data)
                })
            } else{
                console.error('Stomp client is not connected');
            }
        })
        .catch((error) => {
            console.log(error);
            setMessages([
                ...messages,
                {id: messages.length+1, userId: 1, name: "Sabo", leagueId: 2, message:currentChat, user: true}
            ]);
        });

        setCurrentChat("");




    }

    const {team} = useContext(TeamContext)


    const navigateBack = useNavigateBack("/");


    return (
        <>
            <button className="goBackButton" onClick={navigateBack}>Go back</button>
            <div className={styles.leagueView}>
                <div className={styles.leaderboard}>
                    <h1>League Leaderboard</h1>
                    <div className={styles.leagueTable}>
                        <table>
                            <thead>
                                <tr>
                                    <th>League Position</th>
                                    <th>Team</th>
                                    <th>Points this round</th>
                                    <th>Total points</th>
                                    <th></th>
                                </tr>

                                {/* Sticky table header and scrollable body?*/}
                            </thead>
                            <tbody>
                                {
                                leagueUsers.map(leagueUser => 
                                <tr key={leagueUser.id}>
                                    <td>{leagueUser.pos}</td>
                                    <td className={styles.userInfo}>
                                        <p>{leagueUser.team}</p>
                                        <p>{leagueUser.name}</p>
                                    </td>
                                    <td>{leagueUser.round}</td>
                                    <td>{leagueUser.total}</td>
                                    <td>
                                        <button onClick={() => navigate(`/team/view/${leagueUser.teamId}`)}>View {leagueUser.name}'s team</button>
                                    </td>
                                </tr>
                                )
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className={styles.leagueContent}>
                    <div className={styles.leagueInfoContainer}>
                        <h1>League details</h1>
                        <div className={styles.leagueInfo}>
                            <div className={styles.info}>
                                <p>League Name</p>
                                <h6>{leagueInfo.name}</h6>
                            </div>

                            <div className={styles.info}>
                                <p>League Type</p>
                                <h6>{leagueInfo.type}</h6>
                            </div>

                            <div className={styles.info}>
                                <p>Matchmaking</p>
                                <h6>{leagueInfo.matchmaking}</h6>
                            </div>
                        
                            {
                            leagueInfo.timeUntilNextRound && 
                            <>
                            <div className={styles.info}>
                                <p>Time Until Next Round</p>
                                <h6>{leagueInfo.timeUntilNextRound}</h6>
                            </div>
                            </>
                            }
                            <div className={styles.info}>
                                <p>Number of users in league</p>
                                <h6>{leagueUsers.length}</h6>
                            </div>
                        </div>

                        <button>Leave this league</button>
                    </div>

                    <div className={styles.leagueContentSection}>
                        <h3>League Chat</h3>
                        <div className={styles.chat}>
                            <div className={styles.messages}>
                                {
                                messages.map((message, index)=>
                                <div key={message.id} className={`${styles.message} ${message.user ? styles.userMessages : styles.nonUserMessage}`}>
                                    <p className={styles.messageText}>{message.message}</p>
                                    <p className={styles.sender}>{message.name}</p>

                                    {
                                    !message.user &&
                                    <button onClick={() => toggleReportConfirmation(index)}><i className="fa-solid fa-flag"></i></button>
                                    }

                                    {
                                    (reportConfirmation && index === reportedMessageIndex) &&
                                    <>
                                    <div>
                                        <form>
                                            <label htmlFor="reportDescription">Report for: </label>
                                            <select id="reportDescription" name="report" onChange={(e) => setReportDescription(e.target.value)}>
                                                <option value="">Description</option>
                                                <option value="Bad Language">Bad Language</option>
                                                <option value="Cheating">Cheating</option>
                                                <option value="Other">Other</option>
                                            </select>
                                        </form>
                                        <p>Are you sure you want to report this message?</p>
                                        <div>
                                            <button onClick={() => reportMessage(message.id)}>Report</button>
                                            <button onClick={() => toggleReportConfirmation(-1)}>Cancel</button>
                                        </div>
                                    </div>
                                    </>
                                    }
                                    
                                </div>
                                )
                                }
                            </div>
                            <div className={styles.send}>
                                <textarea className="textBox" value={currentChat} onChange={(e) => setCurrentChat(e.target.value)}></textarea>
                                <button onClick={(e) => sendMessage(e)}>Send</button>
                            </div>
                        </div>
                    </div>
                    
                    <div className={`${styles.leagueContentSection} ${styles.topPerformersContainer}`}>
                        <h3>Top players in this league</h3>
                        <div className={styles.topPerformers}>
                        {
                        topPerformingPlayers.map(player=> 
                            <div key={player.id} className={styles.topPerformer}>
                                <img src={player.image} alt={player.name}></img>
                                <p>{player.name}</p>
                                <p>Fantasy Points:</p>
                                <p>{player.points}</p>
                                <button>View</button>
                            </div>
                        )
                        }
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default LeagueView;