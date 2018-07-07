package ipb.dam.apptrainer.DB;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to hide information about the system
 * You can use that, like a data base;
 */

public class DataBase extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // ourInstance holds the only instance of this class
    private static final DataBase ourInstance = new DataBase();

    //  This gets the instance of the class
    public static DataBase getInstance() {
        return ourInstance;
    }

    public DataBase(){
            //empty constructor;
    }

    /**
     *
     * @param json This parameters is use to take all the Json received from server,
     *             in this function the jason is converted to a string and save at database;
     */
    public void setTrainigTrackerDB(JSONObject json){

        pref = getApplicationContext().getSharedPreferences("Trainig", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("Trainig", json.toString());

        //save in data base
        editor.commit();
    }
    /**
     *
     * @param json This parameters is use to take all the Json received from server,
     *             in this function the jason is converted to a string and save at database;
     */

    public void setDataDB (JSONObject json){
        pref = getApplicationContext().getSharedPreferences("Data", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("Data", json.toString());

        //save in data base
        editor.commit();

    }

    /**
     *
     * @return The Json saved in the setDataDB can be take with this fuction;
     */

    public JSONObject getDataDBJ() throws JSONException {
        JSONObject jsonObject = new JSONObject(pref.getString("Data", null));
        return jsonObject;
    }

    /**
     *
     * @return The Json saved in the setTrainigTrackerDB can be take with this fuction;
     */

    public JSONObject getTrainigTrackerDBJ() throws JSONException {
        JSONObject jsonObject = new JSONObject(pref.getString("Trainig", null));
        return jsonObject;
    }

    /**
     *
     * @return The Json saved in the setDataDB can be take in this fuction in format of string;
     */

    public String getDataDB(){

        return pref.getString("Data", null);
    }

    /**
     *
     * @return The Json saved in the setTrainigTrackerDB can be take in this fuction in format of string;
     */

    public String getTrainigTrackerDB(){

        return pref.getString("Trainig", null);
    }


}
