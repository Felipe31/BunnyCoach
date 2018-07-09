package ipb.dam.apptrainer.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * This class is used to hide information about the system
 * You can use that, like a data base;
 */

public class DataBase extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * {@code ourInstance } holds the only instance of this class
      */
    private static DataBase ourInstance = null;

    /**
     * Name of the database for this application
     */
    private static final String DATA_BASE_NAME = "bunny_coach_shared_preferences";

    /**
     * Key for storing data of the current training status of the user.
     */
    private static final String KEY_TRAINING = "key_training";

    /**
     * Key for storing data of training received from the server.
     */
    private static final String KEY_DATA = "key_data";

    /**
     * @param context Context of the class.
     * @return Instance of the class
     */
    public static synchronized DataBase getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new DataBase(context);
        }

        return ourInstance;
    }

    private DataBase(Context context){
        preferences = context.getSharedPreferences(DATA_BASE_NAME, MODE_PRIVATE);
    }

    /**
     * @param json This parameters is use to take all the Json received from server,
     *             in this function the jason is converted to a string and save at database;
     */
    public void setTrainigTrackerDB(JSONObject json){
        Log.i("TrainigTrackerDB", json.toString());
        editor = preferences.edit();
        editor.putString(KEY_TRAINING, json.toString());
        try {
            Log.i("Trainig.bankBuild", getTrainigTrackerDBJ().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //save in data base
        editor.commit();
    }
    /**
     *
     * @param json This parameters is use to take all the Json received from server,
     *             in this function the jason is converted to a string and save at database;
     */

    public void setDataDB (JSONObject json){
        Log.i("DataDB", json.toString());
        editor = preferences.edit();
        editor.putString(KEY_DATA, json.toString());
        try {
            Log.i("Data.build", getDataDBJ().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //save in data base
        editor.commit();

    }

    /**
     *
     * @return The Json saved in the setDataDB can be take with this fuction;
     */

    public JSONObject getDataDBJ() throws JSONException {
        return new JSONObject(
                preferences.getString(
                        KEY_DATA,
                        null)
        );
    }

    /**
     *
     * @return The Json saved in the {@link #setTrainigTrackerDB(JSONObject)} can be take with this fuction;
     */

    public JSONObject getTrainigTrackerDBJ() throws JSONException {
        return new JSONObject(
                preferences.getString(KEY_TRAINING,
                        "{\"unused\":\"\",\"qtd_exercises_done\":0,\"0\":[],\"qtd_exercises\":0,\"1\":[],\"2\":[],\"3\":[],\"4\":[],\"5\":[],\"6\":[]}")
        );
    }

    /**
     * TODO check if this is needed
     * @return The Json saved in the setDataDB can be take in this fuction in format of string;
     */

    public String getDataDB(){

        return preferences.getString(KEY_DATA, null);
    }

    /**
     * TODO check if this is needed
     * @return The Json saved in the setTrainigTrackerDB can be take in this fuction in format of string;
     */

    public String getTrainigTrackerDB(){

        return preferences.getString(KEY_TRAINING, null);
    }


}