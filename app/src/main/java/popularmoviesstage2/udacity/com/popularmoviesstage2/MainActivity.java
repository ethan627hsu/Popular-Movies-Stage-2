package popularmoviesstage2.udacity.com.popularmoviesstage2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static popularmoviesstage2.udacity.com.popularmoviesstage2.MovieUtils.getMovies;
import static popularmoviesstage2.udacity.com.popularmoviesstage2.MovieUtils.parseMovieJson;

//The MainActivity is the activity that displays the movies in a grid
public class MainActivity extends AppCompatActivity {

    GridView movieList;
    Context context;
    Integer listState;
    Parcelable gridState = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        context = this;


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

        if(savedInstanceState != null){
            gridState = savedInstanceState.getParcelable("list");
            listState = savedInstanceState.getInt("menu");
            switch (listState) {
                case 2:
                    new GetMovies().execute();
                    break;
                case 0:
                    fetchandSetMovies(true);
                    break;
                case 1:
                    fetchandSetMovies(false);
                    break;
            }
        }
        else {
            listState = 0;
            fetchandSetMovies(true);
        }

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

            case R.id.item_favorites: {
                listState = 2;
                new GetMovies().execute();
                break;
            }

            case R.id.item_popular:
                listState = 0;
                typeState = true;
                break;

            case R.id.item_top_rated:
                listState = 1;
                typeState = false;
                break;
        }
        item.setChecked(true);
        if (item.getItemId() != R.id.item_favorites) {
            fetchandSetMovies(typeState);
        }
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

    @Override
    protected void onStart() {
        super.onStart();

        GridView grid = findViewById(R.id.gv_movie_list);
        grid.setAdapter(null);
        switch (listState) {
            case 2:
                new GetMovies().execute();
                break;
            case 0:
                fetchandSetMovies(true);
                break;
            case 1:
                fetchandSetMovies(false);
                break;
        }
    }

    public void fetchandSetMovies(boolean typeState) {
        //Then querying the data based on which menu item was clicked
        getMovies(this, typeState, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Using a try/catch block because parsing json can sometimes return errors
                try {
                    //When we get the data, we will immediately convert it to a Movie[] using
                    //our MovieUtils parseMovieJson method so the adapter can interpret it
                    Movie[] movies = parseMovieJson(response);

                    //Now, we can set the adapter of the movieList to be a new MovieAdapter that
                    //knows our current context and the movie data
                    movieList.setAdapter(new MovieAdapter(MainActivity.this, movies));

                    if(gridState != null) {
                        movieList.onRestoreInstanceState(gridState);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    public class GetMovies extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {

            ContentResolver resolver = getContentResolver();

            String[] projection = null;
            String selection = null;
            String[] args = null;
            Cursor c = resolver.query(FavoritesContract.BASE_CONTENT_URI,
                    projection, selection, args, null);

            if (c.getCount() == 0) {
                return null;
            } else {
                c.moveToFirst();
                Integer size = c.getCount();
                Log.v("THESIZE", size.toString());
                ArrayList<Movie> movies = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Movie returnMovie = new Movie();
                    returnMovie.setId(c.getString(1));
                    returnMovie.setMovieTitle(c.getString(2));
                    returnMovie.setPlotSynopsis(c.getString(3));
                    returnMovie.setReleaseDate(c.getString(4));
                    returnMovie.setMoviePoster(c.getString(5));
                    returnMovie.setVoteAverage(c.getDouble(6));
                    movies.add(returnMovie);
                    c.moveToNext();
                }
                return movies;
            }
        }


        @Override
        protected void onPostExecute(ArrayList<Movie> m) {
            super.onPostExecute(m);
            GridView grid = findViewById(R.id.gv_movie_list);
            if (m != null) {
                grid.setAdapter(new MovieAdapter(context, m.toArray(new Movie[m.size()])));
                if(gridState != null) {
                    movieList.onRestoreInstanceState(gridState);
                }
            } else {
                grid.setAdapter(null);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("menu",listState);
        outState.putParcelable("list",movieList.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("RUNSTATE","restore ran well");


    }
}
