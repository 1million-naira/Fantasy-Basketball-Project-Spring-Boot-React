import React from "react";
import Header from "./Header";
import { Outlet } from "react-router-dom";
import { useNavigateBack } from "./Router/NavigateBack";


function Main(){

    // const location = useLocation();
    // const navigate = useNavigate();

    // const locationKey = location.key;
    // const navigateBack = useCallback(() => {
    //     if(locationKey === "default"){
    //         navigate("/", {replace: true});
    //     } else{
    //         navigate(-1);
    //     }
    // }, [locationKey, navigate]);

    return (
        <>
            <Header/>
        </>
    );

}
export default Main;