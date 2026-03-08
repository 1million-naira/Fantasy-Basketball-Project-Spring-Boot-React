import React, { useContext, useEffect, useState } from "react";
import { TeamSelectionContext } from "../Context/TeamSelection";
import styles from "../Styles/ShowPlayers.module.css";
import Pagination from "../Pagination/Pagination";
import axios from "axios";
import CustomButton from "../components/CustomButton";

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
            <div style={{backgroundColor: '#232323', padding: '4px 10px', borderRadius: 12, display: 'flex', justifyContent: 'center'}}>
                <table style={{width: '100%'}}>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Position</th>
                            <th>Value</th>
                            <th>Add</th>
                        </tr>
                    </thead>
                    <tbody>
                        {players.length > 0 ? players.map((player) => (
                            <tr key={player.id}>
                                <td><img src={player.image} alt={player.name} style={{borderRadius: 50, width: 45, height: 50}}></img></td>
                                <td>{player.name}</td>
                                <td>{player.pos}</td>
                                <td>{player.value} <i class="fa-solid fa-star" style={{color: "var(--header)"}}></i></td>
                                <td><CustomButton onClick={() => addPlayer(player)} label='Add Player' size="sm"/></td>
                            </tr>
                        )) : <tr><td colSpan="5">No players found</td></tr>}
                    </tbody>
                </table>
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