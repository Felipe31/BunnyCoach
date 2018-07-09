package ipb.dam.apptrainer.training;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.login.LoginSingleton;

// TODO: 23/06/18 Check the necessity of percent;
public class TrainingFragment extends Fragment {

    /**
     * Argument indexing the data related to the activity title
     */
    private static final String ARG_EXERCISE_TITLE = "arg_profile_title";

    /**
     * Argument indexing the data related to the description, where we have a brief of description
     */
    private static final String ARG_DESCRIPTION = "arg_current_int";

    /**
     * Argument indexing the data related to the description, where we have a brief of description
     */
    private static final String ARG_INFO = "arg_info";

    /**
     * Argument indexing the data related to percentage of exercise done, ranging from 1 to 100
     */
    private static final String ARG_PERCENTAGE = "arg_percentage";

    /**
     * Argument indexing the data related to data of gif
     */
    private static final String ARG_EXERCISE_GIF = "arg_exercise_gif";

    /**
     * Argument indexing the data related to data of gif
     */
    private static final String ARG_IDX = "arg_idx";


    /**
     * String holding the training title given in {@link #newInstance(String, String, String, int, int, int)}
     */
    private String description, exerciseTitle, info;
    private int exerciseGif, idx;


    AnimationDrawable exerciseAnimation;
    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance(String, String, String, int, int, int)}
     * instead.
     *
     */
    public TrainingFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param exerciseTitle Name of the exercise shown on the app screen
     *
     * @param description Brief description of the exercise to be done
     *
     * @param info More info about the exercise to be done
     *
     * @param percentage Percentage of training, ranging from 0 to 100
     *                   representing how much
     *                   of the all given exercises
     *                   the person has done.
     *
     * @param exerciseGif Animated drawable to be shown.
     *
     * @return
     */
    public static TrainingFragment newInstance(@NonNull String exerciseTitle,
                                               @NonNull String description,
                                               @NonNull String info,
                                               int percentage, int idx, @DrawableRes int exerciseGif) {

        TrainingFragment fragment = new TrainingFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        args.putString(ARG_EXERCISE_TITLE, exerciseTitle);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_INFO, description);
        args.putInt(ARG_EXERCISE_GIF, exerciseGif);
        args.putInt(ARG_PERCENTAGE, percentage);
        args.putInt(ARG_IDX, idx);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recovers the arguments set in #newInstance(String, String) method in this class.
        // Since its arguments should not be null, this if should never fail.
        if (getArguments() != null) {
            exerciseTitle = getArguments().getString(ARG_EXERCISE_TITLE);
            description = getArguments().getString(ARG_DESCRIPTION);
            info = getArguments().getString(ARG_INFO);
            exerciseGif = getArguments().getInt(ARG_EXERCISE_GIF);
            idx = getArguments().getInt(ARG_IDX);
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_training, container, false);

        final TextView title = root.findViewById(R.id.fragment_exercise_txtv_title);
        final TextView progressTxt = root.findViewById(R.id.fragment_exercise_description_textview);
        final SeekBar seekBar = root.findViewById(R.id.seekBarID);
        try {

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            JSONArray trainOfTheDay = LoginSingleton.getInstance().getTrainingTracker().getJSONArray(String.valueOf(day-1));
            String exerciseStatus = description+"\n\n"+ String.valueOf(trainOfTheDay.getJSONObject(idx).getInt("done"))+" of "
                    +String.valueOf(trainOfTheDay.getJSONObject(idx).getInt("qtd"))+" done";

            progressTxt.setText(exerciseStatus);
            seekBar.setMax(trainOfTheDay.getJSONObject(idx).getInt("qtd"));
            seekBar.setProgress(trainOfTheDay.getJSONObject(idx).getInt("done"));
            /*
             * This part define the behaviour of seekbar in the app,
             * Then this part control the part of percentage
             */
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                private int percentage;

                @Override
                public void onProgressChanged(SeekBar seekBar, int percentage, boolean b) {
                    this.percentage = percentage;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    percentage = seekBar.getProgress();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    try {
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        JSONObject jsonObject = LoginSingleton.getInstance().getTrainingTracker();
                        JSONArray trainOfTheDay = jsonObject.getJSONArray(String.valueOf(day-1));
                        String exerciseStatus = description +"\n\n"+ String.valueOf(percentage)+" of "
                                +String.valueOf(trainOfTheDay.getJSONObject(idx).getInt("qtd"))+" done";
                        progressTxt.setText(exerciseStatus);

                        trainOfTheDay.getJSONObject(idx).put("done", percentage);
                        LoginSingleton.getInstance().updateDoneTrainingTracker(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ImageView exerciseGifImageView = root.findViewById(R.id.fragment_training_imageView);

        // Set up texts to be shown
        title.setText(exerciseTitle);
        exerciseGifImageView.setImageDrawable(ContextCompat.getDrawable(root.getContext(),exerciseGif));
        exerciseAnimation = (AnimationDrawable) exerciseGifImageView.getDrawable();
        exerciseAnimation.start();

        return root;
    }


}
