package popularmoviesstage1.udacity.com.popularmoviesstage1;

import java.io.Serializable;

//The Movie class stores the data associated with one movie

//Hiding compiler warnings so our function can pass as serializable
@SuppressWarnings("serial")
public class Movie implements Serializable{

    //Specifying the needed data as fields for the class

    //The movie titles and movie synopsis are pure Strings
    private String movieTitle;
    private String plotSynopsis;

    //The release date is the returned date in its original String format
    private String releaseDate;

    //The movie poster contains the file name for the image that leads to the movie poster
    private String moviePoster;

    //The vote average is a decimal number 1-10 that determines how well users liked the movie
    private Double voteAverage;

    //A blank constructor so we can initialize a new instance of the Movie class
    public Movie() {
    }

    //Appropriate getters and setters to change and access all of the values of the data
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
