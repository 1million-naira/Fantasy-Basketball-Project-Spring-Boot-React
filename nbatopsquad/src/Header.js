import React, { useContext, useEffect, useState } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "./Context/AuthContext";
import { TeamContext } from "./Context/TeamUpdate";
import axios from "axios";
import { NotificationContext } from "./Context/NotificationContext";
import styles from "./Styles/Header.module.css";
import { useDispatch, useSelector } from "react-redux";
import { clearTeam } from "./redux/reducer/teamReducer";
import { setNotifs } from "./redux/reducer/notifsReducer";
import Notifications from "./Notifications";

function Header(){
    const {setToken} = useAuth();
    const dispatch = useDispatch();
    // const {notifications} = useContext(NotificationContext)
    const notifs = useSelector((state) => state.notifications);



    const [notificationDisplay , setNotificationDisplay] = useState({display: 'none'});


    const [notisVisible, setNotisVisible] = useState(false);
    const toggleNotifications = () => {
        setNotisVisible(!notisVisible);
    }

    const navigate = useNavigate();

    const [user, setUser] = useState({username: "test", budget: 100});

    useEffect(() => {
        axios.get("http://localhost:8080/api/users/user")
        .then((response => {
            setUser(response.data);
        }))
        .catch((error) => {
            console.log(error);
        });

        // axios.get("http://localhost:8080/api/notifications/user")
        // .then((response) => {
        //     dispatch(setNotifs(response.data));
        // })
        // .catch((error) => {
        //     console.log(error);
        // });
    }, [])

    const handleLogout = () => {
        dispatch(clearTeam());
        setToken();
        navigate("/auth");

    };
    return (
        <>
            <header>
                <nav>
                    <ul>
                        <li className={styles.logo}><Link to={"/"}>TOPSQUAD <i className="fa-solid fa-basketball"></i></Link></li>
                        <li><Link to={"/"}>HOME</Link></li>
                        {/* <li>Community</li> */}
                        <li><i className="fa-solid fa-user"></i> {user.username.toUpperCase()}</li>
                        <li><i className="fa-solid fa-money-bill"></i> <span>{user.budget*1000000}</span></li>
                        {/* <li onMouseEnter={(e) => {setNotificationDisplay({display: 'block'})}} onMouseLeave={(e) => {setNotificationDisplay({display: 'none'})}}>
                            <div className={styles.inbox}>
                                <i className="fa-solid fa-inbox"></i>
                            </div>
                            <div style={notificationDisplay} className={styles.notifications}>
                                {
                                notifs.map(notification =>
                                    <div key={notification.id} className={styles.notification}>
                                        <p>{notification.message}</p>
                                        <p>{notification.time}</p>
                                    </div>
                                )
                                }
                            </div>
                        </li> */}

                        <li>
                            <button className={styles.inboxButton} onClick={() => toggleNotifications()}>
                                <i className="fa-solid fa-inbox"></i>
                            </button>

                            {
                            notisVisible && 
                            <>
                            <button onClick={() => toggleNotifications()}>&times;</button>
                            <Notifications/>
                            </>
                            }
                        </li>
                        <li>
                            <button className={styles.logOut} onClick={() => handleLogout()}>LOGOUT <i className="fa-solid fa-arrow-right-from-bracket"></i></button>
                        </li>
                    </ul>
                </nav>
            </header>
            <Outlet/>
        </>
    );
}

export default Header