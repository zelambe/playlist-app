Problems with my app

Whenever you search for something, the keyboard pushes up the list fragments
that I have, so the search box gets covered (but it works!)

Also, whenever you add as song to the playlist, the app will crash if you try
to go back to the main activity. This is caused by an indexing error in my
oberver for my playlist that I dont know how to fix

Also, i accidently put my search fragment inside of my viewmodel folder

Creative Portion:

Songs in the playist have the picture of the album art on the side

(10 / 10 points) The app displays the current top tracks in a GridView on startup
(10 / 10 points) The app uses a tab bar with two tabs, one for searching for tracks and one for looking at the playlist
(10 / 10 points) Data is pulled from the API and processed into a GridView on the main page. Makes use of a Fragment to display the results seamlessly.
(15 / 15 points) Selecting a track from the GridView opens a new activity with the track cover, title, and 3 other pieces of information as well as the ability to save it to the playlist.
(5 / 5 points) User can change search query by editing text field.
(10 / 10 points) User can save a track to their playlist, and the track is saved into a SQLite database.
	The app crashes as you describe. To fix it, just add some checks to the observer that make sure the index is in bounds. (-5)
(0 / 5 points) User can delete a track from the playlist (deleting it from the SQLite database itself).
	I wasn't able to find this... (-5)
(4 / 4 points) App is visually appealing
(1/ 1 point) Properly attribute Last.fm API as source of data.
(5 / 5 points) Code is well formatted and commented.
(10 / 10 points) All API calls are done asynchronously and do not stall the application.
(5 / 15 points) Creative portion: Be creative!
	A nice addition, but not rigorous enough to get full credit. 

Total: 80 / 100

