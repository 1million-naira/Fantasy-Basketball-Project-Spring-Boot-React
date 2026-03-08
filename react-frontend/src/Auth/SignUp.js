import axios from "axios";
import React, { useState } from "react";
import { toast } from "react-toastify";
import CustomButton from "../components/CustomButton";



function SignUp({setLogin}){
    const [inputs, setInputs] = useState({});

    const handleChange = event =>{
        const name = event.target.name;
        const value = event.target.value;

        setInputs(values => ({...values, [name]: value}));
    };

    const handleSignUp = (e) => {
        e.preventDefault();

        axios.post("http://localhost:8080/api/auth/register", inputs)
        .then((response) => {
            console.log(response.data);
            toast.success("Account successfully created! Please Login", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            setLogin(true);
        })
        .catch((error) => {
            console.log(error);
            console.log(inputs);
            toast.error("Sign up could not be completed! Please try again", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
        });
    }
    return (
        <div style={{width: '100%'}}>
            <div className="form-container">
                <div>
                    <h2 style={{textAlign: 'center'}}>Sign Up</h2>
                </div>
                <form>
                    <div className="inputs">
                        <div style={{ position: 'relative', marginTop: 8}}>
                            <i className="fa-regular fa-user"></i>
                            <input type="text" placeholder="Username" name="username" onChange={handleChange}></input>
                        </div>

                        <div style={{ position: 'relative', marginTop: 8}}>
                            <i className="fa-regular fa-envelope"></i>
                            <input type="email" placeholder="Email" name="email" onChange={handleChange}></input>
                        </div>

                        <div style={{ position: 'relative', marginTop: 8}}>
                            <i className="fa-solid fa-lock"></i>
                            <input type="password" placeholder="Password" name="password" onChange={handleChange}></input>
                        </div>

                        <div className="submit-container">
                            <div style={{width: '100%', marginTop: 8}}>
                                <CustomButton onClick={(e) => handleSignUp(e)} label='Sign Up'/>
                            </div>
                            <div style={{textAlign: 'center', marginTop: 8}}>
                                <span>OR</span>
                                <span>ALREADY HAVE AN ACCOUNT?</span>
                            </div>
                            <div style={{width: '100%', marginTop: 8}}>
                                <CustomButton onClick={() => setLogin(true)} label='Log In'/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default SignUp;