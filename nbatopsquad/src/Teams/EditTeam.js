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
        <>
            <button className="goBackButton" onClick={navigateBack}>Go back</button>
            <EditTeamSession/>

            <div className={styles.substitutionStage}>
                <div className={styles.subIn}>
                    {starterStage && 
                    <>
                    <h3>Substitute out</h3>
                    <PlayerCard key={starterStage.id} name={starterStage.name} image={starterStage.image}></PlayerCard>
                    </>
                    }
                </div>

                <div className={styles.subOut}>
                    {benchStage &&
                    <>
                    <h3>Substitute In</h3>
                    <PlayerCard key={benchStage.id} name={benchStage.name} image={benchStage.image}></PlayerCard>
                    </>
                    }
                </div>
            </div>

            <button onClick={() => substitute(currentlySelectedStarter, currentlySelectedBench)}>Substitute</button>
            <button onClick={() => confirmTeam()}>Save Changes</button>
        </>
    );
}

export default EditTeam;