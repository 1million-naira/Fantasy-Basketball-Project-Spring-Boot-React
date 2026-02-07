
# TOPSQUAD Fantasy Basketball 

> The frontend is currently in a prototype stage and may not provide the best user experience. Improvements are planned.



## Home screen
<img width="1512" height="943" alt="Screenshot 2026-02-07 at 01 18 46" src="https://github.com/user-attachments/assets/3734463f-8c1a-41da-85d2-81d4653a2a5f" />


## Edit team screen (Make changes with existing players in your team)
<img width="1512" height="943" alt="Screenshot 2026-02-07 at 04 00 43" src="https://github.com/user-attachments/assets/9f42f8c8-f267-460a-b905-a3d18e8797fc" />


## Transfer screen (Add new players to your team)
<img width="1512" height="943" alt="Screenshot 2026-02-07 at 04 03 06" src="https://github.com/user-attachments/assets/bcf7ba5c-1a53-411f-a0df-a5015bc057e8" />


## Admin screen (Includes admin features such as enabling/disabling transfers and moderating users)
<img width="1512" height="943" alt="Screenshot 2026-02-07 at 04 08 33" src="https://github.com/user-attachments/assets/890168c6-e91c-4160-9f6c-41d98b91e8eb" />

## League screen (View a league that you are part of)
<img width="1512" height="943" alt="Screenshot 2026-02-07 at 04 02 18" src="https://github.com/user-attachments/assets/1b93f534-47db-44c5-b247-251150c1f641" />


# How to run the project

## Hello
1. First, please clone the repository using the command:
> git clone (repository URL)

2. Change the current directory to the cloned repository

3. Change the current directory to the Java Spring Boot project using the command:
> cd NewTopSquadProject

This project uses a local MySQL database. You can create the required database using MySQL Workbench:

* **Open MySQL Workbench** and connect to your local MySQL server (host: `127.0.0.1`, port: `3306`, username: `root`, password: `password`).
* Make sure to have MySQL community server downloaded first!

* **Create a new database**:
   * Open the SQL editor at that connection and run command:
  > CREATE DATABASE TopSquad;


4. Then run this command to run all the dependencies needed for the Java project:
> ./gradlew build

5. Once this is complete, run the project with:
> ./gradlew bootRun

6. Please open another terminal window and change the current directory of that window to the React project using:
> cd nbatopquad

7. The next step is dependent on having NodeJS, which allows JavaScript to run in the command line for React: Please see the link below:
> https://nodejs.org/en/download/

8. Then run the React project with the following command, and it will open in the browser:
> npm start

9. The app should now be open. Please navigate as you wish

10. To close both Java and React in their respective windows, please press the shortcut: 
> CTRL + C
# Fantasy-Basketball-Project-Spring-Boot-React
