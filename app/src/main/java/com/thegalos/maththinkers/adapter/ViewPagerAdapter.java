package com.thegalos.maththinkers.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thegalos.maththinkers.fragment.score.ScoreAdvanced;
import com.thegalos.maththinkers.fragment.score.ScoreOwn;
import com.thegalos.maththinkers.fragment.score.ScoreQuick;
import com.thegalos.maththinkers.fragment.score.ScoreTimeAttack;


public class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(FragmentActivity fa) {
                super(fa);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
                switch (position) {
                        case 0:
                                return new ScoreOwn(); //ChildFragment1 at position 0
                        case 1:
                                return new ScoreTimeAttack(); //ChildFragment2 at position 1
                        case 2:
                                return new ScoreQuick(); //ChildFragment3 at position 2
                        case 3:
                                return new ScoreAdvanced(); //ChildFragment4 at position 3
                }
                return new ScoreOwn();
        }

        @Override
        public int getItemCount() {
                return 4;
        }

//
//        @Override
//        public Fragment getItem(int position) {
//                switch (position)
//                {
//                        case 0:
//                                return new HighScoreOwn(); //ChildFragment1 at position 0
//                        case 1:
//                                return new HighScoreTimeAttack(); //ChildFragment2 at position 1
//                        case 2:
//                                return new HighScoreQuick(); //ChildFragment3 at position 2
//                        case 3:
//                                return new HighScoreAdvanced(); //ChildFragment4 at position 3
//                }
//                return null; //does not happen
//        }
//        @Override
//        public int getCount() {
//                return 4;
//        }
}