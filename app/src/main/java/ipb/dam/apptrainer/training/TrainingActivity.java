package ipb.dam.apptrainer.training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.HomeActivity;
import ipb.dam.apptrainer.login.LoginSingleton;

public class TrainingActivity extends AppCompatActivity{

    private JSONObject trainOfTheDay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_training);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.training_title));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        try {
            if(LoginSingleton.getInstance().getData() == null) {
                LoginSingleton.getInstance().errorHandler(new JSONObject("{\"error\":[\""+getResources().getString(R.string.info_data_outdated)+"\"]}"));
                LoginSingleton.getInstance().makeLogout(this);
                return;
            }
            JSONArray trainingArray = LoginSingleton.getInstance().getData().getJSONArray("training");



            int i;
            for ( i = 0; i < trainingArray.length(); i++){
                if (day-1 == Integer.parseInt(trainingArray.getJSONObject(i).getString("day"))) {
                    trainOfTheDay = trainingArray.getJSONObject(i);
                    break;
                }
            }
            if(trainOfTheDay == null) {
                LoginSingleton.getInstance().errorHandler(null);
                return;
            }

            final ViewPager pager = findViewById(R.id.content_training_viewpager);
            final TrainingActivity.ScreenSlidePagerAdapter adapter = new TrainingActivity.ScreenSlidePagerAdapter(trainOfTheDay.getJSONArray("exercises").length());
            pager.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LoginSingleton.getInstance().isLogged()) {
            LoginSingleton.getInstance().makeLogout(this);
        }
        Log.i("onResume - Training", LoginSingleton.getInstance().getTrainingTracker().toString());
        if(LoginSingleton.getInstance().getTrainingTracker().has("unused")){
            Toast.makeText(this, getResources().getString(R.string.no_exercise_today), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            this.finish();
            return;
        }

}


    private int getDrawableTraining(int id) {

        switch (id){
            case 2:
                return R.drawable.arm_gif;
            case 1002:
                return R.drawable.abdominal_gif;
            case 1003:
                return R.drawable.leg_gif;
            case 1004:
                return R.drawable.backs_gif;
            default:
                return R.drawable.jump_rope_gif;
        }

    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


        private final int NUM_PAGES;

        private ScreenSlidePagerAdapter(Integer numPages) {
            super(TrainingActivity.this.getSupportFragmentManager());
            NUM_PAGES = numPages;
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(getLocalClassName(), "=======> "+ String.valueOf(position));


            try {
                return TrainingFragment.newInstance(trainOfTheDay.getJSONArray("exercises").getJSONObject(position).getString("name"),
                        trainOfTheDay.getJSONArray("exercises").getJSONObject(position).getString("describe"),
                        trainOfTheDay.getJSONArray("exercises").getJSONObject(position).getString("info"),
                        0,
                        position,
                        getDrawableTraining(Integer.parseInt(trainOfTheDay.getJSONArray("exercises").getJSONObject(position).getString("id"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
