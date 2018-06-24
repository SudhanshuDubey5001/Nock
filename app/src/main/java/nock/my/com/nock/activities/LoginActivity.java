package nock.my.com.nock.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import nock.my.com.nock.AlertBox;
import nock.my.com.nock.R;

public class LoginActivity extends AppCompatActivity {

    public static FirebaseAuth auth = null;

    EditText email;
    EditText pass;

    String emailmain;
    String passmain;

    public static String UserName;
    public static String UserKey;
    public static boolean ChooseMateScreenON = false;
    DatabaseReference database;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        if (auth != null) {
            Intent in = new Intent(this, ChooseMate.class);
            startActivity(in);
            this.finish();
        } else {
            auth = FirebaseAuth.getInstance();
        }
        database = FirebaseDatabase.getInstance().getReference("Users");

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(3)
                .setLabel("Wait")
                .setCancellable(true);

        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);

        Button login = findViewById(R.id.login);
        Button forgotPass = findViewById(R.id.forgotPass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                emailmain = LoginActivity.this.email.getText().toString();
                passmain = LoginActivity.this.pass.getText().toString();

                auth.signInWithEmailAndPassword(emailmain, passmain)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                hud.dismiss();
                                if (task.isSuccessful()) {

                                    Query query = database.orderByChild("Email");
                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.child("Email").getValue().toString().equals(emailmain)) {
                                                    UserName = snapshot.child("userName").getValue().toString();
                                                    UserKey = snapshot.getKey();

                                                    Log.d("my", "userid=" + UserName);
                                                    Log.d("my", "key=" + snapshot.getKey());
                                                }
                                                Log.d("my", snapshot.getValue().toString());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Success")
                                            .setMessage("You are logged in")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ChooseMateScreenON = true;
                                                    Intent in = new Intent(LoginActivity.this, ChooseMate.class);
                                                    startActivity(in);
                                                    LoginActivity.this.finish();
                                                    dialogInterface.dismiss();
                                                }
                                            }).show();

                                } else {
                                    AlertBox.builder(LoginActivity.this, "Failed", task.getException().getMessage(), "OK");
                                }
                            }
                        });
            }
        });

//        forgotPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in = new Intent(LoginActivity.this,ForgotPassActivity.class);
//                startActivity(in);
//            }
//        });
    }
}
