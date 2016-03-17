package com.tvlistings.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tvlistings.R;

import butterknife.ButterKnife;

/**
 * Created by Rohit on 3/14/2016.
 */
public abstract class BaseListingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
        ButterKnife.bind(this);
    }

    protected abstract int getContentViewId();
}
