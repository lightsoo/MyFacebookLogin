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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        //일반 버튼으로 로그인!
        loginButton = (Button)findViewById(R.id.btn_fblogin);
//        setLabel();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOrLogout2();

//                loginOrLogout();

//                if (!isLogin()) { // 현재 로그인이 안되어있따는 것
//                    //아래 login 함수에서 동작하는 것과 같은 코드.
//
//
//
//                    mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
//                            //페이스북 가입하면 일련번호를 생성해서 그걸 id로 쓴다.
//                            AccessToken token = AccessToken.getCurrentAccessToken();
//                            //token. 하면 여러가지 정보가있다.
//                            Toast.makeText(LoginActivity.this, "id : " + token.getUserId(), Toast.LENGTH_SHORT).show();
//                            user = new User(token.getToken(), "2");
//                            Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user); // load login request method in UserApi
//                            call.enqueue(new Callback() {
//                                @Override
//                                public void onResponse(Response response, Retrofit retrofit) {
//                                    if (response.isSuccess()) {
//                                        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
//                                        PropertyManager.getInstance().setFaceBook(user.getId());
//                                        PropertyManager.getInstance().setFlag(user.getFlag());
//                                        goMainActivity();
//                                    } else {
//                                        if (response.code() == CODE_ID_PASS_INCORRECT) {
//                                            Toast.makeText(LoginActivity.this, "ID or Password incorrect", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(LoginActivity.this, "Server Failure.", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Throwable t) {
//                                    Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//
//                        @Override
//                        public void onError(FacebookException error) {
//
//                        }
//                    });
//
//
////                    mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
////                    mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
//                    //read permission은 글을 읽는 것까지 허용하겠다는것
//                    //세번째 인자에 퍼미션 목록이 들어간다.
//                    //퍼미션 목록은 개발자 사이트에 status & review에 Approved Item에 나와있다.
//                    //write나 기타 추가 퍼미션을 원하면 Submit Items for Approval에 따로 submit해야함
//                    mLoginManager.logInWithReadPermissions(LoginActivity.this, null);
//
//
//                } else {
//                    mLoginManager.logOut();
//                }
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
                                PropertyManager.getInstance().setFaceBookId(userLoginId);
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


//                    flag = PropertyManager.getInstance().getFlag();
//                    user = new User(id, flag);
//                    Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onResponse(Response response, Retrofit retrofit) {
//                            if(response.isSuccess()){
//                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
//                                PropertyManager.getInstance().setFaceBook(user.getId());
//                                PropertyManager.getInstance().setFlag(user.getFlag());
//                                goMainActivity();
//                            } else {
//                                if(response.code() == CODE_ID_PASS_INCORRECT){
//                                    Toast.makeText(LoginActivity.this, "ID or Password incorrect", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(LoginActivity.this, "Server Failure.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                            Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });



                }
//                else{
//                    setLabel();
//                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //트랙킹 종료
        tracker.stopTracking();
    }

    //현재 로그인 여부에 따라 버튼 텍스트 변경
    private void setLabel(){
        if(!isLogin()){ // 현재 로그인이 안되어있따는 것
            loginButton.setText("login");
        } else {
            loginButton.setText("logout");
        }
    }
    //토큰이 있는지 없는지에 따라서 로그인 판정
    private boolean isLogin(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token == null ? false:true;
    }

/*
    private void loginOrLogout() {
        Log.d("test", String.valueOf(isLogin()));
        //토큰이 NULL이다 + 로그인이 안되있다
        if(!isLogin()) {
            Log.d("test", "토큰이 없다.");
            mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //페이스북 가입하면 일련번호를 생성해서 그걸 id로 쓴다.
                    AccessToken token = AccessToken.getCurrentAccessToken();
                    //token. 하면 여러가지 정보가있다.
                    Toast.makeText(LoginActivity.this, "id : " + token.getUserId(), Toast.LENGTH_SHORT).show();
                    user = new User(token.getToken(), "2");
                    Call call = NetworkManager.getInstance().getAPI(LoginAPI.class).login(user); // load login request method in UserApi
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            if (response.isSuccess()) {
//                                Message msg = new Message();
                                Message msg = (Message) response.body();
                                Log.d("test", "msg : " + msg);

                                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                PropertyManager.getInstance().setFaceBookId(user.getId());
                                PropertyManager.getInstance().setLoginType(PropertyManager.LOGIN_TYPE_FACEBOOK);
                                goMainActivity();
                            } else {
                                if (response.code() == CODE_ID_PASS_INCORRECT) {
                                    Toast.makeText(LoginActivity.this, "ID or Password incorrect", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Server Failure.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
//            mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
            //read permission은 글을 읽는 것까지 허용하겠다는것
            //세번째 인자에 퍼미션 목록이 들어간다.
            //퍼미션 목록은 개발자 사이트에 status & review에 Approved Item에 나와있다.
            //write나 기타 추가 퍼미션을 원하면 Submit Items for Approval에 따로 submit해야함
            mLoginManager.logInWithReadPermissions(this, null);
        }else {
            mLoginManager.logOut();
        }
    }*/

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
}
