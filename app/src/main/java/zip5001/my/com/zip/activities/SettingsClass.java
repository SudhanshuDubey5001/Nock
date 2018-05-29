package zip5001.my.com.zip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import zip5001.my.com.zip.R;

public class SettingsClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(this,ChooseMate.class);
        startActivity(in);
    }
}
