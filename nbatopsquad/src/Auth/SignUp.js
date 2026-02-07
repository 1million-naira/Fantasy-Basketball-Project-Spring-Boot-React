import axios from "axios";
import React, { useState } from "react";
import { toast } from "react-toastify";



function SignUp({setLogin}){
    const [inputs, setInputs] = useState({});

    const handleChange = event =>{
        const name = event.target.name;
        const value = event.target.value;

        setInputs(values => ({...values, [name]: value}));
    };

    const handleSignUp = (e) => {
        e.preventDefault();

        axios.post("http://localhost:8080/api/auth/regsiter", inputs)
        .then((response) => {
            console.log(response.data);
            toast.success("Account successfully created! Please Login", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            setLogin(true);
        })
        .catch((error) => {
            console.log(error);
            toast.error("Sign up could not be completed! Please try again", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        });
    }
    return (
        <>
            <div className="container">
                <div className="form-container">
                    <div className="header">
                        <div className="text">Sign Up</div>
                        <div className="underline"></div>
                    </div>
                    <form>
                        <div className="inputs">
                            <div className="input">
                                <i className="fa-regular fa-user"></i>
                                <input type="text" placeholder="Username" onChange={handleChange}></input>
                            </div>

                            <div className="input">
                                <i className="fa-regular fa-envelope"></i>
                                <input type="email" placeholder="Email" onChange={handleChange}></input>
                            </div>

                            <div className="input">
                                <i className="fa-solid fa-lock"></i>
                                <input type="password" placeholder="Password" onChange={handleChange}></input>
                            </div>

                            <div className="submit-container">
                                <div className="submit">
                                    <button className="auth-button" onClick={(e) => handleSignUp(e)}>Sign Up</button>
                                </div>
                                <div className="form-prompt">
                                    <span>OR</span>
                                    <span>ALREADY HAVE AN ACCOUNT?</span>
                                </div>
                                <div className="submit">
                                    <button  onClick={() => setLogin(true)} className="auth-button">Log In</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
};

export default SignUp;