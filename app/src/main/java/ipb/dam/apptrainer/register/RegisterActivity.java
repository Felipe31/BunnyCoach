package ipb.dam.apptrainer.register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;

public class RegisterActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    EditText edittext;
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_register);
        edittext = (EditText) findViewById(R.id.register_birthday);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(view -> {
            new DatePickerDialog(RegisterActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        Button register_back_login = findViewById(R.id.register_back_login);

        register_back_login.setOnClickListener(view -> {
            finish();
        });

        Button register_btn = findViewById(R.id.register_btn);

        register_btn.setOnClickListener(view -> {

            startActivity(new Intent(this, ProfileChooserActivity.class));
        });


    }





    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}
