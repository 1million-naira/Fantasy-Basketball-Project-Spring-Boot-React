# Project Log

## Week 1
* I needed official real life basketball data for my application so I web-scraped data from basketball websites using the selenium library in python.
* I format the data into JSON, and map (deserialise) the data into different my JAVA objects.
* Using JAVA persistence API in Spring boot, I was able to map the objects to database entities.
* I also started planning for how to model the aspects of my problem into a data model: For example, how would I model a fantasy league that different users can join?
* Using JAVA persistence API I was able to use the annotations to establish relationships between main entities for a relational database.


## Week 2
* I started to think about how to actually use real statistics of basketball players to calculate points for each player, so I web-scraped box-scores from a real life basketball game.
* I serialise the box-score as JSON into my application.

## Week 3
* I am cleaning up a lot of my previously scraped data. For example, a lot of numerical values are actually in string format, so I decide to scrape for the data again.
* This meant that I had to change types of the attributes in the JAVA classes to match this change in the JSON.
* I actually establish more meaningful relationships between entities. For example, each player in the database is associated to exactly one player info object.
* I start thinking about how different users can interact with each other and compete in leagues, so I write code to randomly pair 2 users in a league into a user match object.
* I then try to expand on this by implementing round robin matchmaking, which is where user matches are allocated so that users are guaranteed to be paired with all other users within a league. (This is still in development).
* I wrote an application flow class for testing purposes in order to see if main features work as expected such as user creation, team creation, league creation and joining leagues.
* I tried to implement rounds for leagues, for example, competitive leagues have multiple rounds of fixtures. A league would be associated with many round objects in the database.

## Week 4
* Now I am trying to calculate points for players in my previously mentioned box-score data.
* I wrote a function to assign points to players based on their performance in different stats after reading in the box score.
* I also try to improve on the method of round robin matchmaking by writing code generating the rounds dynamically and assigning matches to the current round.
* I am also writing logic to determine if a round for a league is complete if it has been a certain amount of time since it was created (started).
* I decided to develop a REST API, so now I am trying to implement all the previous logic into different endpoints. For example, an endpoint to get a list of players from the database.
* I tested different endpoints using POSTMAN API.


## Week 5
