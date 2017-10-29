package com.yrazlik.tvseriestracker.fragments.tabfragments;

import android.support.v4.app.Fragment;

import com.gittigidiyormobil.BaseFragment;
import com.gittigidiyormobil.GGMainApplication;
import com.gittigidiyormobil.R;
import com.tmob.app.Commons;
import com.tmob.gittigidiyor.ui.home.FeedFragment;

/**
 * Created by yrazlik on 15/06/16.
 */
public class TabTrendingFragment extends TabBaseFragment {

    @Override
    public void initiateFragment() {
        if(getChildFragmentManager().findFragmentById(R.id.fragment_tab_container) == null) {
            if(tabRootFragment == null) {
                tabRootFragment = FeedFragment.instantiateFragment(Commons.ANIM_UNDEFINED);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        GGMainApplication.hideKeyboard(getActivity());
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_tab_container);
        return currentFragment != null && ((BaseFragment) currentFragment).onBackPressed();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
