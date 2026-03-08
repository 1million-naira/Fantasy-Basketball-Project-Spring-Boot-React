import React, { useContext, useEffect, useState } from "react";
import PlayerFilter from "../Players/PlayerFilter";
import TeamView from "./TeamView";
import styles from "../Styles/Transfer.module.css";
import ShowTransferPlayers from "../Players/ShowTransferPlayers";
import { TeamContext } from "../Context/TeamUpdate";
import TransferSession from "./TransferSession";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { updateTeam } from "../redux/reducer/teamReducer";
import { useNavigateBack } from "../Router/NavigateBack";
import { toast } from "react-toastify";
import CustomButton from "../components/CustomButton";


function Transfer(){
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {refreshTransferSession, setTransferSession, transferSession, budget, setBudget} = useContext(TeamContext);

    useEffect(() => {
        axios.get("http://localhost:8080/api/users/user")
        .then((response => {
            setBudget(response.data.budget);
        }))
        .catch((error) => {
            console.log(error);
        });
    }, []);


    const [currentPos, setCurrentPos] = useState('');
    const [currentLeague, setCurrentLeague] = useState('');
    const[search, setSearch] = useState('');
    const teamState = useSelector((state) => state.team);

    const confirmTransfers = () => {
        const ids = [];
        transferSession.map(player => ids.push(player.id));
        console.log(ids);

        axios.put("http://localhost:8080/api/fantasy/team", {players: ids})
        .then((response) => {
            console.log(response.data);
            dispatch(updateTeam(transferSession));
            toast.success("Transfers were completed successfully!", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            navigate("/");  
        })
        .catch((error) => {
            console.log(error);
            if(error.response.status === 409){
                toast.error("The transfer market is closed, no transfers can be made", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            }
        })
    }

    const navigateBack = useNavigateBack("/");

    return (
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label="Go back"/>

            <h2 style={{fontStyle: 'italic', fontWeight: 100, textAlign: 'center'}}>Tip: Select a player in your team then choose a player to transfer in!</h2>

            <div className={styles.transferButtons}>
                <CustomButton onClick={() => {confirmTransfers()}} label="Confirm Transfers"/>
                <CustomButton onClick={() => setTransferSession(teamState)} label="Undo All Transfers"/>
            </div>


            <div style={{textAlign: 'center'}}>
                <h2>Your budget:</h2>
                <span>{budget * 1000000} <i className="fa-solid fa-coins"></i></span>
            </div>

            <div style={{display: 'flex', justifyContent: 'space-between', gap: 12, padding: '0 10px', marginTop: 16}}>
                <div style={{width: '60%'}}>
                    <TransferSession/>
                </div>
                <div style={{width: '35%'}}>
                    <PlayerFilter setCurrentPos={setCurrentPos} setCurrentLeague={setCurrentLeague} setSearch={setSearch}/>
                    <ShowTransferPlayers currentPos={currentPos} currentLeague={currentLeague} search={search}/>
                </div>
            </div>

        </div>
    );
}

export default Transfer