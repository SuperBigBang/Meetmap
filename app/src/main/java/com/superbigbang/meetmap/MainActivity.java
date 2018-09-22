package com.superbigbang.meetmap;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    public Jnavi jnavi;
    private GoogleMap mMap;
    private String token_auth_key;
    private String container;
    private String naviaddress;
    private String URLofCreatedNaviaddress;
    private Map<String, String> mapfromgson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        jnavi = new Jnavi(null, null);
        jnavi.loginuser(this, "12345", "email", "superbigbang+1@yandex.ru");

        if (savedInstanceState != null) {
            token_auth_key = savedInstanceState.getString("Auth_key");
            container = savedInstanceState.getString("CONTAINER");
            naviaddress = savedInstanceState.getString("NAVIADDRESS");
        } else {
        }
//Log.e("token", )
        //  Log.e("Map is not null?", Integer.toString(jnavi.out.size()));

           /* String get_text = jnavi.execute("GET", "https://staging-api.naviaddress.com/api/v1.5/Addresses/7495/5563").get();
            System.out.println(get_text);*/

        /*   String URL = "https://staging-api.naviaddress.com/api/v1.5/Addresses/7495/5563";
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Rest Response", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Rest Response", error.toString());
                        }
                    }
            );
            requestQueue.add(objectRequest);*/


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Auth_key", token_auth_key);
        outState.putString("CONTAINER", container);
        outState.putString("NAVIADDRESS", naviaddress);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_auth) {

            Snackbar.make(MainActivity.this.getCurrentFocus(), "Залогинились", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            if (jnavi.getOut() == null) {
                Log.e("null?", "is null");
            } else {
                if (token_auth_key == null) {
                    token_auth_key = jnavi.getOut().get("AUTH_TOKEN");
                    Log.e("Auth_key", token_auth_key);
                } else {
                    Log.e("token", "is not empty");
                }
            }

        } else if (id == R.id.nav_createnavaddress) {
            if (token_auth_key != null) {
                jnavi.createnaviaddress(this, "52.0320300", "113.5296947", "free", "ru", token_auth_key);
                Snackbar.make(MainActivity.this.getCurrentFocus(), "Создали точку, теперь нужно подтвердить", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                Snackbar.make(MainActivity.this.getCurrentFocus(), "Необходимо залогиниться",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        } else if (id == R.id.nav_acceptnaviaddress) {
            if (token_auth_key != null) {
                System.out.println(jnavi.getOut().size());
                container = jnavi.getOut().get("CONTAINER");
                naviaddress = jnavi.getOut().get("NAVIADDRESS");
                System.out.println(jnavi.getOut().toString());
                // System.out.println(naviaddress);
                if (naviaddress == null) {
                    Snackbar.make(MainActivity.this.getCurrentFocus(), "Сначала создайте адрес",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    StringBuilder stringBuilder = new StringBuilder("https://staging-api.naviaddress.com/api/v1.5/addresses/accept");
                    stringBuilder.append("/");
                    stringBuilder.append(container);
                    stringBuilder.append("/");
                    stringBuilder.append(naviaddress);
                    URLofCreatedNaviaddress = stringBuilder.toString();
                    jnavi.acceptnaviaddress(this, URLofCreatedNaviaddress, container, naviaddress, token_auth_key);
                    Snackbar.make(MainActivity.this.getCurrentFocus(), "Точка создана и подтверждена. Можно найти на карте (на https://staging.naviaddress.com/map)",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            } else {
                Snackbar.make(MainActivity.this.getCurrentFocus(), "Необходимо залогиниться, а затем создать адрес",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(52.0333333, 113.5000000);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Площадь г. Читы"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
