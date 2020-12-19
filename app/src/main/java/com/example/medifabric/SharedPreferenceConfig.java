package com.example.medifabric;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceConfig {

    private SharedPreferences sharedPreferences;
    private Context context;



    public SharedPreferenceConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);

    }

    public void writeLoginStatus(boolean status, String uid, String pwd, String uemail, String ucontact, String uaddres, String ugender, String dob,String name, String pubkey, String privkey,String uprofileurl){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid",uid);
        editor.putString("password",pwd);
        editor.putString("useremail",uemail);
        editor.putString("usercontact",ucontact);
        editor.putString("useraddress",uaddres);
        editor.putString("usergender",ugender);
        editor.putString("userdob",dob);
        editor.putString("username",name);
        editor.putString("PublicKey",pubkey);
        editor.putString("PrivateKey",privkey);
        editor.putString("profileURL",uprofileurl);
        editor.apply();
        //editor.putBoolean(context.getResources().getString(R.string.login_status_preference), status);
        // editor.commit();
    }

    public String readLoginStatus(){
        //boolean status = false;

        String name = sharedPreferences.getString("userid","");
        String password = sharedPreferences.getString("password","");

        if(name!="" && password!=""){
            return name;
        }
        else
            return "";
        //status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference), false);
        //return status;
    }

    public String read_userid(){
        return sharedPreferences.getString("userid","");
    }
    public String read_username(){
        return sharedPreferences.getString("username","");
    }
    public String read_userpubk(){
        return sharedPreferences.getString("PublicKey","");
    }
    public String read_userprik(){
        return sharedPreferences.getString("PrivateKey","");
    }

    public String read_password(){
        return sharedPreferences.getString("password","");
    }

    public String read_email(){
        return sharedPreferences.getString("useremail","");
    }

    public String read_contact(){
        return sharedPreferences.getString("usercontact","");
    }

    public String read_address(){
        return sharedPreferences.getString("useraddress","");
    }
    public String read_gender(){
        return sharedPreferences.getString("usergender","");
    }
    public String read_dob(){
        return sharedPreferences.getString("userdob","");
    }
    public String read_uprofileurl(){ return sharedPreferences.getString("profileURL","");}

}
