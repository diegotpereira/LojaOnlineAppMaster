package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

        cliqueEmQualquerProduto();
    }

    private void cliqueEmQualquerProduto() {
        listener = new CategoriaProdutoInfoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                CategoriaProdutoInfo produto = CategoriaProdutos.get(position);

                Intent intent = new Intent(CategoriaActivity.this, ProdutoInfoActivity.class);
                intent.putExtra("Produto Nome", produto.getProdutoNome());
                intent.putExtra("Produto Preço", produto.getProdutoPreco());
                intent.putExtra("Produto Imagem", produto.getProdutoImagem());
                intent.putExtra("Produto Data Vencimento", produto.getProdutoDataVencimento());
                intent.putExtra("Produto EhFavorito", String.valueOf(produto.isEhFavorito()));
                intent.putExtra("Eh Oferecido", "Não");
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        definirDadosCategoria();

        definirNavegacao();

        exibirIconeCarrinho();

        verificarPrecoTotalZero();
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
                        final String ProdutoDataVencimento = dataSnapshot.child("vencido").getValue().toString();

                        // verficar favoritos
                        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference x = root.child("favoritos").child(UsuarioId).child(ProdutoNome);

                        ValueEventListener vvalueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                }
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

    private void definirNavegacao() {}

    private void exibirIconeCarrinho() {}

    private void verificarPrecoTotalZero() {}

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}