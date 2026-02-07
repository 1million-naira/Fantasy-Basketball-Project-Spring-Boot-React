import React, { useEffect, useState } from "react";
import styles from "../Styles/ShowPlayers.module.css"
import { TeamContext } from "../Context/TeamUpdate";
import axios from "axios";
import Pagination from "../Pagination/Pagination";


function ViewPlayers({currentPos, currentLeague, search}){
    const [players, setPlayers] = useState([{id: 10, image: "https://www.eurobasket.com/photos/Jiao_Boqiao.jpg", name: "Boqiao Jiao", pos: "SG"}, {id: 22, image: "https://www.eurobasket.com/photos/Huang_Yichao_1.jpg", name: "Yichao Huang", pos: "PF"}])

    const[currentPage, setCurrentPage] = useState(1)
    const [playersPerPage] = useState(10)
    const [totalPlayers, setTotalPlayers] = useState(50)

    const playerFilter = () => {
        axios.get("http://localhost:8080/api/transfers", {params:{page: currentPage-1, position: currentPos, league: currentLeague, search: search}})
        .then((response => {
            setPlayers(response.data.content);
            setTotalPlayers(response.data.totalElements);
        }))
        .catch((error) => {
            console.log(error);
        });
    }

    const getPlayers = () =>{
        setCurrentPage(1);
        playerFilter();
    }

    useEffect(() => {
        playerFilter();
    }, [currentPage])

    return(
        <>
            <button type="submit" onClick={() => getPlayers()} className={styles.getPlayers}>Get Players</button>
            <div className={styles.players}>
                <div className={`${styles.player} ${styles.playerHeader}`}>
                    <span className={styles.imgHeader}></span>
                    <p>Player</p>
                    <p>Position</p>
                    <span className={styles.buttonHeader}></span>
                </div>
                {players.map((player) => 
                <div key={player.id} className={styles.player}>
                    <img src={player.image} alt={player.name}></img>
                    <p>{player.name}</p>
                    <p>{player.pos}</p>
                    <button onClick={() => console.log("Player view")}>View Player</button>
                </div>
                )}
            </div>

            <div className={styles.showPlayerPage}>
                <Pagination 
                total={totalPlayers} 
                currentPage={currentPage} 
                contentPerPage={playersPerPage} 
                paginate={setCurrentPage}
                />
            </div>
        </>
    );
}

export default ViewPlayers