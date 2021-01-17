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


public class QuickMath extends Fragment {
    private TextView timerText;
    private TextView quickMathQuestion;
    private TextView quickMathScore;
    private TextView userFeedback;
    private Button btnCorrect, btnWrong;
    private int score;
    private int wrongOrCorrect;
    private int numOfQuestions;
    private int musicPos;
    private Vibrator vibrator;
    private Animation animation;
    private Boolean music;
    private Boolean flashText;
    private Boolean vibrate;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;

    private SharedPreferences sp;

    OnBackPressedCallback callback;
    Context context;
    List<String> operatorList;


    public QuickMath() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_quick_math, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();

        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                endGame();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback);


        ImageView animation_image_frame = view.findViewById(R.id.ivAnimationTeacher);
        AnimationDrawable animationDrawable = (AnimationDrawable) animation_image_frame.getDrawable();
        animationDrawable.start();

        sp = PreferenceManager.getDefaultSharedPreferences(context);

        timerText = view.findViewById(R.id.tvStopWatch);
        quickMathQuestion = view.findViewById(R.id.tvQuickMathQuestion);
        quickMathScore = view.findViewById(R.id.tvQuickMathsScore);
        btnCorrect = view.findViewById(R.id.btnCorrect);
        btnWrong = view.findViewById(R.id.btnWrong);
        userFeedback = view.findViewById(R.id.tvGo);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        animation = AnimationUtils.loadAnimation(context, R.anim.correct_button);

        btnCorrect.setOnClickListener(v -> choose(0));
        btnWrong.setOnClickListener(v -> choose(1));
        userFeedback.setOnClickListener(v -> {
            timer();
//                userFeedback.setEnabled(false);
            userFeedback.setOnClickListener(null);
        });


        //Gets user preferences
        music = sp.getBoolean("Music", false);
        boolean addition = sp.getBoolean("Addition", false);
        boolean subtraction = sp.getBoolean("Subtraction", false);
        boolean multiply = sp.getBoolean("Multiply", false);
        boolean division = sp.getBoolean("Division", false);
        flashText = sp.getBoolean("FlashText",true);
        vibrate = sp.getBoolean("Vibrate",true);
        boolean runBefore = sp.getBoolean("runBefore_quickMath", false);
        operatorList = new ArrayList<>();
        if (addition)
            operatorList.add("add");
        if (subtraction)
            operatorList.add("sub");
        if (multiply)
            operatorList.add("mul");
        if (division)
            operatorList.add("div");
        Log.d("question" , "add is: " + addition + "sub is: " + subtraction + "mul is: " + multiply + "div is: " + division);

        generateQuestion();

        timer();

        mediaPlayer = MediaPlayer.create(context, R.raw.quick_math);
        mediaPlayer.setLooping(true);

        if (music) {
            mediaPlayer.start();
        }

        if (!runBefore) {
            sp.edit().putBoolean("runBefore_quickMath", true).apply();
            countDownTimer.cancel();
            timerText.setText(R.string.start_timer);
            ShowcaseConfig config = new ShowcaseConfig();
            config.setRenderOverNavigationBar(true);
            config.setShapePadding(50);
            config.setDelay(500);

            final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(requireActivity(), "quickMathOnBoarding");
            sequence.setConfig(config);
            timerText.post(() -> {
                sequence.addSequenceItem(timerText, (getString(R.string.first_sequence_item_qmg)),
                        (getString(R.string.first_sequence_item_next_qmg)));

                sequence.addSequenceItem(quickMathQuestion, (getString(R.string.second_sequence_item_qmg)),
                        (getString(R.string.second_sequence_item_next_qmg)));

                if (wrongOrCorrect == 0)
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btnCorrect)
                                    .setContentText((getString(R.string.third_sequence_item_qmg)))
                                    .withRectangleShape()
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .build()
                    );
                else
                    sequence.addSequenceItem(
                            new MaterialShowcaseView.Builder(requireActivity())
                                    .setTarget(btnWrong)
                                    .setContentText((getString(R.string.fourth_sequence_item_qmg)))
                                    .withRectangleShape()
                                    .setDismissOnTargetTouch(true)
                                    .setTargetTouchable(true)
                                    .build()
                    );
                sequence.addSequenceItem(
                        new MaterialShowcaseView.Builder(requireActivity())
                                .setTarget(userFeedback)
                                .setContentText((getString(R.string.fifth_sequence_item_qmg)))
                                .setDismissOnTargetTouch(true)
                                .setTargetTouchable(true)
                                .build()
                );
                sequence.start();
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


    public void choose(int i) {
        if (flashText) {
            userFeedback.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_text0));
        }
        if (i == wrongOrCorrect) {
            score++;
            generateQuestion();
            if (flashText) {
                quickMathQuestion.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_question));
            }
            quickMathScore.setText(getString(R.string.score_calc,score));
            numOfQuestions++;
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
            quickMathScore.startAnimation(AnimationUtils.loadAnimation(context,R.anim.correct_button));
            if (vibrate)
                vibrator.vibrate(500);
            else
                vibrator.cancel();

            if (wrongOrCorrect == 1)
                btnWrong.startAnimation(animation);
            else
                btnCorrect.startAnimation(animation);

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
            generateQuestion();
            numOfQuestions++;

        }
    }

    //Generates write or wrong question randomly depending on user preference
    private void generateQuestion() {
        Random rd = new Random();
        String str = operatorList.get(rd.nextInt(operatorList.size()));
        Question question = new Question();
        switch (str) {
            case "add":
                Log.d("question" , "selected in generateQuestion is add");
                question = QuestionGenerator.sumQuestion();
                break;
            case "sub":
                Log.d("question" , "selected in generateQuestion is sub");
                question = QuestionGenerator.subQuestion();
                break;
            case "mul":
                Log.d("question" , "selected in generateQuestion is mul");
                question = QuestionGenerator.mulQuestion();
                break;
            case "div":
                Log.d("question" , "selected in generateQuestion is div");
                question = QuestionGenerator.divQuestion();
                break;
        }
        wrongOrCorrect = rd.nextInt(2);
        if (wrongOrCorrect == 0) {
            quickMathQuestion.setText(question.getCorrectQuestion());
        } else {
            quickMathQuestion.setText(question.getWrongQuestion());
        }
    }

    //Timer that keeps track of time
    private void timer() {
        countDownTimer = new CountDownTimer(30000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished > 10000)
                    timerText.setText(getString(R.string.timer_quick_math,(int) millisUntilFinished / 1000));
                else if (millisUntilFinished < 10000 && millisUntilFinished > 5000) {
                    //Timer flickering gets faster as time runs out
                    timerText.setText(getString(R.string.timer_quick_math_ten_less,(int) millisUntilFinished / 1000));
                    //Flickers only if its enabled in the settings.
                    if (flashText) {
                        timerText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_text2));
                    }
                } else if (millisUntilFinished < 5000 && millisUntilFinished > 3000) {
                    timerText.setText(getString(R.string.timer_quick_math_ten_less,(int) millisUntilFinished / 1000));
                    if (flashText) {
                        timerText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_text1));
                    }
                } else {
                    timerText.setText(getString(R.string.timer_quick_math_ten_less,(int) millisUntilFinished / 1000));
                    if (flashText) {
                        timerText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.flicker_text0));
                    }
                }
            }
            @Override
            public void onFinish() {
                //Shows user results
                endGame();
            }
        }.start();
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
        countDownTimer.cancel();

        boolean newHigh = false;

        if (score >= sp.getInt("quickScore",0)) {
            sp.edit().putInt("quickDifference", numOfQuestions - score)
                    .putInt("quickScore",score)
                    .putInt("quickTotalQuestions", numOfQuestions).apply();
            newHigh = true;
        }

        if (newHigh && !sp.getString("userName", "userName").equals("userName")) {
            tvCongratulations.setText(R.string.new_high_score);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Games").child("QuickMath");
                ref.child("WrongAnswered").setValue(numOfQuestions - score);
                ref.child("Score").setValue(score);
                ref.child("TotalQuestions").setValue(numOfQuestions);

                ref = FirebaseDatabase.getInstance().getReference().child("Score").child("Games").child("QuickMath").push();
                ref.child("WrongAnswered").setValue(numOfQuestions - score);
                ref.child("Score").setValue(score);
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
        tvTotalCorrect.setText(String.valueOf(score));

        tvTotalQ.setText(String.valueOf(numOfQuestions));

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
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        callback.remove();
    }

}