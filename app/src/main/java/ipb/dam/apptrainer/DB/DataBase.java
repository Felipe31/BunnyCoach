package ipb.dam.apptrainer.DB;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

public class DataBase extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    int a;


    public DataBase(){
            //empty constructor;
    }

    /**
     *
     * @param json This parameters is use to take all the Json received from server,
     *             in this function the jason is converted to a string and save at database;
     * @param context This parameters is use to work with the Sharedpreference,
     *                this is use to get the context from the shared preferences;
     */
    public void savePreferences(JSONObject json, Context context){

        pref = getApplicationContext().getSharedPreferences("User", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("User", json.toString());

        //save in data base
        editor.commit();
    }

    /**
     *
     * @return The Json saved in the savePreferences can be take in this fuction in format of string;
     */

    public String getPreferences(){

        return pref.getString("User", null);
    }


}
