package zip5001.my.com.zip.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import zip5001.my.com.zip.Fragment.TabsArrayClass;
import zip5001.my.com.zip.Fragment.ViewPagerManager;
import zip5001.my.com.zip.Fragment.ZoomOutPageTransformer;
import zip5001.my.com.zip.MessageRecieveClass;
import zip5001.my.com.zip.R;
import zip5001.my.com.zip.DatabaseOperations;

public class ChooseMate extends AppCompatActivity{

    static boolean justOnce=true;
    private MenuItem itemDelete;
    private MenuItem itemCancel;

    public void goVisible(){
        itemDelete.setVisible(true);
        itemCancel.setVisible(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mate);

//      Setting text of pagerTitle strip----->
        PagerTitleStrip pagerTitleStrip = findViewById(R.id.tabtitle);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        pagerTitleStrip.setTextColor(Color.WHITE);

//      Setting Pagertab strip------------->
        PagerTabStrip tab = (PagerTabStrip)pagerTitleStrip;
        tab.setTabIndicatorColor(Color.WHITE);


//      Setting up View Pager--------------->
        ViewPager pager = findViewById(R.id.viewPager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setAdapter(new ViewPagerManager(getSupportFragmentManager()));

//      Start the service for receiving message when app is not open---------->
        if(justOnce) {
            justOnce=false;
            Intent in = new Intent(this, MessageRecieveClass.class);
            startService(in);
        }
    }

//  Create option menu and setOnclickListener----------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        itemDelete = menu.findItem(R.id.delete);
        itemDelete.setVisible(false);
        itemCancel = menu.findItem(R.id.delete);
        itemCancel.setVisible(false);

        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsOption:
                Intent in = new Intent(ChooseMate.this, SettingsClass.class);
                startActivity(in);
                break;
            case R.id.logout:
                DatabaseOperations.logOut();
                LoginActivity.auth=null;
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                break;
            case R.id.delete:
                DatabaseOperations.removeFriend(this);
            case R.id.cancel:

        }
        return true;
    }

//  Make user go online---------------->
    @Override
    protected void onResume() {
        super.onResume();
        DatabaseOperations.goOnline();
    }

//  Finish the activity as activity starts again upon the previous ChooseMate activity------------------>
    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

//  Make user go offline and logout------------------->
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        DatabaseOperations.goOffline();
//        DatabaseOperations.logOut();
    }

    @Override
    protected void onStop() {
        super.onStop();

//      Emptying the arrays------------------------------>
        TabsArrayClass.Users.clear();
        TabsArrayClass.MessagesUsers.clear();
        TabsArrayClass.RoomUsers.clear();
    }

//  Again offline and logout------------------------------>
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseOperations.goOffline();
//        DatabaseOperations.logOut();
    }

}
