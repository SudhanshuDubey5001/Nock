package nock.my.com.nock.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import nock.my.com.nock.data.User;


public class SignUpActivity extends AppCompatActivity {

    public static FirebaseAuth auth;
    String userName;
    String Email;
    String PassMain;
    String PassConfirm;

    EditText email;
    EditText UserName;
    EditText passmain;
    EditText passconfirm;

    boolean alreadyPresent;

    static Query query;

    KProgressHUD hud;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = FirebaseDatabase.getInstance().getReference("Users");

        auth = FirebaseAuth.getInstance();


        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(3)
                .setLabel("Wait")
                .setCancellable(true);

        email = findViewById(R.id.email);
        UserName = findViewById(R.id.username);
        passmain = findViewById(R.id.passmain);
        passconfirm = findViewById(R.id.passconfirm);


        //button start--------->
        Button signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alreadyPresent = false;
                hud.show();
                Email = SignUpActivity.this.email.getText().toString();
                PassMain = SignUpActivity.this.passmain.getText().toString();
                PassConfirm = SignUpActivity.this.passconfirm.getText().toString();
                userName = UserName.getText().toString();

                //check username---------------->
                Query query = database.orderByChild("userName");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d("my", snapshot.child("userName").getValue().toString());

                            if (snapshot.child("userName").getValue().toString().equals(userName)) {
                                alreadyPresent = true;
                            } else {
                                alreadyPresent = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("my", databaseError.getMessage());
                    }
                });
                //username checked-----------------------------------------------------


                if (alreadyPresent) {   //already present username
                    hud.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Error")
                            .setMessage("User name already in use!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserName.setText(null);
                                    dialogInterface.dismiss();
                                }
                            }).show();

                } else {        //new username
                    String userid = database.push().getKey();   //get unique key
                    User user = new User(Email, PassMain, null, userName);    //fill details
                    database.child(userid).setValue(user);  //push it into firebase


                    if (PassMain.equals(PassConfirm)) {
                        auth.createUserWithEmailAndPassword(Email, PassMain)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        hud.dismiss();
                                        if (task.isSuccessful()) {
                                            AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
                                            dialog.setTitle("Successful")
                                                    .setMessage("You are registered!")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                                                            startActivity(in);
                                                            SignUpActivity.this.finish();
                                                        }
                                                    }).show();

                                        } else {
                                            AlertBox.builder(SignUpActivity.this, "Sorry!", task.getException().getMessage(), "OK", "Not OK");
                                        }
                                    }
                                });
                    } else {
                        AlertBox.builder(SignUpActivity.this, "Error", "Confirm Password Field do not match", "OK");
                    }
                }
            }
        });
    }
}
