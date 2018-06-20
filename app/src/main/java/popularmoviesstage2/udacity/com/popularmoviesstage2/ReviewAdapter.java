package popularmoviesstage2.udacity.com.popularmoviesstage2;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {//A variable to store the current trailers
    ArrayList<Review> data;
    //A context to access tools with
    Context context;

    //A constructor to receive the data and context
    public ReviewAdapter(@NonNull Context context, @NonNull List<Review> objects) {
        super(context,0,objects);
        data = new ArrayList<>(objects);
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Getting the LayoutInflater from the context
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View reviewCard = inflater.inflate(R.layout.card_review, null);
        TextView reviewAuthor = reviewCard.findViewById(R.id.tv_review_author);
        TextView reviewContent = reviewCard.findViewById(R.id.tv_review_content);
        reviewAuthor.setText(data.get(position).getAuthor() + ":");
        reviewContent.setText(data.get(position).getContent());

        return reviewCard;
    }
}
