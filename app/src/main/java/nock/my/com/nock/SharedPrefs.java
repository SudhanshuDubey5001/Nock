package nock.my.com.nock;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    final public static String NOTI= "notification";

    public static SharedPreferences getPrefs(){
        Context c = MyApplication.getContext();
        return c.getSharedPreferences("dumble",Context.MODE_PRIVATE);
    }

    public static void setNoti(boolean noti_enable){
        getPrefs().edit().putBoolean(NOTI,noti_enable).commit();
    }
}
