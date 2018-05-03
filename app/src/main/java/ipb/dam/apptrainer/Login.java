package ipb.dam.apptrainer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.URL;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Trata login
        Button btn = findViewById(R.id.login_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatEditText user_etxt = findViewById(R.id.user_etxt);
                AppCompatEditText passwd_etxt = findViewById(R.id.passwd_etxt);

                String user = user_etxt.getText().toString();
                String passwd = passwd_etxt.getText().toString();
                System.out.println(user);
                System.out.println(passwd);
            }
        });
    }




    public class connectRest extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... urls) {
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
