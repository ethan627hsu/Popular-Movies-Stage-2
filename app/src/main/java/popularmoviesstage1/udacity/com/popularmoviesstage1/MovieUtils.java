package popularmoviesstage1.udacity.com.popularmoviesstage1;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//The MovieUtils class provides tools for accessing the movie data through theMovieDatabase API and
//a method to build the query URL for movie poster images as well as a method to parse json data to the Movie object format
public class MovieUtils {
    //Adding constant for the key of the results array that holds the movie data in the request json data
    private static final String RESULTS = "results";

    //Adding the keys to access the specific movie data in their movie object
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String POSTER = "poster_path";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String PLOT = "overview";

    //Declaring constants for the specific keys and paths we need to build our request

    //MOVIE_BASE_URL is the main part of the request that tells Android that we want to get movie
    //data from theMovieDatabase API, version 3
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";

    //PARAM_API_KEY has the key that we use to define the parameter that holds the API key to use
    //the database with permission
    private static final String PARAM_API_KEY = "api_key";

    //PATH_POPULAR is a string that will be added to the end of the base URL if the user wants
    //to fetch the most popular movies
    private static final String PATH_POPULAR = "popular";

    //PATH_TOP_RATED is a string that will be added to the end of the base URL if the user wants
    //to fetch the highest rated movies
    private static final String PATH_TOP_RATED = "top_rated";

    //The API_KEY a variable that will hold the API key to be used in the request. It is a variable
    //who's value will be changed when the getMovies function gets a context that is able to access
    //the string resources in strings.xml
    private static String API_KEY;


    //getMovies will get the first page of movies (around 20), based on the specified sort type

    //We need a context so we can access the string resources to get our API key and to use volley

    //Passing in true as the sort type will query the most popular movies and passing in false will
    //query the top rated movies

    //Finally, we need to specify the action to be taken once the JSON has been fetched from volley
    //and the action to be taken when there is an error
    public static void getMovies(final Context context, Boolean sortType,
                                 Response.Listener<JSONObject> networkResponse, Response.ErrorListener errorResponse) {
        //We are declaring a Uri builder called buildingUri that has our base URL in mind
        Uri.Builder buildingUri = Uri.parse(MOVIE_BASE_URL).buildUpon();

        if (sortType) {
            //If the passed in sortType is true, then append the value PATH_POPULAR to the end
            //of the path so we can fetch the popular movies
            buildingUri.appendPath(PATH_POPULAR);
        } else {
            //If the passed in sortType is false, then append the value PATH_TOP_RATED to the end
            //of the path so we can fetch the highest ratred movies
            buildingUri.appendPath(PATH_TOP_RATED);
        }

        //Making the API_KEY variable be equal to the API key specified as string resource
        //in strings.xml
        API_KEY = context.getString(R.string.api_key);

        //Adding the API key as a parameter (with the value of PARAM_API_KEY as the name and the
        //API_KEY as the value) to our request so we can access the data with allowed permission
        buildingUri.appendQueryParameter(PARAM_API_KEY, API_KEY);

        //Specifying the URL variable to hold the request URL outside, so we can access it inside
        //of the try/catch block
        URL requestURL = null;
        try {
            //Having requestURL equal the value of our built Uri that includes the
            // API_KEY parameter and the base path
            requestURL = new URL(buildingUri.build().toString());
        } catch (MalformedURLException e) {
            //Handling the error by printing out the stacktrace that shows where the error
            //came from and why it happened
            e.printStackTrace();
        }

        //Getting the queue that does the requests on the background thread
        RequestQueue queue = Volley.newRequestQueue(context);


        // Requesting a string response from the request
        JsonObjectRequest movieRequest = new JsonObjectRequest(Request.Method.GET, requestURL.toString(),
                null,
                //Listening for the response so we can take action with the json response or the error
                networkResponse, errorResponse);

        // Adding the request to the background thread.
        queue.add(movieRequest);

    }

    //parseMovieJson() turns the response JSONObject into a easy-to-use Movie array
    //Our function will throw a JSONException if it cannot find data at a certain location
    public static Movie[] parseMovieJson(JSONObject json) throws JSONException {

        //Getting the part of the response with the movie data in it
        JSONArray movieJsonArray = json.getJSONArray(RESULTS);

        //Declaring a Movie ArrayList that we can add on values to called movieArrayList
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        //For every index in the response movie array...
        for (int i = 0; i < movieJsonArray.length(); i++) {
            //Get the data of the index
            JSONObject movieJson = movieJsonArray.getJSONObject(i);

            //Create a new movie
            Movie buildMovie = new Movie();

            //Add specified data to it
            buildMovie.setMovieTitle(movieJson.getString(TITLE));
            buildMovie.setReleaseDate(movieJson.getString(RELEASE_DATE));
            buildMovie.setMoviePoster(movieJson.getString(POSTER));
            buildMovie.setVoteAverage(movieJson.getDouble(VOTE_AVERAGE));
            buildMovie.setPlotSynopsis(movieJson.getString(PLOT));

            //And add it back to the movieArrayList
            movieArrayList.add(buildMovie);
        }
        //At the end, return the data in the form of a Movie array
        return movieArrayList.toArray(new Movie[0]);
    }

}
