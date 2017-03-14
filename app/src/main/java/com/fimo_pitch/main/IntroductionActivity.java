package com.fimo_pitch.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fimo_pitch.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroductionActivity extends AppIntro {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        if(sharedPreferences !=null)
        {
            // neu đã xem giới thiệu
            if(sharedPreferences.getBoolean("seen",false))
            {
                Intent intent = new Intent(IntroductionActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            else
            {
                addSlide(AppIntroFragment.newInstance("Tìm kiếm sân bóng qua bản đồ", "Tìm kiếm sân bóng nhanh, hiệu quả",
                        R.drawable.img_demo2,getResources().getColor(R.color.colorPrimary)));
                addSlide(AppIntroFragment.newInstance("Tìm kiếm sân bóng theo danh sách ", "Tìm kiếm sân bóng nhanh, hiệu quả",
                        R.drawable.intro_1,getResources().getColor(R.color.colorPrimary)));
                addSlide(AppIntroFragment.newInstance("Bảng tin giao hữu, tìm đối", "Giao lưu, đăng tin tức và tìm đối thủ tiện lợi",
                        R.drawable.intro_2,getResources().getColor(R.color.colorPrimary)));
                addSlide(AppIntroFragment.newInstance("Quản lý chi tiết", "Bảng giá, thông tin liên hệ....",
                        R.drawable.img_demo3,getResources().getColor(R.color.colorPrimary)));
                showSkipButton(true);
            }
        }

    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        if(sharedPreferences !=null)
        {
            sharedPreferences.edit().putBoolean("seen",true).commit();
        }
        Intent intent = new Intent(IntroductionActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if(sharedPreferences !=null)
        {
            sharedPreferences.edit().putBoolean("seen",true).commit();
        }
        Intent intent = new Intent(IntroductionActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
