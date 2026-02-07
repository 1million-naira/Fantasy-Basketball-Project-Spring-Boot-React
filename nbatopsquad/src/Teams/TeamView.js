import React, { useCallback, useContext } from "react";
import PlayerCard from "../Players/PlayerCard";
import styles from "../Styles/TeamView.module.css"
import { TeamContext } from "../Context/TeamUpdate";
import { useSelector } from "react-redux";

function TeamView(props){
    const {team, setTeam} = useContext(TeamContext)

    const teamState = useSelector((state) => state.team);
    // setTeam(teamState);

    const starters = teamState.filter(player => player.bench === false);
    const bench = teamState.filter(player => player.bench === true);
    return(
        <>
            <div className={styles.team}>
                <div className={styles.starters}>
                    <h3>STARTERS</h3>
                    <div>
                        {starters.map((player) => 
                        <PlayerCard key={player.id} name={player.name} image={player.image} points={player.points} pos={player.pos}></PlayerCard>
                        )}
                    </div>
                </div>
                <div className={styles.bench}>
                    <h3>BENCH</h3>
                    <div>
                        {bench.map((player) => 
                        <PlayerCard key={player.id} name={player.name} image={player.image} points={player.points} pos={player.pos}></PlayerCard>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default TeamView