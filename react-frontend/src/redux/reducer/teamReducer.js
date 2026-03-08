const teamReducer = (state = [], action) => {
    if(action.type === "UPDATE_TEAM"){
        return action.payload;
    }

    if(action.type === "CLEAR_TEAM"){
        return []
    }

   return state;
}

export default teamReducer;

const updateTeam = (team) => {
    return {
        type: "UPDATE_TEAM",
        payload: team
    }
}

const clearTeam = () => {
    return {
        type: "CLEAR_TEAM"
    }
}

export {updateTeam, clearTeam}