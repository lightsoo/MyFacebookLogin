package com.sample.lightsoo.facebook.Data;

/**
 * Created by LG on 2016-04-18.
 */
public class User {

    public String id;
    public String flag;
    public User(String id, String flag){
        this.id = id;
        this.flag = flag;
    }

    public void setId(String id) {
        this.id = id;
    }


    //getter
    public String getId() {
        return id;
    }

    public String getFlag() {
        return flag;
    }



}
