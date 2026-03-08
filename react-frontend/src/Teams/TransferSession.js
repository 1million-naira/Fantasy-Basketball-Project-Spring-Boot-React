import React, { useContext, useEffect } from "react";
import { TeamContext } from "../Context/TeamUpdate";
import PlayerCard from "../Players/PlayerCard";
import styles from "../Styles/TeamView.module.css"
import { useSelector } from "react-redux";

function TransferSession(){
    const {transferSession, updateCurrentlySelected, setTransferSession} = useContext(TeamContext)

    const teamState = useSelector((state) => state.team);

    useEffect(() => {
        setTransferSession(teamState);
    }, [teamState])


    const starters = transferSession.filter(player => player.bench === false);
    const bench = transferSession.filter(player => player.bench === true);


    return(
        <>
            <div className={styles.team}>
                <div className={styles.starters}>
                    <h3>STARTERS</h3>
                    <div>
                        {starters.map((player, index) =>
                        <div key={player.id} onClick={() => updateCurrentlySelected(index)}>
                            <PlayerCard key={player.id} name={player.name} image={player.image} points={player.points} pos={player.pos} value={player.value}></PlayerCard>
                        </div> 
                        )}
                    </div>
                </div>
                <div className={styles.bench}>
                    <h3>BENCH</h3>
                    <div>
                        {bench.map((player, index) =>
                        <div key={player.id} onClick={() => updateCurrentlySelected(index + 5)}>
                            <PlayerCard key={player.id} name={player.name} image={player.image} points={player.points} pos={player.pos} value={player.value}></PlayerCard>
                        </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default TransferSession