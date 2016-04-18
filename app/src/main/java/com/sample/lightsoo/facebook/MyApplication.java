package com.sample.lightsoo.facebook;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;

public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //facebook sdk를 사용하기위해서 한번 로그인하면 이걸 쉐프에 저장한다. 그래서 다음번에 로그인할때 이걸쓰기 위한 처리작업
        //페이스북 기능을 쓰기전에 호출되어야한다.
        //facebook SDK에 application context값을 설저해주고, 쉐프에 저장된 로그인정보(accessToken정보)가 있으면 불러와서
        //AccessToken에 해당 값을 설정해 주는 역할을 한다.
        mContext = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }
}
