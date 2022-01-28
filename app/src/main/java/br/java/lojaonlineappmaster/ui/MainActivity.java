package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
    protected void onStart() {
        super.onStart();

        // Recuperar dados do usuário da visualização de cabeçalho
        Dados_cabecalho_visualizacao_navegacao();

        // Recuperar Favoritos
        Recuperar_favoritos();

        // Primeira visualização
        Recuperar_eletronicos();

        // Segunda visualização
        Recuperar_frutas();

        // Terceira visualização
        Recuperar_Carnes();

        // Quarta visualização
        Recuperar_vegetais();

        // OFERTAS
        Recuperar_ofertas();

        // Ícone de atualização do carrinho
        exibirIconeCarrinho();

        // para verificar se o preço total é zero ou não
        verificarPrecoTotalZero();
    }
    public void Dados_cabecalho_visualizacao_navegacao() {}

    public void Recuperar_favoritos() {}

    public void Recuperar_eletronicos() {}

    public void Recuperar_frutas() {}

    public void Recuperar_Carnes() {}

    public void Recuperar_vegetais() {}

    public void Recuperar_ofertas() {}

    public void exibirIconeCarrinho() {

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CustomCartIconContainer);
    }

    public void verificarPrecoTotalZero(){}

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Perfil) {
            startActivity(new Intent(MainActivity.this, UsuarioPerfilActivity.class));
        } else if (id == R.id.Logout) {
            VerificarLogout();

        } else if (id == R.id.eletronicos) {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Eletronicos");
            startActivity(intent);
        }
        return false;
    }
    private void VerificarLogout() {
        AlertDialog.Builder verifiqueAlerta = new AlertDialog.Builder(MainActivity.this);
        verifiqueAlerta.setMessage("Deseja sair?")
                .setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, EntrarActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = verifiqueAlerta.create();
        alert.setTitle("Sair");
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}