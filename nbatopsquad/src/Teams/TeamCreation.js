import React, { useContext, useState } from "react";
import PlayerSlot from "../Players/PlayerSlot";
import ShowPlayers from "../Players/ShowPlayers";
import { TeamSelectionContext } from "../Context/TeamSelection";
import styles from "../Styles/TeamCreation.module.css"
import PlayerFilter from "../Players/PlayerFilter";
import Pagination from "../Pagination/Pagination";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { updateTeam } from "../redux/reducer/teamReducer";
import { useNavigateBack } from "../Router/NavigateBack";

function TeamCreation(){
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [currentPos, setCurrentPos] = useState('PG')
    const [selectedProLeague, setSelectedProLeague] = useState("CBA")

    const {players, budget, setBudget, removePlayer, removeAllPlayers} = useContext(TeamSelectionContext);
    const [teamName, setTeamName] = useState("");

    const confirmTeam = () => {

        if(players.length !== 8){
            console.log("8 players must be selected!");
            return;
        }

        if(!teamName){
            console.log("Enter team name");
            return;
        }
        
        const ids = [];
        players.map(player => ids.push(player.id)); 

        axios.post("http://localhost:8080/api/fantasy/team", {name: teamName, players: ids})
        .then((respone) => {
            console.log(respone.data);
            console.log(ids);
            navigate("/");

        })
        .catch((error) => {
            console.log(error.response.data);
        })


        setTeamName("");
    }

    const navigateBack = useNavigateBack("/");

    return(
        <>
            <button className="goBackButton" onClick={navigateBack}>Go back</button>
            <div className={styles.teamNameForm}>
                <form>
                    <label htmlFor="teamName">Name your team</label>
                    <input type="text" placeholder="Team name" value={teamName} onChange={(e) => {setTeamName(e.target.value)}}></input>
                </form>
            </div>

            <div className={styles.confirmTeam}>
                <button className={styles.confirm} onClick={() => confirmTeam()}>
                    Confirm your squad
                </button>
            </div>


            <div className={styles.budget}>
                <p>Your budget:</p>
                <span>{budget * 1000000}</span>
            </div>
            <div className={styles.selectionButtons}>
                <div>
                    <span>PG</span>
                    <button onClick={() => setCurrentPos('PG')}><i className="fa-solid fa-user-plus"></i></button>
                </div>

                <div>
                    <span>SG</span>
                    <button onClick={() => setCurrentPos('SG')}><i className="fa-solid fa-user-plus"></i></button>
                </div>

                <div>
                    <span>SF</span>
                    <button onClick={() => setCurrentPos('SF')}><i className="fa-solid fa-user-plus"></i></button>
                </div>

                <div>
                    <span>PF</span>
                    <button onClick={() => setCurrentPos('PF')}><i className="fa-solid fa-user-plus"></i></button>
                </div>

                <div>
                    <span>C</span>
                    <button onClick={() => setCurrentPos('C')}><i className="fa-solid fa-user-plus"></i></button>
                </div>
            </div>

            <div className={styles.select}>
                <div className={styles.selection}>
                    <h1>Current Selection</h1>
                    <div className={styles.selectionView}>
                        <div className={styles.selectionStarters}>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'PG' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'SG' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'SF' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'PF' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'C' && !player.bench)}/>
                        </div>

                        <div className={styles.selectionBench}>
                            <PlayerSlot player={players.filter((player, index) => player.bench)}/>
                        </div>

                    </div>
                    {
                    players && players.length > 0 ?
                    <button className={styles.deselectButton} onClick={() => removeAllPlayers()}>Deselect All</button> :
                    null
                    }
                </div>

                <div className={styles.showPlayers}>
                    <h3>
                        Selecting
                        {currentPos === 'PG'? ' Point Guards' : 
                        currentPos === 'SG' ? ' Shooting Guards' : 
                        currentPos === 'SF' ? ' Small Forwards' :
                        currentPos === 'PF' ? ' Power Forwards' :
                        currentPos === 'C' ? ' Centers' :
                        null
                        }
                    </h3>
                    <div>
                        <button onClick={() => setSelectedProLeague('CBA')}>CBA</button>
                        <button onClick={() => setSelectedProLeague('NBA')}>NBA</button>
                        <button onClick={() => setSelectedProLeague('WNBA')}>WNBA</button>
                        <button onClick={() => setSelectedProLeague('Euro')}>Euro League</button>
                    </div>
                    <h3>{selectedProLeague}</h3>
                    {currentPos ? <ShowPlayers pos={currentPos} selectedLeague={selectedProLeague}/> : <p>Start choosing your squad</p>}
                </div>
            </div>
        </>
    );
}

export default TeamCreation

