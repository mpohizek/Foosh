package foosh.air.foi.hr;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1000;
    private FirebaseAuth mAuth;
    Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()){
                    callSignInActivity();
                }
                else{
                    Toast.makeText(SignInActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private boolean isNetworkAvailable() throws NullPointerException{
        try{
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        } catch (NullPointerException ex){
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) throws NullPointerException{
        IdpResponse response = IdpResponse.fromResultIntent(data);
        Toast toast;
        if (resultCode == RESULT_OK){
            try{
                final FirebaseUser user = mAuth.getCurrentUser();
                if (!user.isEmailVerified()){
                    user.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignInActivity.this, "Verification email sent!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            } catch (NullPointerException ex){
                Toast.makeText(SignInActivity.this, "Error sending verification email!", Toast.LENGTH_LONG).show();
            }
            finally {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
        else{
            if (response == null) {
                toast = Toast.makeText(this, "Sign in was cancelled!", Toast.LENGTH_LONG);
                toast.show();
            }
            else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                toast = Toast.makeText(this, "You have no internet connection", Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                toast = Toast.makeText(this, "Unknown Error!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
    public void callSignInActivity(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
}
