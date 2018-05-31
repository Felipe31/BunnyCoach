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

public class Connection {

    public Connection() {
    }


    public JSONObject requestUserInfo(String usernameApp, String passwdApp) {

        // TODO: 24/05/18 Handle: 401 Unauthorized is returned in requestUserInfo

        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put("email", usernameApp);
            requestJSON.put("password", passwdApp);
            Log.w(this.getClass().getSimpleName(), requestJSON.toString());
            ConnectRest cr = new ConnectRest();
            cr.execute(requestJSON);
        }catch (Exception e){

        }
        return requestJSON;
    }

    public class ConnectRest extends AsyncTask<JSONObject, Integer, Long> {
        @Override
        protected Long doInBackground(JSONObject ...dataJson) {

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
                }finally {
                    conn.disconnect();
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
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

        }
    }
}
