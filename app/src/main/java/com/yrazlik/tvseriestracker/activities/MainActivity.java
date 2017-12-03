package com.yrazlik.tvseriestracker.activities;

import android.app.job.JobInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.yrazlik.tvseriestracker.BackgroundJobService;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.adapters.SearchAdapter;
import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.fragments.FavoritesFragment;
import com.yrazlik.tvseriestracker.fragments.FragmentTags;
import com.yrazlik.tvseriestracker.fragments.ScheduleFragment;
import com.yrazlik.tvseriestracker.fragments.TrendingShowsFragment;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.view.ClearableAutoCompleteTextView;
import com.yrazlik.tvseriestracker.view.RobotoTextView;
import java.util.ArrayList;
import java.util.List;

import static com.yrazlik.tvseriestracker.BackgroundJobService.BG_JOB_SERVICE_TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ClearableAutoCompleteTextView.OnClearListener, ApiResponseListener, AdapterView.OnItemClickListener{


    public interface OnFavoritesChangedListener {
        void onFavoritesChanged();
    }

    private int currentTabId;

    private List<SearchResultDto> searchResults = new ArrayList<>();
    private ClearableAutoCompleteTextView searchBox;
    private SearchAdapter searchAdapter;
    private View search_bar;
    private ImageView searchIcon;
    private RobotoTextView titleTV;

    public void initSearchBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle((title != null && title.length() > 0) ? title : getResources().getString(R.string.app_name));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME);

        LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        search_bar = inf.inflate(R.layout.actionbar_search, null);
        titleTV = search_bar.findViewById(R.id.titleTV);
        titleTV.setText((title != null && title.length() > 0) ? title : getResources().getString(R.string.app_name));
        searchIcon = search_bar.findViewById(R.id.search_icon);
        searchBox = search_bar.findViewById(R.id.search_box);
        searchBox.setVisibility(View.GONE);
        searchBox.setThreshold(1);

        searchBox.setOnItemClickListener(this);
        searchIcon.setOnClickListener(this);
        searchBox.setOnClearListener(this);
        searchBox.addTextChangedListener(searchBoxTextChangedListener);
        actionBar.setCustomView(search_bar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon:
                toggleSearch(search_bar, false);
                break;
        }
    }

    @Override
    public void onClear() {
        toggleSearch(search_bar, true);
    }

    private TextWatcher searchBoxTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ApiHelper.getInstance(MainActivity.this).cancelAllRequests();
            if (s != null && s.length() >= 2) {
                ApiHelper.getInstance(MainActivity.this).searchShows(s.toString(), MainActivity.this);
             //   ServiceRequest.getInstance(getApplicationContext()).cancelPendingRequests(ServiceRequest.TAG_SEARCH_REQUEST);
              //  ServiceRequest.getInstance(getApplicationContext()).makeSearchRequest(s.toString(), SearchActivity.this);
            } else {
                Log.d("DISMISS", "Dismissing");
                searchBox.dismissDropDown();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    protected void toggleSearch(View v, boolean reset) {
        if (reset) {
            searchBox.setText("");
            searchBox.setVisibility(View.GONE);
            titleTV.setVisibility(View.VISIBLE);
            searchIcon.setVisibility(View.VISIBLE);
            // hide the keyboard
            try{
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
            }catch (Exception ignored){}
        } else {
            // hide search icon and show search box
            searchIcon.setVisibility(View.GONE);

            titleTV.setVisibility(View.GONE);
            searchBox.setVisibility(View.VISIBLE);
            searchBox.requestFocus();
            // show the keyboard
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
            }catch (Exception ignored){}
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(searchBox.getText() != null) {
            searchBox.setText("");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToTab(R.id.navigation_home);
                    currentTabId = item.getItemId();
                    return true;
                case R.id.navigation_trending:
                    switchToTab(R.id.navigation_trending);
                    currentTabId = item.getItemId();
                    return true;
                case R.id.navigation_schedule:
                    switchToTab(R.id.navigation_schedule);
                    currentTabId = item.getItemId();
                    return true;
            }
            return false;
        }

    };

    private void switchToTab(int tabId) {
        if(tabId != currentTabId) {
            detachCurrentTabFragment();
            attachTabFragment(tabId);
        }
    }

    private void detachCurrentTabFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if(currentFragment != null) {
            try {
                getSupportFragmentManager().beginTransaction().detach(currentFragment).commit();
            } catch (IllegalStateException e) {
                Log.d("IllegalStateException", "Tried to replace fragment after saveInstanceState(). Do not replace it and prevent app from crash.");
            }
        }
    }

    private void attachTabFragment(int tabId) {
        try {
            switch (tabId) {
                case R.id.navigation_home:
                    Fragment favoritesFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_FAVORITES);
                    if (favoritesFragment == null) {
                        favoritesFragment = FavoritesFragment.newInstance();
                    }
                    attachTab(favoritesFragment, FragmentTags.FRAGMENT_FAVORITES);
                    break;
                case R.id.navigation_trending:
                    Fragment trendingShowsFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_TRENDING_SHOWS);
                    if (trendingShowsFragment == null) {
                        trendingShowsFragment = new TrendingShowsFragment();
                    }
                    attachTab(trendingShowsFragment, FragmentTags.FRAGMENT_TRENDING_SHOWS);
                    break;
                case R.id.navigation_schedule:
                    Fragment scheduleFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_SCHEDULE);
                    if (scheduleFragment == null) {
                        scheduleFragment = new ScheduleFragment();
                    }
                    attachTab(scheduleFragment, FragmentTags.FRAGMENT_SCHEDULE);
                    break;
            }
        } catch (IllegalStateException e) {
            Log.d("IllegalStateException", "Tried to replace fragment after saveInstanceState(). Do not replace it and prevent app from crash.");
        }
    }

    private void attachTab(Fragment fragment, String tag) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (fragment.isDetached()) {
                ft.attach(fragment).commit();
            } else {
                ft.add(R.id.content, fragment, tag).commit();
            }
        } catch (IllegalStateException e) {
            Log.d("IllegalStateException", "Tried to replace fragment after saveInstanceState(). Do not replace it and prevent app from crash.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSearchBar(getResources().getString(R.string.app_name));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        startBackgroundJobService();


    }

    private void startBackgroundJobService() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(BackgroundJobService.class)
                // uniquely identifies the job
                .setTag(BG_JOB_SERVICE_TAG)
                // oreschedule
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 60))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // run jov while it is connected to network
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    OnFavoritesChangedListener favoritesChangedListener = new OnFavoritesChangedListener() {
        @Override
        public void onFavoritesChanged() {
            Fragment favoritesFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_FAVORITES);
            if (favoritesFragment != null && favoritesFragment instanceof FavoritesFragment) {
                ((FavoritesFragment)favoritesFragment).notifyDataSetChanged();
            }

            Fragment trendingShowsFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_TRENDING_SHOWS);
            if (trendingShowsFragment != null && trendingShowsFragment instanceof TrendingShowsFragment) {
                ((TrendingShowsFragment)trendingShowsFragment).notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        favoritesChangedListener.onFavoritesChanged();
    }

    @Override
    public void onResponse(Object response) {
        searchResults = (List<SearchResultDto>) response;
        Log.d("DISMISS", "Received response");
        if(searchResults.size() > 0) {
            Log.d("DISMISS", "results.size > 0");
            searchAdapter = new SearchAdapter(MainActivity.this, R.layout.list_row_search_item, searchResults, favoritesChangedListener);
            searchAdapter.setNotifyOnChange(true);
            searchBox.setAdapter(searchAdapter);
            searchBox.showDropDown();
        }
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {

    }
}
