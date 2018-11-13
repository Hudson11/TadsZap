package com.example.hudson.tadszap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hudson.tadszap.Adapters.FragmentAdapter;
import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {

    static String ANONIMO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        *   IMPLEMENTAÇÃO DO ABAFRAGMENT
        * */
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        SharedPreferences prefs = getSharedPreferences("PREFS_NAME", MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AuthUI.getInstance().signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
