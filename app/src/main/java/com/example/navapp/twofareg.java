package com.example.navapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.*;

import java.util.concurrent.TimeUnit;

public class twofareg extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private EditText phonenumber;
    private Button skipbutton;
    private Button twoFAreg;
    private PhoneAuthCredential cred;
    private String VID;
    private PhoneAuthProvider.ForceResendingToken Token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twofareg);
        fAuth = FirebaseAuth.getInstance();
        phonenumber = findViewById(R.id.editTextPhone);
        skipbutton = findViewById(R.id.Skipbutton);
        twoFAreg = findViewById(R.id.button3);

        skipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(twofareg.this, MapsActivity.class));
            }
        });
        twoFAreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phonenumber.getText().toString();
                final String[] UserVerificationCode = new String[1];


                PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                // This callback will be invoked in two situations:
                                // 1) Instant verification. In some cases, the phone number can be
                                //    instantly verified without needing to send or enter a verification
                                //    code. You can disable this feature by calling
                                //    PhoneAuthOptions.builder#requireSmsValidation(true) when building
                                //    the options to pass to PhoneAuthProvider#verifyPhoneNumber().
                                // 2) Auto-retrieval. On some devices, Google Play services can
                                //    automatically detect the incoming verification SMS and perform
                                //    verification without user action.
                                cred = credential;
                            }
                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // This callback is invoked in response to invalid requests for
                                // verification, like an incorrect phone number.
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
                            public void onCodeSent(
                                    String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number.
                                // We now need to ask the user to enter the code and then construct a
                                // credential by combining the code with a verification ID.
                                // Save the verification ID and resending token for later use.
                                VID = verificationId;
                                Token = token;
                                // ...
                            }
                        };



                FirebaseUser user = fAuth.getCurrentUser();
                user.getMultiFactor().getSession().addOnCompleteListener(new OnCompleteListener<MultiFactorSession>()
                {

                    @Override
                    public void onComplete(@NonNull Task<MultiFactorSession> task) {
                        if (task.isSuccessful()) {
                            MultiFactorSession multiFactorSession = task.getResult();
                            PhoneAuthOptions phoneAuthOptions =
                                    PhoneAuthOptions.newBuilder().setActivity(twofareg.this)
                                            .setPhoneNumber(phoneNumber)
                                            .setTimeout(30L, TimeUnit.SECONDS)
                                            .setMultiFactorSession(multiFactorSession)
                                            .setCallbacks(callbacks)
                                            .build();
                            // Send SMS verification code.
                            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
                        }
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(twofareg.this);
                builder.setTitle("Verification Code");

// Set up the input
                final EditText input = new EditText(twofareg.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String javascript = input.getText().toString();
                        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VID, javascript);

                        MultiFactorAssertion multiFactorAssertion = PhoneMultiFactorGenerator.getAssertion(credential);
// Complete enrollment.
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getMultiFactor()
                                .enroll(multiFactorAssertion, "My personal phone number")
                                .addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // ...
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(twofareg.this, "Successful 2Factor Enrollment", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                                }
                                            }
                                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                System.out.println(VID);
                builder.show();
               // VID = "696969";
                //UserVerificationCode[0] = "696969";
// Ask user for the verification code.
                System.out.println("buh");


 /**/
            }
        });
    }
}