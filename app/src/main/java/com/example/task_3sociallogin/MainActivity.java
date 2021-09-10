package com.example.task_3sociallogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView tvemail,tvname;
    LoginButton fbloginbtn;
    Button googlebtn;


    //AccessTokenTracker mAccessTokenTracker;
    CallbackManager c;
    AccessTokenTracker accessTokenTracker;
    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvemail=findViewById(R.id.email);
        tvname=findViewById(R.id.name);
        fbloginbtn=findViewById(R.id.login_button);
        googlebtn=findViewById(R.id.googlebtn);

        fbloginbtn.setPermissions(Arrays.asList(EMAIL));

        c = CallbackManager.Factory.create();

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,GoogleLogin.class);
                startActivity(intent);
            }
        });

        fbloginbtn.registerCallback(c, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        //getting current user token (if any available user is logged in)
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {

                    tvname.setText("");
                    tvemail.setText("");
                    Toast.makeText(MainActivity.this, "no user logged-in at this time", Toast.LENGTH_SHORT).show();
                } else
                    loaduserProfile(currentAccessToken);
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        c.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken,(object,response)->{
            if(object!=null){
                try{
                    String email=object.getString("email");
                    String name=object.getString("name");
                    tvemail.setText(email);
                    tvname.setText(name);
                }
                catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
        Bundle parameters=new Bundle();
        parameters.putString("fields","email,name");
        request.setParameters(parameters);
        request.executeAsync();
    }
}



