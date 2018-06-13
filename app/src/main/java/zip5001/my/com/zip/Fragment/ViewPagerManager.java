package zip5001.my.com.zip.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.HashMap;

import zip5001.my.com.zip.activities.ChooseMate;

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
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {

            return "Users";
        } else if (position == 1) {
            return "Messages " + TabsArrayClass.MessagesUsers.size();
        } else {
            return "Room";
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
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