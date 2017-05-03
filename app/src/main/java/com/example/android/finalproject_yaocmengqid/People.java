package com.example.android.finalproject_yaocmengqid;

/**
 * Created by Maureen_Ding on 4/24/17.
 */

public class People {
    String name;
    String email;

    public People(String name, String email){
        this.name = name;
        this.email = email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
