
# TOPSQUAD Fantasy Basketball 

> The frontend is currently in a prototype stage and may not provide the best user experience. Improvements are planned.

## Sign Up/Login screen
<img width="1509" height="764" alt="Screenshot 2026-03-08 at 23 32 35" src="https://github.com/user-attachments/assets/82e09279-3cff-4f65-81d4-0cd3a1d7656e" />


## Home screen

<img width="1512" height="943" alt="Screenshot 2026-03-08 at 23 34 25" src="https://github.com/user-attachments/assets/b0404cea-6317-4c56-a8a6-8fec14aa8a4b" />



## Edit team screen (Make changes with existing players in your team)
<img width="1512" height="943" alt="Screenshot 2026-03-08 at 23 35 00" src="https://github.com/user-attachments/assets/f65fb45a-c43f-4e98-8036-102b65096840" />


## Create Team screen (Create your team for the first time)
<img width="1512" height="941" alt="Screenshot 2026-03-08 at 23 39 34" src="https://github.com/user-attachments/assets/19a70aec-b10c-42af-9ee3-4a3c1e18f2d3" />


## Transfer screen (Add new players to your team)
<img width="1512" height="943" alt="Screenshot 2026-03-08 at 23 35 39" src="https://github.com/user-attachments/assets/0a2d9f34-f2ee-40fc-a72f-495b334eda86" />


## League screen (View a league that you are part of)
<img width="1512" height="943" alt="Screenshot 2026-03-08 at 23 36 46" src="https://github.com/user-attachments/assets/1bded044-2cd9-4dc8-9e34-8b9b048f8afa" />

## Create League screen (Create a league)
<img width="1512" height="659" alt="Screenshot 2026-03-08 at 23 37 38" src="https://github.com/user-attachments/assets/51dccc31-34e2-4d43-93cc-585225a56f8a" />

## Admin screen (Includes admin features such as enabling/disabling transfers and moderating users) CURRENTLY BEING REVAMPED
<img width="1512" height="943" alt="Screenshot 2026-02-07 at 04 08 33" src="https://github.com/user-attachments/assets/890168c6-e91c-4160-9f6c-41d98b91e8eb" />



# How to run the project
The project is currently in the process of being deployed for showcasing, please bare with me!

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
