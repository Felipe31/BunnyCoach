package ipb.dam.apptrainer.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginSingleton;

public class AboutActivity extends AppCompatActivity implements CheckBox.OnCheckedChangeListener {

    /**
     * Array for each day of the week, starting on sunday (index 0).
     * @see #checkBoxes
     */
    private TextView[] daysOfTheWeek = new TextView[7];

    /**
     * Array that holds the checkboxes for each day of the week, starting on sunday (index 0).
     * @see #daysOfTheWeek
     */
    private CheckBox[] checkBoxes = new CheckBox[7];
    
    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setContentView(R.layout.activity_about);

        daysOfTheWeek[0] = findViewById(R.id.days_of_the_week_text_view_sunday);
        daysOfTheWeek[1] = findViewById(R.id.days_of_the_week_text_view_monday);
        daysOfTheWeek[2] = findViewById(R.id.days_of_the_week_text_view_tuesday);
        daysOfTheWeek[3] = findViewById(R.id.days_of_the_week_text_view_wednesday);
        daysOfTheWeek[4] = findViewById(R.id.days_of_the_week_text_view_thursday);
        daysOfTheWeek[5] = findViewById(R.id.days_of_the_week_text_view_friday);
        daysOfTheWeek[6] = findViewById(R.id.days_of_the_week_text_view_saturday);

        checkBoxes[0] = findViewById(R.id.days_of_the_week_checkbox_sunday);
        checkBoxes[1] = findViewById(R.id.days_of_the_week_checkbox_monday);
        checkBoxes[2] = findViewById(R.id.days_of_the_week_checkbox_tuesday);
        checkBoxes[3] = findViewById(R.id.days_of_the_week_checkbox_wednesday);
        checkBoxes[4] = findViewById(R.id.days_of_the_week_checkbox_thursday);
        checkBoxes[5] = findViewById(R.id.days_of_the_week_checkbox_friday);
        checkBoxes[6] = findViewById(R.id.days_of_the_week_checkbox_saturday);

        for (TextView textView : daysOfTheWeek)
            textView.setTextColor(getResources().getColor(R.color.activity_about_days_of_the_week_unchecked));


        for (CheckBox checkBoxe : checkBoxes) {
            checkBoxe.setBackgroundResource(R.drawable.activity_about_checkbox_day_of_the_week_background);
            checkBoxe.setOnCheckedChangeListener(this);
        }



        //get the spinner from the xml.
        AppCompatSpinner dropdown = findViewById(R.id.about_hours_spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"0:30", "1:00", "1:30", "2:00"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);




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

            String sh = height.getText().toString();
            String sw = weight.getText().toString();

            String sho;
            switch (dropdown.getSelectedItem().toString()) {
                case ("0:30"):
                    sho = "0.5";
                    break;
                case ("1:00"):
                    sho = "1.0";
                    break;
                case ("1:30"):
                    sho = "1.5";
                    break;
                default:
                    sho = "2.0";
                    break;
            }

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
                s.append(i).append(",");

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
