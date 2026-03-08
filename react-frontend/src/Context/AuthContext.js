import axios from "axios";
import React, { createContext, useContext, useEffect, useMemo, useState } from "react";

const AuthContext = createContext();

export default function AuthProvider({children}){
    const [token, setToken_] = useState(localStorage.getItem("token"));
    const [admin, isAdmin] = useState(false);
    const [userId, setUserId] = useState(localStorage.getItem("userId"));
    const setToken = (newToken) => {
        setToken_(newToken);
    };


    useEffect(() => {
        if(token){
            axios.defaults.headers.common["Authorization"] = "Bearer " + token;
            localStorage.setItem("token", token);
        } else{
            delete axios.defaults.headers.common["Authorization"];
            localStorage.removeItem("token");
        }

        if(userId){
            localStorage.setItem("userId", userId);
        } else{
            localStorage.removeItem("userId");
        }
    }, [token, userId]);

    const contextValue = useMemo(
        () => ({
            token,
            setToken,
            admin,
            isAdmin,
            userId,
            setUserId
        }),
        [token]
    );
    
    return(
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};