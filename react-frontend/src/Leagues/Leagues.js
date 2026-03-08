import React, { useEffect, useState } from "react";
import { useNavigateBack } from "../Router/NavigateBack";
import CustomButton from "../components/CustomButton";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

function Leagues(){

    const navigateBack = useNavigateBack("/");
    const navigate = useNavigate();

    // const [leagues, setLeagues] = useState([
    //     {id: 1000, name: "TheBestLeague", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
    //     {id: 2000,name: "TheGoldLeague", type:"H2H", matchmaking:"Random", users: 40, created: false},
    //     {id: 3000, name: "FarmersLeague👨🏾‍🌾", type:"H2H", matchmaking:"Round Robin", users: 10, created: false},
    //     {id: 4000, name: "TheGreatLeague", type:"Roto", users: 24, created: false},
    //     {id: 5000,name: "TheGrandLine", type:"Roto", users: 15, created: true},
    //     {id: 6000, name: "Sharingan", type:"Roto", users: 18, created: false}
    // ])
    const [leagues, setLeagues] = useState([])
    const [RotoLeagues, setRotoLeagues] = useState(leagues.filter(league => league.type === "Roto"));
    const [H2HLeagues, setH2HLeagues] = useState(leagues.filter(league => league.type === "H2H"));
    const [createdLeagues, setCreatedLeagues] = useState(leagues.filter(league => league.created === true));

    useEffect(() => {
        axios.get("http://localhost:8080/api/fantasy/leagues/user")
        .then((response => {
            console.log(response.data);
            setLeagues(response.data);

            const h2h = response.data.filter(league => league.type === "H2H");
            const roto = response.data.filter(league => league.type === "Roto");
            const created = response.data.filter(league => league.created === true);

            setH2HLeagues(h2h);
            setRotoLeagues(roto);
            setCreatedLeagues(created);
            console.log("Leagues fetchded success");
        })).catch((error) => {
            console.log(error);
        });
    }, [])



    return (
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label="Go back"/>
            <div style={{padding: '5px 25px',}}>
                <div style={{backgroundColor: 'var(--secondaryBackground)', borderRadius: 12, marginTop: 16, padding: '24px'}}>
                    <div style={{display: 'flex', gap: 12, alignItems: 'center'}}>
                        <Link to='/leagues/create'><CustomButton label="Create League" size="lg"/></Link>
                        <span>OR</span>
                        <Link to='/leagues/join'><CustomButton label="Join League" size="lg"/></Link>
                    </div>
                    <h1>Leagues</h1>
                    <div>
                        <h3>Head To Head</h3>
                        <div style={{backgroundColor: '#201E1E', padding: '4px 10px', borderRadius: 12}}>
                            <table style={{width: '100%'}}>
                                <thead>
                                    <tr>
                                        <th>League</th>
                                        <th>Matchmaking</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {H2HLeagues.length > 0 ? H2HLeagues.map((league) => (
                                        <tr key={league.id}>
                                            <td style={{}}>{league.name}</td>
                                            <td>{league.matchmaking}</td>
                                            <td><CustomButton onClick={() => navigate(`/leagues/${league.id}`)} label='View League'/></td>
                                        </tr>
                                    )) : <tr><td colSpan="3" style={{fontWeight: 100, fontStyle: 'italic'}}>No leagues found. We recommend you join a few leagues</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div>
                        <h3>Rotisserie</h3>
                        <div style={{backgroundColor: '#201E1E', padding: '4px 10px', borderRadius: 12}}>
                            <table style={{width: '100%'}}>
                                <thead>
                                    <tr>
                                        <th>League</th>
                                        <th>Matchmaking</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {RotoLeagues.length > 0 ? RotoLeagues.map((league) => (
                                        <tr key={league.id}>
                                            <td style={{}}>{league.name}</td>
                                            <td>{league.matchmaking}</td>
                                            <td><CustomButton onClick={() => navigate(`/leagues/${league.id}`)} label='View League'/></td>
                                        </tr>
                                    )) : <tr><td colSpan="3" style={{fontWeight: 100, fontStyle: 'italic'}}>No leagues found. We recommend you join a few leagues</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div>
                        <h3>Your created Leagues</h3>
                        <div style={{backgroundColor: '#201E1E', padding: '4px 10px', borderRadius: 12}}>
                            <table style={{width: '100%'}}>
                                <thead>
                                    <tr>
                                        <th>League</th>
                                        <th>Matchmaking</th>
                                        <th>Code</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {createdLeagues.length > 0 ? createdLeagues.map((league) => (
                                        <tr key={league.id}>
                                            <td style={{}}>{league.name}</td>
                                            <td>{league.matchmaking ? league.matchmaking : 'N/A'}</td>
                                            <td>{league.code ? league.code : 'N/A'}</td>
                                            <td><CustomButton onClick={() => navigate(`/leagues/${league.id}`)} label='View League'/></td>
                                        </tr>
                                    )) : <tr><td colSpan="3" style={{fontWeight: 100, fontStyle: 'italic'}}>No leagues found. We recommend you create a few leagues</td></tr>}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )

}

export default Leagues;