package ipb.dam.apptrainer.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ipb.dam.apptrainer.R;

public class HomeActivity  extends AppCompatActivity implements Button.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Button chooseButton = findViewById(R.id.content_home_button_check);
        chooseButton.setOnClickListener(this);

        final ViewPager pager = findViewById(R.id.content_home_viewpager);
        final HomeActivity.ScreenSlidePagerAdapter adapter = new HomeActivity.ScreenSlidePagerAdapter();
        pager.setAdapter(adapter);


        // TODO: 15/05/18 Verify if the user is logged in

    }

    /**
     * Called every time a click has been detected by the view {@code v}.
     * @param v View that has triggered this method
     */
    public void onClick(View v){
        //TODO add functionality to the CHECK button
        Toast.makeText(this, "\"Exercises\" not implemented", Toast.LENGTH_SHORT).show();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 1;

        public ScreenSlidePagerAdapter() {
            super(HomeActivity.this.getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(getLocalClassName(), "=======> "+ String.valueOf(position));
            return HomeFragment.newInstance(getResources().getString(R.string.home_exercise_week), 2, 3);
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
