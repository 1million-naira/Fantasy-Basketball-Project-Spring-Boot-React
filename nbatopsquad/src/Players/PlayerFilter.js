import React from "react";

function PlayerFilter({setCurrentPos, setCurrentLeague, setSearch}){
    return (
        <>
            <form>
                <div>
                    <label htmlFor="searchPlayer" style={{fontSize: 16, fontWeight: 300}}>Find a player</label>
                    <div style={{width: '100%', border: '1px solid rgba(255, 255, 255, 0.4)', 
                        position: 'relative', display: 'flex', justifyContent: 'center', 
                        padding: '12px 0', borderRadius: '12px'}}
                    >
                        <i class="fa-solid fa-magnifying-glass" style={{width: '5%', fontSize: 14, position: 'absolute', left: '4%', opacity: 0.7}}></i>
                        <input style={{position: 'relative', left: '10%', border: 'none', padding: '12px 0'}}id="searchPlayer" type="text" placeholder="Search by Name" onChange={(e) => setSearch(e.target.value)}></input>
                    </div>
                </div>

                <div style={{display: 'flex', flexDirection: 'row', justifyContent: 'center', gap: 16, marginTop: 16}}>
                    <div>
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

                    <div>
                        <select id="selectByLeague" onChange={(e) => setCurrentLeague(e.target.value)}>
                            {/* <option>All Leagues</option> */}
                            <option>CBA</option>
                            <option>NBA</option>
                            <option>WNBA</option>
                            <option>Euro</option>   
                        </select>
                    </div>
                </div>
            </form>
        </>
    );
}

export default PlayerFilter