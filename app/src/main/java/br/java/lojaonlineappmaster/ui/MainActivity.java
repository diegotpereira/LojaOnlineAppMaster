package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import br.java.lojaonlineappmaster.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private CircleImageView image;
    private TextView mpessoa_nome;
    private NavigationView navigationView;
    private ViewPager pager;
    private View mNavigationView;

    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;
    private ImageView CarrinhoPersonalizadoIcone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mNavigationView = navigationView.getHeaderView(0);

        drawerLayout = findViewById(R.id.drawer);

        Toolbar mToolBar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void exibirIconeCarrinho() {

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CustomCartIconContainer);
    }
}