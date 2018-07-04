package ipb.dam.apptrainer.home;

import android.support.v7.app.AppCompatActivity;

public interface FragmentLifecycle {

    void onPauseFragment(AppCompatActivity appCompatActivity);
    void onResumeFragment(AppCompatActivity appCompatActivity);

}