import react, { useState } from "react";
import SignUp from "./SignUp";
import Login from "./Login";

function Auth(){
    const [login, setLogin] = useState(false)
    return (
        <>
            {
            !login ? <SignUp setLogin={setLogin}/> : <Login setLogin={setLogin}/>
            }

        </>
    );
}

export default Auth;