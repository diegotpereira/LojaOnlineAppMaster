package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.ui.ui.CarrinhoActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutoInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private CircleImageView mPessoa_imagem;

    private String ProdutoNome;
    private String ProdutoPreco;
    private String ProdutoImagem;
    private String ProdutoDataVencimento;
    private String ProdutoEhFavorito;
    private String EhOferecido;

    // xml Views
    private ImageView PImagem;
    private ImageView PIsFav;

    private TextView mPessoa_nome;
    private TextView PNome;
    private TextView PCategoria;
    private TextView PMontante;
    private TextView PPreco;
    private TextView VelhoPreco;
    private TextView TaxaDeOferta;
    private TextView PDataVencimento;

    private RelativeLayout AddNoCarrinhoContainer;
    private RelativeLayout DeletarDoCarrinhoContainer;
    private RelativeLayout VerificarCarrinhoContainer;

    private LinearLayout OfertaConteiner;

    private Button Voltar;
    private Button Confirmar;

    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsuario;
    private String UsuarioId;

    // Xml Personalida Views (Icone Carrinho)
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;
    private TextView CarrinhoPersonalizadoNumero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_info);

        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser();
        UsuarioId = AtualUsuario.getUid();

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.ProdutoToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Envio de dados
        ProdutoNome = getIntent().getStringExtra("Produto Nome");
        ProdutoPreco = getIntent().getStringExtra("Produto Preço");
        ProdutoImagem = getIntent().getStringExtra("Produto Imagem");
        ProdutoDataVencimento = getIntent().getStringExtra("Produto Data Vencimento");
        ProdutoEhFavorito = getIntent().getStringExtra("Produto Favorito");
        EhOferecido = getIntent().getStringExtra("É Oferecido");

        // definir dados xml
        DefinirXmlViews();

        DefinirDadosProduto();
        aoClicar();
    }
    private void DefinirXmlViews() {
        PImagem = (ImageView) findViewById(R.id.ProdutoImagem);
        PIsFav = (ImageView) findViewById(R.id.ProdutoFav);
        PNome = (TextView) findViewById(R.id.ProdutoNome);
        PCategoria = (TextView) findViewById(R.id.ProdutoCategoria);
        PMontante = (TextView) findViewById(R.id.ProdutoDisponivelQuantidade);
        VelhoPreco = (TextView) findViewById(R.id.ProdutoPrecoVelho);
        TaxaDeOferta = (TextView) findViewById(R.id.TaxaOferta);
        OfertaConteiner = (LinearLayout) findViewById(R.id.OfertaContainer);
        PDataVencimento = (TextView) findViewById(R.id.ProdutoDataVencimento);
        AddNoCarrinhoContainer = (RelativeLayout) findViewById(R.id.AddNoCarrinho);
        DeletarDoCarrinhoContainer = (RelativeLayout) findViewById(R.id.DeletarDoCarrinho);
        VerificarCarrinhoContainer = (RelativeLayout) findViewById(R.id.VerificarCarrinhoContainer);
        Voltar = (Button) findViewById(R.id.VoltarBtn);
        Confirmar = (Button) findViewById(R.id.ConfirmarBtn);

        AtualizarContainer();

    }

    private void AtualizarContainer() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = root.child("carrinho").child(UsuarioId).child(ProdutoNome);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    AddNoCarrinhoContainer.setVerticalGravity(View.GONE);
                    DeletarDoCarrinhoContainer.setVisibility(View.VISIBLE);
                } else {
                    AddNoCarrinhoContainer.setVisibility(View.VISIBLE);
                    DeletarDoCarrinhoContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        x.addListenerForSingleValueEvent(valueEventListener);
    }

    private void DefinirDadosProduto(){
        Picasso.get().load(ProdutoImagem).into(PImagem);
        PNome.setText(ProdutoNome);

        if (EhOferecido.equalsIgnoreCase("sim")) {
            int PrecoDepoisDaOferta = (int) ((Integer.valueOf(ProdutoPreco)) - (Integer.valueOf(ProdutoPreco) * 0.3));
            PPreco.setText("Preço: " + PrecoDepoisDaOferta + " EGP");
            VelhoPreco.setText(ProdutoPreco + " EGP");
            TaxaDeOferta.setText("- 30%");
            VelhoPreco.setPaintFlags(VelhoPreco.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            OfertaConteiner.setVisibility(View.GONE);
            PPreco.setText("Preço: " + ProdutoPreco + "EGP");
        }

        if (ProdutoEhFavorito.equalsIgnoreCase("true")) PIsFav.setImageResource(R.drawable.ic_baseline_favorite_24);
        else PIsFav.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);

        if (ProdutoDataVencimento.equalsIgnoreCase("null")) PDataVencimento.setVisibility(View.GONE);
        else { PDataVencimento.setVisibility(View.VISIBLE); PDataVencimento.setText("Data de Vencimento: " + ProdutoDataVencimento); }

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("produto");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.child("Frutas").getChildren()) {
                    if (snapshot.getKey().equals(ProdutoNome)) {
                        PCategoria.setText("Categoria: Frutas");
                        PMontante.setText("Quantidade Disponível: " + snapshot.child("quantidade").getValue());

                        break;
                    }
                }

                for(DataSnapshot dataSnapshot : snapshot.child("Eletronicos").getChildren()) {
                    if (snapshot.getKey().equals(ProdutoNome)) {
                        PCategoria.setText("Categoria: Eletrônicos");
                        PMontante.setText("Quantidade Disponível: " + snapshot.child("quantidade").getValue());

                        break;
                    }
                }

                for(DataSnapshot dataSnapshot : snapshot.child("Carnes").getChildren()) {
                    if (snapshot.getKey().equals(ProdutoNome)) {
                        PCategoria.setText("Categoria: Carnes");
                        PMontante.setText("Quantidade Disponível: " + snapshot.child("quantidade").getValue());

                        break;
                    }
                }

                for(DataSnapshot dataSnapshot : snapshot.child("Vegetais").getChildren()) {
                    if (snapshot.getKey().equals(ProdutoNome)) {
                        PCategoria.setText("Categoria: Vegetais");
                        PMontante.setText("Quantidade Disponível: " + snapshot.child("quantidade").getValue());

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void aoClicar() {
        PIsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ProdutoEhFavorito.equalsIgnoreCase("true")) {
                    PIsFav.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    ProdutoEhFavorito = "false";

                    // Deletar Favoritos do database
                    DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("favoritos").child(UsuarioId);
                    x.child(ProdutoNome).removeValue();
                } else {
                    PIsFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    ProdutoEhFavorito = "true";

                    // Salvar favoritos no banco
                    DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("favoritos").child(UsuarioId).child(ProdutoNome);
                    x.child("verificado").setValue(true);
                    x.child("produtoimagem").setValue(ProdutoImagem);
                    x.child("produtopreco").setValue("EGP " + ProdutoPreco);
                    x.child("produtotitulo").setValue(ProdutoNome);
                }
            }
        });
        AddNoCarrinhoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarCarrinhoContainer.setVisibility(View.VISIBLE);
            }
        });
        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarCarrinhoContainer.setVisibility(View.GONE);
            }
        });
        Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarCarrinhoContainer.setVisibility(View.GONE);
                DeletarDoCarrinhoContainer.setVisibility(View.VISIBLE);
                AddNoCarrinhoContainer.setVisibility(View.GONE);

                Toast.makeText(ProdutoInfoActivity.this, "O produto adicionado com sucesso ao seu carrinho", Toast.LENGTH_SHORT).show();

                // Adicionar o produto ao carrinho
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("produtoImagem", ProdutoImagem);
                hashMap.put("produtoPreco", ProdutoPreco);
                hashMap.put("quantidade", "1");

                int PrecoDepoisDaOferta;

                if (EhOferecido.equalsIgnoreCase("sim")) PrecoDepoisDaOferta = (int) ((Integer.valueOf(ProdutoPreco)) - (Integer.valueOf(ProdutoPreco) * 0.3) );
                else PrecoDepoisDaOferta = (int) (Integer.valueOf(ProdutoPreco));

                hashMap.put("produtoPreco", String.valueOf(PrecoDepoisDaOferta));

                DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("carrinho").child(UsuarioId);
                x.child(ProdutoNome).setValue(hashMap);

                // Ícone de atualização do carrinho
                AtualizarIconeCarrinho();
            }
        });
        DeletarDoCarrinhoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNoCarrinhoContainer.setVisibility(View.VISIBLE);
                DeletarDoCarrinhoContainer.setVisibility(View.GONE);

                DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("carrinho").child(UsuarioId);
                x.child(ProdutoNome).removeValue();

                Toast.makeText(ProdutoInfoActivity.this, "O produto foi excluído com sucesso do seu carrinho", Toast.LENGTH_SHORT).show();

                // Ícone de atualização do carrinho
                AtualizarIconeCarrinho();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DefinirNavegacao();

        // Ícone de atualização do carrinho
        AtualizarIconeCarrinho();

        AtualizarContainer();

        // para verificar se o preço total é zero ou não
        VerificarTotalPrecoZero();
    }
    private void VerificarTotalPrecoZero() {

    }

    private void DefinirNavegacao(){
        drawerLayout = (DrawerLayout) findViewById(R.id.ProdutoDrawer);
        navigationView = (NavigationView) findViewById(R.id.ProdutoNavigacao);

        // Navegação Cabeçalho
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mPessoa_nome = view.findViewById(R.id.pessoanome);
        mPessoa_imagem = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        obterDadosDoCabecalhoSeNavegacao();
    }

    private void obterDadosDoCabecalhoSeNavegacao() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(UsuarioId);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.Home) {
            startActivity(new Intent(ProdutoInfoActivity.this, MainActivity.class));
        } else if (id == R.id.Perfil) {
            startActivity(new Intent(ProdutoInfoActivity.this, UsuarioPerfilActivity.class));
        }
        return false;
    }

    private void AtualizarIconeCarrinho() {
        // Toolbar & CarrinhoIcone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);
        actionBar.setCustomView(view);

        // xml de itens de ação personalizados
        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        CarrinhoPersonalizadoNumero = (TextView) findViewById(R.id.CarrinhoPersonalizadoNumero);

        PaginaTitulo.setText("Produto Informação");
        definirNumeroItensIconeCarrinho();

        CarrinhoPersonalizadoNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProdutoInfoActivity.this, CarrinhoActivity.class));
            }
        });
    }
    private void definirNumeroItensIconeCarrinho() {
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
}