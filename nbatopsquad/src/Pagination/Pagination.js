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
                <ul className={styles.pagination}>
                    {
                    pageNumbers.map(number =>
                    <li key={number}>
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