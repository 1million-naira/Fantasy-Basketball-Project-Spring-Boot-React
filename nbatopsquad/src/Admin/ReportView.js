import react from "react";
import styles from "../Styles/ReportView.module.css"

function Reportview({report, closeReportView, socketRef}){
    console.log(socketRef);

    const warn = (userId) => {
        if(socketRef && socketRef.connected){
            console.log("Warning user!")
            socketRef.publish({
                destination: "/app/warnUser/" + userId
            })
        } else{
            console.error('Stomp client is not connected');
        }
    }
    return (
        <>  
            <div className={styles.reportDialog}>
                <div className={styles.report}>
                    <div className={styles.info}>
                        <p>Report ID</p>
                        <h6>{report.id}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Reported by</p>
                        <h6>{report.username}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Reporter ID</p>
                        <h6>{report.userId}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Report against</p>
                        <h6>{report.againstName}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Reported ID</p>
                        <h6>{report.againstId}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Report description</p>
                        <h6>{report.description}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Reported message</p>
                        <h6>{report.message}</h6>
                    </div>
                    <div className={styles.info}>
                        <p>Status</p>
                        <h6>{report.resolved}</h6>
                    </div>

                    <div className={styles.actionsContainer}>
                        <h1>Actions:</h1>
                        <div className={styles.actions}>
                            <button onClick={() => warn(report.againstId)}>Warn {report.againstName}</button>
                            <button onClick={() => warn(report.userId)}>Warn {report.username}</button>
                        </div>
                    </div>
                    <button className={styles.close} onClick={() => closeReportView(null)}>&times;</button>
                </div>
            </div>
        </>
    )
}

export default Reportview;