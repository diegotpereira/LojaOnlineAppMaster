package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.java.lojaonlineappmaster.Adapter.MeuAdapter_Recycler_View;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.FavoritaClasse;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPessoa_nome;
    private CircleImageView imagem;

    private FirebaseAuth mAuth;
    private FirebaseUser atualUsuario;

    private String UsuarioId;
    private RecyclerView.Adapter meuAdapter;
    private RelativeLayout CarrinhoPersonlizadoContainer;
    private TextView PaginaTitulo;
    private TextView CarrinhoPersonalizadoNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        mAuth = FirebaseAuth.getInstance();
        atualUsuario = mAuth.getCurrentUser();
        UsuarioId = atualUsuario.getUid();

        mToolbar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // definir o Navigation Viewer e obter seus dados
        DefinirNavegacao();
    }

    @Override
    protected void onStart() {
        super.onStart();

        ExibirIconeDoCarrinho();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.Home) {
            startActivity(new Intent(FavoritosActivity.this, MainActivity.class));
        } else if (id == R.id.Perfil) {
            startActivity(new Intent(FavoritosActivity.this, UsuarioPerfilActivity.class));
        } else if (id == R.id.MeusPedidos) {
            startActivity(new Intent(FavoritosActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Carrinho) {
            startActivity(new Intent(FavoritosActivity.this, CarrinhoActivity.class));
        } else if (id == R.id.Frutas) {
            startActivity(new Intent(FavoritosActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Vegetais) {
            startActivity(new Intent(FavoritosActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Carnes) {
            startActivity(new Intent(FavoritosActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Eletronicos) {
            startActivity(new Intent(FavoritosActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Sair) {
            VerificarLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void VerificarLogout() {
        AlertDialog.Builder verifiqueAlerta = new AlertDialog.Builder(FavoritosActivity.this);
        verifiqueAlerta.setMessage("Deseja sair?")
                .setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FavoritosActivity.this, EntrarActivity.class);
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

    private void DefinirNavegacao() {
        View mNavigationView;
        navigationView = findViewById(R.id.navegation_view2);
        drawerLayout = findViewById(R.id.drawer2);
        navigationView.setNavigationItemSelectedListener(this);

        mNavigationView = navigationView.getHeaderView(0);
        mPessoa_nome = mNavigationView.findViewById(R.id.pessoanome);
        imagem = mNavigationView.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        ObterDadosDoCabecalhoDeNavegacao();
    }

    private void RecuperarFavoritos() {
        LinearLayout meuLayout = (LinearLayout) findViewById(R.id.recyclerViewlayout);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.favorito_recycler_view, meuLayout, false);
        final RecyclerView rc = meuLayout.findViewById(R.id.recyclerView);

        GridLayoutManager mGridLayoutManager;
        mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rc.setLayoutManager(mGridLayoutManager);

        final List<FavoritaClasse> lista_de_favoritos = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favoritos")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    FavoritaClasse fav = new FavoritaClasse();
                    fav = ds.getValue(FavoritaClasse.class);
                    lista_de_favoritos.add(fav);
                }
                meuAdapter = new MeuAdapter_Recycler_View(lista_de_favoritos);
                rc.setAdapter(meuAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    private void ObterDadosDoCabecalhoDeNavegacao() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(UsuarioId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("Nome").getValue().toString();
                    String foto = snapshot.child("Imagem").getValue().toString();

                    if (foto.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(imagem);
                    } else {
                        Picasso.get().load(foto).placeholder(R.drawable.profile).into(imagem);
                        mPessoa_nome.setText(nome);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
        RecuperarFavoritos();
    }

    private  void ExibirIconeDoCarrinho() {
        // Toolbar & Carrinho icone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);
        actionBar.setCustomView(view);

        CarrinhoPersonlizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        CarrinhoPersonalizadoNumero = (TextView)findViewById(R.id.CarrinhoPersonalizadoNumero);

        PaginaTitulo.setText("Favoritos");
        DefinirNumeroItensIconeCarrinho();

        CarrinhoPersonlizadoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavoritosActivity.this, CarrinhoActivity.class));
            }
        });
    }

    private void DefinirNumeroItensIconeCarrinho() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("carrinho").child(UsuarioId);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getChildrenCount() == 1) {
                        CarrinhoPersonalizadoNumero.setVisibility(View.GONE);
                    } else {
                        CarrinhoPersonalizadoNumero.setVisibility(View.VISIBLE);
                        CarrinhoPersonalizadoNumero.setText(String.valueOf(snapshot.getChildrenCount() - 1));
                    }
                } else {
                    CarrinhoPersonalizadoNumero.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    public void verificarPrecoTotalZero(){
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("carrinho").child(UsuarioId);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("carrinho").child(UsuarioId).child("precoTotal").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

}