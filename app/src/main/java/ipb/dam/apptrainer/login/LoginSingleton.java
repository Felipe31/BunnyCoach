package ipb.dam.apptrainer.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.Iterator;

import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.serverConnection.Connection;

public class LoginSingleton {


    private static final LoginSingleton ourInstance = new LoginSingleton();

    public static LoginSingleton getInstance() {
        return ourInstance;
    }

    // TODO: 25/05/18 check which more info the app requires

    private boolean isLogged = false;
    private JSONObject data = null;
    private Context context = null;
    private String profile = null;

    private String token = null;
    private String height = null;
    private String weight = null;
    private String hours_per_day = null;
    private String working_days = null;

    private LoginSingleton() {
    }

    public boolean isLogged() {
        return isLogged;
    }

    protected void makeLogin(Context context, String usernameApp, String passwdApp) {

        this.context = context;

        // TODO: 25/05/18 remove the following if statement
        if(usernameApp.equals("") && passwdApp.equals("")){
            isLogged = true;
            this.loginSuccessful();
        }

        Connection.getInstance().requestUserLogin(usernameApp, passwdApp);

    }

    public void loginSuccessful(Context context) {
        this.context = context;
        loginSuccessful();
    }
    public void loginSuccessful(){
        if(context != null) {
            isLogged = true;
            Intent intent = new Intent(context, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((AppCompatActivity)context).finish();
            context = null;
        }

    }


    public boolean makeLogout() {
        isLogged = false;
        if(context != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
        }

        return true;
    }

    public boolean updateAbout( String height, String weight, String hours_per_day, String working_days){

        this.height = height;
        this.weight = weight;
        this.hours_per_day = hours_per_day;
        this.working_days = working_days;

        try {
            Connection.getInstance().updateAbout(getToken(), height, weight, hours_per_day, working_days);
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "token not defined");
            return false;
        }
        return true;
    }

    public void registrationFailed(JSONObject error){
        Iterator<?> keys = error.keys();
        Log.e(this.getClass().getSimpleName(), "Error on registration");

        try {
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if (error.get(key) instanceof JSONObject) {
                    Log.e(this.getClass().getSimpleName(), error.getString(key));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
        Log.w(this.getClass().getSimpleName(), data.toString());
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(Context context, String profile) {
        this.profile = profile;
        this.context = context;

        try {
            Connection.getInstance().registerProfile(getToken() , profile);
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "Token not defined");
            makeLogout();
        }

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHours_per_day() {
        return hours_per_day;
    }

    public void setHours_per_day(String hours_per_day) {
        this.hours_per_day = hours_per_day;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
    }
}
