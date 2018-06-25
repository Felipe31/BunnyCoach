package ipb.dam.apptrainer.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.about.AboutActivity;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;
import ipb.dam.apptrainer.register.RegisterActivity;
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
            this.loginSuccessful(null);
        }

        Connection.getInstance().requestUserLogin(usernameApp, passwdApp);

    }

    public void loginSuccessful(Context context, JSONObject result) {
        this.context = context;
        loginSuccessful(result);
    }
    private void loginSuccessful(JSONObject result){
        if(context != null) {
            try {
                setToken(result.getString("token"));
                isLogged = true;
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((AppCompatActivity)context).finish();
                context = null;
                if(data != null) setData(data);
            } catch (JSONException e) {
                e.printStackTrace();
                makeLogout();
            }
        }

    }


    private boolean makeLogout() {
        isLogged = false;
        if(context != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
            return true;
        }

        return false;
    }

    public void updateAbout(Context context, String height, String weight, String hours_per_day, String working_days){

        this.context = context;
        this.height = height;
        this.weight = weight;
        this.hours_per_day = hours_per_day;
        this.working_days = working_days;

        try {
            Connection.getInstance().updateAbout(getToken(), height, weight, hours_per_day, working_days);
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "Token not defined");
            errorHandler(null);
        }
    }


    public void registerUser(Context context, String name, String email, String password, String birthday) {
        this.context = context;
        Connection.getInstance().registerUser(name, email, password, birthday);
    }

    public void registrationSuccessful(JSONObject result) {
        try {
            setToken(result.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
            makeLogout();
        }

        if(context != null) {
            context.startActivity(new Intent(context, ProfileChooserActivity.class));
            Toast.makeText(context, context.getResources().getString(R.string.info_registration_successful), Toast.LENGTH_SHORT).show();
        }

    }

    public void errorHandler(JSONObject error) {
        if(context == null)
            return;

        if(error == null){
            Toast.makeText(context, context.getResources().getString(R.string.info_internal_error), Toast.LENGTH_SHORT).show();
                return;
        }

        Iterator<String> keys = error.keys();
        Log.e(this.getClass().getSimpleName(), "Error on operation" + error.toString());
        Toast.makeText(context, context.getResources().getString(R.string.info_operation_failed), Toast.LENGTH_SHORT).show();


        try {
            while( keys.hasNext() ) {
                String key = keys.next();
                Log.e(this.getClass().getSimpleName(), ((JSONArray)error.get(key)).get(0).toString());
                Toast.makeText(context, ((JSONArray)error.get(key)).get(0).toString(), Toast.LENGTH_SHORT).show();
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
            errorHandler(null);
            makeLogout();
        }

    }

    public void profileSuccessful(){

        if(context != null)
            context.startActivity(new Intent(context, AboutActivity.class));

    }

    public String getToken() throws Resources.NotFoundException{
        if(token == null ) {
            throw new Resources.NotFoundException();
        }
        else if(token.equals("")) {
            throw new Resources.NotFoundException();
        }
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

    public void resultHandler(JSONObject result) {
        if(context instanceof LoginActivity)
            loginSuccessful(result);
        else if(context instanceof RegisterActivity)
            registrationSuccessful(result);

    }
}
