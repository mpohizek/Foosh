package foosh.air.foi.hr.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import foosh.air.foi.hr.fragments.MyListingsFragment;

/**
 * Upravlja tabovima kod pregleda Mojih oglasa.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    final String tabTitles[] = new String[] {"OBJAVLJENI", "PRIJAVLJENI"};

    /**
     * Konstruktor.
     * @param fm
     */
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Mijenja fragmente prilikom swipe-anja tabova.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyListingsFragment.getInstance("OBJAVLJENI");
            case 1:
                return MyListingsFragment.getInstance("PRIJAVLJENI");
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
