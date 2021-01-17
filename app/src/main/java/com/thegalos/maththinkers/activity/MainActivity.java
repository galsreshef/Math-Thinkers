package com.thegalos.maththinkers.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.thegalos.maththinkers.R;
import com.thegalos.maththinkers.fragment.Main;
import com.thegalos.maththinkers.fragment.Splash;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FragmentManager fm;
    Handler handler;
    Runnable runnable;
    SharedPreferences sp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String savedLang = sp.getString("lang", "en");
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();
        if (!savedLang.equals(currentLanguage)) {
            setLocale(savedLang);
        }
        setContentView(R.layout.activity_main);

        MobileAds.initialize(MainActivity.this, initializationStatus -> {
        });
        boolean runBefore = sp.getBoolean("runBefore", false);
        mAuth = FirebaseAuth.getInstance();
        fm = getSupportFragmentManager();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("fireAuth", "signInAnonymously:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && runBefore) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("First_played").setValue(ServerValue.TIMESTAMP);
                            sp.edit().putBoolean("runBefore", true).apply();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("fireAuth", "signInAnonymously:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                    if (savedInstanceState != null) {
                        Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, "Fragment");
                        if (fragment != null)
                            fm.beginTransaction()
                                    .replace(R.id.flAppFragment, fragment, "Main").commit();

                    } else {
                        fm.beginTransaction()
                                .replace(R.id.flAppFragment, new Splash(), "Splash").commit();
                        handler = new Handler(Looper.getMainLooper());
                        runnable = () -> fm.beginTransaction()
                                .replace(R.id.flAppFragment, new Main(), "Main").commit();
                        handler.postDelayed(runnable, 2500);

                    }
                });



    }

    private void setLocale(String language) {
        sp.edit().putString("lang", language).apply();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(runnable);
        sp.edit().remove("askedName").apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("bundle state" , "Activity saved to bundle");
        Fragment fragment;
        if (getSupportFragmentManager().findFragmentByTag("Main") != null)
            fragment = getSupportFragmentManager().findFragmentByTag("Main");
        else
            fragment = getSupportFragmentManager().findFragmentByTag("Splash");

        assert fragment != null;
        getSupportFragmentManager().putFragment(outState,"Fragment", fragment);
    }
}