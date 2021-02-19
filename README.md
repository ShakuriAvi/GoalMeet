# GoalMeet
The purpose of the app is to connect football fans in order to meet in teams or individuals and hold football games.

There are two types of users in the app:
* Soccer Team Managers: This is a user who created a team.
* The second type is a user who is looking for a soccer team.

Each user has different permissions: 
* The team manager is the only one who can confirm the admission of new players to the team,
schedule games with other teams and determining the end result after the team meeting.
* A player who does not have a team he manages can send requests to other teams to make a switch between teams.
The program is implemented by working with Firebase, real-time updates between users שnd various photo packages designed by me and by various creators on the internet.
Each user can select a profile picture and change it.

Users can have conversations with each other through real-time chat.
Through chat users can determine where and when to meet, request different requests from football team managers and talk with different users.


# HomePage
Login by email and password.

![Home](https://user-images.githubusercontent.com/65177459/108538539-9489c200-72e7-11eb-9aa9-6637bc9babc1.jpg)
<img src="https://user-images.githubusercontent.com/65177459/108538539-9489c200-72e7-11eb-9aa9-6637bc9babc1.jpg" width="48">
# Application registration page 

![register](https://user-images.githubusercontent.com/65177459/108538582-a1a6b100-72e7-11eb-98ce-c2ab55f74157.jpg)
# User profile

![profile](https://user-images.githubusercontent.com/65177459/108538603-a66b6500-72e7-11eb-99f8-fb35696e9d24.jpg)
# The possible options in the application

![nav1](https://user-images.githubusercontent.com/65177459/108539006-1aa60880-72e8-11eb-96b5-554890dc2159.jpg)
![20210219_215331](https://user-images.githubusercontent.com/65177459/108554590-f9034c00-72fc-11eb-9868-09d3b85335af.jpg)

# Clicking on the My Team.
A user who belongs to a particular football team, is interested in looking at his team. By clicking on My Team.
if he does not belong to any team he will be notified.

![showHisTeam](https://user-images.githubusercontent.com/65177459/108538658-b84d0800-72e7-11eb-8e30-41b3592e9eb1.jpg)



# Main Flow : Scheduling a meeting between two teams by their managers
The team manager sends a message to the other manager via chat.

![chatsRTMeet](https://user-images.githubusercontent.com/65177459/108539262-6d7fc000-72e8-11eb-8b0a-6f41350d85f0.jpg)

One of the team managers sends the other manager an invitation to schedule a meeting.

![RToMeet](https://user-images.githubusercontent.com/65177459/108539278-753f6480-72e8-11eb-84a8-83fbf97d1fef.jpg)

The team manager who receives the request decides whether to approve or reject the request. If he refuses / approves the team manager who sends the request is updated.
If the team manager approves the request, the game is saved in the system and by clicking on the games, everyone can see the result of the game.

![games](https://user-images.githubusercontent.com/65177459/108539401-9e5ff500-72e8-11eb-952b-ebf020170d2b.jpg)
** Main Flow : A player is interested in joining the team.**
The player checks which teams exist in the system by clicking on available team

![avalibleTeam](https://user-images.githubusercontent.com/65177459/108539573-d8c99200-72e8-11eb-9932-6cc84b7815bb.jpg)

The player selects the team he wants to join and then sends her a message or a request to join.

![showElseTeam](https://user-images.githubusercontent.com/65177459/108539339-8c7e5200-72e8-11eb-821a-e60695c94abf.jpg)

The player chats with the team manager, to check if the team is interested in another player and see if the team is suitable for him.

![chatRToJoin](https://user-images.githubusercontent.com/65177459/108539659-f696f700-72e8-11eb-8ec4-1f27857b85c1.jpg)

The team manager decides whether to approve the request or not. The player is updated according to the decision of the team manager. If the team manager decided to approve it, the next time the player clicks on the "My Team" option the new team with player privileges will appear.

![requestToJoin](https://user-images.githubusercontent.com/65177459/108545701-cce1ce00-72f0-11eb-8e65-b320818c9a35.jpg)


# Clicking on the CreateTeam.

![createTeam](https://user-images.githubusercontent.com/65177459/108539874-36f67500-72e9-11eb-9349-ec9973f0de00.jpg)

# Clicking on the chat.
Displays all the people the user has talked to and their status (online or offline).

![lastChat](https://user-images.githubusercontent.com/65177459/108538845-e7fc1000-72e7-11eb-8da1-ebd759d89e71.jpg)

# Clicking on the Players.
Displays all registered users in the application and their details.

![players](https://user-images.githubusercontent.com/65177459/108539191-56d96900-72e8-11eb-9457-86bd0e418471.jpg)
