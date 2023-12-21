package com.task.mywallpaper.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.task.mywallpaper.MainActivity;
import com.task.mywallpaper.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private EditText otp;
    private Button submit;
    private TextView resend;

    private MKLoader mkLoader;
    private String number,id;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        otp = findViewById(R.id.otp);
        submit = findViewById(R.id.submit);
        resend = findViewById(R.id.resend);
        mkLoader = findViewById(R.id.mkLoader);

        mAuth = FirebaseAuth.getInstance();
        number = getIntent().getStringExtra("number");


        sendVerificationCode();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 14-11-2023  otp verification
                if (TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(VerificationActivity.this, "enter otp", Toast.LENGTH_SHORT).show();
                } else if (otp.getText().toString().replace("","").length()!=6) {
                    Toast.makeText(VerificationActivity.this, "enter valid otp", Toast.LENGTH_SHORT).show();
                }else {

                    mkLoader.setVisibility(View.VISIBLE);

                   /* PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,otp.getText().toString()
                            .replace("",""));
                    signInWithPhoneAuthCredential(credential);
                  */
                    startActivity(new Intent(VerificationActivity.this, MainActivity.class));
                    finish();

                }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });
    }

    private void sendVerificationCode() {
        new CountDownTimer(60000,100){

            @Override
            public void onTick(long l) {
                resend.setText(""+1/1000);
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {

                resend.setText("Resend");
                resend.setEnabled(true);

            }
        }.start();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new  PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(VerificationActivity.this, "failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String Id,
                    @NonNull PhoneAuthProvider.ForceResendingToken token) {
         VerificationActivity.this.id = id;


            }
        }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mkLoader.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          startActivity(new Intent(VerificationActivity.this, MainActivity.class));
                          finish();
                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI

                            Toast.makeText(VerificationActivity.this, "Verificatio Failed Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }}