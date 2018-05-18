package ipb.dam.apptrainer.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_about);


        Button about_btn = findViewById(R.id.about_btn);

        about_btn.setOnClickListener(view -> {

            startActivity(new Intent(this, HomeActivity.class));
        });
    }
}
