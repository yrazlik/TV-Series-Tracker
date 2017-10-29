package com.yrazlik.tvseriestracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yrazlik.tvseriestracker.adapters.TrendingShowsListAdapter;
import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.data.ShowDto;
import com.yrazlik.tvseriestracker.fragments.FragmentTags;
import com.yrazlik.tvseriestracker.fragments.NullFragment;
import com.yrazlik.tvseriestracker.fragments.TrendingShowsFragment;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;
import com.yrazlik.tvseriestracker.util.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToTab(R.id.navigation_home);
                    return true;
                case R.id.navigation_trending:
                    switchToTab(R.id.navigation_trending);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    private void switchToTab(int tabId) {
        detachCurrentTabFragment();
        attachTabFragment(tabId);
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
                    Fragment nullFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_NULL);
                    if (nullFragment == null) {
                        nullFragment = new NullFragment();
                    }
                    attachTab(nullFragment, FragmentTags.FRAGMENT_NULL);
                    break;
                case R.id.navigation_trending:
                    Fragment trendingShowsFragment = getSupportFragmentManager().findFragmentByTag(FragmentTags.FRAGMENT_TRENDING_SHOWS);
                    if (trendingShowsFragment == null) {
                        trendingShowsFragment = new TrendingShowsFragment();
                    }
                    attachTab(trendingShowsFragment, FragmentTags.FRAGMENT_TRENDING_SHOWS);
                    break;
                case R.id.navigation_notifications:

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


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

      //  ApiHelper.getInstance(this).searchShows("hello", this);
      //  ApiHelper.getInstance(this).searchShow("friends", this);
      //  ApiHelper.getInstance(this).searchShowById(82, this);
      //  ApiHelper.getInstance(this).searchShowByIdWithCast(82, this);
      //  ApiHelper.getInstance(this).getAllEpisodes(1, this);
      //  ApiHelper.getInstance(this).getEpisode(1, 1, 1, this);
      //  ApiHelper.getInstance(this).getSeasons(1, this);
      //  ApiHelper.getInstance(this).getSeasonEpisodes(1, this);


    }


}
