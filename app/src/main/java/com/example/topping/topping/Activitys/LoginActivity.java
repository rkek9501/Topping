package com.example.topping.topping.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topping.topping.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.soyu.soyulib.soyuHttpTask;

import java.util.StringTokenizer;

public class LoginActivity extends AbstractActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private String Tag = "LoginActivity";
    private static final int RC_SIGN_IN = 10;
    Handler logingHandler = new MessageHandler();
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView textView;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", "value");
//        editor.remove("user");
        editor.commit();

        String text = sp.getString("key",null);

        Log.e("GoogleLoginCheck", "1. onCreate");
        setContentView(R.layout.activity_login);
        mStatusTextView = findViewById(R.id.status);
        mDetailTextView = findViewById(R.id.detail);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        textView = (TextView)findViewById(R.id.login_text);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton loginBtn = (SignInButton)findViewById(R.id.sign_in_button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("GoogleLoginCheck", "8. loginBtn Click " );
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.e("GoogleLoginCheck", "3. mAuthListener");
                user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.e("GoogleLoginCheck", "4. mAuthListener User");
                    userMail = user.getEmail();
                    userName = user.getDisplayName();
                    new soyuHttpTask(logingHandler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://61.84.24.188/topping3/loginCheck.php", "userMail="+userMail, "");
                    /*logingHandler.postDelayed(new Runnable() {
                        //LoginCheck 메소드를 1초후 실행
                        @Override
                        public void run() {
                            LoginCheck();
                        }
                    }, 1000)*/;
                }else{
                    Log.e("GoogleLoginCheck", "4. mAuthListener User Null");
                }

            }
        };
    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            Log.e("GoogleLoginCheck", "6. MessagHandler");
            Log.e(Tag, "obj = "+msg.obj.toString());
            loginCheckPaser(msg.obj.toString());
        }
    }
    void loginCheckPaser(String result) {
        Log.e("GoogleLoginCheck", "7. loginCheckPaser");
        StringTokenizer tokens = new StringTokenizer(result);

        String url = tokens.nextToken("|");
        String data = tokens.nextToken("|");

        Log.e("url", url);
        Log.e("data", data);

        loginCheck = data.trim().toString();
        LoginCheck(loginCheck);
    }
    boolean LoginCheck;
    public void LoginCheck(String CheckLogin){
        Log.e("GoogleLoginCheck", "5. Login Cheek");
        if (CheckLogin.equals("loginOK")){
            Log.e("GoogleLoginCheck", "5. Login Cheek Success");
            Log.e("login", "success");
            LoginCheck=true;
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(LoginActivity.this, "로그인 되었습니다. LoginCheck", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Log.e("GoogleLoginCheck", "5. Login Cheek Failed");
            Log.e("login", "failed");
            LoginCheck=false;
            startActivity(new Intent(getApplicationContext(), HobbyActivity.class));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.e("GoogleLoginCheck", "2. onStart");
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("GoogleLoginCheck", "onStop");
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("GoogleLoginCheck", "9. onActivityResult");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            if(result != null){
//                if (result.isSuccess()) {
//                    Log.e("GoogleLoginCheck", "10. onActivityResult isSuccess");
//                    // Google Sign In was successful, authenticate with Firebase
//                    GoogleSignInAccount account = result.getSignInAccount();
//                    String personName = account.getDisplayName();
//                    String personEmail = account.getEmail();
//                    String personId = account.getId();
//                    String tokenKey = account.getServerAuthCode();
//
//                    firebaseAuthWithGoogle(account);
//
//                    Log.e("GoogleLogin", "personName=" + personName);
//                    Log.e("GoogleLogin", "personEmail=" + personEmail);
//                    Log.e("GoogleLogin", "personId=" + personId);
//                    Log.e("GoogleLogin", "tokenKey=" + tokenKey);
//                } else {
//                    // Google Sign In failed, update UI appropriately
//                    Log.e("GoogleLogin", "login fail cause=" + result.getStatus().getStatusMessage());
//                }
//            }
//
//        }
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(Tag, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    /*private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d(Tag, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Tag, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(Tag, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }*/
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e("GoogleLoginCheck", "11. FirebaseAuthWithGoogle");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.e("GoogleLoginCheck", "FirebaseAuthWithGoogle isNotScucees");
                }else {
                    Log.e("GoogleLoginCheck", "12. FirebaseAuthWithGoogle isScucees");
                    Toast.makeText(LoginActivity.this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
// [START signin]
   private void signIn() {
       Intent signInIntent = mGoogleSignInClient.getSignInIntent();
       startActivityForResult(signInIntent, RC_SIGN_IN);
   }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GoogleLoginCheck", "onConnectionFailed");
    }
    private void handleSignInResult(GoogleSignInResult result){
        Log.d(Tag,"handleSignInResult : "+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            textView.setText(getString(R.string.common_signin_button_text, account.getDisplayName()));
            updateUI(user);
        }else {
        }
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(getString(R.string.common_signin_button_text, user.getEmail()));
            mDetailTextView.setText(getString(R.string.common_signin_button_text, user.getUid()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.common_signin_button_text_long);
            mDetailTextView.setText(null);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }
}
