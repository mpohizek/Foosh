package foosh.air.foi.hr.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import foosh.air.foi.hr.fragments.MainFeedFragment;

/**
 * Adapter kartica za glavni feed: Zaradi i Zaposli
 */
public class MainFeedPagerAdapter extends FragmentPagerAdapter {

    static final int PAGE_COUNT = 2;
    static final String tabTitles[] = new String[] {"ZARADI", "ZAPOSLI"};

    public MainFeedPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private List<MainFeedFragment> registeredFragments = new ArrayList<>(Arrays.asList(MainFeedFragment.getInstance("OBJAVLJENI"),
            MainFeedFragment.getInstance("PRIJAVLJENI")));

    public MainFeedFragment getFragment(int i){
        return registeredFragments.get(i);
    }

    /**
     * Dohvaćanje fragmenta ovisno o poziciji u listi registriranih fragmenata
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return registeredFragments.get(position);
            case 1:
                return registeredFragments.get(position);
            default:
                return null;
        }
    }

    /**
     * Dohvaćanje broja kartica
     * @return
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * Dohvaćanje naziva kartice fragmenta
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
