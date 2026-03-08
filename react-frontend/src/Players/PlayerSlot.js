import React, { useContext } from "react";
import { TeamSelectionContext } from "../Context/TeamSelection";
import styles from "../Styles/PlayerCard.module.css";
import CustomButton from "../components/CustomButton";

function PlayerSlot({player}){

    const {removePlayer} = useContext(TeamSelectionContext);

    return (
        <>
            {
            player.map(player =>
            <div key={player.id} className={styles.playerButtonCard}>
                <img src={player.image} alt={player.name}></img>
                <p className={styles.playerName}>{player.name}</p>
                <p>{player.pos}</p>
                <CustomButton onClick={() => removePlayer(player)} label='Deselect'/>
            </div>
            )
            }
        </>
    );

}

export default PlayerSlot;