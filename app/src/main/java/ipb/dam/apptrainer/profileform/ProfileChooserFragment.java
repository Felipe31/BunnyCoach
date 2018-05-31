package ipb.dam.apptrainer.profileform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import ipb.dam.apptrainer.R;


/**
 * <p>Fragment that showns the bunny image and texts to be displayed to the user.
 * This fragment is meant to be controled by the {@link android.support.v4.view.ViewPager}
 * present in {@link ProfileChooserActivity}.</p>
 * <p>To instantiate this class, <b>DO NOT</b> use the available default constructor
 * because it is required so the Operational System can recreate this fragment if needed.
 * Use {@link #newInstance(String, String, int)} instead</p>
 */
public class ProfileChooserFragment extends Fragment {

    /**
     * Argument indexing the data related to the profile title
     */
    private static final String ARG_PROFILE_TITLE = "arg_profile_title";

    /**
     * Argument indexing the data related to the profile description
     */
    private static final String ARG_PROFILE_DESCRIPTION = "arg_profile_description";

    private static final String ARG_PROFILE_PICTURE = "arg_profile_picture";
    /**
     * String holding the profile title given in {@link #newInstance(String, String, int)}
     */
    private String profileTitle;

    /**
     * String holding the profile description given in {@link #newInstance(String, String, int)}
     */
    private String profileDescription;

    /**
     * Bunny image to be shown in the profile
     */
    private ImageView bunnyImage;

    private int profilePicture;


    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance(String, String, int)}
     * instead.
     *
     */
    public ProfileChooserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profileTitle Title of profile to be shown. Cannot be {@code null}.
     * @param profileDescription Description of profile to be shown. Cannot be {@code null}.
     * @return A new instance of fragment ProfileChooserFragment.
     */
    public static ProfileChooserFragment newInstance(@NonNull String profileTitle, @NonNull String profileDescription, @NonNull int profilePicture) {

        ProfileChooserFragment fragment = new ProfileChooserFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        args.putString(ARG_PROFILE_TITLE, profileTitle);
        args.putString(ARG_PROFILE_DESCRIPTION, profileDescription);
        args.putInt(ARG_PROFILE_PICTURE, profilePicture);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recovers the arguments set in #newInstance(String, String, Drawable) method in this class.
        // Since its arguments should not be null, this if should never fail.
        if (getArguments() != null) {
            profileTitle = getArguments().getString(ARG_PROFILE_TITLE);
            profileDescription = getArguments().getString(ARG_PROFILE_DESCRIPTION);
            profilePicture = getArguments().getInt(ARG_PROFILE_PICTURE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile_chooser, container, false);

        final TextView title = root.findViewById(R.id.fragment_profile_chooser_textview_profile_title);
        final TextView description =
                root.findViewById(R.id.fragment_profile_chooser_textview_profile_description);

        // Set up texts to be shown
        title.setText(profileTitle);
        description.setText(profileDescription);

        if(bunnyImage != null)
            // Tell to the garbage collector to free the memory resources
            // used by the old image since the imageview is about to be changed
            ((BitmapDrawable)bunnyImage.getDrawable()).getBitmap().recycle();

        bunnyImage = root.findViewById(R.id.profile_image_view);
        bunnyImage.setImageResource(profilePicture);

        // TODO fix error, poor performance and error being thrown in my device (Samsung Galaxy S4)


        Toolbar toolbar = root.findViewById(R.id.toolbar_profile_chooser);
        ((AppCompatActivity)root.getContext()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) root.getContext()).getSupportActionBar();
        if( actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }



        return root;
    }

    @Override
    public void onDetach(){
        // free memory used by this imageview
        bunnyImage.setImageDrawable(null);
        super.onDetach();
    }

}
