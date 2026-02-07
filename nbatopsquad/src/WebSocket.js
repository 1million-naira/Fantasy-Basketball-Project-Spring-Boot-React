import { Client } from "@stomp/stompjs";
import { useCallback, useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux"
import { useAuth } from "./Context/AuthContext";
import { addSubscription, clearClient, removeSubscription, setClient } from "./redux/reducer/WebSocketReducer";
import SockJS from "sockjs-client";




export const useWebSocketService = (
    webSocketUrl,
    onConnectCallback,
    onErrorCallback
) => {

    const dispatch = useDispatch();
    const state = useSelector((state) => state.webSocket);

    const {token} = useAuth(); //Authrization token for websocket connection


    const clientRef = useRef(null);
    const isConnected = useRef(false);

    useEffect(() => {
        clientRef.current = state.client;
    }, [state.client]);

    const connect = useCallback(() => {
        //STOMP CLIENT
        if(state.client || isConnected.current){
            return;
        }
        const client = new Client({
            webSocketFactory: () => new SockJS(webSocketUrl),
            debug: (str) => {
                console.log(str);
            },
            reconnectDelay: 5000,
            connectHeaders: {Authorization: `Bearer ${token}`},
            onConnect: () => {
                isConnected.current = true;
                console.log("Connected to Websocket");
                onConnectCallback();
            },
            onStompError: (frame) => {
                onErrorCallback("Broker reported error: " + frame.headers["message"] || "Unknown Error")
                // console.error("Broker reported error: " + frame.headers["message"]);
                console.error('Additional details: ' + frame.body);
            }
        });

        client.activate();
        clientRef.current = client;
        dispatch(setClient(client));

    }, [state.client, webSocketUrl, onConnectCallback, onErrorCallback]);


    const subscribe = useCallback((destination, callback) => {
        const client = clientRef.current;
        if(!client||!isConnected.current){
            return;
        };

        if(state.subscriptions.has(destination)){
            return;
        };

        const subscription = client.subscribe(destination, (message) => {
            if(message.body){
                callback(JSON.parse(message.body));
                console.log("Message received");
            };

            dispatch(addSubscription({destination, subscription}));
        });
    }, [state.subscriptions]);

    const send = useCallback((destination, body) => {
        const client = clientRef.current;
        if(!client || !isConnected.current){
            return;
        }
        client.publish(
            {destination: destination, body: JSON.stringify(body)}
        )
        console.log(JSON.stringify(body));
        // console.log("Message sent");
    }, []);

    const unsubscribe = useCallback((destination) => {
        const subscription = state.subscriptions.get(destination);

        if(subscription){
            subscription.unsubscribe();
            dispatch(removeSubscription(destination));
        }
    }, [state.subscriptions]);

    const disconnect = useCallback(() => {
        const client = clientRef.current;

        if(client && clientRef.current){
            state.subscriptions.forEach(subscription => subscription.unsubscribe());
            client.deactivate();
            dispatch(clearClient());
            isConnected.current = false;
        }
    }, [state.subscriptions]);


    return {connect, subscribe, send, unsubscribe, disconnect};
};