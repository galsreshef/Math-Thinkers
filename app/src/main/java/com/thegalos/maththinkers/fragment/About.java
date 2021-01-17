package com.thegalos.maththinkers.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thegalos.maththinkers.R;

public class About extends Fragment {

    public About() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView ivAnimationKid = view.findViewById(R.id.ivAnimationKid);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivAnimationKid.getDrawable();
        animationDrawable.start();

        view.findViewById(R.id.ivBack).setOnClickListener(v -> requireFragmentManager().popBackStack());

        view.findViewById(R.id.ivLinkedGal).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.linkedin.com/in/gal-reshef-s-computer-science-software-developer/"))));

        view.findViewById(R.id.ivGithub).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/galsreshef"))));

        view.findViewById(R.id.constraintLayoutPlayStore).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=Gal+Reshef"))));


    }
}
