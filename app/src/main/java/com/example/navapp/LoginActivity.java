package com.example.navapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorAssertion;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.MultiFactorResolver;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneMultiFactorGenerator;
import com.google.firebase.auth.PhoneMultiFactorInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    TextView dontHaveAcc;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userid;
    SharedPreferences sharedPreferences;
    public static final String Username = "username";
    public static final String pass_wrd = "password";
    private PhoneAuthCredential cred;
    private String VID;
    private PhoneAuthProvider.ForceResendingToken Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        if(sharedPreferences.contains(Username)){
            //Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            //startActivity(i);
        }

        dontHaveAcc = findViewById(R.id.dontHaveAcc);
        dontHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String username = mUsername.getText().toString();
                    String passwordAAAA = mPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required!");
                    return;
                }
                if (TextUtils.isEmpty(passwordAAAA)) {
                    mPassword.setError("Password is required!");
                    return;
                }

                DocumentReference documentReference = fStore.collection("user_profile").document(username);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot DS = task.getResult();
                        String checkpw;
                        if (DS.exists()) {
                            checkpw = DS.get("password").toString();
                            if (BCrypt.checkpw(passwordAAAA, checkpw)) {
                                //Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                //SharedPreferences.Editor editor = sharedPreferences.edit();
                                //editor.putString(Username, DS.get("username").toString());
                                //editor.putString(pass_wrd, DS.get("password").toString());
                                //editor.commit();

                                firebaseAuth.signInWithEmailAndPassword(DS.get("email").toString(),passwordAAAA).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful())
                                        {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user.isEmailVerified()) {
                                                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                                System.out.println("samuel 'dabuz' buzby");
                                                startActivity(new Intent(getApplicationContext(), twofareg.class));
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginActivity.this, "Please Verify your email", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        if (task.getException() instanceof FirebaseAuthMultiFactorException)
                                        {
                                            FirebaseAuthMultiFactorException e =
                                                    (FirebaseAuthMultiFactorException) task.getException();

                                            MultiFactorResolver multiFactorResolver = e.getResolver();

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
                                            MultiFactorInfo selectedHint = multiFactorResolver.getHints().get(0);
                                            PhoneMultiFactorInfo PMFI = null;
                                            // Ask user which second factor to use.
                                            if (selectedHint.getFactorId() == PhoneMultiFactorGenerator.FACTOR_ID)
                                            {

                                                PMFI = (PhoneMultiFactorInfo) selectedHint;
                                            }



                                            // Send the SMS verification code.
                                            PhoneAuthProvider.verifyPhoneNumber(
                                                    PhoneAuthOptions.newBuilder()
                                                            .setActivity(LoginActivity.this)
                                                            .setMultiFactorSession(multiFactorResolver.getSession())
                                                            .setMultiFactorHint(PMFI)
                                                            .setCallbacks(callbacks)
                                                            .setTimeout(30L, TimeUnit.SECONDS)
                                                            .build());





                                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                            builder.setTitle("Verification Code");

// Set up the input
                                            final EditText input = new EditText(LoginActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                            builder.setView(input);

// Set up the buttons
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    String javascript = input.getText().toString();
                                                    // Ask user for the SMS verification code.
                                                    PhoneAuthCredential credential =
                                                            PhoneAuthProvider.getCredential(VID, javascript);

                                                    // Initialize a MultiFactorAssertion object with the
                                                    // PhoneAuthCredential.
                                                    MultiFactorAssertion multiFactorAssertion =
                                                            PhoneMultiFactorGenerator.getAssertion(credential);

                                                    // Complete sign-in.
                                                    multiFactorResolver
                                                            .resolveSignIn(multiFactorAssertion)
                                                            .addOnCompleteListener(
                                                                    new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                            if (task.isSuccessful()) {
                                                                                // User successfully signed in with the
                                                                                // second factor phone number.
                                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                                editor.putString(Username, DS.get("username").toString());
                                //editor.putString(pass_wrd, DS.get("password").toString());
                                                                                editor.commit();
                                                                                Toast.makeText(LoginActivity.this, "TWO FACTOR LOGGED IN", Toast.LENGTH_SHORT).show();
                                                                                startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                                                            }
                                                                            // ...
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

                                            builder.show();

                                        } else {
                                            // Handle other errors such as wrong password.
                                        }
                                    }

                                });
                                //startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"No account found!", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

    }
}