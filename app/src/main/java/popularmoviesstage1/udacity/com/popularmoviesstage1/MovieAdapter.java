package popularmoviesstage1.udacity.com.popularmoviesstage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

//MovieAdapter is an adapter for a RecyclerView/GridView that will display and hold movie data
//that has been passed into it, it extends BaseAdapter, a good template for building an adapter
public class MovieAdapter extends BaseAdapter {

    //Creating resource ids for using tags (to hold data) with the views
    //Using sample resource ids so that we do not have to create our own
    public static int ViewHolderTag = R.id.SHIFT;
    public static int MovieDataTag = R.id.CTRL;

    //Declaring the base url to fetch images from the image database so we can query image data into
    //the ImageViews of the movie cards, we use w500 resolution to load the images
    //in a much higher resolution
    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    //We need a context to do certain actions like querying images and inflating layouts
    private Context context;

    //movieData is a variable that holds the data for use
    private Movie[] movieData;

    //The constructor to use the MovieAdapter, we need a context and the movie data
    public MovieAdapter(Context c, Movie[] movies) {
        context = c;
        movieData = movies;
    }

    //Overriding the required method to get the size of the data, we return the length
    //of the movie array in our case
    @Override
    public int getCount() {
        return movieData.length;
    }

    //Overriding the required method to get the data at a certain index, in our case,
    //we can return an index of our Movie[], that is specified through the position param
    @Override
    public Object getItem(int position) {
        return movieData[position];
    }

    //Overriding a required method that we do not need, so we return 0
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //The getView method is used by the GridView to get the current view that contains the data
    //at a certain position
    //We use convertView as the base for adding and inflating views for the card
    //We use parent to be the root layout for the inflation
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Storing an instance of our MovieViewHolder, so we don't have to call findViewById so many times
        MovieViewHolder viewHolder;

        //Getting the MovieViewHolder for this particular "card" based on whether we need to make
        //a new card, with a MovieViewHolder, or retrieve an existing one

        //In both cases, we use the View's tag system to get and set associated data with the view
        if (convertView == null) {
            //When this card has no specified view layout...

            //Inflate the movie_card layout into convertView with our specified context
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.movie_card, parent, false);

            //Creating a new ViewHolder based on our movie_card layout
            viewHolder = new MovieViewHolder(convertView);

            //Storing the associated viewHolder with the card
            convertView.setTag(ViewHolderTag, viewHolder);
        } else {

            //If a movie card has already been initialized, then get the specified MovieViewHolder
            viewHolder = (MovieViewHolder) convertView.getTag(ViewHolderTag);
        }
        //Binding the ImageView with the specified movie poster by fetching the movie poster
        //with Picasso
        Picasso.
                //We need a context to use the Picasso image loader tools
                with(context).

                //We build our url to fetch the specified movie poster by appending the movie
                //poster's id to a base url that points to theMovieDB image database

                //Picasso's .load method will get the image from the web
                load(IMAGE_BASE_URL + movieData[position].getMoviePoster())
                //Then, we specify that we want to load the fetched image into our imageview
                //We get the imageview by using a reference within our ViewHolder rather
                //than an expensive findViewById() call
                .into(viewHolder.poster);

        //Storing the associated movie data with the card so we can retrieve it
        //when using onItemClickListener()
        convertView.setTag(MovieDataTag, getItem(position));

        //At the end, we return our end result movie card
        return convertView;
    }

    //We define a custom ViewHolder called MovieViewHolder so that we can prevent long and
    //expensive findViewById searches for views
    private class MovieViewHolder {
        //We need to store a reference to the movie poster ImageVIew in our ViewHolder
        ImageView poster;

        //Creating a constructed that takes in an entire movie_card layout and
        //grabs the movie_poster accordingly, once and for all!
        public MovieViewHolder(View view) {
            poster = view.findViewById(R.id.movie_poster);
        }
    }
}