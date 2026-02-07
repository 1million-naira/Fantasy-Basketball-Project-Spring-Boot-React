import React, { useContext } from "react";
import { TeamSelectionContext } from "../Context/TeamSelection";
import styles from "../Styles/PlayerSlot.module.css";

function PlayerSlot({player}){

    const {removePlayer} = useContext(TeamSelectionContext);

    return (
        <>
            {
            player.map(player =>
            <div key={player.id} className={styles.playerSlot}>
                <img src={player.image} alt={player.name}></img>
                <p className={styles.playerName}>{player.name}</p>
                <p>{player.pos}</p>
                <button onClick={() => removePlayer(player)}>Deselect</button>
            </div>
            )
            }
        </>
    );

}

export default PlayerSlot;