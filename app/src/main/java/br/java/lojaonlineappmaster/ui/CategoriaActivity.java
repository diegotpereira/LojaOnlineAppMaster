package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.java.lojaonlineappmaster.Adapter.CategoriaProdutoInfoAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.CategoriaProdutoInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriaActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPessoa_Nome;
    private CircleImageView mPessoa_imagem;
    //-------------------------------------
    private String CategoriaNome;
    private RecyclerView recyclerView;
    private ArrayList<CategoriaProdutoInfo> CategoriaProdutos;
    private CategoriaProdutoInfoAdapter adapter;
    //--------------------------------------
    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsuario;
    private String UsuarioId;
    private CategoriaProdutoInfoAdapter.RecyclerViewClickListener listener;
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo, CarrinhoPersonalizadoNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser();
        UsuarioId = AtualUsuario.getUid();

        CategoriaNome = getIntent().getStringExtra("Categoria Nome");

        // ao clicar em qualquer produto (vá para ProductInfo Activity para mostrar suas informações)
        cliqueEmQualquerProduto();
    }

    @Override
    protected void onStart() {
        super.onStart();

        definirDadosCategoria();

        definirNavegacao();

        ExibirIconeDoCarrinho();

        verificarPrecoTotalZero();
    }

    private void cliqueEmQualquerProduto() {
        listener = new CategoriaProdutoInfoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                CategoriaProdutoInfo produto = CategoriaProdutos.get(position);

                Intent intent = new Intent(CategoriaActivity.this, ProdutoInfoActivity.class);
                intent.putExtra("Produto Nome", produto.getProdutoNome());
                intent.putExtra("Produto Preco", produto.getProdutoPreco());
                intent.putExtra("Produto Imagem", produto.getProdutoImagem());
                intent.putExtra("Produto Data Vencimento", produto.getProdutoDataVencimento());
                intent.putExtra("Produto EhFavorito", String.valueOf(produto.isEhFavorito()));
                intent.putExtra("Eh Oferecido", "Não");

                startActivity(intent);
            }
        };
    }


    private void definirDadosCategoria() {
        mToolbar = (Toolbar) findViewById(R.id.CategoriaTooBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.CategoriaRecycler);
        CategoriaProdutos = new ArrayList<>();

        adapter = new CategoriaProdutoInfoAdapter(CategoriaActivity.this, CategoriaProdutos, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoriaActivity.this));
        recyclerView.setAdapter(adapter);

        obterDadosProdutos();

        ExibirIconeDoCarrinho();

    }

    private void obterDadosProdutos() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("produto").child(CategoriaNome);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String ProdutoNome = dataSnapshot.getKey().toString();
                        final String ProdutoPreco = dataSnapshot.child("preco").getValue().toString();
                        final String ProdutoImagem = dataSnapshot.child("imagem").getValue().toString();
                        final String ProdutoDataVencimento = dataSnapshot.child("dataVencimento").getValue().toString();

                        // verficar favoritos
                        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference x = root.child("favoritos").child(UsuarioId).child(ProdutoNome);

                        ValueEventListener vvalueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    CategoriaProdutos.add(new CategoriaProdutoInfo(
                                            ProdutoImagem,
                                            ProdutoNome,
                                            ProdutoPreco,
                                            ProdutoDataVencimento,
                                            true));
                                } else {
                                    CategoriaProdutos.add(new CategoriaProdutoInfo(
                                            ProdutoImagem,
                                            ProdutoNome,
                                            ProdutoPreco,
                                            ProdutoDataVencimento,
                                            false));
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        };
                        x.addListenerForSingleValueEvent(vvalueEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void definirNavegacao() {
        drawerLayout = (DrawerLayout) findViewById(R.id.CategoriaDrawer);
        navigationView = (NavigationView) findViewById(R.id.CategoriaNavigation);

        // Navegação Cabeçalho
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        mPessoa_Nome = view.findViewById(R.id.pessoanome);
        mPessoa_imagem = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        ObterDadosDoCabecalhoDeNavegacao();
    }

    private void ObterDadosDoCabecalhoDeNavegacao() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(UsuarioId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Nome = snapshot.child("Nome").getValue().toString();
                    String Imagem = snapshot.child("Imagem").getValue().toString();
                    mPessoa_Nome.setText(Nome);

                    if (Imagem.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(mPessoa_imagem);
                    } else
                        Picasso.get().load(Imagem).placeholder(R.drawable.profile).into(mPessoa_imagem);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
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
            startActivity(new Intent(CategoriaActivity.this,MainActivity.class));
        }
        if (id == R.id.Perfil) {
            startActivity(new Intent(CategoriaActivity.this, UsuarioPerfilActivity.class));

        } else if (id == R.id.MeusPedidos) {
            startActivity(new Intent(CategoriaActivity.this, CategoriaActivity.class));

        } else if (id == R.id.Carrinho) {
            startActivity(new Intent(CategoriaActivity.this, CarrinhoActivity.class));

        } else if (id == R.id.Frutas) {
            CategoriaNome = "Frutas";
            definirDadosCategoria();

        } else if (id == R.id.Vegetais) {
            CategoriaNome = "Vegetais";
            definirDadosCategoria();

        } else if (id == R.id.Carnes) {
            CategoriaNome = "Carnes";
            definirDadosCategoria();

        } else if (id == R.id.Eletronicos) {
            CategoriaNome = "Eletronicos";
            definirDadosCategoria();
        }
        else if (id == R.id.Sair) {
            VerificarLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void VerificarLogout() {
        AlertDialog.Builder verifiqueAlerta = new AlertDialog.Builder(CategoriaActivity.this);
        verifiqueAlerta.setMessage("Deseja sair?")
                .setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CategoriaActivity.this, EntrarActivity.class);
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
    private void ExibirIconeDoCarrinho() {
        // Toolbar & Carrinho icone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);
        actionBar.setCustomView(view);

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        CarrinhoPersonalizadoNumero = (TextView)findViewById(R.id.CarrinhoPersonalizadoNumero);

        PaginaTitulo.setText(CategoriaNome);
        DefinirNumeroItensIconeCarrinho();

        CarrinhoPersonalizadoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoriaActivity.this, CarrinhoActivity.class));
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