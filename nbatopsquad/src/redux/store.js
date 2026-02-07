import { combineReducers } from "redux";
import notifsReducer from "./reducer/notifsReducer";
import { legacy_createStore as createStore } from "redux";
import teamReducer from "./reducer/teamReducer";
import webSocketReducer from "./reducer/WebSocketReducer";

const rootReducer = combineReducers({
    notifications: notifsReducer,
    team: teamReducer,
    webSocket: webSocketReducer
})


const store = createStore(rootReducer);
export default store;