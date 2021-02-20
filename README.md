# GoalMeet
The goal of the app is to connect between football players,  teams or individuals and schedule football games.
The app is not only for football, It can be used for different types of sports like: running, basketball, volleyball etc..

There are two types of users in the app:
* Soccer Team Managers:
   The user who created a team.
* Users who wants to join the soccer team.

Each user has different permissions: 
* The team manager is the only one who can approve the joining request of new players to the team,
  schedule games with other teams and update the end result after the team meeting.
* A player that is not a team manager, not committed to one team and can requests to join other teams.


The program is implemented with Firebase real-time updates between users.
Each user can select a profile picture and change it.

Users can chat each other through real-time chat.
Through chat, users can determine where and when to meet, check if the message was read or not and talk with different users.


# HomePage

Login with Email and Password:

<img src="https://user-images.githubusercontent.com/65177459/108538539-9489c200-72e7-11eb-9aa9-6637bc9babc1.jpg" width="350" height="500">

# Application registration page 

<img src="https://user-images.githubusercontent.com/65177459/108538582-a1a6b100-72e7-11eb-98ce-c2ab55f74157.jpg" width="350" height="500">

# User profile
A user can edit his own profile at any time.

<img src="https://user-images.githubusercontent.com/65177459/108538603-a66b6500-72e7-11eb-99f8-fb35696e9d24.jpg" width="350" height="500">

# The possible options in the application

<img src="https://user-images.githubusercontent.com/65177459/108539006-1aa60880-72e8-11eb-96b5-554890dc2159.jpg" width="350" height="500">
<img src="https://user-images.githubusercontent.com/65177459/108554590-f9034c00-72fc-11eb-9868-09d3b85335af.jpg" width="550">

# Clicking on My Team
A user who belongs to a particular football team, can watch details about his team By clicking on My Team button.
if he does not belong to any team he will be notified.

<img src="https://user-images.githubusercontent.com/65177459/108538658-b84d0800-72e7-11eb-8e30-41b3592e9eb1.jpg" width="350" height="500">

# Main Flow : Scheduling a meeting between two teams by their managers

* The team manager sends a message to the other manager via chat.

<img src="https://user-images.githubusercontent.com/65177459/108591014-2b4d9180-736f-11eb-961f-780132e8dffb.jpg" width="350" height="500">

* One of the team managers sends the other manager an invitation to schedule a meeting.

<img src="https://user-images.githubusercontent.com/65177459/108539278-753f6480-72e8-11eb-84a8-83fbf97d1fef.jpg" width="350" height="500">

* The team manager who receives the request decides whether to approve or reject the request.
  the team manager who sends the request is get updated.
  If the team manager approves the request, the game is saved in the system and by clicking on games button, everyone can see the result of the game.

<img src="https://user-images.githubusercontent.com/65177459/108539401-9e5ff500-72e8-11eb-952b-ebf020170d2b.jpg" width="350" height="500">

# Main Flow : A player is interested join the team
The player checks which teams exist in the system by clicking on available team.

<img src="https://user-images.githubusercontent.com/65177459/108539573-d8c99200-72e8-11eb-9932-6cc84b7815bb.jpg" width="350" height="500">

* The player selects the team he wants to join and sends her a message or a request to join.

<img src="https://user-images.githubusercontent.com/65177459/108539339-8c7e5200-72e8-11eb-821a-e60695c94abf.jpg" width="350" height="500">

* The player chats with the team manager, to check if the team is interested in another player and see if the team is suitable for him.

<img src="https://user-images.githubusercontent.com/65177459/108591061-5e902080-736f-11eb-9b11-f3f7a472ebc5.jpg" width="350" height="500">

* The team manager decides whether to approve the request or not. The player is updated according to the decision of the team manager.
  If the team manager decided to approve it, the next time the player clicks on "My Team" button, it shows details about the new team.

<img src="https://user-images.githubusercontent.com/65177459/108545701-cce1ce00-72f0-11eb-8e65-b320818c9a35.jpg" width="350" height="500">

# Clicking on Create team

Allows the user to open a new team, add players, write description for the team and choose the team logo. 
The logo will be chosen with the help of Recycle View.

<img src="https://user-images.githubusercontent.com/65177459/108539874-36f67500-72e9-11eb-9349-ec9973f0de00.jpg" width="350" height="500">

# Clicking on chat
Displays all the people the user has talked with and their status (online or offline).

<img src="https://user-images.githubusercontent.com/65177459/108538845-e7fc1000-72e7-11eb-8da1-ebd759d89e71.jpg" width="350" height="500">

# Clicking on Players
Displays all registered users in the application and their details.

<img src="https://user-images.githubusercontent.com/65177459/108539191-56d96900-72e8-11eb-9457-86bd0e418471.jpg" width="350" height="500">
