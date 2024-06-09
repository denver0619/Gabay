package com.digiview.gabay.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.gabay.MainActivity;
import com.digiview.gabay.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText inputEmail, inputPassword, inputReEnterPassword;
    Button  emailSignup, googleSignup;
    TextView textViewLoginRedirect;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> activityResultLauncher =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode()==RESULT_OK) {
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                        try {
                            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                            firebaseAuth.signInWithCredential(authCredential)
                                    .addOnCompleteListener(
                                            new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        reloadThisActivity();
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Account Registration Failed", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                    );
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonGoogleRegister) {
            //Google
            GoogleSignInOptions signInOptions = new GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.web_client_id_default))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
            Intent intent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(intent);
        } else if (v.getId() == R.id.buttonEmailRegister) {
            RegisterUserWithEmail();
        } else if (v.getId() == R.id.textViewLoginRedirect) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //initialize all needed views
        inputEmail = findViewById(R.id.textInputRegisterEmail);
        inputPassword = findViewById(R.id.textInputRegisterPassword);
        inputReEnterPassword = findViewById(R.id.textInputRegisterConfirmPassword);
        textViewLoginRedirect = findViewById(R.id.textViewLoginRedirect);
        emailSignup = findViewById(R.id.buttonEmailRegister);
        googleSignup = findViewById(R.id.buttonGoogleRegister);

        FirebaseApp.initializeApp(RegisterActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();

        //GOOGLE
        googleSignup.setOnClickListener(this);
        //EMAIL
        emailSignup.setOnClickListener(this);

        textViewLoginRedirect.setOnClickListener(this);

    }

    private void RegisterUserWithEmail() {
        String emailText = Objects.requireNonNull(inputEmail.getText()).toString().trim();
        String passwordText = Objects.requireNonNull(inputPassword.getText()).toString().trim();
        String reEnterPasswordText = Objects.requireNonNull(inputReEnterPassword.getText()).toString().trim();

        if (emailText.isEmpty() || passwordText.isEmpty() || reEnterPasswordText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!passwordText.equals(reEnterPasswordText)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Send verification email
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                            // Redirect to login activity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        // if there is a user go to main activity
        if(currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // reload this activity
    private void reloadThisActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }


}
