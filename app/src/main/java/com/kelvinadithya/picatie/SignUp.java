package com.kelvinadithya.picatie;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Arrays;
import java.util.List;

public class SignUp extends AppCompatActivity {

    Button Signup;
    TextView login;
    ImageView btn;
    CheckBox cb;
    String email1, pass1, name1;
    EditText name, email, pass, Location;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        Location = (EditText)findViewById(R.id.editTextTextPostalAddress);
        Signup = (Button) findViewById(R.id.button);
        login = (TextView)findViewById(R.id.textView5);
        name = (EditText)findViewById(R.id.editText);
        email = (EditText)findViewById(R.id.editText2);
        pass = (EditText)findViewById(R.id.editText3);
        btn = (ImageView)findViewById(R.id.imageView5);

        Places.initialize(getApplicationContext(),"AIzaSyBUsDaEb6nYn9my1CMjtyHDJdoRiQL9Vks");
        Location.setFocusable(false);
        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(SignUp.this);

                startActivityForResult(intent,100);
            }
        });




        btn.setOnClickListener(new View.OnClickListener() {
            private int passwordNotVisible=1;
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btn.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
                    passwordNotVisible = 0;
                } else {

                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btn.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    passwordNotVisible = 1;
                }


                pass.setSelection(pass.length());

            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SignUp.this,Log_in.class);
                startActivity(in);

            }
        });
    }

    private void submitForm() {
        name1 = name.getText().toString().trim();
        email1 = email.getText().toString().trim();
        pass1 = pass.getText().toString().trim();


        if (name1.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }
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

            mAuth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(getApplicationContext(), Log_in.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_LONG).show();
                        finish();
                        //Intent i = new Intent(getApplicationContext(), Log_In.class);
                        //startActivity(i);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                }
            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK){
            Place place = Autocomplete.getPlaceFromIntent(data);
            Location.setText(place.getAddress());
        }
        else if (resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
