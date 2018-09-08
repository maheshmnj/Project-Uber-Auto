package com.project.uberauto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.TimeUnit;

public class VerifyActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    AVLoadingIndicatorView avi;
    String mcode;
    TextView sendotp;
    EditText phone;
    Button verify;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance();
        phone = findViewById(R.id.phone);
        sendotp = findViewById(R.id.textView2);
        verify = findViewById(R.id.button);
        verify.setOnClickListener(verifyotp);
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(phone.getText().toString().length()==10) sendOtp();
            else{
                Toast.makeText(VerifyActivity.this, "enter 10 digit phone no", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
    View.OnClickListener verifyotp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(VerifyActivity.this, "Something Went Wrong :(", Toast.LENGTH_SHORT).show();
        }
    };

    public void sendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone.getText().toString().trim(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
        avi.show();
//        https://material.io/tools/color/#!/?view.left=1&view.right=0&primary.color=D50000&secondary.color=AD1457
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("", "onVerificationCompleted:" + credential);
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            Log.d("", "onCodeSent:" + verificationId);
            Intent verifyActivity = new Intent(VerifyActivity.this, DriverMapsActivity.class);
            startActivity(verifyActivity);
            // Save verification ID and resending token so we can use them later
            mcode = verificationId;
            mResendToken = token;
            Log.d("#otp = ",mcode);

            // ...
        }
    };
//   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mcode, mcode);

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
