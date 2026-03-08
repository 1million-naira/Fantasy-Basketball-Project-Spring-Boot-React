import React from 'react';
import CustomButton from '../components/CustomButton';
import { useNavigateBack } from '../Router/NavigateBack';
import { Link } from 'react-router-dom';


function JoinLeague(){
    const navigateBack = useNavigateBack("/");
    return (
        <div style={{padding: '5px 25px'}}>
            <CustomButton onClick={navigateBack} label="Go back"/>
            <div style={{backgroundColor: 'var(--secondaryBackground)', borderRadius: 12, marginTop: 16, padding: '24px'}}>
                <h1>Choose a League Type to Join</h1>

                <div style={{marginTop: 24}}>
                    <h4>Join a private league by entering a code</h4>
                    <Link to='/leagues/join/private'><CustomButton label='Join private league' size='lg'/></Link>
                </div>
                <div style={{marginTop: 24}}>
                    <h4>Join a random public league where no code is required</h4>
                    <Link><CustomButton label='Join public league' size='lg'/></Link>
                </div>
                
            </div>
        </div>
    )
}

export default JoinLeague;