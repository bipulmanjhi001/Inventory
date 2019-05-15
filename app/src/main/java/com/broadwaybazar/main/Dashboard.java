package com.broadwaybazar.main;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.broadwaybazar.R;
import com.broadwaybazar.drawer.Follow_Up;
import com.broadwaybazar.drawer.Order;
import com.broadwaybazar.drawer.Order_Deliver;
import com.broadwaybazar.drawer.Payment_Receive;
import com.broadwaybazar.drawer.Register;
import com.broadwaybazar.drawer.Search_Now;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Class fragmentClass;
    DrawerLayout drawer;
    Fragment fragment = null;
    private static final String SHARED_PREF_NAME = "Inventorypref";
    String name, email, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            token = bundle.getString("token");
            email = bundle.getString("email");
        } else {
            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            name = sp.getString("keyusername", "");
            token = sp.getString("keyid", "");
            email = sp.getString("email", "");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, new Register()).commit();
        setTitle("New Register");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            fragmentClass = Register.class;

        } else if (id == R.id.nav_gallery) {

            fragmentClass = Follow_Up.class;

        } else if (id == R.id.nav_slideshow) {

            fragmentClass = Search_Now.class;

        } else if (id == R.id.nav_tools) {

            fragmentClass = Order.class;

        } else if (id == R.id.nav_deliver) {

            fragmentClass = Order_Deliver.class;

        } else if (id == R.id.nav_receive) {

            fragmentClass = Payment_Receive.class;

        }
        /*else if (id == R.id.nav_logout) {
            Intent intent=new Intent(Dashboard.this,Login.class);
            startActivity(intent);
            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
        }*/

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, fragment).commit();
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
