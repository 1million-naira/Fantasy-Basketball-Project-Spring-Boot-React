import react from "react";
import styles from "../Styles/Pagination.module.css";

function Pagination({total, currentPage, contentPerPage, paginate}){
    const pageNumbers = []

    for(let i = 1; i <= Math.ceil(total/contentPerPage); i++){
        pageNumbers.push(i)
    }

    const updatePage= (e, number) => {
        e.preventDefault();
        paginate(number)
    }
    
    return (
        <>
            <nav>
                <ul style={{listStyle: "none",
                    width: "100%",
                    marginTop: "12px",
                    padding: 0,
                    // border: "1px solid white",
                    display: "flex",
                    flexWrap: "wrap",
                    gap: "8px",
                    justifyContent: "center"
                }}>
                    {
                    pageNumbers.map(number =>
                    <li key={number} style={{backgroundColor: 'var(--monoHeader)', width: '20px', borderRadius: '5px', padding: '3px'}}>
                        <a 
                        onClick={(e) => updatePage(e, number)}
                        href="#"
                        >
                            {number}
                        </a>
                    </li>
                    )
                    }
                </ul>
            </nav>
        </>
    );
}

export default Pagination;