import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { TeamContext } from "../Context/TeamUpdate";
import styles from "../Styles/Teams.module.css";
import PlayerCard from "../Players/PlayerCard";
import axios from "axios";
import { useNavigateBack } from "../Router/NavigateBack";

function Teams(){
    const {teamId} = useParams(); //Use effect to fetch the team of this id.
    const {team} = useContext(TeamContext);
    const [teamToView, setTeamToView] = useState(team);
    const [teamName, setTeamName] = useState("Team Name");
    const [totalPoints, setTotalPoints] = useState(0);



    useEffect(() => {
        axios.get(`http://localhost:8080/api/fantasy/team/${teamId}`)
        .then((response) => {
            setTeamName(response.data.name);
            setTotalPoints(response.data.totalPoints);
            setTeamToView(response.data.players);
        })
        .catch((error) => {
            console.log(error);
        });
    }, [])

    const starters = teamToView.filter(player => player.bench === false);
    const bench = teamToView.filter(player => player.bench === true);

    const navigateBack = useNavigateBack("/");

    return (
        <>
            <button className="goBackButton" onClick={navigateBack}>Go back</button>
            <div className={styles.teamContainer}>
                <h1>{teamName}</h1>
                <div className={styles.points}>
                    <h2>Total Points</h2>
                    <h2>{totalPoints}</h2>
                </div>
                <div className={styles.team}>
                    <div className={styles.starters}>
                        <h3>STARTERS</h3>
                        <div>
                            {starters.map((player) => 
                            <PlayerCard key={player.id} name={player.name} image={player.image} points={player.points}></PlayerCard>
                            )}
                        </div>
                    </div>
                    <div className={styles.bench}>
                        <h3>BENCH</h3>
                        <div>
                            {bench.map((player) => 
                            <PlayerCard key={player.id} name={player.name} image={player.image} points={player.points}></PlayerCard>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </>
    );

}
export default Teams;