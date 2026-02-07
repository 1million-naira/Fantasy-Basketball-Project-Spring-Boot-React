import React from "react";

function PlayerFilter({setCurrentPos, setCurrentLeague, setSearch}){
    return (
        <>
            <form>
                <div className="formInput">
                    <label htmlFor="selectByPos">
                        Select By Position
                    </label>
                    <select id="selectByPos" 
                    onChange={(e)=>{
                        console.log(e.target.value);
                        setCurrentPos(e.target.value);
                    }}
                    >
                        {/* <option>All Positions</option> */}
                        <option>PG</option>
                        <option>SG</option>
                        <option>SF</option>
                        <option>PF</option>
                        <option>C</option>
                    </select>
                </div>

                <div className="formInput">
                    <label htmlFor="selectByLeague">
                        Select By League
                    </label>
                    <select id="selectByLeague" onChange={(e) => setCurrentLeague(e.target.value)}>
                        <option>CBA</option>
                        <option>NBA</option>
                        <option>WNBA</option>
                        <option>Euro</option>   
                    </select>
                </div>

                <div className="formInput">
                    <label htmlFor="searchPlayer">Search a player's name</label>
                    <input id="searchPlayer" type="text" placeholder="name" onChange={(e) => setSearch(e.target.value)}></input>
                </div>
            </form>
        </>
    );
}

export default PlayerFilter