import react, { useState } from "react";
import SignUp from "./SignUp";
import Login from "./Login";

function Auth(){
    const [login, setLogin] = useState(false)
    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '5px 25px', width: '50%', margin: 'auto', height: '100vh'
        }}>
            <div style={{width: '50%'}}>
                <h1>Welcome to TopSquad Fantasy Basketball</h1>
            </div>
            {
            !login ? <SignUp setLogin={setLogin}/> : <Login setLogin={setLogin}/>
            }

        </div>
    );
}

export default Auth;