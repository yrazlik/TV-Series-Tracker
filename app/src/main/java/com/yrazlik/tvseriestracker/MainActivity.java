package com.yrazlik.tvseriestracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.yrazlik.tvseriestracker.data.SearchResultDto;
import com.yrazlik.tvseriestracker.restclient.ApiHelper;
import com.yrazlik.tvseriestracker.restclient.ApiResponseListener;
import com.yrazlik.tvseriestracker.restclient.error.TVSeriesApiError;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ApiResponseListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

      //  ApiHelper.getInstance(this).searchShows("hello", this);
      //  ApiHelper.getInstance(this).searchShow("friends", this);
      //  ApiHelper.getInstance(this).searchShowById(82, this);
      //  ApiHelper.getInstance(this).searchShowByIdWithCast(82, this);
      //  ApiHelper.getInstance(this).getAllEpisodes(1, this);
      //  ApiHelper.getInstance(this).getEpisode(1, 1, 1, this);
      //  ApiHelper.getInstance(this).getSeasons(1, this);
        ApiHelper.getInstance(this).getSeasonEpisodes(1, this);

    }

    @Override
    public void onResponse(Object response) {
        if(response instanceof List) {

        }
    }

    @Override
    public void onFail(TVSeriesApiError apiError) {
        if(apiError != null) {
            Toast.makeText(this, apiError.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
