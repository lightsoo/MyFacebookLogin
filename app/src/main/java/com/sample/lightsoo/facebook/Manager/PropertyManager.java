package com.sample.lightsoo.facebook.Manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sample.lightsoo.facebook.MyApplication;

public class PropertyManager {

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private static final String KEY_LOGIN_TYPE = "key_login_type";
    private static final String FILED_FACEBOOK_ID ="facebook";


    public static final String LOGIN_TYPE_FACEBOOK = "login_type_facebook";

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }
    // singleton holder pattern : thread safe, lazy class initialization, memory saving.
    public static class InstanceHolder{ private static final PropertyManager INSTANCE = new PropertyManager();}
    public static PropertyManager getInstance(){ return InstanceHolder.INSTANCE; }


    public void setFaceBookId(String id){
        mEditor.putString(FILED_FACEBOOK_ID, id);
        mEditor.commit();
    }

    public void setLoginType(String loginType){
        mEditor.putString(KEY_LOGIN_TYPE, loginType);
        mEditor.commit();
    }

    public String getFaceBookId(){
        return mPrefs.getString(FILED_FACEBOOK_ID, "");
    }


    public String getLoginType(){
        return mPrefs.getString(KEY_LOGIN_TYPE, "");
    }


}
