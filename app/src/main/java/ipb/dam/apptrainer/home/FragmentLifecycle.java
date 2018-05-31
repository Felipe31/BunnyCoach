package ipb.dam.apptrainer.home;

import android.support.v7.app.AppCompatActivity;

public interface FragmentLifecycle {

    public void onPauseFragment(AppCompatActivity appCompatActivity);
    public void onResumeFragment(AppCompatActivity appCompatActivity);

}