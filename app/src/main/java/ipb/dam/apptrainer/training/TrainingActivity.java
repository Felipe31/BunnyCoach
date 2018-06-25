package ipb.dam.apptrainer.training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginActivity;
import ipb.dam.apptrainer.login.LoginSingleton;

public class TrainingActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_training);

        final ViewPager pager = findViewById(R.id.content_training_viewpager);
        final TrainingActivity.ScreenSlidePagerAdapter adapter = new TrainingActivity.ScreenSlidePagerAdapter(6);
        pager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!LoginSingleton.getInstance().isLogged()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
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
            return TrainingFragment.newInstance("Exercise nº "+String.valueOf(position), "Description", 0 , R.drawable.jump_rope);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
