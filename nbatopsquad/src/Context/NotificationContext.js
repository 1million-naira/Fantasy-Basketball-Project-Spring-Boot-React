import axios from "axios";
import React, { createContext, useState } from "react";

export const NotificationContext = createContext();

export default function NotificationProvider({children}){
    const [notifications, setNotifications] = useState([
        // {id: 10000, username: "Sanji", message: "You created a league with the name: Noice and code: xhdj98", time: "time"},
        // {id: 20000, username: "Sanji", message: "You created a league with the name: Noice and code: xhdj98", time: "time"}
    ]);


    const addNotification = (notificationToAdd) => {
        const notificationList = notifications.map(notification => {return notification});
        setNotifications(prev => {return [...notifications, notificationToAdd]});
        console.log("Notification added!")
    };

    return (
        <NotificationContext.Provider
            value={{
                notifications,
                setNotifications,
                addNotification
            }}
        >
            {children}

        </NotificationContext.Provider>
    );

}