import React, { useContext, useEffect, useState } from "react";
import styles from "../Styles/ShowPlayers.module.css"
import { TeamContext } from "../Context/TeamUpdate";
import axios from "axios";
import Pagination from "../Pagination/Pagination";
import CustomButton from "../components/CustomButton";


function ShowTransferPlayers({currentPos, currentLeague, search}){
    const {transferPlayer, currentlySelected} = useContext(TeamContext)
    const [players, setPlayers] = useState([{id: 10, image: "https://www.eurobasket.com/photos/Jiao_Boqiao.jpg", name: "Boqiao Jiao", pos: "SG", value: 1}, {id: 22, image: "https://www.eurobasket.com/photos/Huang_Yichao_1.jpg", name: "Yichao Huang", pos: "PF", value: 1}])

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
        <div>
            <div style={{display: 'flex', justifyContent: 'center', marginTop: 12}}>
                <CustomButton onClick={() => getPlayers()} label='Get Players'/>
            </div>
            <div style={{backgroundColor: '#232323', padding: '4px 10px', marginTop: 12, borderRadius: 12}}>
                <table>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Position</th>
                            <th>Value</th>
                            <th>Transfer</th>
                        </tr>
                    </thead>
                    <tbody>
                        {players.length > 0 ? players.map((player) => (
                            <tr key={player.id}>
                                <td><img src={player.image} alt={player.name} style={{borderRadius: 50, width: 45, height: 50}}></img></td>
                                <td>{player.name}</td>
                                <td>{player.pos}</td>
                                <td>{player.value} <i class="fa-solid fa-star" style={{color: "var(--header)"}}></i></td>
                                <td><CustomButton onClick={() => transferPlayer(player, currentlySelected)} label='Transfer Player' size="sm"/></td>
                            </tr>
                        )) : <tr><td colSpan="5">No players found</td></tr>}
                    </tbody>
                </table>
            </div>

            <div className={styles.showPlayerPage}>
                <Pagination 
                total={totalPlayers} 
                currentPage={currentPage} 
                contentPerPage={playersPerPage} 
                paginate={setCurrentPage}
                />
            </div>
        </div>
    );
}

export default ShowTransferPlayers