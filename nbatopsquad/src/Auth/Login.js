import React, { useContext, useState } from "react";
import { useAuth } from "../Context/AuthContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { NotificationContext } from "../Context/NotificationContext";
import { toast } from "react-toastify";
import CustomButton from "../components/CustomButton";

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
        <div style={{width: '100%'}}>
            <div className="form-container">
                <div>
                    <h2 style={{textAlign: 'center'}}>Log In</h2>
                </div>
                <form>
                    <div className="inputs">
                        <div style={{ position: 'relative', marginTop: 8}}>
                            <i
                                className="fa-regular fa-user"
                                // style={{
                                //     position: 'absolute',
                                //     left: '10px',
                                //     top: '50%',
                                //     transform: 'translateY(-50%)'
                                // }}
                            />
                            <input
                                type='text'
                                placeholder="Email or Username"
                                name="email"
                                onChange={handleChange}
                                style={{}}
                            />
                        </div>

                        <div style={{ position: 'relative', marginTop: 8}}>
                            <i className="fa-solid fa-lock" style={{
                                opacity: 0.7, 
                                // position: 'absolute', 
                                // left: '10px', 
                                // top: '50%', transform: 
                                // 'translateY(-50%)'
                            }}
                            ></i>
                            <input style={{}} type="password" placeholder="Password" name="password" onChange={handleChange}></input>
                        </div>
                    </div>
                    <div style={{marginTop: 12}}>
                        <div style={{width: '100%', marginTop: 8}}>
                            <CustomButton onClick={(e) => handleLogin(e)} label='Log In'/>
                        </div>
                        <div style={{textAlign: 'center', marginTop: 8}}>
                            <span>OR</span>
                            <span>NOT A MEMBER YET?</span>
                        </div>
                        <div style={{width: '100%', marginTop: 8}}>
                            <CustomButton onClick={() => setLogin(false)} label='Sign Up'/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
};


export default Login;