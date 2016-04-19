package com.sample.lightsoo.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.sample.lightsoo.facebook.Data.User;
import com.sample.lightsoo.facebook.Manager.NetworkManager;
import com.sample.lightsoo.facebook.Manager.PropertyManager;
import com.sample.lightsoo.facebook.RestAPI.LoginAPI;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //server response code
    private static final int CODE_ID_PASS_INCORRECT = 531;

    //for facebook
    CallbackManager callbackManager;
    Button loginButton;
    LoginManager mLoginManager;
    AccessTokenTracker tracker;

    String loginType;
    String userLoginId;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        //일반 버튼으로 로그인!
        loginButton = (Button)findViewById(R.id.btn_fblogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrLogout2();
            }
        });

        //이렇게 생성해주기만하면 트래킹이 작동한다. 그래서 액티비티 종료되면 트랙킹도 종료해야한다.
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                setLabel();

                final AccessToken token = AccessToken.getCurrentAccessToken();
                if(token != null){

                    userLoginId = token.getUserId();
                    user = new User(userLoginId, PropertyManager.LOGIN_TYPE_FACEBOOK);
                    Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            if(response.isSuccess()){
                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                PropertyManager.getInstance().setUserLoginId(userLoginId);
                                PropertyManager.getInstance().setLoginType(PropertyManager.LOGIN_TYPE_FACEBOOK);
                                goMainActivity();
                            } else {
                                if(response.code() == CODE_ID_PASS_INCORRECT){
                                    Toast.makeText(LoginActivity.this, "ID or Password incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Server Failure.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //트랙킹 종료
        tracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이걸 반드시해줘야한다. 얘가 있어야 콜백이 호출된다. 액티비티가 받은 결과를 callbackmanager로 토스!!!
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //로그인 성공하면 메인으로 이동하고 이전액티비티는 종료한다.
    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        tracker.stopTracking();
        finish();
    }

    private void loginOrLogout2(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token == null) {
            mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
            mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
            mLoginManager.logInWithReadPermissions(this, null);
        } else {
            mLoginManager.logOut();
        }
    }


}
