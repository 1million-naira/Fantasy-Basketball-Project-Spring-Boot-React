import React from "react";
import { Link } from "react-router-dom";

function SecondaryHeader() {
    return (
        <nav style={{display: 'flex', justifyContent: 'flex-start', alignItems: 'center', padding: '10px', gap: 20}}>
            <ul>
                <li><Link to="/">Status</Link></li>
                <li><Link to="/team/edit">Edit Team</Link></li>
                <li><Link to="/transfers">Transfers</Link></li>
                <li><Link to="/leagues">Leagues</Link></li>
                <li><Link>Chats</Link></li>  
                <li><Link>Fixtures</Link></li>
                <li><Link>Players</Link></li>
            </ul>
        </nav>
    );
}

export default SecondaryHeader;