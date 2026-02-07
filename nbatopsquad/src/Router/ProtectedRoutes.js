import React from "react";
import { useAuth } from "../Context/AuthContext";
import { Navigate, Outlet } from "react-router-dom";


function ProtectedRoute({children}){

    const {token} = useAuth();

    if(!token){
        return <Navigate to="/auth"/>
    };

    return children;

};

export default ProtectedRoute;