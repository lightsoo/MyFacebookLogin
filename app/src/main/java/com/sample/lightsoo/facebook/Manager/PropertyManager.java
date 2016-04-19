package com.sample.lightsoo.facebook.Manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sample.lightsoo.facebook.MyApplication;

public class PropertyManager {

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    public static final String KEY_ID = "key_id";
    public static final String KEY_FLAG = "key_flag";

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }
    // singleton holder pattern : thread safe, lazy class initialization, memory saving.
    public static class InstanceHolder{ private static final PropertyManager INSTANCE = new PropertyManager();}
    public static PropertyManager getInstance(){ return InstanceHolder.INSTANCE; }

    private static final String FILED_FACEBOOK_ID ="facebook";

    public void setFaceBook(String id){
        mEditor.putString(FILED_FACEBOOK_ID, id);
        mEditor.commit();
    }
    public void setFlag(String flag){
        mEditor.putString(KEY_FLAG, flag);
        mEditor.commit();
    }
    public String getFaceBookId(){
        return mPrefs.getString(FILED_FACEBOOK_ID, "");
    }

    public String getFlag(){
        return mPrefs.getString(KEY_FLAG, "");
    }



}
