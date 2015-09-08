package ifn701.safeguarder.entities;

/**
 * Created by mutanthybrid on 24/08/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ifn701.safeguarder.activities.DetailedTabFragment;
import ifn701.safeguarder.activities.QuickTabFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DetailedTabFragment detailedtab = new DetailedTabFragment();
                return detailedtab;
            case 1:
                QuickTabFragment quicktab = new QuickTabFragment();
                return quicktab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}