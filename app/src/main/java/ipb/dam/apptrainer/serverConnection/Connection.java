package ipb.dam.apptrainer.serverConnection;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
            requestJSON.put("name", name);
            requestJSON.put("email", email);
            requestJSON.put("password", password);
            requestJSON.put("birthday", birthday);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }


    public void registerProfile(String token, String profile){
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("token", token);
            requestJSON.put("profile", profile);

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
            requestJSON.put("hours_per_day", hours_per_day);
            requestJSON.put("working_days", working_days);

            sendJSON(requestJSON);
        }catch (Exception e){
            Log.w(this.getClass().getSimpleName(), "JSON build error");
        }

    }


    public void requestUserLogin(String usernameApp, String passwdApp) {

        // TODO: 24/05/18 Handle: 401 Unauthorized is returned in requestUserLogin

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
                try {
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    String body = dataJson[0].toString();
                    OutputStream output = new BufferedOutputStream(conn.getOutputStream());
                    output.write(body.getBytes());
                    output.flush();
                    InputStream result = conn.getInputStream();
                    byte[] bytes = new byte[2000];
                    Log.i("Resultado", "Antes do read");
                    result.read(bytes);
                    Log.i("Resultado", new String(bytes, StandardCharsets.UTF_8));

                    return new JSONObject(new String(bytes, StandardCharsets.UTF_8));
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

            Log.w(this.getClass().getSimpleName(), result.toString());

            if(result.has("erro"))
                return;
            else if(result.has("statistics")) {
                LoginSingleton loginSingleton = LoginSingleton.getInstance();
                loginSingleton.setData(result);
                loginSingleton.loginSuccessful();
            }
        }
    }
}
