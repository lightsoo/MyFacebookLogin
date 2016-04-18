package com.sample.lightsoo.facebook;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tacademy on 2015-10-28.
 */
public class PropertyManager {
    private static PropertyManager instance;
    //정확하게 만들려면 싱크로나이즈드해야한다!
    public static PropertyManager getInstance(){
        if(instance==null){
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    private static final String FILED_FACEBOOK_ID ="facebook";

    public void setFaceBook(String id){
        mEditor.putString(FILED_FACEBOOK_ID, id);
        mEditor.commit();
    }

    public String getFaceBookId(){
        return mPrefs.getString(FILED_FACEBOOK_ID, "");
    }
}
