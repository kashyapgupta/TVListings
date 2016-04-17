package com.tvlistings.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.tvlistings.R;
import com.tvlistings.controller.factory.TVListingServiceFactory;
import com.tvlistings.controller.network.TVListingNetworkClient;
import com.tvlistings.controller.service.DiscoverService;
import com.tvlistings.controller.service.ServiceCallbacks;
import com.tvlistings.model.BaseResponse;
import com.tvlistings.model.DiscoveredData;
import com.tvlistings.model.searchResult.SearchResultContent;
import com.tvlistings.view.adapter.DiscoverRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Rohit on 4/15/2016.
 */
public class DiscoverHomeActivity extends BaseSearchActivity implements ServiceCallbacks {
    @Bind(R.id.activity_discover_home_sort_by_spinner)
    Spinner mSortBySpinner;

    @Bind(R.id.activity_discover_home_year_spinner)
    Spinner mYearSpinner;

    @Bind(R.id.activity_discover_home_type_spinner)
    Spinner mTypeSpinner;

    @Bind(R.id.activity_discover_home_results_recycler_view)
    RecyclerView mDiscoveredRecyclerView;

    List<String> year = new ArrayList<>();
    private int mCurrentPage = 1;
    private int mPageCount;

    RequestQueue mQueue;
    Context mContext;

    LinearLayoutManager mLinearLayoutManager;
    DiscoverRecyclerViewAdapter mDiscoverRecyclerViewAdapter;

    DiscoveredData mDiscoveredMovies;
    String mSortTextId = "popularity.asc";
    int mYear;
    String mType = "Movies";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_discover_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = TVListingNetworkClient.getInstance().getRequestQueue();
        mContext = this;

        mDiscoveredRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mDiscoveredRecyclerView.setLayoutManager(mLinearLayoutManager);

        mDiscoverRecyclerViewAdapter = new DiscoverRecyclerViewAdapter(mQueue, mContext);
        mDiscoveredRecyclerView.setAdapter(mDiscoverRecyclerViewAdapter);
        mDiscoverRecyclerViewAdapter.clearData();

        ArrayAdapter<CharSequence> sortByAdapter = ArrayAdapter
                .createFromResource(this, R.array.movie_sort_by,
                        android.R.layout.simple_spinner_item);

        sortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSortBySpinner.setAdapter(sortByAdapter);
        final String[] sort_by = getResources().getStringArray(R.array.movie_sort_by);
        final String[] sort_by_ids = getResources().getStringArray(R.array.movie_sort_by_ids);
        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sort = String.valueOf(parent.getItemAtPosition(position));
                Log.i("selected", sort);
                mDiscoverRecyclerViewAdapter.clearData();
                for (int i = 0; i < sort_by.length; i++) {
                    if (sort.equals(sort_by[i])) {
                        mSortTextId = sort_by_ids[i];
                    }
                }
                Log.i("id", mSortTextId);
                if (mType.equalsIgnoreCase("Movies")) {
                    ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredMovies(mSortTextId, mYear, mCurrentPage, DiscoverHomeActivity.this);
                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int current_year = calendar.get(Calendar.YEAR);
        mYear = current_year;
        Log.i("current year", String.valueOf(current_year));
        while (current_year > 1899) {
            year.add(String.valueOf(current_year));
            current_year--;
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearSpinner.setAdapter(yearAdapter);

        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYear = Integer.valueOf((String) parent.getItemAtPosition(position));
                mDiscoverRecyclerViewAdapter.clearData();
                Log.i("year", String.valueOf(mYear));
                if (mType.equalsIgnoreCase("Movies")) {
                    ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredMovies(mSortTextId, mYear, mCurrentPage, DiscoverHomeActivity.this);
                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                mDiscoverRecyclerViewAdapter.clearData();
                if (mType.equalsIgnoreCase("Movies")) {
                    ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredMovies(mSortTextId, mYear, mCurrentPage, DiscoverHomeActivity.this);
                }else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (mType.equalsIgnoreCase("Movies")) {
            ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredMovies(mSortTextId, mYear, mCurrentPage, DiscoverHomeActivity.this);
        }else {

        }
    }

    @Override
    public void loadMore() {
        if (mCurrentPage < mPageCount) {
            mCurrentPage++;
            ((DiscoverService) TVListingServiceFactory.getInstance().getService(DiscoverService.class)).getDiscoveredMovies(mSortTextId, mYear, mCurrentPage, DiscoverHomeActivity.this);
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof SearchResultContent) {
            super.onSuccess(response);
        }else if (response instanceof DiscoveredData) {
            mDiscoveredMovies = (DiscoveredData) response;
            mPageCount = mDiscoveredMovies.getTotal_pages();
            mDiscoverRecyclerViewAdapter.setData(mDiscoveredMovies.getResults());
        }
    }
}
