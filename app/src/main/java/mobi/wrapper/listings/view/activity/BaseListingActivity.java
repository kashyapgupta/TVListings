package mobi.wrapper.listings.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Rohit on 3/14/2016.
 */
public abstract class BaseListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
        ButterKnife.bind(this);
    }

    protected abstract int getContentViewId();
}
