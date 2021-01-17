package com.thegalos.maththinkers.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thegalos.maththinkers.R;
import com.thegalos.maththinkers.adapter.ViewPagerAdapter;

public class Pager2 extends Fragment {

    ViewPager2 viewPager;
    public Pager2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView ivHighScores = view.findViewById(R.id.ivHighScores);
        final AnimationDrawable ivLogoDrawable = (AnimationDrawable) ivHighScores.getDrawable();
        ivLogoDrawable.start();

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(requireActivity()));
        viewPager.setPageTransformer(null);
        viewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("")
        ).attach();
    }
}
