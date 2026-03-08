import React, { useState } from "react";
import { useNavigateBack } from "../Router/NavigateBack";
import CustomButton from "../components/CustomButton";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

function JoinPrivate () {
    const navigateBack = useNavigateBack("/")
    const navigate = useNavigate();

    const [search, setSearch] = useState('');

    const joinLeague = () => {
        axios.post(`http://localhost:8080/api/fantasy/leagues/-1/member?code=${search}`)
        .then((response) => {
            console.log(response.data);
            toast.success("Successfully joined league with code: " + search, {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            navigate("/leagues/join")

        }).catch((error) => {
            console.log(error)
            toast.error("Unable to join this league", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        })
    }
    return(
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label='Go back'/>
            <div style={{backgroundColor: 'var(--secondaryBackground)', borderRadius: 12, marginTop: 16, padding: '24px'}}>
                <h1>Join a private league</h1>
                <div>
                    <label htmlFor="searchLeague" style={{fontSize: 16, fontWeight: 300}}>League code</label>
                    <div style={{width: '20%', border: '1px solid rgba(255, 255, 255, 0.4)', 
                        position: 'relative', display: 'flex', justifyContent: 'center', alignItems: 'center', 
                        padding: '12px 0', borderRadius: '12px', marginTop: 8}}
                    >
                        <i class="fa-solid fa-magnifying-glass" style={{width: '5%', fontSize: 14, position: 'absolute', left: '8%', opacity: 0.7}}></i>
                        <input style={{position: 'relative', border: 'none', padding: '12px 0'}}id="searchLeague" type="text" placeholder="Enter league code" onChange={(e) => setSearch(e.target.value)}></input>
                    </div>
                </div>
                <div style={{marginTop: 16}}>
                    <CustomButton onClick={joinLeague} label='Join League' disabled={search.trim().length === 0} size="lg"/>
                </div>
            </div>
        </div>
    )
}

export default JoinPrivate;