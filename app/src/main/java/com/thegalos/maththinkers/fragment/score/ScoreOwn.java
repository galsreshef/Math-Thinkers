package com.thegalos.maththinkers.fragment.score;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thegalos.maththinkers.R;

public class ScoreOwn extends Fragment {


    public ScoreOwn() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_own, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Context context = getContext();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        TextView tvOwnTimeAttack = view.findViewById(R.id.tvOwnTimeAttack);
        TextView tvOwnQuick = view.findViewById(R.id.tvOwnQuick);
        TextView tvOwnAdvanced = view.findViewById(R.id.tvOwnAdvanced);
        String str = sp.getInt("timeAttackScore", 0) + " "
                + getResources().getString(R.string.correct_questions) + " " +
                getString(R.string.in) + " " + sp.getInt("timeAttackTime", 0) + " " + getResources().getString(R.string.seconds)
                + "\n" + getString(R.string.correct_answer_ratio) + " " + sp.getFloat("timeAttackRatio", 0);
        tvOwnTimeAttack.setText(str);

        str = sp.getInt("quickScore", 0) + " "
                + getResources().getString(R.string.correct_questions);

        tvOwnQuick.setText(str);
        str = sp.getInt("advancedScore", 0) + " "
                + getResources().getString(R.string.correct_questions )+ " " +
                getString(R.string.in) + " " + sp.getInt("advancedTime", 0) + " " + getResources().getString(R.string.seconds);
        tvOwnAdvanced.setText(str);
    }
}