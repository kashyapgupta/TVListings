package mobi.wrapper.listings.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import mobi.wrapper.listings.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rohit on 5/24/2016.
 */
public class AboutUsActivity extends Activity {

    @Bind(R.id.activity_about_us_content_image)
    ImageView mTMDBimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        mTMDBimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org"));
                startActivity(intent);
            }
        });
    }
}
