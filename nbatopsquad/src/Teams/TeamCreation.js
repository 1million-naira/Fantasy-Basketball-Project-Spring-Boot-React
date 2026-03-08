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
import CustomButton from "../components/CustomButton";

function TeamCreation(){
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [currentPos, setCurrentPos] = useState('PG')
    const [selectedProLeague, setSelectedProLeague] = useState("CBA")
    const [search, setSearch] = useState("");

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
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label='Go back'/>


            <div style={{margin: 'auto'}}>
                <div style={{textAlign: 'center', marginTop: 16, width: '100%', display: 'flex', justifyContent: 'flex-end'}}>
                    <CustomButton onClick={() => confirmTeam()} label='Confirm your squad'/>
                </div>
                <form>
                    <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                        <label htmlFor="teamName">Name your team</label>
                        <input type="text" placeholder="Team name" value={teamName} onChange={(e) => {setTeamName(e.target.value)}}></input>
                    </div>
                </form>
            </div>

            <div style={{textAlign: 'center', marginTop: 32}}>
                <h2>Your budget:</h2>
                <span>{budget * 1000000} <i className="fa-solid fa-coins"></i></span>
            </div>
            <div className={styles.selectionButtons}>
                <div>
                    <span>PG</span>
                    <CustomButton onClick={() => setCurrentPos('PG')} icon={<i className="fa-solid fa-user-plus"></i>} size="sm"/>
                </div>

                <div>
                    <span>SG</span>
                    <CustomButton onClick={() => setCurrentPos('SG')} icon={<i className="fa-solid fa-user-plus"></i>} size="sm"/>
                </div>

                <div>
                    <span>SF</span>
                    <CustomButton onClick={() => setCurrentPos('SF')} icon={<i className="fa-solid fa-user-plus"></i>} size="sm"/>
                </div>

                <div>
                    <span>PF</span>
                    <CustomButton onClick={() => setCurrentPos('PF')} icon={<i className="fa-solid fa-user-plus"></i>} size="sm"/>
                </div>

                <div>
                    <span>C</span>
                    <CustomButton onClick={() => setCurrentPos('C')} icon={<i className="fa-solid fa-user-plus"></i>} size="sm"/>
                </div>
            </div>

            <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center', marginTop: 24, padding: '0 16px'}}>
                <div style={{width: '60%'}}>
                    <h1>Current Selection</h1>
                    {
                    players && players.length > 0 ?
                    <CustomButton onClick={() => removeAllPlayers()} label='Deselect All'/> :
                    null
                    }
                    <div style={{display: 'flex', flexDirection: 'column', gap: '2rem', marginTop: 16}}>
                        <div style={{display: 'flex', gap: '1rem'}}>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'PG' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'SG' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'SF' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'PF' && !player.bench)}/>
                            <PlayerSlot player={players.filter((player, index) => player.pos === 'C' && !player.bench)}/>
                        </div>

                        <div style={{display: 'flex', gap: '1rem'}}>
                            <PlayerSlot player={players.filter((player, index) => player.bench)}/>
                        </div>

                    </div>
                </div>

                <div style={{width: '40%'}}>
                    <h1 style={{fontWeight: '100', textAlign: 'left'}}>
                        Selecting
                        {currentPos === 'PG'? ' Point Guards ' : 
                        currentPos === 'SG' ? ' Shooting Guards ' : 
                        currentPos === 'SF' ? ' Small Forwards ' :
                        currentPos === 'PF' ? ' Power Forwards ' :
                        currentPos === 'C' ? ' Centers ' :
                        null
                        }
                        in the {selectedProLeague}
                    </h1>
                    <div style={{textAlign: 'left'}}>
                        <select id="selectByLeague" onChange={(e) => setSelectedProLeague(e.target.value)}>
                            <option>CBA</option>
                            <option>NBA</option>
                            <option>WNBA</option>
                            <option>Euro</option>   
                        </select>
                    </div>
                    {currentPos ? <ShowPlayers pos={currentPos} selectedLeague={selectedProLeague}/> : <p>Start choosing your squad</p>}
                </div>
            </div>
        </div>
    );
}

export default TeamCreation

