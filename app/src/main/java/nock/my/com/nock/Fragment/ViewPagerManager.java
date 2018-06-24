package nock.my.com.nock.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPagerManager extends FragmentPagerAdapter {

    public ViewPagerManager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return ViewPagerCreation.createInstance(position);

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Boys";
        } else if (position == 1) {
            return "Girls";
        } else if (position == 2) {
            return "Messages " + TabsArrayClass.MessagesUsers.size();
        } else {
            return "Room";
        }
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //to get current page number------------------------------------------------>
    public static class CurrentPageClass extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
        }

        public int getCurrentPage() {
            return currentPage;
        }
    }
}