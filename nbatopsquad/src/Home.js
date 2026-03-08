import React, { useContext, useEffect, useRef, useState } from "react";
import TeamView from "./Teams/TeamView";
import styles from "./Home.module.css";
import { TeamContext } from "./Context/TeamUpdate";
import { Link, useNavigate } from "react-router-dom";
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
import CustomButton from "./components/CustomButton";



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

    
    

    const [teamCreated, setTeamCreated] = useState(false)
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

    const [user, setUser] = useState({budget: 0, points: 0, teamValue: 0, globalRank: 0, username: 'User'});

    useEffect(() => {
        axios.get("http://localhost:8080/api/users/user")
        .then((response) => {
            console.log(response.data);
            setUser(response.data);
        }).catch((error) => {
            console.log(error);
        }); 

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
    }, []);



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
        <div style={{padding: '5px 25px'}}>
            <h1>Welcome to TopSquad</h1>
            <div style={{display: 'flex', flexDirection: 'row-reverse', padding: '0 10px', justifyContent: 'space-between', gap: 12}}>
                <div style={{width: '55%', backgroundColor: '#232323', borderRadius: 12, padding: '8px 16px'}}>
                    {teamCreated ?
                    <> 
                    <div style={{width: '100%', display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                        <h2>Team Name</h2>
                        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', gap: 12}}>
                            <CustomButton onClick={() => navigate("/team/edit")} label='Edit Team'/>
                            <CustomButton onClick={() => navigate("/transfers")} label='Make Transfers'/>
                        </div>
                    </div>
                    <div style={{display: 'flex', justifyContent: 'space-evenly', alignItems: 'center'}}>
                        {/* <div style={{textAlign: 'center'}}>
                            <h2 s>143</h2>
                            <p>Highest</p>
                        </div> */}
                        <div style={{textAlign: 'center'}}>
                            <h1>{user.points}</h1>
                            <p>Points</p>
                        </div>
                        <div style={{textAlign: 'center'}}>
                            <h1>{user.globalRank}</h1>
                            <p>Rank</p>
                        </div>
                    </div>
                    <div style={{marginTop: '10px'}}>
                        <TeamView team={team}/>
                    </div>
                    </>
                    :
                    <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%'}}>
                        <CustomButton onClick={() => navigate("/team/create")} label="Create Your Team" size="lg"/>
                    </div>
                    }
                </div>
                <div style={{display: 'flex', flexDirection: 'column', padding: '8px 16px', alignItems: 'flex-start', justifyContent: 'flex-start', width: '40%', backgroundColor: '#232323', borderRadius: 12}}>
                    <div style={{borderBottom: `1px solid rgba(255, 255, 255, 0.2)`, width: '100%'}}>
                        <h1>Team Name</h1>
                    </div>
                    <div style={{borderBottom: `1px solid rgba(255, 255, 255, 0.2)`, width: '100%'}}>
                        <h2 style={{}}>Points & Rankings</h2>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <p>Total points</p>
                            <p>{user.points}</p>
                        </div>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <p>Number of global users</p>
                            <p>0</p>
                        </div>
                    </div>
                    <div style={{borderBottom: `1px solid rgba(255, 255, 255, 0.2)`, width: '100%', padding: '10px 0'}}>
                        <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                            <h2>My Leagues</h2>
                            <Link to={"/leagues"}><CustomButton label="Create/Join Leagues" icon={<i style={{fontSize: '0.7em'}} class="fa-solid fa-chevron-right"></i>}/></Link>
                        </div>
                        <div>
                            <h3>Head To Head</h3>
                        </div>
                        <div>
                            <h3>Rotisserie</h3>
                        </div>
                        <div>
                            <h3>Your Created Leagues</h3>
                        </div>
                    </div>

                    <div style={{borderBottom: `1px solid rgba(255, 255, 255, 0.2)`, width: '100%', padding: '10px 0'}}>
                        <h2 style={{}}>Transfers/Finances</h2>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <p>Team value</p>
                            <p>{user.teamValue} <i class="fa-solid fa-star" style={{color: "var(--header)"}}></i></p>
                        </div>
                        <div style={{display: 'flex', justifyContent: 'space-between'}}>
                            <p>Budget</p>
                            <p>{user.budget} <i class="fa-solid fa-star" style={{color: "var(--header)"}}></i></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Home