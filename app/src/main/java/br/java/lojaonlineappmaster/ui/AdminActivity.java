package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import br.java.lojaonlineappmaster.fragment.OfertasFragmento;
import br.java.lojaonlineappmaster.fragment.ProdutosFragmento;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.fragment.VendedorFragmento;

public class AdminActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private BottomNavigationView bottomNavigationView;
    private TextView FragmentoTitulo;
    private FirebaseAuth mAuth;
    private RelativeLayout CarrinhoPersonlizadoContainer;
    private TextView PaginaTitulo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.Admin_ToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Painel do Admin");

        FragmentoTitulo = (TextView) findViewById(R.id.FragmentoTitulo);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.Bottom_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(naveListener);

        //
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new ProdutosFragmento()).commit();
        FragmentoTitulo.setText("Todos os Produtos");
    }

    @Override
    protected void onStart() {
        super.onStart();

        ExibirIconeDoCarrinho();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener naveListener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment FragmentoSelecionado = null;
                int id = item.getItemId();

                if (id == R.id.ProdutoID) {
                    FragmentoSelecionado = new ProdutosFragmento();
                    FragmentoTitulo.setText("Todos os produtos");

                } else if (id == R.id.OfertasID) {
                    FragmentoSelecionado = new OfertasFragmento();
                    FragmentoTitulo.setText("todas as Ofertas");

                } else if (id == R.id.VendedorID) {
                    FragmentoSelecionado = new VendedorFragmento();
                    FragmentoTitulo.setText("Todos os vendedores");
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, FragmentoSelecionado).commit();

                return true;
            }
        };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.adminSairId) {
            VerificarLogout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void VerificarLogout() {
        AlertDialog.Builder verficarAlerta = new AlertDialog.Builder(AdminActivity.this);
        verficarAlerta.setMessage("Deseja sair?")
                .setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAuth.signOut();
                Intent intent = new Intent(AdminActivity.this, EntrarActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alerta = verficarAlerta.create();
        alerta.setTitle("Sair");
        alerta.show();
    }

    private  void ExibirIconeDoCarrinho() {
        // Toolbar & Carrinho icone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);

        CarrinhoPersonlizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        PaginaTitulo.setVisibility(View.GONE);
        CarrinhoPersonlizadoContainer.setVisibility(View.GONE);
    }
}