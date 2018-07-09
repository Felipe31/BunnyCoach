package ipb.dam.apptrainer.serverConnection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import ipb.dam.apptrainer.login.LoginSingleton;

public class Connection {

    private static final Connection ourInstance = new Connection();

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
    }

/*
 *
 *  BEGIN - GENERATES THE JSON'S AS THE SERVER EXPECTS THOSE TO BE
 *
 */
    public void registerUser(String name, String email, String password, String birthday){
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("api", "register");
            requestJSON.put("token", "");
            requestJSON.put("email", email);
            requestJSON.put("password", password);
            requestJSON.put("confirmPassword", password);
            requestJSON.put("name", name);
            requestJSON.put("dateBirth", birthday);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }


    public void registerProfile(String token, String profile){
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("api", "types");
            requestJSON.put("token", token);
            requestJSON.put("type", profile);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }
    }

    public void updateAbout(String token, String height, String weight, String hours_per_day, String working_days){
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("api", "profiles");
            requestJSON.put("token", token);
            requestJSON.put("height", height);
            requestJSON.put("weight", weight);
            requestJSON.put("hours", hours_per_day);
            requestJSON.put("daysWeek", working_days);


            sendJSON(requestJSON);
        }catch (Exception e){
            e.printStackTrace();
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }
    }

    public void sendExcercisesDone(String token, JSONArray jsonArray){

        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("api", "exercises");
            requestJSON.put("token", token);
            requestJSON.put("training", jsonArray);
            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }


    public void requestUserLogin(String usernameApp, String passwdApp) {

        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("api", "token");
            requestJSON.put("token", "");
            requestJSON.put("email", usernameApp);
            requestJSON.put("password", passwdApp);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }

/*
*
*  END - GENERATES THE JSON'S AS THE SERVER EXPECTS THOSE TO BE
*
*/

//    Call the AsyncTask for the jsonToSend parameter to be sent to the server
    private void sendJSON(JSONObject jsonToSend){
        ConnectRest cr = new ConnectRest();
        cr.execute(jsonToSend);
    }

    private class ConnectRest extends AsyncTask<JSONObject, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(JSONObject ...dataJson) {
            try {
                String urlString = "https://calvin.estig.ipb.pt/trainer/api/"+dataJson[0].getString("api");
                Log.i(this.getClass().getSimpleName(), urlString);

                URL url = new URL(urlString);
                StringBuilder builder = new StringBuilder();
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                try {
                    conn.setReadTimeout(100000);
                    conn.setConnectTimeout(150000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "Bearer "+dataJson[0].getString("token"));
                    conn.setRequestProperty("Content-Type", "application/json");

                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    dataJson[0].remove("token");
                    dataJson[0].remove("api");
                    String body = dataJson[0].toString();
                    Log.i(this.getClass().getSimpleName(),"Content to be sent to the server:"+ body);

                    OutputStream output = new BufferedOutputStream(conn.getOutputStream());
                    output.write(body.getBytes());
                    output.flush();
                    Log.i(this.getClass().getSimpleName(), String.valueOf(conn.getResponseCode()));

                    InputStream result;

                    if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                        result = conn.getInputStream();
                    } else {
                        /* error from server */
                        result = conn.getErrorStream();

                    }


                    BufferedReader reader = new BufferedReader(new InputStreamReader(result));
                    String line="";
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                    line = builder.toString();

                    if (line.equals("") && conn.getResponseCode() == 200)
                        return new JSONObject("{\"profile_ok\":\"\"}");
                    else {
                        Log.i("Resultado", line);
                        return new JSONObject(line);
                    }
                }finally {
                    conn.disconnect();
                }
            } catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }



//        Handles the result of the AsyncTask calling the right method in LoginSingleton
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            LoginSingleton loginSingleton = LoginSingleton.getInstance();
            if(result == null) {
                loginSingleton.errorHandler(null);
                return;
            }

            try {
                Log.w(this.getClass().getSimpleName(), result.toString());

                if(result.has("profile_ok")){
                    loginSingleton.profileSuccessful(true);
                } else if(result.length() == 1 && result.has("token")){
                    loginSingleton.registrationSuccessful(result);

                } else if(result.has("result")) {
                    result = result.getJSONObject("result");

                    if(result.has("token") && result.has("status")) {
//                  status 0 - ok
//                  status 1 - type missing
//                  status 2 - profile (measures) missing

                        switch (result.getString("status")){
                            case "0": loginSingleton.loginSuccessful(null, result); break;
                            case "1": loginSingleton.registrationSuccessful(result); break;
                            case "2": loginSingleton.profileSuccessful(result.getString("token"), true); break;
                            default: loginSingleton.errorHandler(result);

                        }

                    }
                } else{
                    loginSingleton.errorHandler(result);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
