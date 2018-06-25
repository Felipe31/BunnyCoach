package ipb.dam.apptrainer.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.login.LoginActivity;
import ipb.dam.apptrainer.login.LoginSingleton;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_about);


        Button about_btn = findViewById(R.id.about_btn);

        about_btn.setOnClickListener(view -> {
            LoginSingleton loginSingleton = LoginSingleton.getInstance();
            if(loginSingleton.updateAbout(((EditText)findViewById(R.id.about_height_etxt)).getText().toString(),
                    ((EditText)findViewById(R.id.about_weight_etxt)).getText().toString(),
                    ((EditText)findViewById(R.id.about_hours_etxt)).getText().toString(),
                    ((EditText)findViewById(R.id.about_hours_etxt)).getText().toString())) {

                if (!loginSingleton.isLogged())
                    loginSingleton.loginSuccessful(this, null);
                else {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }
            } else {
                if (loginSingleton.isLogged()) {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                } else {
                    loginSingleton.makeLogout();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }

        });
    }
}
