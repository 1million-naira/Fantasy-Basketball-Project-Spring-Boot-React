import React, { useContext, useEffect } from "react";
import EditTeamSession from "./EditTeamSession";
import { TeamContext } from "../Context/TeamUpdate";
import PlayerCard from "../Players/PlayerCard";
import styles from "../Styles/EditTeam.module.css";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { updateTeam } from "../redux/reducer/teamReducer";
import { useNavigateBack } from "../Router/NavigateBack";
import CustomButton from "../components/CustomButton";


function EditTeam(){
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const {currentlySelectedStarter, currentlySelectedBench, substitute, starterStage, benchStage, editTeamSession, setEditTeamSession} = useContext(TeamContext);

    const confirmTeam = () => {
        const ids = [];
        editTeamSession.map(player => ids.push(player.id));
        console.log(ids);

        axios.put("http://localhost:8080/api/fantasy/team", {players: ids})
        .then((response) => {
            console.log(response.data);
            dispatch(updateTeam(editTeamSession));
            navigate("/");  
        })
        .catch((error) => {
            console.log(error);
        })

    }

    const navigateBack = useNavigateBack("/");
    
    return (
        <div style={{padding: '5px 25px'}}>
            {/* <button className="goBackButton" onClick={navigateBack}>Go back</button> */}
            <CustomButton onClick={navigateBack} label="Go back"/>

            <div style={{textAlign: 'center', display: 'flex', justifyContent: 'center', gap: 16}}>
                {/* <CustomButton onClick={() => substitute(currentlySelectedStarter, currentlySelectedBench)} label="Substitute" size='lg'/> */}
                <CustomButton onClick={() => confirmTeam()} label="Save Changes" size='lg'/>
            </div>
            <div style={{display: 'flex', justifyContent: 'space-between', gap: 12, marginTop: 16, padding: '0 10px'}}>
                <div style={{width: '60%'}}>
                    <EditTeamSession/>
                </div>
                
                <div style={{width: '35%', display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                    <h3 style={{fontStyle: 'italic', fontWeight: 100}}>Tip: Substitute players then save changes</h3>
                    <div style={{display: 'flex', justifyContent: 'space-evenly', gap: 48}}>
                        <div className={styles.subIn}>
                            {starterStage && 
                            <>
                            <h2>Substitute out</h2>
                            <PlayerCard key={starterStage.id} name={starterStage.name} image={starterStage.image}></PlayerCard>
                            </>
                            }
                        </div>

                        <div className={styles.subOut}>
                            {benchStage &&
                            <>
                            <h2>Substitute In</h2>
                            <PlayerCard key={benchStage.id} name={benchStage.name} image={benchStage.image}></PlayerCard>
                            </>
                            }
                        </div>
                    </div>

                    <div style={{marginTop: 24}}>
                        <CustomButton onClick={() => substitute(currentlySelectedStarter, currentlySelectedBench)} label="Substitute" size='lg'/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default EditTeam;