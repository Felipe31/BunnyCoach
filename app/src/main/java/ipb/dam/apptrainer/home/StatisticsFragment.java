package ipb.dam.apptrainer.home;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.components.CircularSeekBar;


/** TODO animate circular seek bar and configure the images to show the training days of the person
 * <p>Fragment that handles the statistics fragment..
 * This fragment is meant to be controled by the {@link android.support.v4.view.ViewPager}
 * present in {@link HomeActivity}.</p>
 * <p>To instantiate this class, <b>DO NOT</b> use the available default constructor
 * because it is required so the Operational System can recreate this fragment if needed.
 * Use {@link #newInstance(float, float, float, float, float, float, boolean[])} instead</p>
 * @author Murillo Henrique Pedroso Ferreira
 */
public class StatisticsFragment extends Fragment implements FragmentLifecycle {


    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_ARM_EXERCISES_DONE = "arg_statistics_arm_exercises_done";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_CHEST_EXERCISES_DONE = "arg_statistics_chest_exercises_done";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_LEG_EXERCISES_DONE = "arg_statistics_leg_exercises_done";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_BACK_EXERCISES_DONE = "arg_statistics_back_exercises_done";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_AEROBIC_EXERCISES_DONE = "arg_statistics_aerobic_exercises_done";

    /**
     * Argument indexing the data related to the circular seek bar percentage
     */
    private static final String ARG_OVERALL_COMPLETENESS = "arg_statistics_overall_completeness";

    /**
     * Argument indexing the data related to the circular seek bar percentage
     */
    private static final String ARG_TRAINING_DAYS = "arg_statistics_training_days";

    /**
     * Numbers of bars vertical statistics bars available in the statistics fragment.
     */
    private static final int NUM_STATISTICS_BARS = 5;

    /**
     * Animation duration in milliseconds to be performed while the statistics bars are
     * growing from bottom to up and the circular seek bar is changing the progress.
     */
    private static final int ANIMATION_DURATION = 800;

    /**
     * Animation start delay in milliseconds to be performed while the statistics bars are
     * growing from bottom to up and the circular seek bar is changing the progress.
     */
    private static final int ANIMATION_START_DELAY = 400;

    /**
     * Array of bars that index the bars from left to right
     */
    private View[] bars = new View[NUM_STATISTICS_BARS ];

    /**
     * <p>Array holding each statistics that will be shown by this fragment.</p>
     * <p>Indexes:</p>
     * <ul>
     *     <li><b>0:</b> Overall completeness (ranging from 0 to 1).</li>
     *     <li><b>1:</b> Completeness of exercises for arms (ranging from 0 to 1).</li>
     *     <li><b>2:</b> Completeness of exercises for the chest (ranging from 0 to 1).</li>
     *     <li><b>3:</b> Completeness of exercises for legs (ranging from 0 to 1).</li>
     *     <li><b>4:</b> Completeness of exercises for the back (ranging from 0 to 1).</li>
     *     <li><b>5:</b> Completeness of aerobic exercises(ranging from 0 to 1).</li>
     * </ul>
     */
    private float[] statistics = new float[6];

    /**
     * Boolean array holding whether the person trains in the i-th day of the week
     * week or not. First day of the week (index 0) is Monday (ISO 8601).
     */
    private boolean[] trainingDays = new boolean[7];

    // TODO put args to enable or disable the days worked last week

    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance(float, float, float, float, float, float, boolean[])}
     * instead.
     *
     */
    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * TODO check if the given parameters have the expected value.
     * @param overallCompletenessPercentage A percentage, ranging from 0 to 1, representing how much
     *                                  of the all given exercises the person has done.
     * @param doneArmsExercisePercentage A percentage, ranging from 0 to 1, representing how much
     *                                  of the all given exercises focusing on arms
     *                                   the person has done.
     * @param doneChestExercisePercentage A percentage, ranging from 0 to 1, representing how much
     *                                  of the all given exercises focusing on the chest
     *                                   the person has done.
     * @param doneLegsExercisePercentage A percentage, ranging from 0 to 1, representing how much
     *                                  of the all given exercises focusing on legs
     *                                   the person has done.
     * @param doneBackExercisePercentage A percentage, ranging from 0 to 1, representing how much
     *                                  of the all given exercises focusing on the back
     *                                   the person has done.
     * @param doneAerobicExercisePercentage A percentage, ranging from 0 to 1, representing how much
     *                                  of the all given aerobic exercises
     *                                   the person has done.
     * @param trainingDays Boolean array where {@code true} means that the person
     *                    trains at the i-th day of the week. First day of the week is
     *                    considered to be Monday (index {@code 0}),
     *                    according to <b>ISO 8601</b>. Must have size 7.
     * @return A new instance of fragment ProfileChooserFragment.
     */
    public static StatisticsFragment newInstance(float overallCompletenessPercentage,
                                                 float doneArmsExercisePercentage,
                                                 float doneChestExercisePercentage,
                                                 float doneLegsExercisePercentage,
                                                 float doneBackExercisePercentage,
                                                 float doneAerobicExercisePercentage,
                                                 boolean[] trainingDays) {

        StatisticsFragment fragment = new StatisticsFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        args.putFloat(ARG_OVERALL_COMPLETENESS, overallCompletenessPercentage);
        args.putFloat(ARG_ARM_EXERCISES_DONE, doneArmsExercisePercentage);
        args.putFloat(ARG_CHEST_EXERCISES_DONE, doneChestExercisePercentage);
        args.putFloat(ARG_LEG_EXERCISES_DONE, doneLegsExercisePercentage);
        args.putFloat(ARG_BACK_EXERCISES_DONE, doneBackExercisePercentage);
        args.putFloat(ARG_AEROBIC_EXERCISES_DONE, doneAerobicExercisePercentage);
        args.putBooleanArray(ARG_TRAINING_DAYS, trainingDays);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recovers the arguments set in #newInstance(String, String) method in this class.
        // Since its arguments should not be null, this if should never fail.
        if (getArguments() != null) { // TODO correct argument taken, no working
            statistics[0] = getArguments().getFloat(ARG_OVERALL_COMPLETENESS);
            statistics[1] = getArguments().getFloat(ARG_ARM_EXERCISES_DONE);
            statistics[2] = getArguments().getFloat(ARG_CHEST_EXERCISES_DONE);
            statistics[3] = getArguments().getFloat(ARG_LEG_EXERCISES_DONE);
            statistics[4] = getArguments().getFloat(ARG_BACK_EXERCISES_DONE);
            statistics[5] = getArguments().getFloat(ARG_AEROBIC_EXERCISES_DONE);
            trainingDays = getArguments().getBooleanArray(ARG_TRAINING_DAYS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        int calculatedOverallProgress = (int) (statistics[0] * 100f);

        //TODO check if the line below will be needed for something
        CircularSeekBar circularSeekBar = root.findViewById(R.id.fragment_statistics_circular_seek_bar);
        TextView overallProgressTextView = root.findViewById(R.id.fragment_statistics_exercises_current_progress);

        animateCircularSeekBar(circularSeekBar, overallProgressTextView, calculatedOverallProgress);

        bars[0] = root.findViewById(R.id.fragment_statistics_bar_arms);
        bars[1] = root.findViewById(R.id.fragment_statistics_bar_chest);
        bars[2] = root.findViewById(R.id.fragment_statistics_bar_legs);
        bars[3] = root.findViewById(R.id.fragment_statistics_bar_back);
        bars[4] = root.findViewById(R.id.fragment_statistics_bar_aerobic);

        return root;
    }

    @Override
    public void onPauseFragment(AppCompatActivity appCompatActivity) {

    }

    @Override
    public void onResumeFragment(AppCompatActivity appCompatActivity) {
        Objects.requireNonNull(appCompatActivity.getSupportActionBar()).setTitle(R.string.home_title);
    }

    @Override
    public void onResume(){
        super.onResume();

        for (int i = 0; i < bars.length; i++)
            expandBar(bars[i], statistics[i+1], // Index 0 is the overall statistics
                    ANIMATION_DURATION);

    }

    /**
     * Grown the view representing the statistics bar from bottom to top.
     * @param v Bar to be grown
     * @param growthRatio From 0 to 1, where 1 is the max height that the bar can have.
     * @param duration Duration of the animation in milliseconds. Cannot be negative.
     */
    private void expandBar(@NonNull View v, float growthRatio, int duration) {

        Animation scaleAnim = new ScaleAnimation(1.0f, 1.0f, // don't change the width
                0f, growthRatio * 100f /* TODO find out why this argument is working in [0,100] and not in [0,1], might not work under different Android versions */,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);

        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(duration);
        scaleAnim.setStartOffset(ANIMATION_START_DELAY);
        v.startAnimation(scaleAnim);
    }

    /**
     * Animates progress of the {@code circularSeekBar}, increasing it from 0 to the desired value.
     * @param circularSeekBar Circular seek bar to be animated.
     * @param targetProgress Progress that will be set after the animation completes. Should be greater than {@code 0}
     */
    private void animateCircularSeekBar(@NonNull CircularSeekBar circularSeekBar,
                                        @NonNull TextView progressTextView,
                                        int targetProgress){

        ValueAnimator anim = ValueAnimator.ofInt(0, targetProgress);
        anim.setDuration(ANIMATION_DURATION);
        anim.setStartDelay(ANIMATION_START_DELAY);
        anim.addUpdateListener(animation -> {
            int animProgress = (Integer) animation.getAnimatedValue();
            progressTextView.setText(String.format(Locale.US, "%d%%", animProgress));
            circularSeekBar.setProgress(animProgress);
        });
        anim.start();

    }


}
