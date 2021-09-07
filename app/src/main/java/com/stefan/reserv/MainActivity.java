package com.stefan.reserv;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.stefan.reserv.Add.QuickAddCinema;
import com.stefan.reserv.Add.QuickAddGenre;
import com.stefan.reserv.Add.QuickAddMovie;
import com.stefan.reserv.Fragments.CinemaFragment;
import com.stefan.reserv.Fragments.HomeFragment;
import com.stefan.reserv.Fragments.MovieFragment;
import com.stefan.reserv.Fragments.ProfileFragment;
import com.stefan.reserv.Model.Movie;
import com.stefan.reserv.Model.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CardView logout_btn;
    private TextView user_email;
    private User current_user;
    private Movie selected_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int intentFragment = getIntent().getExtras().getInt("frgToLoad");
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        logout_btn = findViewById(R.id.logout_button);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        user_email = headerView.findViewById(R.id.header_email);
        if (getIntent().hasExtra("login_current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("login_current_user");
            user_email.setText(current_user.getEmail());
        }
        if (getIntent().hasExtra("movie")) {
            Bundle bundle = getIntent().getExtras();
            selected_movie = bundle.getParcelable("movie");
        }
        if (getIntent().hasExtra("current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("current_user");
            user_email.setText(current_user.getEmail());
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            if (intentFragment == 1) {
                bundle.putParcelable("movie", selected_movie);
                bundle.putParcelable("current_user", current_user);
                MovieFragment movieFragment = new MovieFragment();
                movieFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, movieFragment).commit();
                navigationView.setCheckedItem(R.id.nav_movie);
            } else {
                bundle.putParcelable("current_user", current_user);
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            }
        }
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                current_user = null;
                finish();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menun) {
        Menu menu = navigationView.getMenu();
        MenuItem addMovie = menu.findItem(R.id.nav_add_movie);
        MenuItem addCinema = menu.findItem(R.id.nav_add_cinema);
        MenuItem addGenre = menu.findItem(R.id.nav_add_genre);
        if (!current_user.getRole().equals("admin")) {
            addMovie.setVisible(false);
            addCinema.setVisible(false);
            addGenre.setVisible(false);
        } else {
            addMovie.setVisible(true);
            addCinema.setVisible(true);
            addGenre.setVisible(true);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("current_user", current_user);
        switch (item.getItemId()) {
            case R.id.nav_home:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                break;
            case R.id.nav_cinema:
                CinemaFragment cinemaFragment = new CinemaFragment();
                cinemaFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, cinemaFragment).commit();
                break;
            case R.id.nav_movie:
                Intent i = new Intent(this, MovieList.class);
                i.putExtra("current_user", current_user);
                startActivity(i);
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();
                break;
            case R.id.nav_add_movie:
                startActivity(new Intent(MainActivity.this, QuickAddMovie.class));
                break;
            case R.id.nav_add_cinema:
                startActivity(new Intent(MainActivity.this, QuickAddCinema.class));
            case R.id.nav_add_genre:
                startActivity(new Intent(MainActivity.this, QuickAddGenre.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}