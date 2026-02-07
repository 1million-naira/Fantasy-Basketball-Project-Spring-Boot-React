import axios from "axios";
import react, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setNotifs } from "./redux/reducer/notifsReducer";
import styles from "./Styles/Notifications.module.css";
import Pagination from "./Pagination/Pagination"




function Notifications(){
    const dispatch = useDispatch();
    const notifs = useSelector((state) => state.notifications);

    const[currentPage, setCurrentPage] = useState(1)

    useEffect(() => {
        axios.get("http://localhost:8080/api/notifications/user")
        .then((response) => {
            dispatch(setNotifs(response.data));
        })
        .catch((error) => {
            console.log(error);
        });
    }, [])


    return(
        <>
            <div className={styles.notifications}>
                {
                notifs.map(notification => 
                    <div key={notification.id} className={styles.notification}>
                        <p>{notification.message}</p>
                        <p>{notification.time}</p>
                    </div>
                )
                }
            </div>
            <Pagination
            total={10} 
            currentPage={currentPage} 
            contentPerPage={5} 
            paginate={setCurrentPage}
            />
        </>
    );
}

export default Notifications;