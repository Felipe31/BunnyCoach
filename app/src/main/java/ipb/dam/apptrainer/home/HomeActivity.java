package ipb.dam.apptrainer.home;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginActivity;
import ipb.dam.apptrainer.login.LoginSingleton;

public class HomeActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatActivity appCompatActivity = this;

        final ViewPager pager = findViewById(R.id.content_home_viewpager);
        final ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter();
        pager.setAdapter(adapter);

        OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

            int currentPosition = 0;

            @Override
            public void onPageSelected(int newPosition) {

                FragmentLifecycle fragmentToShow = (FragmentLifecycle)adapter.getItem(newPosition);
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
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LoginSingleton.getInstance().isLogged()) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 2;

        private static final int PAGE_EXERCISES_OF_THE_WEEK = 0;

        private static final int PAGE_STATISTICS = 1;

        private ScreenSlidePagerAdapter() {
            super(HomeActivity.this.getSupportFragmentManager());
        }




        @Override
        public Fragment getItem(int position) {
            switch (position){

                case PAGE_EXERCISES_OF_THE_WEEK:
                    return HomeFragment.newInstance(getResources().getString(R.string.home_exercise_week), 2, 3);

                default:
                    return StatisticsFragment.newInstance();
            }

        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
