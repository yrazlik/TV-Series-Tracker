package com.yrazlik.tvseriestracker.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.yrazlik.tvseriestracker.Commons;
import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.TvSeriesTrackerApp;
import com.yrazlik.tvseriestracker.TvSeriesTrackerNotification;
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
import com.yrazlik.tvseriestracker.util.AdUtils;
import com.yrazlik.tvseriestracker.util.Utils;
import com.yrazlik.tvseriestracker.view.ClearableAutoCompleteTextView;
import com.yrazlik.tvseriestracker.view.RobotoTextView;
import java.util.ArrayList;
import java.util.List;

import static com.yrazlik.tvseriestracker.TvSeriesTrackerFirebaseMessagingService.FIREBASE_PUSH_TOPIC;
import static com.yrazlik.tvseriestracker.activities.ShowDetailActivity.EXTRA_SHOW_ID;
import static com.yrazlik.tvseriestracker.activities.ShowDetailActivity.EXTRA_SHOW_NAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ClearableAutoCompleteTextView.OnClearListener, ApiResponseListener, AdapterView.OnItemClickListener{

    private Intent deeplinkIntent;

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
    private BottomNavigationView navigation;

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
        AdUtils.showInterstitial(false);
        SearchResultDto item = searchResults.get(i);
        ShowDto show = item.getShow();
        CheckBox favoriteCB = view.findViewById(R.id.favoriteCB);
        final boolean isChecked = favoriteCB.isChecked();
        if(isChecked) {
            Utils.removeFromFavoritesList(getApplicationContext(), show);
        } else {
            Utils.saveToFavoritesList(getApplicationContext(), show);
        }
        searchBox.setText("");
        if(favoritesChangedListener != null) {
            favoritesChangedListener.onFavoritesChanged();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            AdUtils.showInterstitial(false);
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
        deeplinkIntent = getIntent();
        TvSeriesTrackerApp.appIsRunning = true;
        setContentView(R.layout.activity_main);
        initAds();
        initSearchBar(getResources().getString(R.string.app_name));

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        subscribeToTopic();


    }

    private void subscribeToTopic() {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(FIREBASE_PUSH_TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Cound not subscribe";
                    }
                    Log.d("FirebaseMsgService", msg);
                    //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("TvSeriesTrackerApp", "Cannot subscribe to topic.");
        }
    }

    private void initAds() {
        AdUtils.initAds(this);
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
        if(deeplinkIntent != null) {
            if(deeplinkIntent.getExtras() != null) {
                if(Commons.isValidString(getIntentMessageBody())) {
                    TvSeriesTrackerNotification notification = new TvSeriesTrackerNotification(deeplinkIntent);
                    handleDeeplinkIntent(notification);
                }
            }
        }
        deeplinkIntent = null;
    }

    private String getIntentMessageBody() {
        try {
            return (String) deeplinkIntent.getExtras().get(TvSeriesTrackerNotification.PUSH_NOTIFICATION_BODY);
        } catch (Exception e) {
            return null;
        }
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

    private void handleDeeplinkIntent(TvSeriesTrackerNotification notification) {
        if(notification != null) {
            if(notification.getNotificationAction() == TvSeriesTrackerNotification.NOTIFICATION_ACTION.ACTION_HOME) {
                AdUtils.showInterstitial(true);
            } else if(notification.getNotificationAction() == TvSeriesTrackerNotification.NOTIFICATION_ACTION.ACTION_SHOW_DETAIL) {
                AdUtils.showInterstitial(true);
                Uri dl = Uri.parse(notification.getDeeplink());
                String path = dl.getPath().replaceAll("\\/", "");
               // ShowDto show = favoritesListAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, ShowDetailActivity.class);
                intent.putExtra(EXTRA_SHOW_ID, Long.parseLong(path));
                intent.putExtra(EXTRA_SHOW_NAME, "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_bottom_in, R.anim.fadeout);
            } else if(notification.getNotificationAction() == TvSeriesTrackerNotification.NOTIFICATION_ACTION.ACTION_TRENDING) {
                AdUtils.showInterstitial(true);
                Menu menu = navigation.getMenu();
                for (int i = 0, size = menu.size(); i < size; i++) {
                    MenuItem item = menu.getItem(i);
                    if(i == 1) {
                        item.setChecked(true);
                    } else {
                        item.setChecked(false);
                    }
                }
                MenuItem item = menu.getItem(1);
                item.setChecked(true);
               mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
            } else if(notification.getNotificationAction() == TvSeriesTrackerNotification.NOTIFICATION_ACTION.SCHEDULE) {
                AdUtils.showInterstitial(true);
                Menu menu = navigation.getMenu();
                for (int i = 0, size = menu.size(); i < size; i++) {
                    MenuItem item = menu.getItem(i);
                    if(i == 2) {
                        item.setChecked(true);
                    } else {
                        item.setChecked(false);
                    }
                }
                MenuItem item = menu.getItem(2);
                item.setChecked(true);
                mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
            } else if(notification.getNotificationAction() == TvSeriesTrackerNotification.NOTIFICATION_ACTION.ACTION_PLAY_STORE) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.yrazlik.tvseriestracker")));
                } catch (Exception e) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.yrazlik.tvseriestracker"));
                        startActivity(i);
                    }catch (Exception ex) {
                        Log.d("TvSeriesTrackerApp", "Invalid url");
                    }
                }
            }
            else {
                //new PushNotificationDialog(MainActivity.this, getBody(notification)).show();
            }
            //((TvSeriesTrackerApp)getApplication()).showInterstitialOnPushNotificaion();
        }
    }
}
