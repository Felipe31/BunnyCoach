package ipb.dam.apptrainer.serverConnection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginSingleton;

public class Connection {

    private static final Connection ourInstance = new Connection();

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
    }

    public void sendJSON(JSONObject jsonToSend){
        ConnectRest cr = new ConnectRest();
        cr.execute(jsonToSend);
    }

    public void registerUser(String name, String email, String password, String birthday){
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("token", "");
            requestJSON.put("name", name);
            requestJSON.put("email", email);
            requestJSON.put("password", password);
            requestJSON.put("confirmPassword", password);
            requestJSON.put("dateBirth", birthday);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }


    public void registerProfile(String token, String profile){
        JSONObject requestJSON = new JSONObject();
        try {
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
            requestJSON.put("token", token);
            requestJSON.put("height", height);
            requestJSON.put("weight", weight);
            requestJSON.put("hours", hours_per_day);
            requestJSON.put("daysWeek", working_days);


            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }
    }

    public void sendExcercisesDone(String token, JSONArray jsonArray){

        JSONObject requestJSON = new JSONObject();
        try {
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
            requestJSON.put("token", "");
            requestJSON.put("email", usernameApp);
            requestJSON.put("password", passwdApp);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }

    static private class ConnectRest extends AsyncTask<JSONObject, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(JSONObject ...dataJson) {

            try {
                URL url = new URL("https://httpbin.org/post");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String response = "";
                try {
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Authorization", "Bearer "+dataJson[0].getString("token"));
                    dataJson[0].remove("token");
//                    String body = dataJson[0].toString();
                    HashMap<String, String> params = (HashMap<String, String>) JsonHandler.jsonToMap(dataJson[0]);
                    OutputStream output = new BufferedOutputStream(conn.getOutputStream());

                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(output, "UTF-8"));
                    writer.write(getPostDataString(params));

                    writer.flush();
                    writer.close();
                    output.close();
                    int responseCode=conn.getResponseCode();
                    LoginSingleton.getInstance().setToken(conn.getHeaderField("Authorization"));

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            Log.i(this.getClass().getSimpleName(), line);
                            response+=line;
                        }
                    }
                    else {
                        Log.i(this.getClass().getSimpleName(), "EMPTYYYYY");
                        response="";
                    }
                    return new JSONObject(response);


//
//
//
////                    output.write(body.getBytes());
//                    output.write(getPostDataString(params).getBytes());
//                    output.flush();
//                    InputStream result = conn.getInputStream();
//                    byte[] bytes = new byte[2000];
//                    Log.i("Resultado", "Antes do read");
//                    result.read(bytes);
//                    Log.i("Resultado", new String(bytes, StandardCharsets.UTF_8));

//                    return new JSONObject(new String(bytes, StandardCharsets.UTF_8));
                }finally {
                    conn.disconnect();
                    Log.i("Resultado", "disconnect");

                }
            } catch(Exception e){

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if(result == JSONObject.NULL) return;


            LoginSingleton loginSingleton = LoginSingleton.getInstance();


            Log.w(this.getClass().getSimpleName(), result.toString());

            if(result.has("statistics") || result.has("token")) {
                loginSingleton.setData(result);
                loginSingleton.loginSuccessful();
            } else{
                //Todo registration failed
                try {
                    loginSingleton.registrationFailed(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }
}
