package com.thegalos.maththinkers.games;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.animation.Animation;
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

public class TimeAttack extends Fragment {
    private TextView timeLeftText;
    private TextView tvTimeAttackScore;
    private TextView userFeedback;
    private Button btn0, btn1, btn2, btn3;
    private int locationOfCorrect;
    private int score = 0;
    private int numOfQuestions = 0;

    private int musicPos;
    private CountDownTimer countDownTimer;
    private Animation correctAnimation, feedBackAnimation;
    private MediaPlayer mediaPlayer;
    private Boolean music, flashText,vibrate;
    private Chronometer chronometer;
    private SharedPreferences sp;
    private Vibrator vibrator;
    private boolean runBefore;
    List<Button> btnList;
    OnBackPressedCallback callback;
    Context context;
    List<String> operatorList;
    public TimeAttack() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_time_attack, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        init(view);

        timer();
        generateQuestions();

        checkAssist(view);
    }

    private void checkAssist(View v) {
        if (!runBefore) {
            sp.edit().putBoolean("runBefore_timeAttack", true).apply();
            runBefore = true;
            if (countDownTimer != null)
                countDownTimer.cancel();
            timeLeftText.setText(R.string.time_left_ten);
            TextView whichOneIsCorrect = v.findViewById(R.id.tvTimeAttackInst);
            whichOneIsCorrect.setText(R.string.find_correct_equation);
            ShowcaseConfig config = new ShowcaseConfig();
            config.setRenderOverNavigationBar(true);
            config.setShapePadding(50);
            config.setDelay(500);


            final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(requireActivity(), "timeAttackOnBoarding");
            sequence.setConfig(config);
            timeLeftText.post(() -> {
                sequence.addSequenceItem(timeLeftText,((getString(R.string.first_sequence_item_ttg)))
                        ,((getString(R.string.first_sequence_item_next_ttg))));

                switch (locationOfCorrect) {
                    case 0: sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn0)
                                    .setContentText(((getString(R.string.second_sequence_item_ttg))) + btn0.getText() +
                                            ((getString(R.string.third_sequence_item_ttg))))
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .withRectangleShape()
                                    .build()
                    );
                        break;
                    case 1: sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn1)
                                    .setContentText(((getString(R.string.second_sequence_item_ttg))) + btn1.getText() +
                                            ((getString(R.string.third_sequence_item_ttg))))
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .withRectangleShape()
                                    .build()
                    );
                        break;
                    case 2: sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn2)
                                    .setContentText(((getString(R.string.second_sequence_item_ttg))) + btn2.getText() +
                                            ((getString(R.string.third_sequence_item_ttg))))
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .withRectangleShape()
                                    .build()
                    );
                        break;
                    case 3: sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btn3)
                                    .setContentText(((getString(R.string.second_sequence_item_ttg))) + btn3.getText() +
                                            ((getString(R.string.third_sequence_item_ttg))))
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .withRectangleShape()
                                    .build()
                    );
                        break;
                }
                sequence.addSequenceItem(userFeedback,(getString(R.string.fourth_sequence_item_ttg)),
                        (getString(R.string.fifth_sequence_item_ttg)));

                sequence.addSequenceItem(tvTimeAttackScore,(getString(R.string.sixth_sequence_item_ttg)),
                        (getString(R.string.seventh_sequence_item_ttg)));

                sequence.start();
            });

        }
    }

    private void init(View v) {
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                endGame();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback);


        sp = PreferenceManager.getDefaultSharedPreferences(context);
        music = sp.getBoolean("Music", true);
        boolean addition = sp.getBoolean("Addition", false);
        boolean subtraction = sp.getBoolean("Subtraction", false);
        boolean multiply = sp.getBoolean("Multiply", false);
        boolean division = sp.getBoolean("Division", false);
        flashText = sp.getBoolean("FlashText",true);
        vibrate = sp.getBoolean("Vibrate",true);
        runBefore = sp.getBoolean("runBefore_timeAttack", false);
        operatorList = new ArrayList<>();
        btnList = new ArrayList<>();
        if (addition)
            operatorList.add("add");
        if (subtraction)
            operatorList.add("sub");
        if (multiply)
            operatorList.add("mul");
        if (division)
            operatorList.add("div");
        Log.d("question" , "add is: " +  addition + "sub is: " +  subtraction + "mul is: " +  multiply + "div is: " +  division);

        ImageView animation_image_frame = v.findViewById(R.id.ivAnimationTeacher);
        AnimationDrawable animationDrawable = (AnimationDrawable)animation_image_frame.getDrawable();
        animationDrawable.start();


        chronometer = new Chronometer(context);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        timeLeftText = v.findViewById(R.id.tvTimeAttackTimeLeft);
        tvTimeAttackScore = v.findViewById(R.id.tvTimeAttackScore);
        btn0 = v.findViewById(R.id.btn0);
        btn1 = v.findViewById(R.id.btn1);
        btn2 = v.findViewById(R.id.btn2);
        btn3 = v.findViewById(R.id.btn3);
        userFeedback = v.findViewById(R.id.tvFeedback);

        btnList.add(btn0);
        btnList.add(btn1);
        btnList.add(btn2);
        btnList.add(btn3);


        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        //Variables to hold animation values.
        correctAnimation = AnimationUtils.loadAnimation(context,R.anim.correct_button);
        feedBackAnimation = AnimationUtils.loadAnimation(context,R.anim.flicker_text0);

        //Required to generate the first question
        //Initial animation
        btn0.startAnimation(AnimationUtils.loadAnimation(context,R.anim.question0));
        btn1.startAnimation(AnimationUtils.loadAnimation(context,R.anim.question1));
        btn2.startAnimation(AnimationUtils.loadAnimation(context,R.anim.question2));
        btn3.startAnimation(AnimationUtils.loadAnimation(context,R.anim.question3));

        btn0.setOnClickListener(v1 -> choose(0));
        btn1.setOnClickListener(v12 -> choose(1));
        btn2.setOnClickListener(v13 -> choose(2));
        btn3.setOnClickListener(v14 -> choose(3));

        mediaPlayer = MediaPlayer.create(context,R.raw.time_attack);
        mediaPlayer.setLooping(true);
        if (music) {
            mediaPlayer.start();
        }
    }


    private void timer() {
        countDownTimer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 4000)
                    if (flashText) {
                        timeLeftText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_timer));
                    }
                if (millisUntilFinished > 10000)
                    timeLeftText.setText(getString(R.string.time_left, (int) millisUntilFinished / 1000));
                else
                    timeLeftText.setText(getString(R.string.time_left_ten_less, (int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                endGame();
            }
        }.start();
    }

    private void generateQuestions() {
        Random rd = new Random();
        String str = operatorList.get(rd.nextInt(operatorList.size()));
        Question question = new Question();
        switch (str) {
            case "add":
                question = QuestionGenerator.sumListQuestion();
                break;
            case "sub":
                question = QuestionGenerator.subListQuestion();
                break;
            case "mul":
                question = QuestionGenerator.mulListQuestion();
                break;
            case "div":
                question = QuestionGenerator.divListQuestion();
                break;
        }

        locationOfCorrect = rd.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrect)
                btnList.get(i).setText(question.getCorrectQuestion());
            else {
                btnList.get(i).setText(question.getWrongQuestions().get(0));
                question.getWrongQuestions().remove(0);
            }
        }
    }

    public void choose(int i) {
        if (flashText) {
            userFeedback.startAnimation(feedBackAnimation);
        }
        if ( i == locationOfCorrect) {
            score++;
            numOfQuestions++;
            generateQuestions();

            if (runBefore)
                countDownTimer.start();

            if (numOfQuestions % 12 == 0) {
                userFeedback.setText(getString(R.string.good_job));
            } else if (numOfQuestions % 12 == 1) {
                userFeedback.setText(getString(R.string.amazing));

            } else if (numOfQuestions % 12 == 2) {
                userFeedback.setText(getString(R.string.fantastic));

            } else if (numOfQuestions % 12 == 3) {
                userFeedback.setText(getString(R.string.damn));

            } else if (numOfQuestions % 12 == 4) {
                userFeedback.setText(getString(R.string.genius));

            } else if (numOfQuestions % 12 == 5) {
                userFeedback.setText(getString(R.string.sweet));

            } else if (numOfQuestions % 12 == 6) {
                userFeedback.setText(getString(R.string.wow));

            } else if (numOfQuestions % 12 == 7) {
                userFeedback.setText(getString(R.string.nice));

            } else if (numOfQuestions % 12 == 8) {
                userFeedback.setText(getString(R.string.excellent));

            } else if (numOfQuestions % 12 == 9) {
                userFeedback.setText(getString(R.string.o_o));

            } else if (numOfQuestions % 12 == 10) {
                userFeedback.setText(getString(R.string.brilliant));

            } else if (numOfQuestions % 12 == 11) {
                userFeedback.setText(getString(R.string.bananas));
            }

        } else {
            numOfQuestions++;
            if (vibrate) {
                vibrator.vibrate(500);

            } else {
                vibrator.cancel();
            }

            if (locationOfCorrect == 0) {
                btn0.startAnimation(correctAnimation);

            } else if (locationOfCorrect == 1) {
                btn1.startAnimation(correctAnimation);

            } else if (locationOfCorrect == 2) {
                btn2.startAnimation(correctAnimation);

            } else if (locationOfCorrect == 3) {
                btn3.startAnimation(correctAnimation);
            }
            Random rd = new Random();
            switch (rd.nextInt(3)) {
                case 0:
                    userFeedback.setText(getString(R.string.oh_no));
                    break;
                case 1:
                    userFeedback.setText(getString(R.string.next));
                    break;
                case 2:
                    userFeedback.setText(getString(R.string.sad));
                    break;
            }
            Handler handler = new Handler((Looper.getMainLooper()));
            handler.postDelayed(this::endGame, 1000);

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
        TextView typeText = scorePopUp.findViewById(R.id.tvType);
        Button btnAgain = scorePopUp.findViewById(R.id.btnAgain);
        Button btnHome = scorePopUp.findViewById(R.id.btnHome);

        scorePopUp.show();
        chronometer.stop();
        countDownTimer.cancel();

        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();

        boolean newHigh = false;

        typeText.setText(R.string.timeTaken);
        tvTotalCorrect.setText(String.valueOf(score));
        tvTotalQ.setText(String.valueOf((int) elapsedMillis / 1000));


        if ((float)((elapsedMillis / 1000) ) / score > sp.getFloat("timeAttackRatio",0)) {
            sp.edit().putFloat("timeAttackRatio", (float)((elapsedMillis / 1000) ) / score)
                    .putInt("timeAttackScore", score)
                    .putInt("timeAttackTime", (int) elapsedMillis / 1000).apply();

            Log.d("testing", "Score Ratio is: " + sp.getFloat("timeAttackRatio",0));
            newHigh = true;
        }

        if (newHigh) {
            tvCongratulations.setText(R.string.new_high_score);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && !sp.getString("userName", "userName").equals("userName") && (score > 0)) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Games").child("TimeAttack");
                ref.child("Score").setValue(score);
                ref.child("ScoreRatio").setValue((float)((elapsedMillis / 1000) ) / score);
                ref.child("TimeTaken").setValue((int) elapsedMillis / 1000);

                ref = FirebaseDatabase.getInstance().getReference().child("Score").child("Games").child("TimeAttack").push();
                ref.child("Score").setValue(score);
                ref.child("ScoreRatio").setValue(((float)((elapsedMillis / 1000) ) / score));
                ref.child("TimeTaken").setValue((int) elapsedMillis / 1000);
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

}