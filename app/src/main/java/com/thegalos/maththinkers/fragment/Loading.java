package com.thegalos.maththinkers.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thegalos.maththinkers.R;
import com.thegalos.maththinkers.games.Advanced;
import com.thegalos.maththinkers.games.QuickMath;
import com.thegalos.maththinkers.games.TimeAttack;

import java.util.Random;


public class Loading extends Fragment {
    private static final int loadingScreenTime = 4000;

    Context context;
    OnBackPressedCallback callback;

    public Loading() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callback.remove();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String gameSelected = sp.getString("gameSelected", "quickMath");
        TextView hintText = view.findViewById(R.id.tvHint);
        TextView timer = view.findViewById(R.id.tvTime);
        Random rd = new Random();
        int randInt = rd.nextInt(4);
        switch (gameSelected) {

            case "quickMath":
                switch (randInt) {
                    case 0:
                        hintText.setText(getString(R.string.hint_background_alert));
                        break;
                    case 1:
                        hintText.setText(getString(R.string.hint_panic));
                        break;
                    case 2:
                        hintText.setText(getString(R.string.start_over));
                        break;
                    case 3:
                        hintText.setText(getString(R.string.check_with_friends));
                        break;
                }
                break;

            case "timeAttack":
                switch (randInt) {
                    case 0:
                        hintText.setText(getString(R.string.hint_time_resets));
                        break;
                    case 1:
                        hintText.setText(getString(R.string.hint_improve));
                        break;
                    case 2:
                        hintText.setText(getString(R.string.hint_difficulty));
                        break;
                    case 3:
                        hintText.setText(getString(R.string.check_with_friends));
                        break;
                }
                break;

            case "advanced":
                switch (randInt) {
                    case 0:
                        hintText.setText(getString(R.string.hint_not_enemy));
                        break;
                    case 1:
                        hintText.setText(getString(R.string.hint_improve));
                        break;
                    case 2:
                        hintText.setText(getString(R.string.hint_difficulty));
                        break;
                    case 3:
                        hintText.setText(getString(R.string.check_with_friends));
                        break;
                }
                break;
        }

        new Handler((Looper.getMainLooper())).postDelayed(() -> {

            ///////////////////////////////////////////

            switch (gameSelected) {

                case "quickMath":
                    if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.flAppFragment, new QuickMath(), "quickMath").commit();
                    }
                    break;

                case "timeAttack":
                    if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.flAppFragment, new TimeAttack(), "timeAttack").commit();
                    }
                    break;

                case "advanced":
                    if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.flAppFragment, new Advanced(), "advanced").commit();
                    }
                    break;
            }


            ///////////////////////////////////////////////////////////////////

        }, loadingScreenTime);
        new CountDownTimer(loadingScreenTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(getResources().getString(R.string.loading_screen_timer, (int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }
}
