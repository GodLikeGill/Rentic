package com.group9.project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group9.project.R;
import com.group9.project.adapters.PageViewerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        PageViewerAdapter adapter = new PageViewerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.all_listings:
                    viewPager.setCurrentItem(0);
                    break;

                case R.id.my_listings:
                    viewPager.setCurrentItem(1);
                    break;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.all_listings);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.my_listings);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }
}