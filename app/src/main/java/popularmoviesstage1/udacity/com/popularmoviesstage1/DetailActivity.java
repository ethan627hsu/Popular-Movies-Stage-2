package popularmoviesstage1.udacity.com.popularmoviesstage1;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

//DetailActivity displays the movie data in detail
public class DetailActivity extends AppCompatActivity {

    //Declaring the base url to fetch images from the image database so we can query image data into
    //the poster ImageView, we use w500 resolution to load the images
    //in a much higher resolution
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    //MOVIE_DATA is a variable used to store the selected movie in the bundle passed to this activity
    public static String MOVIE_DATA = "movie_data";

    //displayMovie holds the selected movie, declared as class field so that other functions (other
    //than onCreate) can use the data
    private Movie displayMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
    }

}
