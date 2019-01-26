package foosh.air.foi.hr.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import foosh.air.foi.hr.fragments.MainFeedFragment;

public class MainFeedPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    final String tabTitles[] = new String[] {"OBJAVLJENI", "PRIJAVLJENI"};

    public MainFeedPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MainFeedFragment.getInstance("OBJAVLJENI");
            case 1:
                return MainFeedFragment.getInstance("PRIJAVLJENI");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
