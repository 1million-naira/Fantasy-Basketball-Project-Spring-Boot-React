import React, { useState } from "react";
import CustomButton from '../components/CustomButton';
import { useNavigateBack } from '../Router/NavigateBack';
import { toast } from "react-toastify";
import axios from "axios";

function CreateLeague(){
    const navigateBack = useNavigateBack("/");

    const [inputs, setInputs] = useState({});

    const [name, setName] = useState('');
    const [type, setType] = useState('');
    const [matchmaking, setMatchmaking] = useState('');
    const [access, setAccess] = useState('');

    const [h2h, setH2h] = useState(false);

    const updateInputs = (e) => {
        const name = e.target.name;
        const value = e.target.value;

        setInputs(prev => ({...prev, [name] : value}))
    }

    const submitCreate = (e) => {
        e.preventDefault();
        console.log(inputs);

        if(!name || name.trim().length === 0){
            toast.error('Please enter a name for league', {autoClose: 5000, hideProgressBar:true, pauseOnHover:true})
            return;
        }

        if(!type){
            toast.error('Please enter a league type', {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            return;
        }

        if (type !== 'h2h' && type !== 'roto') {
            toast.error('Please enter a league type', { autoClose: 5000, hideProgressBar: true, pauseOnHover: true });
            return;
        }

        if(h2h){
            if(!matchmaking){
                toast.error('Please choose matchmaking type', {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                return;
            }
            if (matchmaking !== 'rr' && matchmaking !== 'random') {
                toast.error('Please choose matchmaking type', { autoClose: 5000, hideProgressBar: true, pauseOnHover: true });
                return;
            }
        }

        if(!access){
            toast.error('Please select league access type', {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            return;
        }
        if (access !== 'public' && access !== 'private') {
            toast.error('Please select league access type', { autoClose: 5000, hideProgressBar: true, pauseOnHover: true });
            return;
        }

        axios.post("http://localhost:8080/api/fantasy/leagues", {name: name, type: type, matchmaking: matchmaking, access: access})
        .then((response)=>{
            console.log(response.data);
            console.log('Created League code: ', response.data.code);
            const code = response.data.code;
            const successText = code ? 'League successfully created with code: ' + code : 'League successfully created';
            toast.success(successText, { autoClose: 5000, hideProgressBar: true, pauseOnHover: true });
        }).catch((error) => {
            console.log(error);
        })

        return;
    }

    return (
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label="Go back"/>
            <div style={{backgroundColor: 'var(--secondaryBackground)', borderRadius: 12, marginTop: 16, padding: '24px'}}>
                <h1>Create a League</h1>
                <div style={{width: '35%'}}>
                    <form>
                        <div style={{marginTop: 12}}>
                            <label htmlFor="leagueName">League Name</label>
                            <input name="name" id="leagueName" placeholder="Enter Name Here" onChange={(e) => setName(e.target.value)}></input>
                        </div>

                        
                        <div style={{marginTop: 12}}>
                            <label htmlFor="leagueType">League Type</label>
                            <select name="type" id="leagueType" onChange={(e) => { e.target.value === "h2h" ? setH2h(true) : setH2h(false); setType(e.target.value)}}>
                                <option>Type</option>
                                <option value="h2h">Head To Head</option>
                                <option value="roto">Rotisserie</option>
                            </select>
                        </div>
                        
                        { h2h &&
                        <div style={{marginTop: 12}}>
                            <label htmlFor="leagueMatchmaking">Matchmaking</label>
                            <select name="matchmaking" id="leagueMatchmaking" onChange={(e) => setMatchmaking(e.target.value)}>
                                <option>Matchmaking</option>
                                <option value="rr">Round Robin</option>
                                <option value="random">Random</option>
                            </select>
                        </div>
                        }
                        <div style={{marginTop: 12}}>
                            <label htmlFor="leagueAccess">Access</label>
                            <select name="access" id="leagueAccess" onChange={(e) => setAccess(e.target.value)}>
                                <option>Access</option>
                                <option value="public">Public</option>
                                <option value="private">Private</option>
                            </select>
                        </div>
                    <div style={{marginTop: 24}}>
                        <CustomButton label='Create League' size="lg" onClick={(e) => submitCreate(e)}/>
                    </div>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default CreateLeague;