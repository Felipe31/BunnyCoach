package ipb.dam.apptrainer.login;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import ipb.dam.apptrainer.serverConnection.Connection;

public class LoginSingleton {


    private static final LoginSingleton ourInstance = new LoginSingleton();

    public static LoginSingleton getInstance() {
        return ourInstance;
    }

    // TODO: 25/05/18 check which more info the app requires

    private String username = "";
    private String firstName = "";
    private boolean isLogged = false;


    private LoginSingleton() {
    }

    public boolean isLogged() {
        return isLogged;
    }

    protected boolean makeLogin(String usernameApp, String passwdApp) {

        // TODO: 25/05/18 remove the following if
        if(usernameApp.equals("") && passwdApp.equals("")){
            isLogged = true;
            return true;
        }
        

        Connection connection = new Connection();
        Log.i(this.getClass().getSimpleName(), "Vai chamar connection");
        JSONObject userJSON = connection.requestUserInfo(usernameApp, passwdApp);

        if (userJSON != null) {
            try {
                firstName = userJSON.getString("firstName");
                username = userJSON.getString("username");
                isLogged = true;
                return true;
            } catch (Exception e) {
                Log.w(this.getClass().getSimpleName(), "JSON not properly structured");
            }
            return false;

        }else
            return false;

    }

    public boolean makeLogout() {

        firstName = "";
        username = "";
        isLogged = false;

        return true;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }



}
