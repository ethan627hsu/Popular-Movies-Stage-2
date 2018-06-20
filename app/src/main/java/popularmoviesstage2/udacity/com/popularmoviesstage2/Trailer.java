package popularmoviesstage2.udacity.com.popularmoviesstage2;
//Trailer is a small class for storing objects of trailer data
public class Trailer {
    //To store the title and youtube video key of the trailer
private String title;
private String id;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Trailer() {
    }
}
