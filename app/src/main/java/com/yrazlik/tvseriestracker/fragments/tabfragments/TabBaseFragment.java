package com.yrazlik.tvseriestracker.fragments.tabfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yrazlik.tvseriestracker.R;
import com.yrazlik.tvseriestracker.fragments.BaseFragment;

/**
 * Created by yrazlik on 29/10/17.
 */
public abstract class TabBaseFragment extends Fragment {

    private RelativeLayout fragmentTabContainer;
    protected BaseFragment tabRootFragment;

    public BaseFragment getTabRootFragment() {
        return tabRootFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = inflater.inflate(getLayoutId(), container, false);
        fragmentTabContainer = containerView.findViewById(R.id.fragment_tab_container);
        return containerView;
    }

    /**
     * Use this when you are currently at TAB X and want to reset the same tab(TAB X).
     **/
    public void resetCurrentTab() {
        resetTabState();
    }

    /* Pops all the fragments except root fragment. */
    protected void resetTabState() {
        if(getChildFragmentManager().getBackStackEntryCount() > 1) {
            try {
                FragmentManager fm = getChildFragmentManager();
                fm.popBackStackImmediate(fm.getBackStackEntryAt(1).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (Exception ignored) {/* Can be ignored */}
        }
    }

    public RelativeLayout getFragmentTabContainer() {
        return fragmentTabContainer;
    }

    public abstract int getLayoutId();

    public abstract void initiateFragment();

    public abstract boolean onBackPressed();
}
