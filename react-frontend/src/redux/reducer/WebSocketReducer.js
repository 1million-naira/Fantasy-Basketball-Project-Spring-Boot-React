const webSocketReducer = (state={client: null, subscriptions: new Map()}, action) =>{

    if(action.type === "SET_CLIENT"){
        return {...state, client: action.payload};
    }
    else if(action.type === "ADD_SUBSCRIPTION"){
        return {...state, subscriptions: new Map(state.subscriptions).set(action.payload.destination, 
            action.payload.subscription)};
    }

    else if(action.type === "REMOVE_SUBSCRIPTION"){
        const updatedSubscriptions = new Map(state.subscriptions);
        updatedSubscriptions.delete(action.payload);
        return {...state, subscriptions: updatedSubscriptions};
    }

    else if(action.type === "CLEAR_CLIENT"){
        return {client: null, subscriptions: new Map()};
    }

    return state;
}

export default webSocketReducer;


const setClient = (client) => {
    return{
        type: "SET_CLIENT",
        payload: client
    }
};

const addSubscription = (subscription) => {
    return{
        type: "ADD_SUBSCRIPTION",
        payload: subscription
    }
};

const removeSubscription = (subscription_name) => {
    return{
        type: "REMOVE_SUBSCRIPTION",
        payload: subscription_name
    }
};

const clearClient = () => {
    return{
        type: "CLEAR_CLIENT",
    }
};

export {setClient, addSubscription, removeSubscription, clearClient};