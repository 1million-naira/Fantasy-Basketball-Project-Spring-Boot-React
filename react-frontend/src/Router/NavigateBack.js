import { useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export const useNavigateBack = (fallBackUrl) => {
    const location = useLocation();
    const navigate = useNavigate();

    const locationKey = location.key;
    const navigateBack = useCallback(() => {
        if(locationKey === "default"){
            navigate(fallBackUrl, {replace: true});
        } else{
            navigate(-1);
        }
    }, [fallBackUrl, locationKey, navigate]);

    return navigateBack;

}
