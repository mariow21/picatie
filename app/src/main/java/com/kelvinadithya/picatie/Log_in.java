package com.kelvinadithya.picatie;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Log_in extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button Login;
    TextView Signup, forgetpass1;
    String email1, pass1;
    EditText email, pass;
    ImageView passshow;
    private static final String TAG = "Log_In";
    SignInButton signInButton;
    Button etc;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: "+connectionResult.getErrorMessage());
                        Log.d(TAG, "onConnectionFailed: "+ connectionResult.toString());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        Login = (Button)findViewById(R.id.button);
        Signup = (TextView)findViewById(R.id.textView5);
        forgetpass1 = (TextView)findViewById(R.id.textView6);
        email = (EditText)findViewById(R.id.editText2);
        pass = (EditText)findViewById(R.id.editText3);
        passshow = (ImageView)findViewById(R.id.imageView4);




        passshow.setOnClickListener(new View.OnClickListener() {
            private int passwordNotVisible=1;
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passshow.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
                    passwordNotVisible = 0;
                } else {

                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passshow.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    passwordNotVisible = 1;
                }


                pass.setSelection(pass.length());


            }
        });





        //signInButton=(SignInButton)findViewById(R.id.sign_in_button);

//        signInButton.setOnClickListener(new View.OnClickListener() {
        //       @Override
            //          public void onClick(View view) {
        //       Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        //    startActivityForResult(intent,RC_SIGN_IN);
        //  }
        // });//

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signin();
            }
        });


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Log_in.this, SignUp.class);
                startActivity(in);
            }
        });





    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() != null) {
            // finish();
        }
    }

    private void signin() {
        email1 = email.getText().toString().trim();
        pass1 = pass.getText().toString().trim();

        if (email1.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (pass1.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }

        if (pass1.length() < 6) {
            pass.setError("Minimum lenght of password should be 6");
            pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Sign In successful", Toast.LENGTH_LONG).show();
                    // finish();
                    Intent i = new Intent(getApplicationContext(), Home.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile(){
        Intent intent=new Intent(Log_in.this,Home.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.textView4), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


}

