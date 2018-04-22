package com.aijat.juggernaul;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static com.aijat.juggernaul.SettingsActivity.completedtasks;
import static com.aijat.juggernaul.SettingsActivity.duetasks;
import static com.aijat.juggernaul.SettingsActivity.importanttasks;
import static com.aijat.juggernaul.SettingsActivity.scheduledtasks;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handle color theme
        SharedPreferences sharedPrefs = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE);
        if (sharedPrefs.getBoolean("dark",true)) {
            MainActivity.this.setTheme(R.style.Dark);
        } else {
            MainActivity.this.setTheme(R.style.AppTheme);
        }


        // Handle "your most important tasks" grid
        if (sharedPrefs.getBoolean("importanttasks",true)) {
            importanttasks = true;
        } else {
            importanttasks = false;
        }

        // Handle "my scheduled tasks" grid
        if (sharedPrefs.getBoolean("scheduledtasks",true)) {
            scheduledtasks = true;
        } else {
            scheduledtasks = false;
        }

        // Handle "completed tasks" grid
        if (sharedPrefs.getBoolean("completedtasks",true)) {
            completedtasks = true;
        } else {
            completedtasks = false;
        }

        // handle "due this week" grid
        if (sharedPrefs.getBoolean("duetasks",true)) {
            duetasks = true;
        } else {
            duetasks = false;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Graph"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.getAdapter().notifyDataSetChanged();
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }


}
