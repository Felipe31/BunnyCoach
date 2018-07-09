package ipb.dam.apptrainer.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import ipb.dam.apptrainer.db.DataBase;
import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.about.AboutActivity;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;
import ipb.dam.apptrainer.serverConnection.Connection;

public class LoginSingleton {

    // ourInstance holds the only instance of this class
    private static final LoginSingleton ourInstance = new LoginSingleton();

//    Holds the JSON object coming from the server containing all data about the user
    private JSONObject data = null;
//    JSON object to track the activities of the user related to how many exercises
//    the user did.
//    This variable is reseted every time that:
//      - the server sends a new data object
//      - the user refreshes the statistics of the exercises done.
//    private JSONObject trainingTracker;

//    Context is used to handle Activities when there is response from de server
//    It may be setted only when there is need to communicate to the server
    private Context context = null; // TODO this var should be removed. See warning in ourInstance var

//    Profile holds which type the user says to be:
//    Lazy = 0
//    Balanced = 1
//    Body Builder = 2
    private String profile = null;

//    Session token sent by the server each time the user makes login
    private String token = null;

//  Height of the user
    private String height = null;

//    Weight of the user
    private String weight = null;

//    Hours per day the user chose to workout
    private String hours_per_day = null;

//    Days the user chose to workout
    private String working_days = null;


//    Constructor initializes the variable trainingTracker
//    otherwise the app would crash on training activity
    private LoginSingleton() {
        setTrainingTrackerUnused();
    }

    //  This gets the instance of the class
    public static LoginSingleton getInstance() {
        return ourInstance;
    }

//    Takes the actual context (LoginActivity) and send the user information to the server
    protected void makeLogin(Context context, String usernameApp, String passwdApp) {

        this.context = context;
        //isLogged = true;
        Connection.getInstance().requestUserLogin(usernameApp, passwdApp);

    }

//    Set the variable context if the one in parameters is not null
//    and call loginSuccessful
    public void loginSuccessful(Context context, JSONObject result) {

        if(context != null)
            this.context = context;
        loginSuccessful(result);
    }

//    To execute this method properly, the context variable NEEDS to be setted already
//    loginSuccessful takes the information coming from the server and set it in local variables
//    such as data, calling setData and set isLogged as true.
//    This method also closes all the open activities, including the context one and open HomeActivity
//
//    If there is any error, it makes the logout
    private void loginSuccessful(JSONObject result){
        if(context != null) {
            try {
                Log.i("Login Successful", result.toString());

                setToken(result.getString("token"));
                setData(result);
                //isLogged = true;
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                ((AppCompatActivity)context).finish();
                context = null;
            } catch (JSONException e) {
                e.printStackTrace();
                makeLogout();
            }
        }

    }

//    Set the context and call makeLogout
    public boolean makeLogout(Context context) {
        this.context = context;
        return makeLogout();
    }

//    To execute this method properly, the context variable NEEDS to be setted already
//    set isLogged as false, close all the open activities, including the context one and open LoginActivity
    private boolean makeLogout() {

        DataBase.getInstance(context).setDataDB(null); // Remove data from the database.

        if(context != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
            return true;
        }

        return false;
    }

//    Updates the local variables of the measures of the user and send them to the server
    public void updateAbout(Context context, String height, String weight, String hours_per_day, String working_days){

        this.context = context;
        setHeight(height);
        setWeight(weight);
        setHours_per_day(hours_per_day);
        setWorking_days(working_days);

        try {
            Connection.getInstance().updateAbout(getToken(), height, weight, hours_per_day, working_days);
        } catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "Token not defined");
            errorHandler(null);
        }
    }

//    Send the registration information to the server
    public void registerUser(Context context, String name, String email, String password, String birthday) {
        this.context = context;
        Connection.getInstance().registerUser(name, email, password, birthday);
    }

//    registrationSuccessful is called when the server returns successfully from the registerUser
//    message
//    It goes opens the next activity of the registration steps, which is the ProfileChooserActivity
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

            ((AppCompatActivity)context).finish();
            context = null;
        }

    }

//    Handles error coming from the server and display them on the screen as Toast notifications
//    If there is nothing to display, it displays "Internal error"
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


/*
*    Is called every time the server send a new data JSON object, which as all the user information
*    based on data information, the trainingTracker JSON is populated if it is not in use
*    (if there is no field called "unused" on the trainingTracker JSON
*    The trainingTracker has the following format:
*    {
*       "unused":"",
*       "qtd_exercises_done":0,
*       "0":[],
*       "qtd_exercises":0,
*       "1":[{
*               "id":2,
*               "qtd":5,
*               "done":0,
*               "date":"06-23-2018"
*           },
*           {
 *               "id":1002,
 *               "qtd":5,
 *               "done":0,
 *               "date":"06-23-2018"
 *           }],
*       "2":[],
*       "3":[],
*       "4":[],
*       "5":[],
*       "6":[]
*    }
*/
    public void setData(JSONObject data) throws JSONException {
        if(data != null) {
            DataBase.getInstance(context).setDataDB(data);
            //this.data = data;
            Log.w(this.getClass().getSimpleName(), data.toString());
            Log.i("SetData Thread", getTrainingTracker().toString());
            try {

                setProfile(context, String.valueOf(data.getJSONObject("profile").getString("type")));

                String qtdProfile;
                switch (Integer.parseInt(getProfile())){
                    case 0: qtdProfile = "qtdLazy"; break;
                    case 1: qtdProfile = "qtdBalanced"; break;
                    default: qtdProfile = "qtdBody";
                }

                JSONObject trainingTracker = getTrainingTracker(); // Get new instance from the Database

                if(trainingTracker.has("unused")) {
                    JSONArray exercises;
                    JSONArray training = data.getJSONArray("training");
                    trainingTracker.put("qtd_exercises", 0);
                    trainingTracker.remove("unused");

                    for (int j = 0; j < training.length(); j++) {
                        exercises = training.getJSONObject(j).getJSONArray("exercises");

                        for (int i = 0; i < exercises.length(); i++) {

                            addTrainingTracker(trainingTracker, training.getJSONObject(j).getInt("day"),
                                    exercises.getJSONObject(i).getInt("id"),
                                    exercises.getJSONObject(i).getInt(qtdProfile),
                                    0);
                        }
                    }

                    setTrainingTracker(trainingTracker);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("SetData Thread", getTrainingTracker().toString());

         
        }
    }

//    Used to populate the trainingTracker variable
//    it adds one exercise in a specific day of the trainingTracker JSON
    private void addTrainingTracker(JSONObject trainingTracker, int day, int id, int qtd, int done) {
        try {
            String dayStr = String.valueOf(day);
            if(!trainingTracker.has(dayStr)) {
                trainingTracker.put(dayStr, new JSONArray());
            }
            if(!trainingTracker.has("qtd_exercises")) {
                trainingTracker.put("qtd_exercises", 0);
            }
            trainingTracker.put("qtd_exercises", trainingTracker.getInt("qtd_exercises")+1);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("qtd", qtd);
            jsonObject.put("done", done);
            trainingTracker.getJSONArray(dayStr).put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    Set the profile variable and sends it to the server
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

//    To execute this method properly, the context variable NEEDS to be setted already
//    If the server do the registration of the user's profile, it sets the token variable
//    And call the profileSuccessful method
    public void profileSuccessful(String token, boolean closeCurrentContext){
        setToken(token);
        profileSuccessful(closeCurrentContext);
    }

//    To execute this method properly, the context variable NEEDS to be setted already
//    Finishes or not the current Activity based on the closeCurrentContext value
//    Starts the next activity to collect user's data, which is the AboutActivity
    public void profileSuccessful(boolean closeCurrentContext){

        if(context != null) {
            context.startActivity(new Intent(context, AboutActivity.class));
            if(closeCurrentContext) {
                ((AppCompatActivity) context).finish();
                context = null;
            }
        }

    }

//    Return token if setted, exception if it is not found
    private String getToken() throws Resources.NotFoundException{
        if(token == null ) {
            throw new Resources.NotFoundException();
        }
        else if(token.equals("")) {
            throw new Resources.NotFoundException();
        }
        return token;
    }
//       NOT USED
//    public void resultHandler(JSONObject result) {
//        if(context instanceof LoginActivity)
//            loginSuccessful(result);
//        else if(context instanceof RegisterActivity)
//            registrationSuccessful(result);
//
//    }

//    It updates the trainingTracker statistics of exercises done today
//    and it updates the dates of the exercises done.
    public void updateDoneTrainingTracker(JSONObject jsonObject) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Date cDate = new Date();
        @SuppressLint("SimpleDateFormat") String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);

        try {
            JSONArray trainOfTheDay = jsonObject.getJSONArray(String.valueOf(day-1));

            double sum = 0;
            for(int i = 0; i < trainOfTheDay.length(); i++){
                sum += trainOfTheDay.getJSONObject(i).getInt("done");
                trainOfTheDay.getJSONObject(i).put("date",fDate);
            }
            jsonObject.put("qtd_exercises_done", sum);
            setTrainingTracker(jsonObject); // Update database


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


//    (Re)Initialize the trainingTracker JSON with unused flag
    private void setTrainingTrackerUnused() {
        try {
            setTrainingTracker(new JSONObject("{\"unused\":\"\",\"qtd_exercises_done\":0,\"0\":[],\"qtd_exercises\":0,\"1\":[],\"2\":[],\"3\":[],\"4\":[],\"5\":[],\"6\":[]}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    Gets the sum of the exercises of the today's training
    public Integer getTrainingTrackerExerciseTotalToday() {
        return sumTrainingTrackerExerciseTodayField("qtd");
    }

//    Gets the sum of the done exercises of the today's training
    public Integer getTrainingTrackerExerciseDoneToday() {
        return sumTrainingTrackerExerciseTodayField("done");
    }

//    Gets the sum of the field of the exercises of the today's training
    private int sumTrainingTrackerExerciseTodayField(String field){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int sum = 0;
        try {
            JSONArray dayArray =  getTrainingTracker().getJSONArray(String.valueOf(day));
            for (int i = 0 ; i < dayArray.length(); i++) {
                sum += dayArray.getJSONObject(i).getInt(field);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            sum = 0;
        }

        return sum;
    }

//    Gets all the statistics of the data JSON and format them to fit in the StatisticsFragment
    public JSONObject getStatistics() throws JSONException {
        JSONObject statistics = getData().getJSONObject("statistics");
        statistics.put("total", statistics.getDouble("arm")+
                statistics.getDouble("abdominal")+
                statistics.getDouble("leg")+
                statistics.getDouble("back")+
                statistics.getDouble("aerobic"));

        String[] days = getData().getJSONObject("profile").getString("daysWeek").split(",");

        boolean[] daysBool = new boolean[7];
        Arrays.fill(daysBool, false);

        for (String day : days  ){
            daysBool[Integer.parseInt(day)] = true;
        }
        statistics.put("boolean", daysBool);
        Log.i("Statistics", statistics.toString());
        return statistics;
    }

//    Send statistics of the day to the server and
//    set the trainingTracker as unused
    public void refreshStatistics(Context context) {
        this.context = context;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        try {

            JSONArray exercises =  getTrainingTracker().getJSONArray(String.valueOf(day));

            JSONArray ex = new JSONArray(exercises.toString());
            for(int i = 0; i < ex.length(); i++){
                ex.getJSONObject(i).put("qtd", ex.getJSONObject(i).getInt("done"));
                ex.getJSONObject(i).remove("done");
            }

            //gettrainingtracker função presente na classe trocado por DataBase.getInstance().getTrainigTrackerDBJ()
            Log.i("Refresh Statistics", ex.toString());
            Log.i("Refresh Statistics TT", DataBase.getInstance(context).getTrainigTrackerDBJ().toString());
            setTrainingTrackerUnused();
            Log.i("Refresh Statistics TT", DataBase.getInstance(context).getTrainigTrackerDBJ().toString());
            Connection.getInstance().sendExcercisesDone(token, ex);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

//    Regular getters and setters

    public boolean isLogged() {
        try {
            return DataBase.getInstance(context).getDataDBJ() != null;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public JSONObject getTrainingTracker(){

        try {
            return DataBase.getInstance(context).getTrainigTrackerDBJ();
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // TODO Check return value in case of exception
        }
    }

    private void setTrainingTracker(JSONObject trainingTracker) {
        DataBase.getInstance(context).setTrainigTrackerDB(trainingTracker);
    }

    private void setToken(String token) {
        this.token = token;
    }

    public JSONObject getData() throws JSONException {
        return DataBase.getInstance(context).getDataDBJ();
    }

    public String getProfile() {
        return profile;
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
