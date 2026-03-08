import React, { createContext, useState } from "react";

export const TeamSelectionContext = createContext()

export default function TeamProvider({children}){
    const [players, setPlayers] = useState([])
    const [budget, setBudget] = useState(100);



    const addPlayer = (selectedPlayer) => {
        if(players.length < 8){

            if(budget - selectedPlayer.value < 0){
                console.log("Team value will be over budget");
                return;
            }

            const isPlayerSelected = players.find((player) => player.id === selectedPlayer.id);  // Display a toast message if player already selected??
            const isPositionTaken = players.find((player) => player.pos === selectedPlayer.pos);
            if(!isPlayerSelected && !isPositionTaken){
                setBudget(prev => prev - selectedPlayer.value);
                setPlayers([...players, {...selectedPlayer, bench: false}]);
            };

            if(isPositionTaken){
                if(!isPlayerSelected){
                    if(players.length >= 5){
                        setBudget(prev => prev - selectedPlayer.value);
                        setPlayers([...players, {...selectedPlayer, bench: true}]);
                    }
                }
            }
        }
    }; //Add player to team, the first 5 positions (starters) must have unique positions.
    //If a position has already been taken and the 5 starters are already decided, then we can add that player to team.
    //Otherwise that player can not be added.

    const removePlayer = (removedPlayer) => {
        const isPlayerSelected = players.find((player) => player.id === removedPlayer.id);
        const updatedPlayers = players.map((player) => {
            if(player.pos === removedPlayer.pos && player.id !== removedPlayer.id){
                return {...player, bench: false};
            } else{
                return player;
            }
        });
        if(isPlayerSelected){
            setBudget(prev => prev + removedPlayer.value);
            setPlayers(updatedPlayers.filter((player) => player.id !== removedPlayer.id));
        };
    }; //Remove a player from the team, if a player of that position can be found on the bench.
    //By defaualt it will be from the bench as we can not have duplicate of positions in starters anyways.
    //Take the bench player in that position and turn them to a starter.
    //Then take the updated list and filter out the removed player.
    //Note: objects in state are immutable, that is why we create new list with map.


    const removeAllPlayers = () => {
        setBudget(100);
        setPlayers([]);
    }; //Reset the team creation session

    // const getPlayerIds = () => {
    //     const ids = [];
    //     players.map(player => ids.push(player.id));
    //     console.log(ids);
    // }



    return (
        <TeamSelectionContext.Provider
            value={{
                players,
                budget,
                setBudget,
                addPlayer,
                removePlayer,
                removeAllPlayers
            }}
        >
            {children}
        </TeamSelectionContext.Provider>

    ); //Return the context
}
