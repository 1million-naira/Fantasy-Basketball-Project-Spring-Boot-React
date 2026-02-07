import React, { createContext, useState } from "react";
import { toast } from "react-toastify";

export const TeamContext = createContext()


export default function TeamUpdate({children}){
    const[team, setTeam] = useState([
        {id: 1, image: "https://www.eurobasket.com/photos/Chang_Ling_2.jpg", name: "Chang Ling", pos: 'C', bench:false, value: 1}, 
        {id: 2, image: "https://www.eurobasket.com/photos/Fang_Shuo.jpg", name: "Fang Shuo", pos: 'PG', bench:false, value: 1},
        {id: 3, image: "https://www.eurobasket.com/photos/Chen_Guohao.jpg", name: "Chen Guohao", pos: 'SF', bench:false, value: 1},
        {id: 4, image: "https://www.eurobasket.com/photos/Linjian_Chen_1.jpg", name: "Chen Linjian", pos: 'SG', bench:false, value: 1},
        {id: 5, image: "https://www.eurobasket.com/photos/Chen_Peidong.jpg", name: "Chen Peidong", pos: 'PF', bench:false, value: 1},
        {id: 6, image: "https://www.eurobasket.com/photos/Cheng_Shuaipeng_1.jpg", name: "Shuaipeng Cheng", pos: 'SF', bench:true, value: 1},
        {id: 7, image: "https://www.eurobasket.com/photos/Cui_Jinming_1.jpg", name: "Jinming Cui", pos: 'PG', bench:true, value: 1},
        {id: 8, image: "https://www.eurobasket.com/photos/Dai_Hao.jpg", name: "Hao Dai", pos: 'SG', bench:true, value: 1}
    ])

    
    const [budget, setBudget] = useState(100);

    


    const [transferSession, setTransferSession] = useState(team)
    const [currentlySelected, setCurrentlySelected] = useState(-1)


    const [editTeamSession, setEditTeamSession] = useState(team)
    const [currentlySelectedStarter, setCurrentlySelectedStarter] = useState(-1)
    const [currentlySelectedBench, setCurrentlySelectedBench] = useState(-1)
    const [starterStage, setStarterStage] = useState();
    const [benchStage, setBenchStage] = useState();

    const transferPlayer = (playerToAdd, index) => {
        const isPlayerInTeam = transferSession.find((player) => player.id === playerToAdd.id);
        if(!isPlayerInTeam){
            const updatedTransferSession = transferSession.map((player, i) =>  {
                if(i===index){
                    if(transferSession[i].pos !== playerToAdd.pos){
                        toast.warn("Can not transfer players in out of position", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                        // console.log("Can not transfer players in out of position");
                        return transferSession[i];
                    }
                    
                    const difference = transferSession[i].value - playerToAdd.value;
                    const newBudget = budget + difference;

                    if(newBudget < 0){
                        toast.warn("Team will be over budget", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
                        return;
                    }

                    setBudget(newBudget);
                
                    if(transferSession[i].bench){
                        return {...playerToAdd, bench: true};
                    } else{
                        return {...playerToAdd, bench: false};
                    }
                } else{
                    return player;
                };
            });
            setTransferSession(updatedTransferSession);
            setCurrentlySelected(-1);
        } else{
            toast.warn("This player is already in the transfer session", {autoClose: 5000, hideProgressBar:true, pauseOnHover:true});
            // console.log("This player is already in the transfer session");
        }
    }; //Transfer player to team, take the index of the currently selected player to transfer out in team.
    //If map reaches that index, then return the player to add instead as either bench or starter.
    //Reset currently selected so next player to transfer out can be selected.


    const updateCurrentlySelected = (index) => {
        setCurrentlySelected(index);
    }; //Update the state of currently selected player,
    //which is the index of the player to be substituted in the team


    const refreshTransferSession = () => {
        setTransferSession(team);
    }; //Reset all transfers

    const updateTeamWithTransfers = () => {
        setTeam(transferSession);
    }; //Confirm the transfers


    const updateCurrentlySelectedStarter = (index) => {
        setStarterStage(editTeamSession.find((player, i) => i === index));
        setCurrentlySelectedStarter(index);
        console.log(currentlySelectedStarter);
        // substitute(currentlySelectedStarter, currentlySelectedBench);
    };

    const updateCurrentlySelectedBench = (index) => {
        setBenchStage(editTeamSession.find((player, i) => i === index));
        setCurrentlySelectedBench(index);
        console.log(currentlySelectedBench);
        // substitute(currentlySelectedStarter, currentlySelectedBench);
    };

    const substitute = (starterIndex, benchIndex) => {
        if(starterIndex === -1){
            console.log("Select a starter to substitute");
            return;
        }

        if(benchIndex === -1){
            console.log("Select a bench to substitute in");
            return;
        }

        const starter = editTeamSession.find((player, i) => i === starterIndex);
        const bench = editTeamSession.find((player, i) => i === benchIndex);
        // console.log(bench);

        const updatedEditTeamSession = editTeamSession.map((player, i) => {
            if(i === starterIndex){
                return {...bench, bench:false};
            }

            if(i === benchIndex){
                return {...starter, bench:true};
            }

            return player;
        });

        setEditTeamSession(updatedEditTeamSession);
        setCurrentlySelectedStarter(-1);
        setCurrentlySelectedBench(-1);
        setStarterStage();
        setBenchStage();
    }




    return(
        <TeamContext.Provider
            value={{
                team,
                setTeam,
                budget,
                setBudget,
                transferSession,
                setTransferSession,
                currentlySelected,
                editTeamSession,
                setEditTeamSession,
                currentlySelectedStarter,
                currentlySelectedBench,
                starterStage,
                benchStage,
                substitute,
                updateCurrentlySelectedStarter,
                updateCurrentlySelectedBench,
                transferPlayer,
                updateCurrentlySelected,
                refreshTransferSession,
                updateTeamWithTransfers
            }}
        >
            {children}
        </TeamContext.Provider>
    ) //Return the context


    
}