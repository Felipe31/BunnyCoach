package ipb.dam.apptrainer.training;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import ipb.dam.apptrainer.R;

public class TrainingFragment extends Fragment {

    /**
     * Argument indexing the data related to the activity title
     */
    private static final String ARG_EXERCISE_TITLE = "arg_profile_title";
    private static final String ARG_DESCRIPTION = "arg_current_int";
    private static final String ARG_DONE_BOOL = "arg_total_int";


    /**
     * String holding the training title given in {@link #newInstance(String, String, boolean)}
     */
    private String description, exerciseTitle;
    private boolean done;
    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance(String, String, boolean)}
     * instead.
     *
     */
    public TrainingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static TrainingFragment newInstance(@NonNull String exerciseTitle, @NonNull String description, @NonNull boolean done) {

        TrainingFragment fragment = new TrainingFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        args.putString(ARG_EXERCISE_TITLE, exerciseTitle);
        args.putString(ARG_DESCRIPTION, description);
        args.putBoolean(ARG_DONE_BOOL, done);
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
            done= getArguments().getBoolean(ARG_DONE_BOOL);
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
        String exerciseStatus = String.valueOf(description)+" of "+ String.valueOf(done)+ " done";

        // Set up texts to be shown
        title.setText(exerciseTitle);
        progressTxt.setText(exerciseStatus);

        Button doneButton = root.findViewById(R.id.fragment_training_button);

        doneButton.setOnClickListener((View view) -> {
            doneButton.setEnabled(false);
            doneButton.setTextColor(getResources().getColor(R.color.blue));
            Snackbar snackbar = Snackbar.make(root.getRootView(), R.string.exercise_done_snack, Snackbar.LENGTH_LONG);
/*
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                doneButton.setEnabled(true);
            }
        });
        */
            snackbar.setAction(R.string.exercise_done_snack_undo, view2 -> {
                doneButton.setEnabled(true);
                doneButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            });
            snackbar.show();

        });



        return root;
    }

}
