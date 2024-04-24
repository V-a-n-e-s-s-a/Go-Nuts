package com.example.gonuts;

import android.os.Bundle; // used for passing data between activities
import android.view.View; // represents a basic building block for user interface components
import android.widget.EditText; // provides a text entry for users to enter text
import android.widget.ImageButton; // represents a button with an image that can be clicked
import android.widget.Toast; // provides simple feedback about an operation in a small popup

import androidx.annotation.NonNull; // indicates that a parameter, field, or method return value cannot be null
import androidx.appcompat.app.AppCompatActivity; // base class for activities that use the support library action bar features

import com.google.android.gms.tasks.OnCompleteListener; // listener called when a task completes
import com.google.android.gms.tasks.Task; // represents an asynchronous operation
import com.google.firebase.auth.AuthResult; // represents the result os a successful authentication
import com.google.firebase.auth.FirebaseAuth; // entry point of the Firebase Authentication SDK
import com.google.firebase.auth.FirebaseAuthException; // represent errors that occur during Firebase authentication
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException; // exception thrown when invalid credential is used for authentication

/**
 * @author Vanessa
 *
 * LoginActivity is an interface class that displays the Login screen,
 * allows the player to login to the game, and checks for valid
 * username(email) and password.
 */

public class LoginActivity extends AppCompatActivity {
    /**
     * EditText view used for entering the email address
     */
    private EditText editTextEmail;
    /**
     * EditText view used for entering the password
     */
    private EditText editTextPassword;
    /**
     * ImageButton used as the login button
     */
    private ImageButton buttonLogin;
    /**
     * An instance of FirebaseAuth used for Firebase authentication
     */
    private FirebaseAuth authentication;

    /**
     * A method that is called when the activity is being created
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Initializing variables and setting up any necessary event listeners
         * In this case login_screen.xml
         */
        setContentView(R.layout.login_screen);

        /**
         * Initialize Firebase Authentication
         */
        authentication = FirebaseAuth.getInstance();

        /**
         * Find views by their IDs
         */
        editTextEmail = findViewById(R.id.LoginEmailAddress);
        editTextPassword = findViewById(R.id.LoginPassword);
        buttonLogin = findViewById(R.id.LoginButton);

        /**
         * Set click listener for the login button
         */
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Get user entered email and password
                 */
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                /**
                 * Call login method
                 */
                login(email, password);
            }
        });
    }

    /**
     * Logs in the user with the provided email and password
     *
     * @param email is the user's email
     * @param password is the user's password
     */
    private void login(String email, String password) {
        /**
         * Sign in with email and password
         */
        authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    /**
                     * Makes sure a return value cannot be null
                     * @param task is an object that represents the result of an authentication task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /**
                         * Login is successful
                         */
                        if (task.isSuccessful()) {
                            showToast("Login successful");
                        }

                        else {
                            /**
                             * Login failed
                             */
                            handleLoginFailure(task);
                        }
                    }
                });
    }

    /**
     * Handles login failure by displaying appropriate messages
     * @param task is the authentication task that failed.
     */
    private void handleLoginFailure(Task<AuthResult> task) {
        Exception exception = task.getException();

        /**
         * Check the type of exception
         */
        if (exception != null) {
            /**
             * Check if the exception is an instance of FirebaseAuthException
             */
            if (exception instanceof FirebaseAuthException) {
                FirebaseAuthException authException = (FirebaseAuthException) exception;
                /**
                 * Get the error code
                 */
                String errorCode = authException.getErrorCode();
                /**
                 * Invalid user
                 */
                if ("ERROR_INVALID_EMAIL".equals(errorCode) || "ERROR_USER_NOT_FOUND".equals(errorCode)) {
                    showToast("Incorrect email or user not found");
                }
                /**
                 * Invalid password
                 */
                else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                    showToast("Incorrect password");
                }
                /**
                 * Other authentication error
                 */
                else{
                    showToast("Authentication failed: " + authException.getMessage());
                }
            }
        }
    }

    /**
     * Displays a toast message
     * @param message is the message to display
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
