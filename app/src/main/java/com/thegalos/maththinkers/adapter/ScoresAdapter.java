package com.thegalos.maththinkers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.thegalos.maththinkers.R;
import com.thegalos.maththinkers.objects.Score;

import java.util.List;


public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.MyScoreViewHolder> {

    private final Context context;
    private final List<Score> scoreList;

    public ScoresAdapter(Context context, List<Score> scoreList) {
        this.context = context;
        this.scoreList = scoreList;
    }

    public class MyScoreViewHolder extends RecyclerView.ViewHolder{

        final TextView tvScore;
        final TextView tvPlayerNames;
        final TextView tv3;
        final TextView tv4;



        MyScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            tvScore = itemView.findViewById(R.id.tvPlayerScore);
            tvPlayerNames = itemView.findViewById(R.id.tvName);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);

        }
    }

    @NonNull
    @Override
    public MyScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_score,parent,false);
        return new MyScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyScoreViewHolder holder, int position) {
        Score scoreObject = scoreList.get(position);
        holder.tvScore.setText(String.valueOf(scoreObject.getScore()));
        holder.tvPlayerNames.setText(scoreObject.getPlayerName());
        String str;
        switch (scoreObject.getType()) {
            case "Quick":
                str = scoreObject.getScore() + " " + context.getResources().getString(R.string.from) + " " + scoreObject.getTotalQuestions() + " " + context.getResources().getString(R.string.correct_questions);
                holder.tv3.setText(str);

                break;
            case "TimeAttack":
                str = context.getResources().getString(R.string.correct_to_wrong_ration) + " " + scoreObject.getRatio();
                holder.tv3.setText(str);
                str = context.getResources().getString(R.string.game_was) + " " + scoreObject.getTimeTaken() + " " + context.getResources().getString(R.string.seconds);
                holder.tv4.setText(str);

                break;
            case "Advanced":
                str = scoreObject.getScore() + " " + context.getResources().getString(R.string.from) + " " + scoreObject.getTotalQuestions() + " " + context.getResources().getString(R.string.correct_questions);
                holder.tv3.setText(str);
                str = context.getResources().getString(R.string.game_was) + " " + scoreObject.getTimeTaken() + " " + context.getResources().getString(R.string.seconds);
                holder.tv4.setText(str);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }
}