package zip5001.my.com.zip.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerManager extends FragmentPagerAdapter{

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
        if(position==0){
            return "Users";
        }else if(position==1){
            return "Messages "+ TabsArrayClass.MessagesUsers.size();
        }else {
            return "Room";
        }
    }
}
