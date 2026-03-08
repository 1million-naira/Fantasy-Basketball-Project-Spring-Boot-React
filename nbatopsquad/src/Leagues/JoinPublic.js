import React, { useState } from "react";
import { useNavigateBack } from "../Router/NavigateBack";
import CustomButton from "../components/CustomButton";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

function JoinPublic () {
    const navigateBack = useNavigateBack("/")


    const joinLeague = () => {
    }
    return(
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label='Go back'/>
            <div style={{backgroundColor: 'var(--secondaryBackground)', borderRadius: 12, marginTop: 16, padding: '24px'}}>
                <h1>Join a public</h1>
            </div>
        </div>
    )
}

export default JoinPublic;