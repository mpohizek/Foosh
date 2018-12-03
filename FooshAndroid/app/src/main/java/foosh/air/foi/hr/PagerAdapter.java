package foosh.air.foi.hr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import foosh.air.foi.hr.fragments.MyAdsFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    final String tabTitles[] = new String[] {"OBJAVLJENI", "PRIJAVLJENI"};

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyAdsFragment.getInstance("OBJAVLJENI");
            case 1:
                return MyAdsFragment.getInstance("PRIJAVLJENI");
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
