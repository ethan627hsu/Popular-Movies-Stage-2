package popularmoviesstage2.udacity.com.popularmoviesstage2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//DetailActivity displays the movie data in detail
public class DetailActivity extends AppCompatActivity {

    private static final String VIDEO_BASE_URL = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAM = "v";
    //MOVIE_DATA is a variable used to store the selected movie in the bundle passed to this activity
    public static String MOVIE_DATA = "movie_data";
    //Declaring the base url to fetch images from the image database so we can query image data into
    //the poster ImageView, we use w500 resolution to load the images
    //in a much higher resolution
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private boolean starState = false;
    private Integer moviePlace;
    private Context context;
    //displayMovie holds the selected movie, declared as class field so that other functions (other
    //than onCreate) can use the data
    private Movie displayMovie;

    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;

        final float DISPLAY_DENSITY = this.getResources().getDisplayMetrics().density;
        //Getting the movie that was passed from the MainActivity
        displayMovie = (Movie) getIntent().getSerializableExtra(MOVIE_DATA);

        //Setting the action bar title to be the title of the current movie
        getSupportActionBar().setTitle(displayMovie.getMovieTitle());

        //Finding all of the elements by their ids
        ImageView poster = findViewById(R.id.iv_poster);
        TextView dateText = findViewById(R.id.tv_date);
        TextView ratingText = findViewById(R.id.tv_rating);
        TextView descriptionText = findViewById(R.id.tv_description);


        //Loading the movie poster using a context and the ImageView with the poster
        // with the same strategy as the implementation in MovieAdapter.java's getView()
        Picasso.with(this).load(IMAGE_BASE_URL + displayMovie.getMoviePoster()).into(poster);

        //Binding the text data with the corresponding TextViews
        dateText.setText(displayMovie.getReleaseDate());
        //Using the double's toString method to display the double as a string
        ratingText.setText(displayMovie.getVoteAverage().toString() + "/10 Rating");
        descriptionText.setText(displayMovie.getPlotSynopsis());

       /* Trailer trailer  = new Trailer();
        trailer.setTitle("A Cool Trailer");
        Trailer trailer2  = new Trailer();
        trailer2.setTitle("A Fun Trailer");
        Trailer trailer3  = new Trailer();
        trailer3.setTitle("Weird Trailer");
        Trailer trailer4  = new Trailer();
        trailer4.setTitle("Wacky Trailer");

        ArrayList<Trailer> trailers = new ArrayList<>();
        trailers.add(trailer);
        trailers.add(trailer2);
        trailers.add(trailer3);
        trailers.add(trailer4);
        trailers.add(trailer);*/

        scrollView = findViewById(R.id.parent_scroll);

        final ListView trailerList = findViewById(R.id.lv_trailers);

        trailerList.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollView.smoothScrollTo(0, 0);
            }
        });

        MovieUtils.getTrailers(this, displayMovie.getId(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Trailer> trailers = MovieUtils.parseTrailerJson(response);
                    TrailerAdapter adapter = new TrailerAdapter(context, trailers);

                    ViewGroup.LayoutParams parameters = trailerList.getLayoutParams();
                    parameters.height = (int) (trailers.size() * 64 * DISPLAY_DENSITY) + 16;
                    trailerList.setLayoutParams(parameters);
                    trailerList.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);


        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoId = (String) view.getTag();
                Uri videoSite = Uri.parse(VIDEO_BASE_URL).buildUpon().
                        appendQueryParameter(VIDEO_PARAM, videoId).build();
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, videoSite);
                startActivity(videoIntent);
            }
        });

        scrollView.smoothScrollTo(0, 0);

        Button reviewButton = findViewById(R.id.bn_reviews);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MovieUtils.getReviews(context, displayMovie.getId(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<Review> reviews = MovieUtils.parseReviewJson(response);

                            Intent launchIntent = new Intent(context, ReviewActivity.class);
                            launchIntent.putExtra("movie_title", displayMovie.getMovieTitle());
                            launchIntent.putExtra("review_data", reviews);

                            startActivity(launchIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);

            }
        });

        new GetMovieIdTask().execute(displayMovie);

        final Drawable borderStar = getDrawable(R.drawable.ic_star_border_black_24dp);
        final Drawable fillStar = getDrawable(R.drawable.ic_star_yellow_24dp);
        final ImageView starImage = findViewById(R.id.iv_star);


        final ContentResolver contentResolver = getContentResolver();
        starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (starState == true) {

                    Integer i = contentResolver.delete(FavoritesContract.BASE_CONTENT_URI,
                            FavoritesContract.FavoritesEntry._ID + "=" + moviePlace.toString(), null);


                    moviePlace = null;

                    starState = false;
                    starImage.setImageDrawable(borderStar);


                } else {
                    moviePlace = Integer.parseInt(contentResolver.insert(FavoritesContract.BASE_CONTENT_URI,
                            parseMovie(displayMovie)).getPathSegments().get(0));


                    starState = true;
                    starImage.setImageDrawable(fillStar);
                }
            }
        });
    }

    private ContentValues parseMovie(Movie m) {
        ContentValues returnValues = new ContentValues();
        returnValues.put(FavoritesContract.FavoritesEntry.COLUMN_DATE, m.getReleaseDate());
        returnValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, m.getId());
        returnValues.put(FavoritesContract.FavoritesEntry.COLUMN_PLOT, m.getPlotSynopsis());
        returnValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER, m.getMoviePoster());
        returnValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, m.getMovieTitle());
        returnValues.put(FavoritesContract.FavoritesEntry.COLUMN_VOTE, m.getVoteAverage());

        return returnValues;
    }

    public class GetMovieIdTask extends AsyncTask<Movie, Void, Integer> {

        @Override
        protected Integer doInBackground(Movie... params) {

            ContentResolver resolver = getContentResolver();

            String[] projection = new String[]{FavoritesContract.FavoritesEntry._ID};
            String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
            String[] args = new String[]{params[0].getId()};
            Cursor cursor = resolver.query(FavoritesContract.BASE_CONTENT_URI,
                    projection, selection, args, null);

            if (cursor.getCount() == 0) {
                return null;
            } else {
                cursor.moveToFirst();
                return cursor.getInt(0);
            }
        }


        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);

            ImageView star = findViewById(R.id.iv_star);
            final Drawable fillStar = getDrawable(R.drawable.ic_star_yellow_24dp);
            final Drawable borderStar = getDrawable(R.drawable.ic_star_border_black_24dp);

            if (i == null) {
                moviePlace = null;
                starState = false;
                star.setImageDrawable(borderStar);
            } else {
                moviePlace = i;
                starState = true;
                star.setImageDrawable(fillStar);
            }
        }
    }

}
