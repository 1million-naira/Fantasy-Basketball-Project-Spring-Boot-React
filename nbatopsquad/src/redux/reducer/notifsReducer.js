const notifsReducer = (state = [], action) => {
    if(action.type === "ADD_NOTIF"){
        return [...state, action.payload];
    }

    if(action.type === "SET_NOTIFS"){
        return action.payload;
    }
    return state;
}

export default notifsReducer;

const addNotif = (notification) => {
    return{
        type: "ADD_NOTIF",
        payload: notification
    }
}


const setNotifs = (notifications) => {
    return{
        type: "SET_NOTIFS",
        payload: notifications
    }
}

export {addNotif, setNotifs};