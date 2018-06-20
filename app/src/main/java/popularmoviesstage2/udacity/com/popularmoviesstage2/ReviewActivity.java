package popularmoviesstage2.udacity.com.popularmoviesstage2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        String movieTitle = getIntent().getStringExtra("movie_title");
        ArrayList<Review> reviews = getIntent().getParcelableArrayListExtra("review_data");

        getSupportActionBar().setTitle(movieTitle + " Reviews");

        ListView reviewList = findViewById(R.id.review_list);

        reviewList.setAdapter(new ReviewAdapter(this,reviews));

        reviewList.setDivider(null);


    }
}
