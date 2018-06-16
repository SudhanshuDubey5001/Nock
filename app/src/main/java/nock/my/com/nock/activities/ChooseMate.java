package nock.my.com.nock.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import nock.my.com.nock.ChatScreenView.ChoosingAdapter;
import nock.my.com.nock.ChatScreenView.ChoosingViewholder;
import nock.my.com.nock.DatabaseOperations;
import nock.my.com.nock.Fragment.TabsArrayClass;
import nock.my.com.nock.Fragment.ViewPagerCreation;
import nock.my.com.nock.Fragment.ViewPagerManager;
import nock.my.com.nock.Fragment.ZoomOutPageTransformer;
import nock.my.com.nock.MessageRecieveClass;
import nock.my.com.nock.R;

public class ChooseMate extends AppCompatActivity {

    private MenuItem itemDelete;
    private MenuItem itemCancel;
    static Menu menu;
    private boolean firstTime = true;
    ViewPager pager;

    public void goVisible() {
        itemDelete = menu.findItem(R.id.delete);
        itemDelete.setVisible(true);
        itemCancel = menu.findItem(R.id.cancel);
        itemCancel.setVisible(true);
    }

    public void goInvisible() {
        itemDelete = menu.findItem(R.id.delete);
        itemDelete.setVisible(false);
        itemCancel = menu.findItem(R.id.cancel);
        itemCancel.setVisible(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mate);

//      setTitle------>
        setTitle("Welcome " + LoginActivity.UserName.toUpperCase());

//      Setting text of pagerTitle strip----->
        PagerTitleStrip pagerTitleStrip = findViewById(R.id.tabtitle);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        pagerTitleStrip.setTextColor(Color.WHITE);

//      Setting Pagertab strip------------->
        PagerTabStrip tab = (PagerTabStrip) pagerTitleStrip;
        tab.setTabIndicatorColor(Color.WHITE);


//      Setting up View Pager--------------->
        pager = findViewById(R.id.viewPager);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        ViewPagerManager manager=new ViewPagerManager(getSupportFragmentManager());
        manager.notifyDataSetChanged();
        pager.setAdapter(manager);

//      Start the service for receiving message when app is not open---------->
        MessageRecieveClass.value = true;
        Intent in = new Intent(this, MessageRecieveClass.class);
        startService(in);
    }

    //  Create option menu and setOnclickListener----------------------------->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        ChooseMate.menu = menu;

        itemDelete = menu.findItem(R.id.delete);
        itemDelete.setVisible(false);
        itemCancel = menu.findItem(R.id.cancel);
        itemCancel.setVisible(false);
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
                LoginActivity.auth = null;
                DatabaseOperations.goOffline();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.delete:
                DatabaseOperations dop = new DatabaseOperations();
                Log.d("my", "Dab gaya delete..sach mein!!!!!");
//                ViewPagerManager.CurrentPageClass page = new ViewPagerManager.CurrentPageClass();

                Log.d("my", "Actual id: "+pager.getCurrentItem());
                if (pager.getCurrentItem()== ViewPagerCreation.USERS_B) {
                    Log.d("my","User mein!!!");
                    dop.remove(DatabaseOperations.deleteUsers_B, ChoosingAdapter.knowId());
                }else if(pager.getCurrentItem()== ViewPagerCreation.USERS_G){
                    dop.remove(DatabaseOperations.deleteUsers_G, ChoosingAdapter.knowId());
                }else if (pager.getCurrentItem()== ViewPagerCreation.MESSAGES) {
                    Log.d("my","Message mein!!!");
                    dop.remove(DatabaseOperations.deleteNew, ChoosingAdapter.knowId());
                } else {
                    Log.d("my","Room mein!!!");
                    dop.remove(DatabaseOperations.deleteRoom, ChoosingAdapter.knowId());
                }
                DatabaseOperations.deleteUsers_B.clear();
                DatabaseOperations.deleteUsers_G.clear();
                DatabaseOperations.deleteNew.clear();
                DatabaseOperations.deleteRoom.clear();
                cancel();
                goInvisible();
                break;
            case R.id.cancel:
                cancel();
                goInvisible();
            }
        return true;
    }

    public void cancel(){
            ChoosingViewholder.LongClickheld=false;
            for(View v:ChoosingViewholder.viewArray){
                v.setBackgroundColor(Color.WHITE);
            }

            DatabaseOperations.deleteUsers_B.clear();
            DatabaseOperations.deleteUsers_G.clear();
            DatabaseOperations.deleteNew.clear();
            DatabaseOperations.deleteRoom.clear();
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
        MessageRecieveClass.stopSenders.clear();
        MessageRecieveClass.stopSendersRedundantMsg.clear();
//        DatabaseOperations.logOut();
    }

    @Override
    protected void onStop() {
        super.onStop();

//      Emptying the arrays------------------------------>
        TabsArrayClass.Users.clear();
        TabsArrayClass.UsersGirl.clear();
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
