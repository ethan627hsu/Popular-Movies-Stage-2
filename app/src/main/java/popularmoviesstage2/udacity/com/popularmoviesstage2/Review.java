package popularmoviesstage2.udacity.com.popularmoviesstage2;
import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String content;
    private String author;

    public Review() {

    }

    protected Review(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);

        //Getting the data from the parse array
        this.content = data[0];
        this.author = data[1];
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.content,
                this.author,});
    }
}
