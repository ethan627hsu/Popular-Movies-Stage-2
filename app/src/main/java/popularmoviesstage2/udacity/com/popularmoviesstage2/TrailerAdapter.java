package popularmoviesstage2.udacity.com.popularmoviesstage2;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//TrailerAdapter is an adapter to display movie trailer listings in a list
public class TrailerAdapter extends ArrayAdapter<Trailer>{

    //A variable to store the current trailers
    ArrayList<Trailer> data;
    //A context to access tools with
    Context context;

    //A constructor to receive the data and context
    public TrailerAdapter(@NonNull Context context, @NonNull List<Trailer> objects) {
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

        View trailerCard = inflater.inflate(R.layout.card_trailer, null);
        TextView trailerTitle = trailerCard.findViewById(R.id.tv_trailer_title);
        trailerTitle.setText(data.get(position).getTitle());

        trailerCard.setTag(data.get(position).getId());

        return trailerCard;
    }
}
