package com.sample.lightsoo.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

//페북 개발자 사이트 setting에서 액티비티를 이걸로 바꿔야한다.
public class SplashActivity extends AppCompatActivity {

    Handler mHandler = new Handler(Looper.getMainLooper());

    private String id;
    private String flag;

    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginManager mLoginManager = LoginManager.getInstance();
    AccessTokenTracker mTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FacebookID값 얻어
        final String id = PropertyManager.getInstance().getFaceBookId();
        if(!TextUtils.isEmpty(id)){
            //트랙커는 생성만 해도 트랙킹을 시작한다.
            mTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    AccessToken token = AccessToken.getCurrentAccessToken();
                    if(token != null){
                        if(token.getUserId().equals(id)){
                            flag = PropertyManager.getInstance().getFlag();
                            User user = new User(id, flag);
                            Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user);
                            call.enqueue(new Callback() {
                                @Override
                                public void onResponse(Response response, Retrofit retrofit) {
                                    if(response.isSuccess()) { // code : 200, 201
                                        Toast.makeText(SplashActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        goMainActivity();
                                    } else { //other error code
                                        if(response.code() == 500)
                                            Toast.makeText(SplashActivity.this, "server  db query error : " + response.code() + "\nTry again later", Toast.LENGTH_SHORT).show();
                                        else
                                        if(response.code() == 501)
                                            Toast.makeText(SplashActivity.this, "Password or ID is not correct!", Toast.LENGTH_SHORT).show();
                                        goLoginActivity();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(SplashActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                                    goLoginActivity();
                                }
                            });

//                            NetworkManager.getInstance().loginFacebookToken(SplashActivity.this, token.getToken(), new NetworkManager.OnResultListener<String>() {
//                                @Override
//                                public void onSuccess(String result) {
//                                    //지금이 코드상에서는 스플래쉬에서 메인으로 안넘어가는게 정상이다. 네트웍을 시뮬레이션 해서 그럼
//                                    //여기에 OK는 그냥 만약에 OK가 오게 되면 메인으로 넘어간다는 뜻이다.
//                                    if(result.equals("OK")){
//                                        goMainActivity();
//                                    }
//                                }
//
//                                @Override
//                                public void onFail(int code) {
//                                    Toast.makeText(SplashActivity.this, "login Fail..", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                        } else{
                            Toast.makeText(SplashActivity.this, "Facebook id change", Toast.LENGTH_SHORT).show();
                            //로그인은 성공했는데 id가 다른 경우
                            mLoginManager.logOut(); //기존에 페북앱에 로그인 되어있던 id를 로그아웃한다.
                            goLoginActivity();
                        }
                    }
                }
            };

            mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //로그인 성공하고 난 후에 하는 활동들!
                }

                @Override
                public void onCancel() {

                }
                //로그인이 실패했을 경우.
                //로그인 id를 삭제하고 보낼수도잇고 아닐 수도 잇다.
                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(SplashActivity.this, "facebook login fail..", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                }
            });

            mLoginManager.logInWithReadPermissions(this, null);

        } else {//id가 없다면 로그인 페이지로 이동!!!
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SplashActivity.this, "Welcome! please log-in!", Toast.LENGTH_SHORT).show();
                    goLoginActivity();
                }
            }, 1500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTokenTracker != null){
            mTokenTracker.stopTracking();;
            mTokenTracker = null; //이거안해도됨?
        }
    }
}