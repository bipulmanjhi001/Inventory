package com.broadwaybazar.main;


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.broadwaybazar.R;
import com.broadwaybazar.api.URL;
import com.broadwaybazar.drawer.Follow_Up;
import com.broadwaybazar.drawer.Order;
import com.broadwaybazar.drawer.Order_Deliver;
import com.broadwaybazar.drawer.Payment_Receive;
import com.broadwaybazar.drawer.Register;
import com.broadwaybazar.drawer.Search_Now;
import com.broadwaybazar.model.UpdateMeeDialog;
import com.broadwaybazar.model.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Class fragmentClass;
    DrawerLayout drawer;
    Fragment fragment = null;
    private static final String SHARED_PREF_NAME = "Inventorypref";
    String name, email, token,versionName;
    public static String PACKAGE_NAME;
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        final PackageManager packageManager = getApplicationContext().getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);
                PACKAGE_NAME = getApplicationContext().getPackageName();
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = null;
            }
        }

        UPDATE();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, new Register()).commit();
        setTitle("Add Customer");

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

    private void UPDATE() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_GETVERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject getversion = obj.getJSONObject("version");
                                String version_code = getversion.getString("version_code");
                                String version_name = getversion.getString("version_name");

                                if (version_name.equals(versionName)) {

                                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();

                                } else {

                                    UpdateMeeDialog updateMeeDialog = new UpdateMeeDialog();
                                    updateMeeDialog.showDialogAddRoute(Dashboard.this, PACKAGE_NAME);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
