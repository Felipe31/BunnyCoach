package ipb.dam.apptrainer.home;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginActivity;
import ipb.dam.apptrainer.login.LoginSingleton;
import ipb.dam.apptrainer.db.DataBase;

public class HomeActivity  extends AppCompatActivity {

    private ViewPager pager;
    private FragmentLifecycle fragmentToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DataBase.getInstance(this); // Give context to the db, otherwise the line below will crash
        LoginSingleton.getInstance().setContext(this); // Set context to the Singleton class
        Log.i(getClass().getSimpleName(), "DATE CHANGED SINCE LAST SYNC: Send statistics to the database");
        LoginSingleton.getInstance().refreshOldStatistics(this  );
        Log.i(getClass().getSimpleName(), "---DATE CHANGED SINCE LAST SYNC: Send statistics to the database");

        AppCompatActivity appCompatActivity = this;
        pager = findViewById(R.id.content_home_viewpager);
        final ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter();
        pager.setAdapter(adapter);

        OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

            private int currentPosition = 0;

            @Override
            public void onPageSelected(int newPosition) {

                fragmentToShow = (FragmentLifecycle)adapter.getItem(newPosition);
//                fragmentToShow.onResumeFragment(appCompatActivity, LoginSingleton.getInstance().getTrainingTracker().getInt("qtd_exercises_done"));
                fragmentToShow.onResumeFragment(appCompatActivity);

                FragmentLifecycle fragmentToHide = (FragmentLifecycle)adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment(appCompatActivity);

                currentPosition = newPosition;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            public void onPageScrollStateChanged(int arg0) { }
        };



        pager.addOnPageChangeListener(pageChangeListener);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.statistics_title));
//        toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                LoginSingleton.getInstance().makeLogout(this);
                return true;
            case R.id.menu_about:
                LoginSingleton.getInstance().setContext(this);
                LoginSingleton.getInstance().profileSuccessful(false);
                return true;
            case R.id.menu_refresh_statistics:
                LoginSingleton.getInstance().refreshStatistics(this);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!LoginSingleton.getInstance().isLogged()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        AppCompatActivity appCompatActivity = this;
        if(fragmentToShow != null)
            fragmentToShow.onResumeFragment(appCompatActivity);

       /* Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK)-1;
        int lastSync = DataBase.getInstance(this).getLastSyncDate();
        if(lastSync != -1 && lastSync < today){
            Log.i(getClass().getSimpleName(), "DATE CHANGED SINCE LAST SYNC: Send statistics to the database");
            DataBase.getInstance(this).updateSyncDate();
        }*/

    }

    @Override
    public void onBackPressed(){

        if(pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            pager.setCurrentItem(pager.getCurrentItem() - 1);


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 2;

        private static final int PAGE_EXERCISES_OF_THE_DAY = 1;

        private static final int PAGE_STATISTICS = 0;

        private ScreenSlidePagerAdapter() {
            super(HomeActivity.this.getSupportFragmentManager());
        }




        @Override
        public Fragment getItem(int position) {

                switch (position){

                    case PAGE_EXERCISES_OF_THE_DAY:
                            return HomeFragment.newInstance(getResources().getString(R.string.home_exercise_day));


                    default:

                        try {
                            JSONObject statistics= LoginSingleton.getInstance().getStatistics();

                            Log.i("Statistcs - total", String.valueOf(BigDecimal.valueOf(statistics.getDouble("total")/5).floatValue()));
                            Log.i("Statistcs - arm", String.valueOf(BigDecimal.valueOf(statistics.getDouble("arm")).floatValue()));
                            Log.i("Statistcs - abdominal", String.valueOf(BigDecimal.valueOf(statistics.getDouble("abdominal")).floatValue()));
                            Log.i("Statistcs - leg", String.valueOf(BigDecimal.valueOf(statistics.getDouble("leg")).floatValue()));
                            Log.i("Statistcs - back", String.valueOf(BigDecimal.valueOf(statistics.getDouble("back")).floatValue()));
                            Log.i("Statistcs - aerobic", String.valueOf(BigDecimal.valueOf(statistics.getDouble("aerobic")).floatValue()));
                            return StatisticsFragment.newInstance(
                                    BigDecimal.valueOf(statistics.getDouble("total")/5).floatValue()/100,
                                    BigDecimal.valueOf(statistics.getDouble("arm")).floatValue()/100,
                                    BigDecimal.valueOf(statistics.getDouble("abdominal")).floatValue()/100,
                                    BigDecimal.valueOf(statistics.getDouble("leg")).floatValue()/100,
                                    BigDecimal.valueOf(statistics.getDouble("back")).floatValue()/100,
                                    BigDecimal.valueOf(statistics.getDouble("aerobic")).floatValue()/100,
                                    (boolean[]) statistics.get("boolean")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return StatisticsFragment.newInstance(0,0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    new boolean[] {false, false, false, false, false, false, false});
                        }
                }
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
