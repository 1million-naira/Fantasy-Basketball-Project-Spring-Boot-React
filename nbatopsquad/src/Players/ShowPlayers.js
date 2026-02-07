import React, { useContext, useEffect, useState } from "react";
import { TeamSelectionContext } from "../Context/TeamSelection";
import styles from "../Styles/ShowPlayers.module.css";
import Pagination from "../Pagination/Pagination";
import axios from "axios";

function ShowPlayers({pos, selectedLeague}){

    const {addPlayer} = useContext(TeamSelectionContext)
    const[players, setPlayers] = useState([
        {id: 1, image: "https://www.eurobasket.com/photos/Chang_Ling_2.jpg", name: "Chang Ling", pos: 'C', bench:false, value: 1}, 
        {id: 2, image: "https://www.eurobasket.com/photos/Fang_Shuo.jpg", name: "Fang Shuo", pos: 'PG', bench:false, value: 1},
        {id: 3, image: "https://www.eurobasket.com/photos/Chen_Guohao.jpg", name: "Chen Guohao", pos: 'SF', bench:false, value: 1},
        {id: 4, image: "https://www.eurobasket.com/photos/Linjian_Chen_1.jpg", name: "Chen Linjian", pos: 'SG', bench:false, value: 1},
        {id: 5, image: "https://www.eurobasket.com/photos/Chen_Peidong.jpg", name: "Chen Peidong", pos: 'PF', bench:false, value: 1},
        {id: 6, image: "https://www.eurobasket.com/photos/Cheng_Shuaipeng_1.jpg", name: "Shuaipeng Cheng", pos: 'SF', bench:true , value: 1},
        {id: 7, image: "https://www.eurobasket.com/photos/Cui_Jinming_1.jpg", name: "Jinming Cui", pos: 'PG', bench:true, value: 1},
        {id: 8, image: "https://www.eurobasket.com/photos/Dai_Hao.jpg", name: "Hao Dai", pos: 'SG', bench:true, value: 1}
    ])

    const[currentPage, setCurrentPage] = useState(1)
    const [playersPerPage] = useState(10)
    const [totalPlayers, setTotalPlayers] = useState(50)
    const [prevPos, setPrevPos] = useState(pos)

    //Use Effect hook to make call to API to get players based on position and league props.

    useEffect(()=>{
        if(prevPos !== pos){
            setCurrentPage(1);
        }
        axios.get("http://localhost:8080/api/transfers", {params:{page: currentPage-1,position: pos, league: selectedLeague}})
        .then((response => {
            setTotalPlayers(response.data.totalElements);
            setPlayers(response.data.content);
            setPrevPos(pos);
        }))
        .catch((error) => {
            console.log(error);
        })
    }, [currentPage, pos, selectedLeague])

    return (
        <>
            <p>Select Your Player</p>
            <div className={styles.players}>
                {players.map(player => 
                <div key={player.id} className={styles.player}>
                    <img src={player.image} alt={player.name}></img>
                    <p>{player.name}</p>
                    <p>{player.pos}</p>
                    <p>{player.value*1000000}</p>
                    <button onClick={() => addPlayer(player)}>Select Player</button>
                </div>
                )}
            </div>
            
            <Pagination 
            total={totalPlayers} 
            currentPage={currentPage} 
            contentPerPage={playersPerPage} 
            paginate={setCurrentPage}
            className={styles.showPlayerPage}
            />
        </>
    );

}

export default ShowPlayers