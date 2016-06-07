package mobi.wrapper.listings.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import mobi.wrapper.listings.R;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.InputFilterMinMax;
import mobi.wrapper.listings.controller.MultiSpinner;
import mobi.wrapper.listings.controller.factory.TVListingServiceFactory;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.controller.service.GenresService;
import mobi.wrapper.listings.controller.service.SearchService;
import mobi.wrapper.listings.controller.service.ServiceCallbacks;
import mobi.wrapper.listings.model.BaseResponse;
import mobi.wrapper.listings.model.genresList.MovieGenresList;
import mobi.wrapper.listings.model.genresList.ShowGenresList;
import mobi.wrapper.listings.model.keywordsSearchResult.KeywordSearchResult;
import mobi.wrapper.listings.view.adapter.KeywordsRecyclerViewAdapter;
import mobi.wrapper.listings.view.callback.LoadMoreData;
import mobi.wrapper.listings.view.callback.SelectedKeyword;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apmem.tools.layouts.FlowLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rohit on 4/15/2016.
 */
public class DiscoverHomeActivity extends Activity implements ServiceCallbacks,LoadMoreData,SelectedKeyword {
    @Bind(R.id.activity_discover_home_sort_by_spinner)
    Spinner mSortBySpinner;

    @Bind(R.id.activity_discover_home_type_spinner)
    Spinner mTypeSpinner;

    @Bind(R.id.activity_discover_home_select_max_vote_count_text_view)
    TextView mMaxVotesTextView;

    @Bind(R.id.activity_discover_home_select_max_rating_text_view)
    TextView mMaxRatingTextView;

    @Bind(R.id.activity_discover_home_no_keyword_text_view)
    TextView mNoKeywordTextView;

    @Bind(R.id.activity_discover_home_keyword_loading_progressBar)
    ProgressBar mKeywordProgressBar;

    @Bind(R.id.activity_discover_home_keyword_frame_layout)
    FrameLayout mKeywordsFrameLayout;

    @Bind(R.id.activity_discover_home_genres_spinner)
    MultiSpinner mGenresSpinner;

    @Bind(R.id.activity_discover_home_select_max_release_date_button)
    Button mSelectMaxDateButton;

    @Bind(R.id.activity_discover_home_select_min_release_date_button)
    Button mSelectMinDateButton;

    @Bind(R.id.activity_discover_home_select_min_release_date_text_view)
    TextView mMinDateTextView;

    @Bind(R.id.activity_discover_home_select_max_release_date_text_view)
    TextView mMaxDateTextView;

    @Bind(R.id.activity_discover_home_select_keywords_recycler_view)
    RecyclerView mKeywordsRecyclerView;

    @Bind(R.id.activity_discover_home_select_keywords_edit_text)
    EditText mKeywordsEditText;

    @Bind(R.id.activity_discover_home_select_max_vote_count_edit_text)
    EditText mMaxVotesEditText;

    @Bind(R.id.activity_discover_home_select_min_vote_edit_text)
    EditText mMinVotesEditText;

    @Bind(R.id.activity_discover_home_discover_button)
    Button mDiscoverButton;

    @Bind(R.id.activity_discover_home_select_keywords_flow_layout)
    FlowLayout mKeywordFlowLayout;

    @Bind(R.id.activity_discover_home_select_max_rating_edit_text)
    EditText mMaxRatingEditText;

    @Bind(R.id.activity_discover_home_select_min_rating_edit_text)
    EditText mMinRatingEditText;

    LinearLayoutManager mKeywordLinearLayoutManager;
    KeywordsRecyclerViewAdapter mKeywordsRecyclerViewAdapter;

    List<String> year = new ArrayList<>();
    private int mCurrentPage = 0;
    private int mPageCount;
    KeywordSearchResult mKeywordSearchResult;

    ArrayList<String> showGenres = new ArrayList<>();
    ArrayList<Integer> showGenresIds = new ArrayList<>();
    ArrayList<String> movieGenres = new ArrayList<>();
    ArrayList<Integer> movieGenresIds = new ArrayList<>();

    RequestQueue mQueue;
    Context mContext;

    String mSortTextId = "popularity.desc";
    String mType;
    String mSearchKeyword;
    ArrayList<Integer> mMultipleKeywordsIds = new ArrayList<>();
    private ArrayList<String> mMultipleKeywords = new ArrayList<>();
    ArrayList<Integer> mSelectedGenres = new ArrayList<>();
    int mMinVotes = 0, mMaxVotes = 0;
    double mMinRating = 0.0, mMaxRating = 0.0;
    String mMinReleaseDate, mMaxReleaseDate;
    String DISCOVER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mContext = this;
        setContentView(R.layout.activity_discover_home);
        ButterKnife.bind(this);

        mSelectMaxDateButton.setBackgroundResource(R.mipmap.ic_date_range_white_48dp);
        mSelectMinDateButton.setBackgroundResource(R.mipmap.ic_date_range_white_48dp);

        mMaxRatingEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1.0, 10.0)});
        mMinRatingEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0.0, 9.0)});

        ((GenresService) TVListingServiceFactory.getInstance().getService(GenresService.class)).getMovieGenres(DiscoverHomeActivity.this);
        ((GenresService) TVListingServiceFactory.getInstance().getService(GenresService.class)).getShowGenres(DiscoverHomeActivity.this);

        mKeywordsRecyclerView.setHasFixedSize(true);
        mKeywordLinearLayoutManager = new LinearLayoutManager(this);
        mKeywordsRecyclerView.setLayoutManager(mKeywordLinearLayoutManager);
        mKeywordsRecyclerViewAdapter = new KeywordsRecyclerViewAdapter(mQueue, this);
        mKeywordsRecyclerView.setAdapter(mKeywordsRecyclerViewAdapter);

        mSelectedGenres.clear();
        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter
                .createFromResource(mContext, R.array.movie_sort_by,
                        android.R.layout.simple_spinner_item);

        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSortBySpinner.setAdapter(sortByAdapter);

        mKeywordsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchKeyword = String.valueOf(s);
                mCurrentPage = 0;
                mKeywordsFrameLayout.setVisibility(View.VISIBLE);
                mKeywordProgressBar.setVisibility(View.VISIBLE);
                if (mSearchKeyword.isEmpty()) {
                    mKeywordProgressBar.setVisibility(View.GONE);
                    mKeywordsFrameLayout.setVisibility(View.GONE);
                }
                mSearchKeyword = mSearchKeyword.trim();
                try {
                    mSearchKeyword = URLEncoder.encode(mSearchKeyword, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (mKeywordProgressBar.getVisibility() == View.VISIBLE) {
                    mKeywordsRecyclerViewAdapter.clearData();
                }
                ((SearchService) TVListingServiceFactory.getInstance().getService(SearchService.class)).searchKeyword(mSearchKeyword, mCurrentPage, DiscoverHomeActivity.this);
            }
        });

        String[] types = new String[]{"Movies", "TV Shows"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(typeAdapter);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mType = (String) parent.getItemAtPosition(position);
                if (mType.equalsIgnoreCase("Movies")) {
                    mSelectedGenres.clear();
                    ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter
                            .createFromResource(mContext, R.array.movie_sort_by,
                                    android.R.layout.simple_spinner_item);

                    sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mSortBySpinner.setAdapter(sortByAdapter);

                    mMaxVotesTextView.setVisibility(View.VISIBLE);
                    mMaxRatingTextView.setVisibility(View.VISIBLE);
                    mMaxVotesEditText.setVisibility(View.VISIBLE);
                    mMaxRatingEditText.setVisibility(View.VISIBLE);

                    DISCOVER_URL = String.format(UrlConstants.DISCOVER_URL, "movie");

                    if (movieGenres != null && movieGenres.size() > 0) {

                        mGenresSpinner.setItems(movieGenres, "Select", new MultiSpinner.MultiSpinnerListener() {
                            @Override
                            public void onItemsSelected(boolean[] selected) {
                                mSelectedGenres.clear();
                                for (int i = 0; i < selected.length; i++) {
                                    if (selected[i]) {
                                        mSelectedGenres.add(movieGenresIds.get(i));
                                    }
                                }
                            }
                        });
                    }
                } else {
                    mSelectedGenres.clear();
                    ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter
                            .createFromResource(mContext, R.array.tv_shows_sort_by,
                                    android.R.layout.simple_spinner_item);

                    sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSortBySpinner.setAdapter(sortByAdapter);

                    DISCOVER_URL = String.format(UrlConstants.DISCOVER_URL, "tv");
                    Log.i("url", DISCOVER_URL);

                    mMaxVotesTextView.setVisibility(View.GONE);
                    mMaxRatingTextView.setVisibility(View.GONE);
                    mMaxVotesEditText.setVisibility(View.GONE);
                    mMaxRatingEditText.setVisibility(View.GONE);

                    if (showGenres != null && showGenres.size() > 0) {

                        mGenresSpinner.setItems(showGenres, "Select", new MultiSpinner.MultiSpinnerListener() {
                            @Override
                            public void onItemsSelected(boolean[] selected) {
                                mSelectedGenres.clear();
                                for (int i = 0; i < selected.length; i++) {
                                    if (selected[i]) {
                                        mSelectedGenres.add(showGenresIds.get(i));
                                    }
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] movie_sort_by = getResources().getStringArray(R.array.movie_sort_by);
        final String[] movie_sort_by_ids = getResources().getStringArray(R.array.movie_sort_by_ids);
        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sort = String.valueOf(parent.getItemAtPosition(position));
                Log.i("selected", sort);
                for (int i = 0; i < movie_sort_by.length; i++) {
                    if (sort.equals(movie_sort_by[i])) {
                        mSortTextId = movie_sort_by_ids[i];
                    }
                }
                Log.i("id", mSortTextId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSelectMinDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar current = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                mMinReleaseDate = String.format(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                mMinDateTextView.setText(mMinReleaseDate);
                            }
                        },
                        current.get(Calendar.YEAR),
                        current.get(Calendar.MONTH),
                        current.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(), "Select Date");
            }
        });

        mSelectMaxDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar current = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            mMaxReleaseDate = String.format(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                            mMaxDateTextView.setText(mMaxReleaseDate);
                        }
                    },
                        current.get(Calendar.YEAR),
                        current.get(Calendar.MONTH),
                        current.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show(getFragmentManager(), "Select Date");
            }
        });

        mDiscoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMaxVotesEditText.getText().toString().isEmpty()) {
                    mMaxVotes = Integer.valueOf(String.valueOf(mMaxVotesEditText.getText()));
                }
                if (!mMinVotesEditText.getText().toString().isEmpty()) {
                    mMinVotes = Integer.valueOf(String.valueOf(mMinVotesEditText.getText()));
                }
                if (!mMaxRatingEditText.getText().toString().isEmpty()) {
                    mMaxRating = Double.valueOf(String.valueOf(mMaxRatingEditText.getText()));
                }
                if (!mMinRatingEditText.getText().toString().isEmpty()) {
                    mMinRating = Double.valueOf(String.valueOf(mMinRatingEditText.getText()));
                }
                String tempURL = DISCOVER_URL;

                if (mMinVotes >= 0) {
                    tempURL = String.format(tempURL + "&vote_count.gte=" + mMinVotes);
                }
                if (mMinRating >= 0.0) {
                    tempURL = String.format(tempURL+"&vote_average.gte="+mMinRating);
                }

                if (mType.equalsIgnoreCase("Movies")) {
                    if (mMaxVotes > 0) {
                        tempURL = String.format(tempURL + "&vote_count.lte=" + mMaxVotes);
                    }
                    if (mMaxRating > 0.0) {
                        tempURL = String.format(tempURL + "&vote_average.lte=" + mMaxRating);
                    }
                }
                boolean inRange = false;
                if (!TextUtils.isEmpty(mMinReleaseDate) && !TextUtils.isEmpty(mMaxReleaseDate)) {

                    String maxDate = mMaxReleaseDate;
                    String minDate = mMinReleaseDate;
                    maxDate = maxDate.trim();
                    minDate = minDate.trim();
                    maxDate = maxDate.replace("-", "");
                    minDate = minDate.replace("-", "");

                    if (Integer.valueOf(minDate) >= Integer.valueOf(maxDate)) {
                        Toast.makeText(mContext, "Invalid Date Range", Toast.LENGTH_SHORT).show();
                    }else {
                        inRange = true;
                        if (mType.equalsIgnoreCase("Movies")) {
                            tempURL = String.format(tempURL + "&primary_release_date.gte=" + mMinReleaseDate + "&primary_release_date.lte=" + mMaxReleaseDate);
                        } else {
                            tempURL = String.format(tempURL+"&first_air_date.gte="+mMinReleaseDate+"&first_air_date.lte="+mMaxReleaseDate);
                        }
                    }
                }else if (!TextUtils.isEmpty(mMinReleaseDate)) {
                    inRange = true;
                    if (mType.equalsIgnoreCase("Movies")) {
                        tempURL = String.format(tempURL + "&primary_release_date.gte=" + mMinReleaseDate);
                    } else {
                        tempURL = String.format(tempURL+"&first_air_date.gte="+mMinReleaseDate);
                    }
                }else if (!TextUtils.isEmpty(mMaxReleaseDate)) {
                    inRange = true;
                    if (mType.equalsIgnoreCase("Movies")) {
                        tempURL = String.format(tempURL +"&primary_release_date.lte=" + mMaxReleaseDate);
                    } else {
                        tempURL = String.format(tempURL+"&first_air_date.lte="+mMaxReleaseDate);
                    }
                }else {
                    inRange = true;
                }

                tempURL = String.format(tempURL +"&sort_by=" + mSortTextId);
                if (mMultipleKeywordsIds.size() > 0) {
                    tempURL = String.format(tempURL+"&with_keywords=");
                    for (int i = 0; i < mMultipleKeywordsIds.size(); i++) {
                        if (i != mMultipleKeywordsIds.size()-1) {
                            tempURL = String.format(tempURL+mMultipleKeywordsIds.get(i)+",");
                        }else {
                            tempURL = String.format(tempURL+mMultipleKeywordsIds.get(i));
                        }
                    }
                }
                if (mSelectedGenres.size() > 0) {
                    tempURL = String.format(tempURL +"&with_genres=");
                    for (int i = 0; i < mSelectedGenres.size(); i++) {
                        if (i != mSelectedGenres.size()-1) {
                            tempURL = String.format(tempURL+mSelectedGenres.get(i)+",");
                        }else {
                            tempURL = String.format(tempURL+mSelectedGenres.get(i));
                        }
                    }
                }
                Log.i("final url", tempURL);

                if (inRange) {
                    Intent intent = new Intent(mContext, DiscoveredResultActivity.class);
                    intent.putExtra("url", tempURL);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            ((SearchService)TVListingServiceFactory.getInstance().getService(SearchService.class)).searchKeyword(mSearchKeyword, mCurrentPage, DiscoverHomeActivity.this);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof MovieGenresList) {
            MovieGenresList movieGenresList = (MovieGenresList) response;
            for (int i = 0; i < movieGenresList.getGenres().size(); i++) {
                movieGenres.add(movieGenresList.getGenres().get(i).getName());
                movieGenresIds.add(movieGenresList.getGenres().get(i).getId());
            }
        }else if (response instanceof ShowGenresList) {
            ShowGenresList showGenresList = (ShowGenresList) response;
            for (int i = 0; i < showGenresList.getGenres().size(); i++) {
                showGenres.add(showGenresList.getGenres().get(i).getName());
                showGenresIds.add(showGenresList.getGenres().get(i).getId());
            }
        }else if (response instanceof KeywordSearchResult) {
            mKeywordSearchResult = (KeywordSearchResult) response;
            if (mKeywordSearchResult.getResults().size() == 0) {
                mKeywordsFrameLayout.setVisibility(View.VISIBLE);
                mNoKeywordTextView.setVisibility(View.VISIBLE);
                mKeywordProgressBar.setVisibility(View.GONE);
                mKeywordsRecyclerView.setVisibility(View.GONE);
            }else {
                mKeywordProgressBar.setVisibility(View.GONE);
                mKeywordsFrameLayout.setVisibility(View.VISIBLE);
                mNoKeywordTextView.setVisibility(View.GONE);
                mKeywordsRecyclerView.setVisibility(View.VISIBLE);
                mPageCount = mKeywordSearchResult.getTotal_pages();
                mCurrentPage++;
                mKeywordsRecyclerViewAdapter.setData(mKeywordSearchResult, mCurrentPage < mPageCount);
            }
        }
    }

    @Override
    public void selectedKeyword(int id, String name) {
        boolean exists = false;
        Log.i("keyword id", String.valueOf(id));
        for (int i = 0; i < mMultipleKeywordsIds.size(); i++) {
            if (id == mMultipleKeywordsIds.get(i) ) {
                exists = true;
            }
        }
        mKeywordsEditText.setText("");
        mKeywordsFrameLayout.setVisibility(View.GONE);
        if (!exists) {
            mMultipleKeywordsIds.add(id);
            mMultipleKeywords.add(name);
        }
        RelativeLayout relativeLayout;
        mKeywordFlowLayout.setVisibility(View.VISIBLE);
        mKeywordFlowLayout.removeAllViews();
        if (mMultipleKeywordsIds.size() > 0) {
            for (int i = 0; i < mMultipleKeywordsIds.size(); i++) {
                relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.keyword_selected, mKeywordFlowLayout, false);
                TextView textView = (TextView) relativeLayout.findViewById(R.id.keyword_selected_name);
                final Button crossButton = (Button) relativeLayout.findViewById(R.id.keyword_selected_crossbutton_button);
                final int finalI = i;
                crossButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int j = finalI;
                        while (j >= mMultipleKeywords.size()) {
                            j--;
                        }
                        Log.i("position", String.valueOf(j));
                        mMultipleKeywords.remove(j);
                        Log.i("keywords", String.valueOf(mMultipleKeywords));
                        mMultipleKeywordsIds.remove(j);
                        mKeywordFlowLayout.removeViewAt(j);
                        if (mKeywordFlowLayout.getChildCount() == 0) {
                            mKeywordFlowLayout.setVisibility(View.GONE);
                        }
                    }
                });
                textView.setText(mMultipleKeywords.get(i));
                mKeywordFlowLayout.addView(relativeLayout);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mKeywordsRecyclerView.getVisibility() == View.VISIBLE) {
            mKeywordsRecyclerView.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }
}
