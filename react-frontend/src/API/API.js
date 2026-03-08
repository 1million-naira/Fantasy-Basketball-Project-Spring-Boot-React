import axios from "axios";
import { useAuth } from "../Context/AuthContext";

const {token} = useAuth();

const instance = axios.create({
    
})