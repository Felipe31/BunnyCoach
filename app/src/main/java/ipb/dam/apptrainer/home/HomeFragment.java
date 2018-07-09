package ipb.dam.apptrainer.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.db.DataBase;
import ipb.dam.apptrainer.login.LoginSingleton;
import ipb.dam.apptrainer.training.TrainingActivity;

public class HomeFragment extends Fragment implements FragmentLifecycle {

    /**
     * Argument indexing the data related to the activity title
     */
    private static final String ARG_FRAGMENT_TITLE = "arg_profile_title";


    /**
     * String holding the home title given in {@link #newInstance(String)}
     */
    private String fragmentTitle;

    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance(String)}
     * instead.
     *
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fragmentTitle Title of app to be shown. Cannot be {@code null}.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(@NonNull String fragmentTitle) {

        HomeFragment fragment = new HomeFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, fragmentTitle);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recovers the arguments set in #newInstance(String, String) method in this class.
        // Since its arguments should not be null, this if should never fail.
        if (getArguments() != null) {
            fragmentTitle = getArguments().getString(ARG_FRAGMENT_TITLE);

        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView title = root.findViewById(R.id.fragment_home_txtv_title);
        final TextView progressTxt = root.findViewById(R.id.fragment_home_txtv_progress);
        String exerciseStatus = String.valueOf( getProgressToday())+ "% done";

        // Set up texts to be shown
        title.setText(fragmentTitle);
        progressTxt.setText(exerciseStatus);

        SeekBar seekBar = root.findViewById(R.id.fragment_home_seekbar);

        seekBar.setOnTouchListener((view, motionEvent) -> true);
        seekBar.setMax(100);
        Log.i("Current", String.valueOf( LoginSingleton.getInstance().getTrainingTrackerExerciseDoneToday()));
        Log.i("Total", String.valueOf(LoginSingleton.getInstance().getTrainingTrackerExerciseTotalToday()));
        seekBar.setProgress( getProgressToday());

        final Button chooseButton = root.findViewById(R.id.content_home_button_check);
        chooseButton.setOnClickListener(view -> startActivity(new Intent(root.getContext(), TrainingActivity.class)));

        ImageView profileImage = root.findViewById(R.id.home_imageview);
        try {
            JSONObject jsonObject = DataBase.getInstance(getContext()).getDataDBJ();

            if(jsonObject != null){

                switch (jsonObject.getJSONObject("profile").getInt("type")) {
                    case 0: // Lazy
                        profileImage.setImageResource(R.drawable.gordo);
                        break;

                    case 1: // Balanced
                        profileImage.setImageResource(R.drawable.normal);
                        break;

                    case 2: // Bodybuilder
                        profileImage.setImageResource(R.drawable.forte);
                        break;
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }


    @Override
    public void onPauseFragment(AppCompatActivity appCompatActivity) {

    }

    @Override
    public void onResumeFragment(AppCompatActivity appCompatActivity) {

        SeekBar seekBar = appCompatActivity.findViewById(R.id.fragment_home_seekbar);
        TextView progressTxt = appCompatActivity.findViewById(R.id.fragment_home_txtv_progress);
        seekBar.setProgress(getProgressToday());
        String exerciseStatus = String.valueOf(getProgressToday())+ "% done";
        progressTxt.setText(exerciseStatus);
        Log.i("Current", String.valueOf( LoginSingleton.getInstance().getTrainingTrackerExerciseDoneToday()));
        Log.i("Total", String.valueOf(LoginSingleton.getInstance().getTrainingTrackerExerciseTotalToday()));

        Objects.requireNonNull(appCompatActivity.getSupportActionBar()).setTitle(R.string.home_title);


    }

    public int getProgressToday() {
        if(LoginSingleton.getInstance().getTrainingTrackerExerciseTotalToday() != 0)
            return LoginSingleton.getInstance().getTrainingTrackerExerciseDoneToday()*100/LoginSingleton.getInstance().getTrainingTrackerExerciseTotalToday();
        else
        return 100;
    }
}
