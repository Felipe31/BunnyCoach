package ipb.dam.apptrainer.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ipb.dam.apptrainer.R;
import ipb.dam.apptrainer.home.components.CircularSeekBar;
import ipb.dam.apptrainer.profileform.ProfileChooserFragment;


/**
 * <p>Fragment that handles the statistics fragment..
 * This fragment is meant to be controled by the {@link android.support.v4.view.ViewPager}
 * present in {@link HomeActivity}.</p>
 * <p>To instantiate this class, <b>DO NOT</b> use the available default constructor
 * because it is required so the Operational System can recreate this fragment if needed.
 * Use {@link #newInstance()} instead</p>
 * @author Murillo Henrique Pedroso Ferreira
 */
public class StatisticsFragment extends Fragment {


    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_ARM_EXERCISES_MADE = "arg_statistics_arm_exercises_made";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_CHEST_EXERCISES_MADE = "arg_statistics_chest_exercises_made";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_LEG_EXERCISES_MADE = "arg_statistics_leg_exercises_made";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_BACK_EXERCISES_MADE = "arg_statistics_back_exercises_made";

    /**
     * Argument indexing the data related to the statistics bar below the seek bar
     */
    private static final String ARG_AEROBIC_EXERCISES_MADE = "arg_statistics_aerobic_exercises_made";

    /**
     * Argument indexing the data related to the circular seek bar percentage
     */
    private static final String ARG_OVERALL_COMPLETENESS = "arg_statistics_overall_completeness";

    // TODO put args to enable or disable the days worked last week

    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance()}
     * instead.
     *
     */
    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * TODO update args conform to the vars defined at the beginning of this class
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileChooserFragment.
     */
    public static StatisticsFragment newInstance() {

        StatisticsFragment fragment = new StatisticsFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        //args.putString(ARG_PROFILE_TITLE, profileTitle); TODO update args
        //args.putString(ARG_PROFILE_DESCRIPTION, profileDescription); TODO update args
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recovers the arguments set in #newInstance(String, String) method in this class.
        // Since its arguments should not be null, this if should never fail.
        if (getArguments() != null) {
            //profileTitle = getArguments().getString(ARG_PROFILE_TITLE); TODO update args
            //profileDescription = getArguments().getString(ARG_PROFILE_DESCRIPTION); TODO update args
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        CircularSeekBar circularSeekBar = root.findViewById(R.id.fragment_statistics_circular_seek_bar);
        circularSeekBar.setMaxProgress(100);
        circularSeekBar.setProgress(91);
        circularSeekBar.invalidate();

        return root;
    }


}
