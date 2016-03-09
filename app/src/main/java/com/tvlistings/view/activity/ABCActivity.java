package com.tvlistings.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.tvlistings.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rohitgarg on 3/9/16.
 */
public class ABCActivity extends Activity {
    @Bind(R.id.activity_abc_title_text_view)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc);
        ButterKnife.bind(this, this);
    }
}
