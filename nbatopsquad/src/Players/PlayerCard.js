import React from "react";
import styles from "../Styles/PlayerCard.module.css";



function PlayerCard({image="https://www.eurobasket.com/photos/Jiao_Boqiao.jpg", name="Boquio Jiao", points, pos, value}){
    return(
        <>
            <div>
                <div className={`${styles.playerButtonCard}`}>
                    <img className="playerImage" src={image} alt={name}/>
                    <p className={styles.playerName}>{name}</p>
                    {pos && <p className={styles.playerPosition}>{pos}</p>}
                    <span className="playerPoints">{points}</span>
                    {value && <p className={styles.playerValue}>{value} <i class="fa-solid fa-star" style={{color: "var(--header)"}}></i></p>}
                </div>
            </div>
        </>
    );
}



export default PlayerCard

