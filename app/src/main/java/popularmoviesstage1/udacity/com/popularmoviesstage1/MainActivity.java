package popularmoviesstage1.udacity.com.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

//The MainActivity is the activity that displays the movies in a grid
public class MainActivity extends AppCompatActivity {

    GridView movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting a reference to the grid defined in the layout
        movieList = findViewById(R.id.gv_movie_list);

        //Setting the GridView's columns to be two, so the user can see the posters nice and clear
        movieList.setNumColumns(2);

        //Adding an OnClickListener to open DetailActivity when a movie is clicked
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Getting the movie by grabbing associated data from the view with a key
                Movie m = (Movie) view.getTag(MovieAdapter.MovieDataTag);
                //Opening the DetailActivity with the specified movie
                openDetailActivity(m);
            }
        });

        //Running the getMovies method in our MovieUtils, to get the data, we pass in a callback
        //that triggers when the data is fetched
        MovieUtils.getMovies(this, true, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Using a try/catch block because parsing json can sometimes return errors
                try {
                    //When we get the data, we will immediately convert it to a Movie[] using
                    //our MovieUtils parseMovieJson method so the adapter can interpret it
                    Movie[] movies = MovieUtils.parseMovieJson(response);

                    //Now, we can set the adapter of the movieList to be a new MovieAdapter that
                    //knows our current context and the movie data
                    movieList.setAdapter(new MovieAdapter(MainActivity.this, movies));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    //openDetailActivity() function will open the detail activity

    //We need a parameter for the movie data so the DetailActivity
    //knows the data to display
    private void openDetailActivity(Movie movie) {
        Intent launchIntent = new Intent(MainActivity.this, DetailActivity.class);
        launchIntent.putExtra(DetailActivity.MOVIE_DATA, movie);
        startActivity(launchIntent);

    }

    //Overriding onOptionsItemSelected to take action when a menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Having typeState determine which data source to query from (top_rated or popular)
        boolean typeState = false;
        switch (item.getItemId()) {

            case R.id.item_popular:
                typeState = true;
                break;

            case R.id.item_top_rated:
                typeState = false;
                break;
        }
        item.setChecked(true);
        //Then querying the data based on which menu item was clicked
        MovieUtils.getMovies(this, typeState, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Using a try/catch block because parsing json can sometimes return errors
                try {
                    //When we get the data, we will immediately convert it to a Movie[] using
                    //our MovieUtils parseMovieJson method so the adapter can interpret it
                    Movie[] movies = MovieUtils.parseMovieJson(response);

                    //Now, we can set the adapter of the movieList to be a new MovieAdapter that
                    //knows our current context and the movie data
                    movieList.setAdapter(new MovieAdapter(MainActivity.this, movies));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);

        return super.onOptionsItemSelected(item);
    }
    //Overriding onCreateOptionsMenu to inflate our menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //Inflating our moviepickmenu with the menu inflater
        inflater.inflate(R.menu.moviepickmenu, menu);
        return true;
    }
}
