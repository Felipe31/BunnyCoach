package ipb.dam.apptrainer.login;

import android.annotation.SuppressLint;
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

import java.security.acl.LastOwnerException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
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


    private boolean isLogged = false;
    private JSONObject data = null;
    private JSONObject trainingTracker;
    private Context context = null;
    private String profile = null;

    private String token = null;
    private String height = null;
    private String weight = null;
    private String hours_per_day = null;
    private String working_days = null;

    private boolean isRefresh = false;

    private LoginSingleton() {
        setTrainingTrackerUnused();
    }

    public boolean isLogged() {
        return isLogged;
    }

    protected void makeLogin(Context context, String usernameApp, String passwdApp) {

        this.context = context;
        isLogged = true;
        Connection.getInstance().requestUserLogin(usernameApp, passwdApp);

    }

    public void loginSuccessful(Context context, JSONObject result) {

        if(context != null)
            this.context = context;
        loginSuccessful(result);
    }
    private void loginSuccessful(JSONObject result){
        if(context != null) {
            try {
                Log.i("Login Successful", result.toString());

                setToken(result.getString("token"));
                setData(result);
                isLogged = true;
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
    public boolean makeLogout(Context context) {
        this.context = context;
        return makeLogout();
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

            ((AppCompatActivity)context).finish();
            context = null;
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
        if(data != null) {
            this.data = data;
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

                if(getTrainingTracker().has("unused")) {
                    JSONArray exercises;
                    JSONArray training = data.getJSONArray("training");
                    getTrainingTracker().put("qtd_exercises", 0);
                    getTrainingTracker().remove("unused");

                    for (int j = 0; j < training.length(); j++) {
                        exercises = training.getJSONObject(j).getJSONArray("exercises");

                        for (int i = 0; i < exercises.length(); i++) {

                            addTrainingTracker(training.getJSONObject(j).getInt("day"),
                                    exercises.getJSONObject(i).getInt("id"),
                                    exercises.getJSONObject(i).getInt(qtdProfile),
                                    0);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("SetData Thread", getTrainingTracker().toString());

         
        }
    }

    private void addTrainingTracker(int day, int id, int qtd, int done) {
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

    public void profileSuccessful(String token, boolean closeCurrentContext){
        setToken(token);
        profileSuccessful(closeCurrentContext);
    }
    public void profileSuccessful(boolean closeCurrentContext){

        if(context != null) {
            context.startActivity(new Intent(context, AboutActivity.class));
            if(closeCurrentContext) {
                ((AppCompatActivity) context).finish();
                context = null;
            }
        }

    }

    private String getToken() throws Resources.NotFoundException{
        if(token == null ) {
            throw new Resources.NotFoundException();
        }
        else if(token.equals("")) {
            throw new Resources.NotFoundException();
        }
        return token;
    }

    private void setToken(String token) {
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

    public JSONObject getTrainingTracker() {
        return trainingTracker;
    }

    private void setTrainingTracker(JSONObject trainingTracker) {
        this.trainingTracker = trainingTracker;
    }

    public void updateDoneTrainingTracker() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Date cDate = new Date();
        @SuppressLint("SimpleDateFormat") String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);

        try {
            JSONArray trainOfTheDay = getTrainingTracker().getJSONArray(String.valueOf(day-1));

            double sum = 0;
            for(int i = 0; i < trainOfTheDay.length(); i++){
                sum += trainOfTheDay.getJSONObject(i).getInt("done");
                trainOfTheDay.getJSONObject(i).put("date",fDate);
            }
            getTrainingTracker().put("qtd_exercises_done", sum);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void setTrainingTrackerUnused() {
        try {
            setTrainingTracker(new JSONObject("{\"unused\":\"\",\"qtd_exercises_done\":0,\"0\":[],\"qtd_exercises\":0,\"1\":[],\"2\":[],\"3\":[],\"4\":[],\"5\":[],\"6\":[]}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Integer getTrainingTrackerExerciseTotalToday() {
        return sumTrainingTrackerExerciseTodayField("qtd");
    }

    public Integer getTrainingTrackerExerciseDoneToday() {
        return sumTrainingTrackerExerciseTodayField("done");
    }

    private int sumTrainingTrackerExerciseTodayField(String field){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int sum = 0;
        try {
            JSONArray dayArray =  trainingTracker.getJSONArray(String.valueOf(day));
            for (int i = 0 ; i < dayArray.length(); i++) {
                sum += dayArray.getJSONObject(i).getInt(field);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            sum = 0;
        }

        return sum;
    }

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

            Log.i("Refresh Statistics", ex.toString());
            Log.i("Refresh Statistics TT", getTrainingTracker().toString());
            setTrainingTrackerUnused();
            Log.i("Refresh Statistics TT", getTrainingTracker().toString());
            Connection.getInstance().sendExcercisesDone(token, ex);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
