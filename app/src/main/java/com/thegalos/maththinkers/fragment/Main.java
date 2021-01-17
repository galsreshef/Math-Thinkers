package com.thegalos.maththinkers.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.thegalos.maththinkers.R;

import es.dmoral.toasty.Toasty;

public class Main extends Fragment {

    private MediaPlayer mediaPlayer;
    private Boolean music;
    private SharedPreferences sp;
    private int length;
    boolean addition = false;
    boolean subtraction = false;
    boolean multiply = false;
    boolean division = false;
    boolean changeName = false;
    boolean flashing;
    boolean vibration;
    boolean help;
    boolean tempMusic;
    Context context;
    boolean night;
    FirebaseUser user;

    public Main() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getContext();
        init(view);

    }

    private void init(View view) {
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        music = sp.getBoolean("Music",true);

        night = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        user = FirebaseAuth.getInstance().getCurrentUser();

        ImageView animation = view.findViewById(R.id.mathAnim);

        mediaPlayer = MediaPlayer.create(context,R.raw.main);
        mediaPlayer.setLooping(true);

        if (music)
            mediaPlayer.start();

        AnimationDrawable animationDrawable = (AnimationDrawable)animation.getDrawable();
        animationDrawable.start();

        view.findViewById(R.id.btnQuickMath).setOnClickListener(v -> {
            String userName = sp.getString("userName", "User Name");
            if (userName.equals("User Name") && !sp.getBoolean("askedName",false)) {
                sp.edit().putBoolean("askedName", true).apply();
                showNameSelection("quickMath");
            } else
                startGame("quickMath");
        });

        view.findViewById(R.id.btnTimeAttack).setOnClickListener(v -> {
            String userName = sp.getString("userName", "User Name");
            if (userName.equals("User Name") && !sp.getBoolean("askedName",false)) {
                sp.edit().putBoolean("askedName", true).apply();
                showNameSelection("timeAttack");
            } else
                startGame("timeAttack");
        });

        view.findViewById(R.id.btnAdvanced).setOnClickListener(v -> {
            String userName = sp.getString("userName", "User Name");
            if (userName.equals("User Name") && !sp.getBoolean("askedName",false)) {
                sp.edit().putBoolean("askedName", true).apply();
                showNameSelection("advanced");
            } else
                startGame("advanced");
        });

        view.findViewById(R.id.ivHighScore).setOnClickListener(v -> {
            if (getFragmentManager() != null)
                getFragmentManager().beginTransaction()
                        .replace(R.id.flAppFragment, new Pager2(), "Pager").addToBackStack("Pager").commit();
        });

        view.findViewById(R.id.ivAbout).setOnClickListener(v -> {
            if (getFragmentManager() != null)
                getFragmentManager().beginTransaction()
                        .replace(R.id.flAppFragment, new About(), "About").addToBackStack("About").commit();
        });

        view.findViewById(R.id.ivSettings).setOnClickListener(v -> showSettings());
    }

    private void showNameSelection(String gameName) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(DialogInterface::dismiss);
        dialog.setContentView(R.layout.dialog_settings);
        dialog.findViewById(R.id.switchVibration).setVisibility(View.GONE);
        dialog.findViewById(R.id.switchFlashing).setVisibility(View.GONE);
        dialog.findViewById(R.id.switchHelp).setVisibility(View.GONE);
        dialog.findViewById(R.id.switchMusic).setVisibility(View.GONE);
        dialog.findViewById(R.id.ivEdit).setVisibility(View.GONE);
        final EditText etName = dialog.findViewById(R.id.etName);
        etName.setEnabled(true);
        etName.setText("");

        etName.setHint(sp.getString("userName", "User Name"));

        dialog.findViewById(R.id.tvSave).setOnClickListener(v1 -> {
            if (etName.getText().toString().length() != 0) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(etName.getText().toString())
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("firebase", "User profile updated.");
                                sp.edit().putString("userName", etName.getText().toString()).apply();
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).setValue(user.getDisplayName());
                                dialog.dismiss();
                                startGame(gameName);
                                Toasty.success(context, getResources().getString(R.string.setting_saved), Toast.LENGTH_SHORT, true).show();
                            }
                        });

            } else {
                Toasty.error(context, getResources().getString(R.string.must_provide_name), Toast.LENGTH_SHORT, true).show();
                dialog.dismiss();
                //TODO Save Name In Firebase and update UI with toasty
            }
        });

        dialog.show();




    }


    public void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying() && music) {
            mediaPlayer.pause();
            length = mediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying() && music) {
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void startGame(String gameSelected) {
        sp.edit().putString("gameSelected", gameSelected).apply();
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnCancelListener(DialogInterface::dismiss);
        dialog.setContentView(R.layout.dialog_select_operations);
        ImageView ivPlusSign = dialog.findViewById(R.id.ivPlusSign);
        ImageView ivMinusSign = dialog.findViewById(R.id.ivMinusSign);
        ImageView ivMultiplySign = dialog.findViewById(R.id.ivMultiplySign);
        ImageView ivDivisionSign = dialog.findViewById(R.id.ivDivisionSign);
        TextView tvGameSelected = dialog.findViewById(R.id.tvGameSelected);
        TextView tvPlay = dialog.findViewById(R.id.tvPlay);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        tvGameSelected.setText(gameSelected);
        DrawableCompat.setTint(ivPlusSign.getDrawable(), ContextCompat.getColor(context, android.R.color.darker_gray));
        DrawableCompat.setTint(ivMinusSign.getDrawable(), ContextCompat.getColor(context, android.R.color.darker_gray));
        DrawableCompat.setTint(ivMultiplySign.getDrawable(), ContextCompat.getColor(context, android.R.color.darker_gray));
        DrawableCompat.setTint(ivDivisionSign.getDrawable(), ContextCompat.getColor(context, android.R.color.darker_gray));
        //////////////////////////////////////////////////////////////////////
        addition = sp.getBoolean("Addition", false);
        subtraction = sp.getBoolean("Subtraction", false);
        multiply = sp.getBoolean("Multiply", false);
        division = sp.getBoolean("Division", false);
        if (addition) {
            if (night)
                DrawableCompat.setTint(ivPlusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
            else
                DrawableCompat.setTint(ivPlusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
        }

        if (subtraction) {
            if (night)
                DrawableCompat.setTint(ivMinusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
            else
                DrawableCompat.setTint(ivMinusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
        }

        if (multiply) {
            if (night)
                DrawableCompat.setTint(ivMultiplySign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
            else
                DrawableCompat.setTint(ivMultiplySign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
        }

        if (division) {
            if (night)
                DrawableCompat.setTint(ivDivisionSign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
            else
                DrawableCompat.setTint(ivDivisionSign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
        }


        //////////////////////////////////////////////////////////////////////

        ivPlusSign.setOnClickListener(v -> {
            if (addition) {
                addition = false;
                DrawableCompat.setTint(ivPlusSign.getDrawable(), ContextCompat.getColor(context, android.R.color.darker_gray));
            } else {
                addition = true;
                if (night)
                    DrawableCompat.setTint(ivPlusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
                else
                    DrawableCompat.setTint(ivPlusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
            }
        });

        ivMinusSign.setOnClickListener(v -> {
            if (subtraction) {
                subtraction = false;
                DrawableCompat.setTint(ivMinusSign.getDrawable(), ContextCompat.getColor(requireContext(),android.R.color.darker_gray));

            } else {
                subtraction = true;
                if (night)
                    DrawableCompat.setTint(ivMinusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
                else
                    DrawableCompat.setTint(ivMinusSign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
            }
        });

        ivMultiplySign.setOnClickListener(v -> {
            if (multiply) {
                multiply = false;
                DrawableCompat.setTint(ivMultiplySign.getDrawable(), ContextCompat.getColor(requireContext(),android.R.color.darker_gray));

            } else {
                multiply = true;
                if (night)
                    DrawableCompat.setTint(ivMultiplySign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
                else
                    DrawableCompat.setTint(ivMultiplySign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));                   }
        });

        ivDivisionSign.setOnClickListener(v -> {
            if (division) {
                division = false;
                DrawableCompat.setTint(ivDivisionSign.getDrawable(), ContextCompat.getColor(requireContext(),android.R.color.darker_gray));

            } else {
                division = true;
                if (night)
                    DrawableCompat.setTint(ivDivisionSign.getDrawable(), ContextCompat.getColor(context, R.color.button_night));
                else
                    DrawableCompat.setTint(ivDivisionSign.getDrawable(), ContextCompat.getColor(context, R.color.button_day));
            }
        });


        tvCancel.setOnClickListener(view -> dialog.dismiss());

        tvPlay.setOnClickListener(v -> {
            sp.edit().putBoolean("Addition", addition).apply();
            sp.edit().putBoolean("Subtraction", subtraction).apply();
            sp.edit().putBoolean("Multiply", multiply).apply();
            sp.edit().putBoolean("Division", division).apply();
            if (!addition && !subtraction && !multiply && !division)
                Toasty.error(context, "You must select at least one operator", Toast.LENGTH_SHORT, true).show();
            else {
                dialog.dismiss();
                if (getFragmentManager() != null)
                    getFragmentManager().beginTransaction()
                            .replace(R.id.flAppFragment, new Loading(), "Loading").commit();

            }

        });
        dialog.show();
    }

    private void showSettings() {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setOnCancelListener(DialogInterface::dismiss);
        dialog.setContentView(R.layout.dialog_settings);

        final EditText etName = dialog.findViewById(R.id.etName);
        final ImageView ivEdit = dialog.findViewById(R.id.ivEdit);
        final SwitchMaterial  switchVibration = dialog.findViewById(R.id.switchVibration);
        final SwitchMaterial switchFlashing = dialog.findViewById(R.id.switchFlashing);
        final SwitchMaterial switchHelp = dialog.findViewById(R.id.switchHelp);
        final SwitchMaterial switchMusic = dialog.findViewById(R.id.switchMusic);
        final TextView tvSave = dialog.findViewById(R.id.tvSave);
        final TextView tvCancel = dialog.findViewById(R.id.tvCancel);


        flashing = sp.getBoolean("FlashText", true);
        vibration = sp.getBoolean("Vibrate",true);
        help = sp.getBoolean("runBefore_quickMath", false)
                && sp.getBoolean("runBefore_timeAttack", false)
                && sp.getBoolean("runBefore_advanced", false);

        tempMusic = music;
        switchFlashing.setChecked(flashing);
        switchVibration.setChecked(vibration);
        switchHelp.setChecked(help);
        switchMusic.setChecked(music);


        etName.setText(sp.getString("userName","User Name"));
        changeName = false;


        ivEdit.setOnClickListener(v12 -> {
            etName.setText("");
            changeName = true;
            etName.setEnabled(true);
            etName.requestFocus();
        });

        switchVibration.setOnCheckedChangeListener((buttonView, isChecked) -> vibration = isChecked);

        switchFlashing.setOnCheckedChangeListener((buttonView, isChecked) -> flashing = isChecked);

        switchHelp.setOnCheckedChangeListener((buttonView, isChecked) -> help = isChecked);

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> tempMusic = isChecked);

        tvCancel.setOnClickListener(v1 -> dialog.dismiss());

        tvSave.setOnClickListener(v1 -> {

            if (tempMusic != music) {
                music = tempMusic;
                sp.edit().putBoolean("Music", music).apply();
                if (music && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
            sp.edit().putBoolean("Vibrate", vibration).putBoolean("FlashText", flashing)
                    .putBoolean("runBefore_quickMath", help)
                    .putBoolean("runBefore_timeAttack", help)
                    .putBoolean("runBefore_advanced", help)
                    .apply();


            if (changeName) {
                if (!sp.getString("userName", "User Name").equals(etName.getText().toString()) && etName.getText().toString().length() != 0) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(etName.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("firebase", "User profile updated.");
                                    sp.edit().putString("userName", etName.getText().toString()).apply();
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).setValue(user.getDisplayName());
                                    dialog.dismiss();
                                    Toasty.success(context, getResources().getString(R.string.setting_saved), Toast.LENGTH_SHORT, true).show();
                                }
                            });

                } else {
                    Toasty.error(context, getResources().getString(R.string.could_not_save_new_name), Toast.LENGTH_SHORT, true).show();
                    dialog.dismiss();
                    //TODO Save Name In Firebase and update UI with toasty
                }
            } else {
                dialog.dismiss();
                Toasty.success(context, getResources().getString(R.string.setting_saved), Toast.LENGTH_SHORT, true).show();
            }


        });

        dialog.show();
    }

}