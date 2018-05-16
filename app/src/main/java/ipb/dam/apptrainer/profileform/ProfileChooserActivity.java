package ipb.dam.apptrainer.profileform;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.HomeActivity;

/**
 * <p>Handles the activity responsible of showing the different kind of profiles available
 * for the user, whether the user wants to change its profile or it is the first time
 * that the user is using the app and has to choose one so the training can be choose
 * based on his preferences.</p>
 *
 * <p>This class also contains the pager adapter {@link ScreenSlidePagerAdapter}</p>
 */
public class ProfileChooserActivity extends AppCompatActivity implements Button.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chooser);

        final Button chooseButton = findViewById(R.id.content_profile_chooser_button_choose);
        chooseButton.setOnClickListener(this);

        final ViewPager pager = findViewById(R.id.content_profile_chooser_viewpager_choose_profile);
        final ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter();
        pager.setAdapter(adapter);

    }

    /**
     * Called every time a click has been detected by the view {@code v}.
     * @param v View that has triggered this method
     */
    public void onClick(View v){
        //TODO add functionality to the CHOOSE button
        // For test purpose only
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, HomeActivity.class));
    }

    /**
     * Class that handles each profile page to be chosen by the user
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        /**
         * Page that shows the description of the lazy profile
         * @see #PAGE_PROFILE_BALANCED
         * @see #PAGE_PROFILE_BODYBUILDER
         */
        private static final int PAGE_PROFILE_LAZY = 0;

        /**
         * Page that shows the description of the balanced profile
         * @see #PAGE_PROFILE_LAZY
         * @see #PAGE_PROFILE_BODYBUILDER
         */
        private static final int PAGE_PROFILE_BALANCED = 1;

        /**
         * Page that shows the description of the bodybuilder profile
         * @see #PAGE_PROFILE_LAZY
         * @see #PAGE_PROFILE_BALANCED
         */
        private static final int PAGE_PROFILE_BODYBUILDER = 2;

        /**
         * Number of available pages (each page will be one different profile).
         * @see #PAGE_PROFILE_LAZY
         * @see #PAGE_PROFILE_BALANCED
         * @see #PAGE_PROFILE_BODYBUILDER
         */
        private static final int NUM_PAGES = 3;

        public ScreenSlidePagerAdapter() {
            super(ProfileChooserActivity.this.getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {

            String profileTitle;
            String profileDescription;
            int profilePicture;
            final Resources res = getResources();

            switch (position){

                case PAGE_PROFILE_LAZY:
                    profileTitle = res.getString(R.string.profile_chooser_lazy_title);
                    profileDescription = res.getString(R.string.profile_chooser_lazy_text);
                    profilePicture =  R.drawable.gordo;
                    break;

                case PAGE_PROFILE_BALANCED:
                    profileTitle = res.getString(R.string.profile_chooser_balanced_title);
                    profileDescription = res.getString(R.string.profile_chooser_balanced_text);
                    profilePicture = R.drawable.normal;
                    break;

                default:
                    profileTitle = res.getString(R.string.profile_chooser_bodybuilder_title);
                    profileDescription = res.getString(R.string.profile_chooser_bodybuilder_text);
                    profilePicture =  R.drawable.forte;


            }

            return ProfileChooserFragment.newInstance(profileTitle, profileDescription, profilePicture);
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }


    }

}
