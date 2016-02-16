## Project Overview

Application for showing most popular / highest-rared movies info.

* present the user with an grid arrangement of movie posters. The sort order can be by most popular, or by highest-rated.
* tap on a movie poster and transition to a details screen with additional information such as:
  * original title
  * movie poster image thumbnail
  * a plot synopsis (called overview in the api)
  * user rating (called vote_average in the api)
  * release date

* view and play trailers ( either in the youtube app or a web browser).
* read reviews of a selected movie.
* mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection and does not require an API request.
* modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

## Usage

You need to provide your own movie api key under the _MOVIE_API_KEY_ key. Prefered way to do this is to write ```MOVIE_API_KEY="my_api_key``` 
