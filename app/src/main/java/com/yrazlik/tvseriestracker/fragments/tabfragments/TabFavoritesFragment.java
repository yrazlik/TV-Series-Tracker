package com.yrazlik.tvseriestracker.fragments.tabfragments;

import android.os.Handler;
import android.support.v4.app.Fragment;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.fragments.NullFragment;

/**
 * Created by yrazlik on 29/07/17.
 */
public class TabFavoritesFragment extends TabBaseFragment {

    @Override
    public void initiateFragment() {
        if(getChildFragmentManager().findFragmentById(R.id.fragment_tab_container) == null) {
            if(tabRootFragment == null) {
                tabRootFragment = NullFragment.instantiateFragment();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        GGMainApplication.hideKeyboard(getActivity());
        if(getChildFragmentManager().getBackStackEntryCount() == 1) {
            try {
                ((GGMainActivity) getActivity()).switchToTab(BottomBarUtils.TABS.TAB_HOME);
                return true;
            } catch (Exception ignored) {}
        }
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragment_tab_container);
        return currentFragment != null && ((BaseFragment) currentFragment).onBackPressed();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(redirectAfter != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BottomBarUtils.sDisableFragmentAnimation = true;
                    TabRedirectTask.newInstance(getContext(), redirectAfter, redirectData).redirect();
                    redirectAfter = null;
                }
            }, 10);
        }
    }

    public void updateUsernameText() {
        if(tabRootFragment instanceof ProfileMainFragment) {
            ((ProfileMainFragment) tabRootFragment).updateUsernameText();
        }
    }
}
