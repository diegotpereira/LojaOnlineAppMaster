package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.fragment.MeuCarrinhoFragmento;
import de.hdodenhof.circleimageview.CircleImageView;

public class CarrinhoActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPessoa_nome;
    private CircleImageView mPessoa_imagem;
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        fa = this;
        mToolbar = (Toolbar) findViewById(R.id.carrinhoToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Meu Carrinho");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(
                R.id.CarrinhoFrame, new MeuCarrinhoFragmento()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        drawerLayout = (DrawerLayout) findViewById(R.id.carrinhoDrawer);
        navigationView = (NavigationView) findViewById(R.id.carrinhoNavigationViewer);

        // Navegação Cabeçalho
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        mPessoa_nome = view.findViewById(R.id.pessoanome);
        mPessoa_imagem = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        ObterDadosDoCabecalhoDeNavegacao();

        ExibirIconeDoCarrinho();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.Home){
            startActivity(new Intent(CarrinhoActivity.this,MainActivity.class));
        }
        if (id == R.id.Perfil) {
            startActivity(new Intent(CarrinhoActivity.this, UsuarioPerfilActivity.class));
        } else if (id == R.id.MeusPedidos) {
            startActivity(new Intent(CarrinhoActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Carrinho) {
            startActivity(new Intent(CarrinhoActivity.this, CarrinhoActivity.class));
        } else if (id == R.id.Frutas) {
            Intent intent = new Intent(CarrinhoActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Frutas");
            startActivity(intent);
        } else if (id == R.id.Vegetais) {
            Intent intent = new Intent(CarrinhoActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Vegetais");
            startActivity(intent);
        } else if (id == R.id.Carnes) {
            Intent intent = new Intent(CarrinhoActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Carnes");
            startActivity(intent);
        } else if (id == R.id.Eletronicos) {
            Intent intent = new Intent(CarrinhoActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Eletronicos");
            startActivity(intent);
        } else if (id == R.id.Sair) {
            VerificarLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void VerificarLogout() {
        AlertDialog.Builder verifiqueAlerta = new AlertDialog.Builder(CarrinhoActivity.this);
        verifiqueAlerta.setMessage("Deseja sair?")
                .setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CarrinhoActivity.this, EntrarActivity.class);
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

    private void ObterDadosDoCabecalhoDeNavegacao() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String AtualUsuario = mAuth.getCurrentUser().getUid();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(AtualUsuario);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Nome = snapshot.child("Nome").getValue().toString();
                    String Imagem = snapshot.child("Imagem").getValue().toString();
                    mPessoa_nome.setText(Nome);

                    if (Imagem.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(mPessoa_imagem);
                    } else {
                        Picasso.get().load(Imagem).placeholder(R.drawable.profile).into(mPessoa_imagem);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void ExibirIconeDoCarrinho() {
        // Toolbar & Carrinho icone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);
//        actionBar.setCustomView(view);

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        PaginaTitulo.setVisibility(View.GONE);
        CarrinhoPersonalizadoContainer.setVisibility(View.GONE);
    }
}