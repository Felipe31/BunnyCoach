package ipb.dam.apptrainer.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ipb.dam.apptrainer.R;

public class HomeFragment extends Fragment {

    /**
     * Argument indexing the data related to the activity title
     */
    private static final String ARG_FRAGMENT_TITLE = "arg_profile_title";
    private static final String ARG_PROGRESS_TEXT = "arg_progress_text";


    /**
     * String holding the home title given in {@link #newInstance(String, String)}
     */
    private String fragmentTitle;

    private String progressText;


    /**
     * <b>DO NOT</b> use this constructor to instantiate this class.
     * It should only be used by the Operational System, use {@link #newInstance(String, String)}
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
    public static HomeFragment newInstance(@NonNull String fragmentTitle, @NonNull String progressText) {

        HomeFragment fragment = new HomeFragment();

        // Set the arguments so the title and description won't be lost
        // if the operational system recreates this class using the default constructor.
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, fragmentTitle);
        args.putString(ARG_FRAGMENT_TITLE, progressText);
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
            progressText = getArguments().getString(ARG_PROGRESS_TEXT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView title = root.findViewById(R.id.fragment_home_txtv_title);
        final TextView progressTxt = root.findViewById(R.id.fragment_home_txtv_progress);

        // Set up texts to be shown
        title.setText(fragmentTitle);
        progressTxt.setText(progressText);

        return root;
    }

}
