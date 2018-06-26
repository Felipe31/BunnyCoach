package ipb.dam.apptrainer.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;
import ipb.dam.apptrainer.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Context context = this;

        Button btn = findViewById(R.id.login_btn);
        btn.setOnClickListener(view -> {
                AppCompatEditText userEtxt = findViewById(R.id.user_etxt);
                AppCompatEditText passwdEtxt = findViewById(R.id.passwd_etxt);

                String user = userEtxt.getText().toString();
                String passwd = passwdEtxt.getText().toString();

                LoginSingleton.getInstance().makeLogin(context, user, passwd);
        });

        AppCompatEditText passwdEtxt = findViewById(R.id.passwd_etxt);
        passwdEtxt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btn.performClick();
                return true;
            }
            return false;
        });


        TextView register_txtv = findViewById(R.id.register_btn);

        register_txtv.setOnClickListener(view -> {
            startActivity(new Intent(context, RegisterActivity.class));

        });


        TextView forgotPasswdTxtv = findViewById(R.id.forgot_passwd_txtv);
        forgotPasswdTxtv.setOnClickListener(view -> {
            // TODO: 15/05/18 Implement simple screen
            Toast.makeText(context, "\"Forgot password\" not implemented", Toast.LENGTH_SHORT).show();
        });

    }




}
