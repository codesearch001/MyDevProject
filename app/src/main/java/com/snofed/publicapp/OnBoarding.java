package com.snofed.publicapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class OnBoarding extends AppCompatActivity {

    //Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SlideAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStarted, next_btn, done_btn,skip_btn;
    Animation animation;
    int currentPos;
    private int dotsCount;
  //  SharePreferenceProvider pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_on_boarding);
        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        next_btn = findViewById(R.id.next_btn);
        done_btn = findViewById(R.id.done_btn);
        skip_btn = findViewById(R.id.skip_btn);

        //Call adapter
        sliderAdapter = new SlideAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //Dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

    }



    public void Skip(View view) {

        startActivity(new Intent(this, HomeDashBoardActivity.class));
        finishAffinity();
    }
    public void DONE(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }



    public void Next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {

        dots = new TextView[5];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(getResources().getColor(R.color.white));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.highlight_text_color));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;
//            for (int i = 0; i < dotsCount; i++)
//                dots[i].setTextColor(Color.WHITE);
//
//            dots[position].setTextColor(getResources().getColor(R.color.app_color1));
            if (position == 0) {
                skip_btn.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                done_btn.setVisibility(View.INVISIBLE);

                //letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 1) {

                // animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_anim);
                //letsGetStarted.setAnimation(animation);
                //letsGetStarted.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                skip_btn.setVisibility(View.VISIBLE);
                done_btn.setVisibility(View.INVISIBLE);

            }else if (position == 2) {

                // animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_anim);
               // letsGetStarted.setAnimation(animation);
                //letsGetStarted.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                skip_btn.setVisibility(View.VISIBLE);
                done_btn.setVisibility(View.INVISIBLE);

            }else if (position == 3) {

                // animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_anim);
                //letsGetStarted.setAnimation(animation);
                //letsGetStarted.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.VISIBLE);
                skip_btn.setVisibility(View.VISIBLE);
                done_btn.setVisibility(View.INVISIBLE);
            }else if (position == 4) {

                // animation = AnimationUtils.loadAnimation(OnBoarding.this, R.anim.bottom_anim);
               // letsGetStarted.setAnimation(animation);
               // letsGetStarted.setVisibility(View.VISIBLE);
                next_btn.setVisibility(View.INVISIBLE);
                skip_btn.setVisibility(View.VISIBLE);
                done_btn.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
