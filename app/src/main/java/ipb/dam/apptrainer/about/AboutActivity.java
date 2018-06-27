package ipb.dam.apptrainer.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.login.LoginActivity;
import ipb.dam.apptrainer.login.LoginSingleton;
import ipb.dam.apptrainer.profileform.ProfileChooserActivity;

public class AboutActivity extends AppCompatActivity implements CheckBox.OnCheckedChangeListener {

    /**
     * Array for each day of the week, starting on monday (index 0).
     * @see #checkBoxes
     */
    private TextView[] daysOfTheWeek = new TextView[7];

    /**
     * Array that holds the checkboxes for each day of the week, starting on monday (index 0).
     * @see #daysOfTheWeek
     */
    private CheckBox[] checkBoxes = new CheckBox[7];
    
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_about);

        daysOfTheWeek[0] = findViewById(R.id.days_of_the_week_text_view_monday);
        daysOfTheWeek[1] = findViewById(R.id.days_of_the_week_text_view_tuesday);
        daysOfTheWeek[2] = findViewById(R.id.days_of_the_week_text_view_wednesday);
        daysOfTheWeek[3] = findViewById(R.id.days_of_the_week_text_view_thursday);
        daysOfTheWeek[4] = findViewById(R.id.days_of_the_week_text_view_friday);
        daysOfTheWeek[5] = findViewById(R.id.days_of_the_week_text_view_saturday);
        daysOfTheWeek[6] = findViewById(R.id.days_of_the_week_text_view_sunday);

        checkBoxes[0] = findViewById(R.id.days_of_the_week_checkbox_monday);
        checkBoxes[1] = findViewById(R.id.days_of_the_week_checkbox_tuesday);
        checkBoxes[2] = findViewById(R.id.days_of_the_week_checkbox_wednesday);
        checkBoxes[3] = findViewById(R.id.days_of_the_week_checkbox_thursday);
        checkBoxes[4] = findViewById(R.id.days_of_the_week_checkbox_friday);
        checkBoxes[5] = findViewById(R.id.days_of_the_week_checkbox_saturday);
        checkBoxes[6] = findViewById(R.id.days_of_the_week_checkbox_sunday);

        for (TextView textView : daysOfTheWeek)
            textView.setTextColor(getResources().getColor(R.color.activity_about_days_of_the_week_unchecked));


        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i].setBackgroundResource(R.drawable.activity_about_checkbox_day_of_the_week_background);
            checkBoxes[i].setOnCheckedChangeListener(this);
        }

        Button about_btn = findViewById(R.id.about_btn);

        about_btn.setOnClickListener(view -> {

            String workingDays = getWorkingDays();

            if (workingDays == null) {
                Toast.makeText(this, "Choose at least one day of the week", Toast.LENGTH_SHORT).show();
                return;
            }
            //else
            //    Toast.makeText(this, workingDays, Toast.LENGTH_SHORT).show();

            EditText height = findViewById(R.id.about_height_etxt);
            EditText weight = findViewById(R.id.about_weight_etxt);
            EditText hours = findViewById(R.id.about_hours_etxt);

            String sh = height.getText().toString();
            String sw = weight.getText().toString();
            String sho = hours.getText().toString();

            if(sh.isEmpty()|| sw.isEmpty() || sho.isEmpty()) {
                Toast.makeText(this, "Please, fill up all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginSingleton.getInstance().updateAbout(this, sh, sw, sho, workingDays);

        });
    }

    /**
     * Creates a {@link String} representation of the selected working days in the following format:
     * <b>1,2,4</b> -> User has selected: monday, tuesday and thursday to train.
     * @return {@code null} if the user has not selected any days of the week to train,
     * otherwise returns a string to be sent to the server.
     */
    private @Nullable String getWorkingDays(){

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < checkBoxes.length; i++)
            if (checkBoxes[i].isChecked())
                s.append((i + 1) % checkBoxes.length /* This makes Monday = 1, Sunday = 0 */).append(",");

        return (s.length() == 0) ? null : s.substring(0, s.toString().lastIndexOf(',')/* Remove last comma */);

    }

    @Override
    public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {
        // The tag has been specified at the XML file, only values from 0 to 6 (7 checkboxes).
        int dayIndex =  Integer.valueOf((String) checkbox.getTag());
        daysOfTheWeek[dayIndex].setTextColor(getResources().getColor(isChecked ?
                R.color.activity_about_days_of_the_week_checked : R.color.activity_about_days_of_the_week_unchecked));
    }



}
