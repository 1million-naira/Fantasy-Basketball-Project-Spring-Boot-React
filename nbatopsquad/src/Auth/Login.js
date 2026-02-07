import React, { useContext, useState } from "react";
import { useAuth } from "../Context/AuthContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { NotificationContext } from "../Context/NotificationContext";
import { toast } from "react-toastify";

function Login({setLogin}){
    const {setToken, isAdmin, setUserId} = useAuth();
    // const {notifications} = useContext(NotificationContext);
    const navigate = useNavigate();

    const [inputs, setInputs] = useState({});

    const handleChange = event =>{
        const name = event.target.name;
        const value = event.target.value;

        setInputs(values => ({...values, [name]: value}));
    };

    const handleLogin = (e) => {
        e.preventDefault();
        
    
        axios.post('http://localhost:8080/api/auth/login', inputs)
        .then((response) => {
            setToken(response.data.accessToken);
            setUserId(response.data.userId);
            // isAdmin(response.data.admin);
            setUserId(response.data.userId);
            
            if(response.data.admin === true){
                navigate("/admin");
            } else{
                navigate("/");
            }
        }).catch((error) => {
            console.log(error);
            toast.error("Incorrect credentials! Please enter the correct email and password!", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        });

        // setToken("This is a test token");
        // navigate("/");
    };
    


    return (
        <>
            <div className="container">
                <div className="form-container">
                    <div className="header">
                        <div className="text">Log In</div>
                        <div className="underline"></div>
                    </div>
                    <form>
                        <div className="inputs">

                            <div className="input">
                                <i className="fa-regular fa-envelope"></i>
                                <input type="email" placeholder="Email" name="email" onChange={handleChange}></input>
                            </div>

                            <div className="input">
                                <i className="fa-solid fa-lock"></i>
                                <input type="password" placeholder="Password" name="password" onChange={handleChange}></input>
                            </div>

                            <div className="submit-container">
                                <div className="submit">
                                    <button onClick={(e) => handleLogin(e)} type="submit" className="auth-button">Log In</button>
                                </div>
                                <div className="form-prompt">
                                    <span>OR</span>
                                    <span>NOT A MEMBER YET?</span>
                                </div>
                                <div className="submit">
                                    <button onClick={() => setLogin(false)} className="auth-button">Sign Up</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};


export default Login;