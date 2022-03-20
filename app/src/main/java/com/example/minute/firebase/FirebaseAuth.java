package com.example.minute.firebase;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.example.minute.MainActivity;
import com.example.minute.databinding.ActivityMainBinding;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class FirebaseAuth {
    private com.google.firebase.auth.FirebaseAuth fbAuth;
    private FirebaseUser currentUser;
    private GoogleSignInClient mGoogleSignInClient;

    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    private String TAG = "log";

    // Constructor: sharedPreferences to save current user, Activity context
    public FirebaseAuth(Context context) {
        sharedPreferences = context.getSharedPreferences("firebase", 0);
        mContext = context;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    // saves user info to sharedPreferences
    public void saveCurrentUser() {
        editor = sharedPreferences.edit();
        editor.putString("uid", currentUser.getUid());
        try {
            editor.putString("name", currentUser.getDisplayName());
            editor.putString("email", currentUser.getEmail());
        } catch (Exception e) {
            // Anonymously signed in
            editor.putString("name", "");
            editor.putString("email", "");
        }
        editor.commit();
    }
    // clear user info after logging out
    public void clearCurrentUser() {
        editor = sharedPreferences.edit();
        editor.putString("name", "");
        editor.putString("email", "");
        editor.putString("uid", "");
        editor.commit();
    }

    // FIREBASE SIGN IN/OUT
    public void signInOptions() {
        // not exactly sure what to do here yet, but want to check if a Google user is already
        // logged in, if not anonymous sign in

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount googleAcc = GoogleSignIn.getLastSignedInAccount(mContext);
        if(googleAcc==null){
            anonymousSignIn();
        } else {
            currentUser = fbAuth.getCurrentUser();
        }
    }

    // Anonymous sign in - default
    public void anonymousSignIn() {
        fbAuth.signInAnonymously()
                .addOnCompleteListener(mContext.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            currentUser = fbAuth.getCurrentUser();
                            saveCurrentUser();
                            Toast.makeText(mContext,
                                    "Anonymous authentication success!",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext,
                                    "Anonymous authentication failed. "
                                            + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Sign in using Google Authentication
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        // NEW WAY OF DOING startActivityForResult
        mStartForResult.launch(signInIntent);
    }

    // NEW WAY OF DOING getActivityResult
    // can't get rid of the error on registerForActivityResult....??
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                            // links Google acc to firebase user
                            // get id token for logged in Google Account
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e);

                        }
                    }
                }
            });

    // Links anonymous account with Google account
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        try {
            Objects.requireNonNull(fbAuth.getCurrentUser()).linkWithCredential(credential)
                    .addOnCompleteListener(mContext.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "linkWithCredential:success");
                                currentUser = task.getResult().getUser();
                                saveCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            // if linking with the anonymous acc fails, just sign in with Google firebase auth
            fbAuth.signInWithCredential(credential)
                    .addOnCompleteListener(mContext.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "Google sign in:success");
                                currentUser = task.getResult().getUser();
                                saveCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "Google sign in:failure", task.getException());
                            }
                        }
                    });
        }
    }

    // Sign out of account and clear user info
    public void signOut() {
        fbAuth.signOut();
        clearCurrentUser();
    }
}
