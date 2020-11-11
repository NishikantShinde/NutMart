package com.li.dryfruits.ui.verification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.li.dryfruits.R;
import com.li.dryfruits.data.model.UserModel;
import com.li.dryfruits.ui.dashboard.DashboardActivity;
import com.li.dryfruits.util.AppConstants;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.aabhasjindal.otptextview.OtpTextView;

public class PhoneVerificationActivity extends AppCompatActivity {

    String phoneNumber;
    UserModel userModel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.otp_view)
    OtpTextView otpView;
    @BindView(R.id.verify)
    Button verify;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.resend_otp)
    TextView resendOtp;
    private String verificationId;
    private FirebaseAuth mAuth;
    Bundle b;
    private boolean fromLogin = false;
    DatabaseReference databaseUsers;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        setTitle("");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(AppConstants.sharedPreferences, MODE_PRIVATE);
        b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("user")) {
                userModel = b.getParcelable("user");
                phoneNumber = userModel.getMobile();
                fromLogin = false;
            } else if (b.containsKey(AppConstants.VerifyLogin)) {
                phoneNumber = b.getString(AppConstants.VerifyLogin);
                fromLogin = true;
            }
        }

        sendVerificationCode(phoneNumber);

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNumber);
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpCode= otpView.getOTP();
                if (otpCode.length()==6){
                    verificationCode(otpCode);
                }else if (otpCode.length()>0){
                    Toast.makeText(PhoneVerificationActivity.this, "Please Enter correct OTP", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PhoneVerificationActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
           /* case R.id.action_done:
                done();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void verificationCode(String code) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if (fromLogin) {
                        Intent i = new Intent(PhoneVerificationActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        String uid = AppConstants.getLoginUID(PhoneVerificationActivity.this);
                        addUser(uid);
                    }
                } else {
                    Toast.makeText(PhoneVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUser(String uid) {
        String id = databaseUsers.push().getKey();
        userModel.setUserId(uid);
        databaseUsers.child(id).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("", "");
                Intent i = new Intent(PhoneVerificationActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("", "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("", e.getMessage());
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        resendOtp.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        CountDownTimer countDownTimer=new CountDownTimer(20000,10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                resendOtp.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }.start();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBaback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBaback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpView.setOTP(code);
                verificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}