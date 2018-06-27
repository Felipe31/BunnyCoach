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
import android.widget.AdapterView;

import org.json.JSONException;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginActivity;
import ipb.dam.apptrainer.login.LoginSingleton;

public class HomeActivity  extends AppCompatActivity {

    private ViewPager pager;
    private FragmentLifecycle fragmentToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        private static final int PAGE_EXERCISES_OF_THE_WEEK = 1;

        private static final int PAGE_STATISTICS = 0;

        private ScreenSlidePagerAdapter() {
            super(HomeActivity.this.getSupportFragmentManager());
        }




        @Override
        public Fragment getItem(int position) {

            try {

                switch (position){

                    case PAGE_EXERCISES_OF_THE_WEEK:
                            return HomeFragment.newInstance(getResources().getString(R.string.home_exercise_day),
                                    LoginSingleton.getInstance().getTrainingTracker().getInt("qtd_exercises_done"),
                                    LoginSingleton.getInstance().getTrainingTracker().getInt("qtd_exercises"));


                    default:
                        // TODO dummy call for now. Change for the values taken from the server
                        return StatisticsFragment.newInstance(0.89f,
                                0.2f, 0.4f, 1f,
                                0.6f, 0.4f,
                                new boolean[]{false, false, true, true, true, false, true});
                }
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
