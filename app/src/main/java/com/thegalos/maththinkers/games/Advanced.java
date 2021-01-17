package com.thegalos.maththinkers.games;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thegalos.maththinkers.objects.Question;
import com.thegalos.maththinkers.QuestionGenerator;
import com.thegalos.maththinkers.R;
import com.thegalos.maththinkers.fragment.Loading;
import com.thegalos.maththinkers.fragment.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class Advanced extends Fragment {
    private Button btn0, btn1, btn2, btn3;
    private TextView tvQuestion;
    private TextView tvScoreAdvanced;
    private int locationOfCorrect, score = 0, numOfQuestions = 0, musicPos = 0;
    private Chronometer chronometer;
    private Chronometer chronometer1;
    boolean music, flashText,vibrate;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sp;

    private Vibrator vibrator;
    Context context;
    OnBackPressedCallback callback;

    List<String> operatorList;
    List<Button> btnList;
    MaterialShowcaseSequence materialShowcaseSequence;

    public Advanced() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_advanced, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        ////////////////////////////////////////////
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                endGame();
            }
        };

//        OnBackPressedCallback
        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback);


        ///////////////////////////////////////////



//    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView animation_image_frame = view.findViewById(R.id.ivAnimationTeacher);
        AnimationDrawable animationDrawable = (AnimationDrawable)animation_image_frame.getDrawable();
        animationDrawable.start();


        chronometer1 = new Chronometer(context);
        chronometer1.setBase(SystemClock.elapsedRealtime());
        chronometer1.start();
        btn0 = view.findViewById(R.id.btn00);
        btn1 = view.findViewById(R.id.btn01);
        btn2 = view.findViewById(R.id.btn02);
        btn3 = view.findViewById(R.id.btn03);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvScoreAdvanced = view.findViewById(R.id.tvScoreAdvanced);
        chronometer = view.findViewById(R.id.cmChronometer);

        chronometer.start();

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        //Gets user preferences
        sp = PreferenceManager.getDefaultSharedPreferences(context);

        Boolean addition = sp.getBoolean("Addition", false);
        Boolean subtraction = sp.getBoolean("Subtraction", false);
        Boolean multiply = sp.getBoolean("Multiply", false);
        Boolean division = sp.getBoolean("Division", false);
        music = sp.getBoolean("Music", false);
        flashText = sp.getBoolean("FlashText",true);
        vibrate = sp.getBoolean("Vibrate",true);
        boolean runBefore = sp.getBoolean("runBefore_advanced", false);

        operatorList = new ArrayList<>();
        if (addition)
            operatorList.add("add");

        if (subtraction)
            operatorList.add("sub");

        if (multiply)
            operatorList.add("mul");

        if (division)
            operatorList.add("div");

        if (addition && multiply)
            operatorList.add("mulAdd");

        if (subtraction && multiply)
            operatorList.add("mulSub");

        if (addition && division)
            operatorList.add("divAdd");

        if (subtraction && multiply)
            operatorList.add("divSub");

        Log.d("question" , "add is: " + addition + "sub is: " + subtraction + "mul is: " +  multiply + "div is: " +  division);
        mediaPlayer = MediaPlayer.create(context, R.raw.advanced);
        mediaPlayer.setLooping(true);
        if (music) {
            mediaPlayer.start();

        }

        btn0.setOnClickListener(v -> choose(0));
        btn1.setOnClickListener(v -> choose(1));
        btn2.setOnClickListener(v -> choose(2));
        btn3.setOnClickListener(v -> choose(3));

        btnList = new ArrayList<>();
        btnList.add(btn0);
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);

        //Generate the starting question
        generateQuestion();

        if (runBefore) {
            sp.edit().putBoolean("runBefore_advanced", true).apply();
            ShowcaseConfig config = new ShowcaseConfig();
            config.setRenderOverNavigationBar(true);
            config.setShapePadding(50);
            config.setDelay(500);


            materialShowcaseSequence = new MaterialShowcaseSequence(requireActivity(), "gall");
            materialShowcaseSequence.setConfig(config);
            chronometer.post(() -> {
                materialShowcaseSequence.addSequenceItem(chronometer,((getString(R.string.first_sequence_item_ag)))
                        ,((getString(R.string.first_sequence_item_next_ag))));

                materialShowcaseSequence.addSequenceItem(tvQuestion,((getString(R.string.second_sequence_item_ag)))
                        ,((getString(R.string.second_sequence_item_next_ag))));

                switch (locationOfCorrect) {
                    case 0: materialShowcaseSequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn0)
                                    .setContentText(((getString(R.string.third_sequence_item_ag))) + btn0.getText() +
                                            ((getString(R.string.fourth_sequence_item_ag))))
                                    .withRectangleShape()
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .build()
                    );
                        break;
                    case 1: materialShowcaseSequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn1)
                                    .setContentText(((getString(R.string.third_sequence_item_ag))) + btn1.getText() +
                                            ((getString(R.string.fourth_sequence_item_ag))))
                                    .withRectangleShape()
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .build()
                    );
                        break;
                    case 2: materialShowcaseSequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn2)
                                    .setContentText(((getString(R.string.third_sequence_item_ag))) + btn2.getText() +
                                            ((getString(R.string.fourth_sequence_item_ag))))
                                    .withRectangleShape()
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .build()
                    );
                        break;
                    case 3: materialShowcaseSequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn3)
                                    .setContentText(((getString(R.string.third_sequence_item_ag))) + btn3.getText() +
                                            ((getString(R.string.fourth_sequence_item_ag))))
                                    .withRectangleShape()
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .build()
                    );
                        break;
                }
                materialShowcaseSequence.addSequenceItem(tvScoreAdvanced,((getString(R.string.fifth_sequence_item_ag)))
                        ,((getString(R.string.sixth_sequence_item_ag))));
                materialShowcaseSequence.start();
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying() && music) {
            musicPos = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying() && music) {
            mediaPlayer.seekTo(musicPos);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        callback.remove();

    }



    public void choose(int i) {
//        XXX
        if (i == locationOfCorrect) {
            score++;
            numOfQuestions++;
            generateQuestion();

        } else {
            tvScoreAdvanced.startAnimation(AnimationUtils.loadAnimation(context,R.anim.correct_button));
            numOfQuestions++;

            if (vibrate) { vibrator.vibrate(500);
            }
            else {
                vibrator.cancel();
            }

            if (locationOfCorrect == 0) {
                btn0.startAnimation(AnimationUtils.loadAnimation(context,R.anim.correct_button));

            } else if (locationOfCorrect == 1) {
                btn1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.correct_button));

            } else if (locationOfCorrect == 2) {
                btn2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.correct_button));

            } else if (locationOfCorrect == 3) {
                btn3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.correct_button));
            }
            Handler handler = new Handler((Looper.getMainLooper()));
            handler.postDelayed(this::generateQuestion, 1000);
        }
        tvScoreAdvanced.setText(getString(R.string.advanced_score,score,numOfQuestions));
        if (flashText) {
            tvQuestion.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_question));
        }
    }

    private void generateQuestion() {
        Random rd = new Random();

        /////////////////////////////////////////////////////////////////////////////////////////////////
        String str = operatorList.get(rd.nextInt(operatorList.size()));
        Question question = new Question();

        switch (str) {
            case "add":
                Log.d("question" , "selected in generateQuestion is add");
                question = QuestionGenerator.sumComplexQuestion();

                break;
            case "sub":
                Log.d("question" , "selected in generateQuestion is sub");
                question = QuestionGenerator.subComplexQuestions();
                break;
            case "mul":
                Log.d("question" , "selected in generateQuestion is mul");
                question = QuestionGenerator.mulQuestion();
                break;
            case "div":
                Log.d("question" , "selected in generateQuestion is div");
                question = QuestionGenerator.divQuestion();
                break;
            case "mulAdd":
                Log.d("question" , "selected in generateQuestion is mulAdd");
                question = QuestionGenerator.mulAddQuestion();
                break;
            case "mulSub":
                Log.d("question" , "selected in generateQuestion is mulSub");
                question = QuestionGenerator.mulSubQuestion();
                break;
            case "divAdd":
                Log.d("question" , "selected in generateQuestion is divAdd");
                question = QuestionGenerator.divAddQuestion();
                break;
            case "divSub":
                Log.d("question" , "selected in generateQuestion is divSub");
                question = QuestionGenerator.divSubQuestion();
                break;

        }
        Log.d("questions" , "question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

        locationOfCorrect = rd.nextInt(4);
        tvQuestion.setText(question.getQuestion());

        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrect)
                btnList.get(i).setText(String.valueOf(question.getCorrectAnswer()));
            else {
                btnList.get(i).setText(String.valueOf(question.getWrongAnswer().get(0)));
                question.getWrongAnswer().remove(0);
            }
        }
    }

    public void endGame() {
        final Dialog scorePopUp = new Dialog(context);
        scorePopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scorePopUp.setContentView(R.layout.dialog_end_game);
        scorePopUp.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);
        scorePopUp.setCanceledOnTouchOutside(false);
//        Objects.requireNonNull(scorePopUp.getWindow()).getAttributes().windowAnimations = R.style.ScorePopUpAnimation;
        Objects.requireNonNull(scorePopUp.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvCongratulations = scorePopUp.findViewById(R.id.tvSettings);
        TextView tvTotalCorrect = scorePopUp.findViewById(R.id.tvTotalCorrect);
        TextView tvTotalQ = scorePopUp.findViewById(R.id.tvTotalQ);
        Button btnAgain = scorePopUp.findViewById(R.id.btnAgain);
        Button btnHome = scorePopUp.findViewById(R.id.btnHome);

        scorePopUp.show();
        chronometer.stop();

        int recordScore = sp.getInt("advancedScore", 0);

        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer1.getBase();

        boolean newHigh = false;

        tvTotalCorrect.setText(String.valueOf(score));
        tvTotalQ.setText(String.valueOf(numOfQuestions));

        if (score >= recordScore && numOfQuestions - score <= sp.getInt("advancedWrong",100) ) {
            sp.edit().putInt("advancedWrong", numOfQuestions - score)
                    .putInt("advancedScore", score)
                    .putInt("advancedTime", (int) elapsedMillis / 1000)
                    .putInt("advancedTotalQuestions", numOfQuestions).apply();
            chronometer1.stop();
            newHigh = true;
        }

        if (newHigh) {
            tvCongratulations.setText(R.string.new_high_score);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && !sp.getString("userName", "userName").equals("userName")) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Games").child("Advanced");
                ref.child("Score").setValue(score);
                ref.child("WrongAnswered").setValue(numOfQuestions - score);
                ref.child("TimeTaken").setValue((int) elapsedMillis / 1000);
                ref.child("TotalQuestions").setValue(numOfQuestions);

                ref = FirebaseDatabase.getInstance().getReference().child("Score").child("Games").child("Advanced").push();
                ref.child("Score").setValue(score);
                ref.child("WrongAnswered").setValue(numOfQuestions - score);
                ref.child("TimeTaken").setValue((int) elapsedMillis / 1000);
                ref.child("TotalQuestions").setValue(numOfQuestions);
                ref.child("userName").setValue(user.getDisplayName());


            }

        } else {
            if (numOfQuestions - score < 4 && numOfQuestions > 10) {
                tvCongratulations.setText(getString(R.string.hello_genius));

            } else if (numOfQuestions - score > 0 && numOfQuestions - score < 5) {
                tvCongratulations.setText(getString(R.string.nice));

            } else if (numOfQuestions < 10 && numOfQuestions > 1) {
                tvCongratulations.setText(getString(R.string.you_can_do_better));

            } else if (numOfQuestions == 0) {
                tvCongratulations.setText(getString(R.string.hey_buddy));

            } else {
                tvCongratulations.setText(getString(R.string.need_more_practice));
            }
        }

        btnHome.setOnClickListener(view -> {
            scorePopUp.dismiss();
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flAppFragment, new Main(), "Main").commit();
            }
        });

        btnAgain.setOnClickListener(v -> {
            scorePopUp.dismiss();
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flAppFragment, new Loading(), "Loading").commit();
            }
        });

//        scorePopUp.setOnCancelListener(dialog -> getFragmentManager().beginTransaction()
//                .replace(R.id.flAppFragment, new Main(), "Main").commit());
    }
}