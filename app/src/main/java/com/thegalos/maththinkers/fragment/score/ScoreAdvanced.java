package com.thegalos.maththinkers.fragment.score;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thegalos.maththinkers.R;
import com.thegalos.maththinkers.adapter.ScoresAdapter;
import com.thegalos.maththinkers.objects.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreAdvanced extends Fragment {

    Context context;

    public ScoreAdvanced() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.score_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();


        final RecyclerView rvAdvanced = view.findViewById(R.id.rvGameScore);
        TextView tvGameSelected = view.findViewById(R.id.tvOwnScore);
        tvGameSelected.setText(getResources().getText(R.string.advanced_game));

        final List<Score> advancedScoreList = new ArrayList<>();

        DatabaseReference refAdvanced = FirebaseDatabase.getInstance().getReference().child("Score").child("Games").child("Advanced");



        Query queryAdvanced = refAdvanced.orderByChild("Score").limitToLast(5);
        queryAdvanced.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Score scoreObject = new Score();
                    scoreObject.setType("Advanced");
                    if (snapshot.child("userName").exists())
                        scoreObject.setPlayerName(snapshot.child("userName").getValue(String.class));

                    if (snapshot.child("Score").exists())
                        scoreObject.setScore(snapshot.child("Score").getValue(Integer.class));

                    if (snapshot.child("TimeTaken").exists())
                        scoreObject.setTimeTaken(snapshot.child("TimeTaken").getValue(Integer.class));

                    if (snapshot.child("ScoreRatio").exists())
                        scoreObject.setRatio(snapshot.child("ScoreRatio").getValue(Float.class));

                    if (snapshot.child("TotalQuestions").exists())
                        scoreObject.setTotalQuestions(snapshot.child("TotalQuestions").getValue(Integer.class));

                    if (snapshot.child("WrongAnswered").exists())
                        scoreObject.setTotalWrong(snapshot.child("WrongAnswered").getValue(Integer.class));

                    advancedScoreList.add(scoreObject);
                }
                Collections.reverse(advancedScoreList);
                rvAdvanced.setHasFixedSize(true);
                rvAdvanced.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                final ScoresAdapter feedAdapter = new ScoresAdapter(context, advancedScoreList);
//                rvAdvanced.setPadding(0, 220, 0, 0);
                rvAdvanced.setAdapter(feedAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}