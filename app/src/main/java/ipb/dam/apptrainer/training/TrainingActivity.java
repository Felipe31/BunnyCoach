package ipb.dam.apptrainer.training;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.training.TrainingFragment;

public class TrainingActivity extends AppCompatActivity implements Button.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);


        final ViewPager pager = findViewById(R.id.content_training_viewpager);
        final TrainingActivity.ScreenSlidePagerAdapter adapter = new TrainingActivity.ScreenSlidePagerAdapter(6);
        pager.setAdapter(adapter);


        // TODO: 15/05/18 Verify if the user is logged in
    }

    /**
     * Called every time a click has been detected by the view {@code v}.
     * @param v View that has triggered this method
     */
    public void onClick(View v){
        //Toast.makeText(this, "Exercise done", Toast.LENGTH_SHORT).show();


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
            return TrainingFragment.newInstance("Exercise nº "+String.valueOf(position), "Description", position % 2 == 0);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
