package com.stefan.reserv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.stefan.reserv.Add.QuickAddGrade;
import com.stefan.reserv.Add.QuickAddGenre;
import com.stefan.reserv.Add.QuickAddBook;
import com.stefan.reserv.Fragments.FavoritesFragment;
import com.stefan.reserv.Fragments.HomeFragment;
import com.stefan.reserv.Fragments.ProfileFragment;
import com.stefan.reserv.Model.Book;
import com.stefan.reserv.Model.User;
import com.stefan.reserv.Utils.PreferenceUtils;

import java.io.ByteArrayInputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CardView logout_btn;
    private TextView user_email, user_name;
    private User current_user;
    private ImageView profile_pic;
    private Book selected_book;
    private byte[] pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);

        logout_btn = findViewById(R.id.logout_button);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.user_name);
        user_email = headerView.findViewById(R.id.header_email);
        profile_pic = headerView.findViewById(R.id.nav_header_profile_pic);
        if (getIntent().hasExtra("login_current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("login_current_user");
        } else if (getIntent().hasExtra("current_user")) {
            Bundle bundle = getIntent().getExtras();
            current_user = bundle.getParcelable("current_user");
        } else {
            String id = PreferenceUtils.getId(this);
            String username = PreferenceUtils.getUsername(this);
            String email = PreferenceUtils.getEmail(this);
            String pass = PreferenceUtils.getPassword(this);
            String role = PreferenceUtils.getRole(this);
            String grade = PreferenceUtils.getGradeId(this);
            if (PreferenceUtils.getPic(this) != null) {
                pic = PreferenceUtils.getPic(this);
            }
            if (pic == null) {
                current_user = new User(id, username, email, pass, role, grade);
            } else {
                current_user = new User(id, username, email, pass, role, pic, grade);
            }
        }
        user_email.setText(current_user.getEmail());
        user_name.setText(current_user.getUsername());
        if (current_user.getProfile_pic() != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(current_user.getProfile_pic());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            profile_pic.setImageBitmap(theImage);
        }
        if (getIntent().hasExtra("movie")) {
            Bundle bundle = getIntent().getExtras();
            selected_book = bundle.getParcelable("movie");
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("current_user", current_user);
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveEmail("", MainActivity.this);
                PreferenceUtils.savePassword("", MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                current_user = null;
                finish();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu m) {
        Menu menu = navigationView.getMenu();
        MenuItem addMovie = menu.findItem(R.id.nav_add_movie);
        MenuItem addCinema = menu.findItem(R.id.nav_add_cinema);
        MenuItem addGenre = menu.findItem(R.id.nav_add_genre);
        if (current_user != null) {
            if (!current_user.getRole().equals("admin")) {
                addMovie.setVisible(false);
                addCinema.setVisible(false);
                addGenre.setVisible(false);
            } else {
                addMovie.setVisible(true);
                addCinema.setVisible(true);
                addGenre.setVisible(true);
            }
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
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                favoritesFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, favoritesFragment).commit();
                break;
            case R.id.nav_movie:
                Intent i = new Intent(this, BookList.class);
                i.putExtra("current_user", current_user);
                startActivity(i);
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();
                break;
            case R.id.nav_add_movie:
                startActivity(new Intent(MainActivity.this, QuickAddBook.class));
                break;
            case R.id.nav_add_cinema:
                startActivity(new Intent(MainActivity.this, QuickAddGrade.class));
            case R.id.nav_add_genre:
                startActivity(new Intent(MainActivity.this, QuickAddGenre.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}