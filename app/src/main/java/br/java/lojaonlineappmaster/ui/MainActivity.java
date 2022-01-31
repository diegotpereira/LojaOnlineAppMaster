package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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

import java.util.ArrayList;
import java.util.List;

import br.java.lojaonlineappmaster.Adapter.GridProdutoAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.FavoritaClasse;
import br.java.lojaonlineappmaster.model.HorizontalProdutoModel;
import br.java.lojaonlineappmaster.model.Modelo;
import br.java.lojaonlineappmaster.model.Ofertas;
import br.java.lojaonlineappmaster.model.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private TextView mPessoa_nome;
    private NavigationView navigationView;
    private ViewPager pager;
    private View mNavigationView;
    private List<Modelo> modelos;

    private DatabaseReference m;
    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsuario;

    private static List<FavoritaClasse> favoritos;

    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView CarrinhoPersonalizadoNumero;
    private TextView PaginaTitulo;
    private ImageView CarrinhoPersonalizadoIcone;

    private String UsuarioId;
    private String nome;
    private String foto;
    private CircleImageView imagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser();
        UsuarioId = AtualUsuario.getUid();

        navigationView = findViewById(R.id.navegation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mNavigationView = navigationView.getHeaderView(0);

        mPessoa_nome = mNavigationView.findViewById(R.id.pessoanome);
        imagem = mNavigationView.findViewById(R.id.circimage);
        drawerLayout = findViewById(R.id.drawer);

        mToolBar = findViewById(R.id.main_TooBar);
        setSupportActionBar(mToolBar);
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Recuperar dados do usuário da visualização de cabeçalho
        ObterDadosDoCabecalhoDeNavegacao();

        // Recuperar Favoritos
        RecuperarFavoritos();

        // Primeira visualização
        RecuperarEletronicos();

        // Segunda visualização
        RecuperarFrutas();

        // Terceira visualização
        RecuperarCarnes();

        // Quarta visualização
        RecuperarVegetais();

        // OFERTAS
        RecuperarOfertas();

        // Ícone de atualização do carrinho
        ExibirIconeDoCarrinho();

        // para verificar se o preço total é zero ou não
        verificarPrecoTotalZero();
    }
    public void Dados_cabecalho_visualizacao_navegacao() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(UsuarioId);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nome = snapshot.child("Nome").getValue().toString();
                    foto = snapshot.child("Imagem").getValue().toString();

                    if (foto.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(imagem);
                    } else
                        Picasso.get().load(foto).placeholder(R.drawable.profile).into(imagem);
                    mPessoa_nome.setText(nome);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    public void RecuperarFavoritos() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favoritos")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        favoritos = new ArrayList<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    FavoritaClasse fav = new FavoritaClasse();
                    fav = ds.getValue(FavoritaClasse.class);
                    favoritos.add(fav);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void RecuperarEletronicos() {
        LinearLayout meuLayout = (LinearLayout) findViewById(R.id.meu_cardView);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_produto_layout, meuLayout, false);

        TextView gridlayouttitulo = meuLayout.findViewById(R.id.grid_produto_layout_textview);
        gridlayouttitulo.setText("Eletrônicos");

        Button GridLayoutViewBtn = meuLayout.findViewById(R.id.grid_button_layout_viewall_button);

        final GridView gv = meuLayout.findViewById(R.id.produto_layout_gridview);
        final List<HorizontalProdutoModel> ultimosModelos = new ArrayList<>();

        final GridProdutoAdapter meu_adapter;
        meu_adapter = new GridProdutoAdapter(ultimosModelos, favoritos, MainActivity.this);

        m = FirebaseDatabase.getInstance().getReference().child("produto").child("Eletronicos");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Usuario meu_usuario = new Usuario();
                    meu_usuario = ds.getValue(Usuario.class);
                    meu_usuario.setCategoria(ds.getKey().toString());
                    ultimosModelos.add(new HorizontalProdutoModel(meu_usuario.getImagem(),
                            meu_usuario.getCategoria(), meu_usuario.getPreco(), false,
                            meu_usuario.getDataVencimento()));
                }
                gv.setAdapter(meu_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                intent.putExtra("Categoria Nome", "Eletrônicos");
                startActivity(intent);
            }
        });
    }

    public void RecuperarFrutas() {
        LinearLayout meuLayout = (LinearLayout) findViewById(R.id.meu_cardView2);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_produto_layout, meuLayout, false);

        TextView gridLayoutTitulo = meuLayout.findViewById(R.id.grid_produto_layout_textview);
        gridLayoutTitulo.setText("Frutas");

        Button GridLayoutViewBtn = meuLayout.findViewById(R.id.grid_button_layout_viewall_button);

        final GridView gv = meuLayout.findViewById(R.id.produto_layout_gridview);
        final List<HorizontalProdutoModel> ultimoModelos = new ArrayList<>();
        final GridProdutoAdapter meu_adapter;
        meu_adapter = new GridProdutoAdapter(ultimoModelos, favoritos, MainActivity.this);

        m = FirebaseDatabase.getInstance().getReference().child("produto").child("Frutas");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Usuario meu_usuario = new Usuario();
                    meu_usuario = ds.getValue(Usuario.class);
                    meu_usuario.setCategoria(ds.getKey().toString());
                    ultimoModelos.add(new HorizontalProdutoModel(meu_usuario.getImagem(),
                            meu_usuario.getCategoria(),
                            meu_usuario.getPreco(),
                            false,
                            meu_usuario.getDataVencimento()));
                }
                gv.setAdapter(meu_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                intent.putExtra("Categoria Nome", "Frutas");
                startActivity(intent);
            }
        });
    }

    public void RecuperarCarnes() {
        LinearLayout meuLayout = (LinearLayout) findViewById(R.id.meu_cardView3);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_produto_layout, meuLayout, false);

        TextView gridLayoutTitulo = meuLayout.findViewById(R.id.grid_produto_layout_textview);
        gridLayoutTitulo.setText("Carnes");

        Button GridLayoutViewBtn = meuLayout.findViewById(R.id.grid_button_layout_viewall_button);

        final GridView gv = meuLayout.findViewById(R.id.produto_layout_gridview);
        final List<HorizontalProdutoModel> ultimosModelos = new ArrayList<>();

        final GridProdutoAdapter meu_adapter;
        meu_adapter = new GridProdutoAdapter(ultimosModelos, favoritos, MainActivity.this);

        m = FirebaseDatabase.getInstance().getReference().child("produto").child("Carnes");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Usuario meu_usuario = new Usuario();
                    meu_usuario = ds.getValue(Usuario.class);
                    meu_usuario.setCategoria(ds.getKey().toString());

                    ultimosModelos.add(new HorizontalProdutoModel(meu_usuario.getImagem(),
                            meu_usuario.getCategoria(),
                            meu_usuario.getPreco(),
                            false,
                            meu_usuario.getDataVencimento()));
                }
                gv.setAdapter(meu_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                intent.putExtra("Categoria Nome", "Carnes");
                startActivity(intent);
            }
        });
    }

    public void RecuperarVegetais() {
        LinearLayout meuLayout = (LinearLayout) findViewById(R.id.meu_cardView4);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_produto_layout, meuLayout, false);

        TextView grigLayoutTitulo = meuLayout.findViewById(R.id.grid_produto_layout_textview);
        grigLayoutTitulo.setText("Vegetais");

        Button GridLayoutViewBtn = meuLayout.findViewById(R.id.grid_button_layout_viewall_button);

        final GridView gv = meuLayout.findViewById(R.id.produto_layout_gridview);
        final List<HorizontalProdutoModel> ultimosModelos = new ArrayList<>();

        final GridProdutoAdapter meu_adapter;
        meu_adapter = new GridProdutoAdapter(ultimosModelos, favoritos, MainActivity.this);

        m = FirebaseDatabase.getInstance().getReference().child("produto").child("Vegetais");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Usuario meu_usuario = new Usuario();
                    meu_usuario = ds.getValue(Usuario.class);
                    meu_usuario.setCategoria(ds.getKey().toString());
                    ultimosModelos.add(new HorizontalProdutoModel(meu_usuario.getImagem(),
                            meu_usuario.getCategoria(),
                            meu_usuario.getPreco(),
                            false,
                            meu_usuario.getDataVencimento()));
                }
                gv.setAdapter(meu_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        GridLayoutViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                intent.putExtra("Categoria Nome", "Vegetais");
                startActivity(intent);
            }
        });
    }

    public void RecuperarOfertas() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("ofertas");

        modelos = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Ofertas ofertas = new Ofertas();
                    ofertas = ds.getValue(Ofertas.class);
                    ofertas.setTitulo(ds.getValue().toString());

                    modelos.add(new Modelo(
                            ofertas.getImg(),
                            ofertas.getTitulo(),
                            ofertas.getDescricao()));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }



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
        } else if (id == R.id.MeusPedidos) {
            startActivity(new Intent(MainActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Carrinho) {
            startActivity(new Intent(MainActivity.this, CarrinhoActivity.class));
        } else if (id == R.id.Frutas) {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Frutas");
            startActivity(intent);
        } else if (id == R.id.Vegetais) {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Vegetais");
            startActivity(intent);
        } else if (id == R.id.Carnes) {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Carnes");
            startActivity(intent);
        } else if (id == R.id.Eletronicos) {
            Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Eletronicos");
            startActivity(intent);
        } else if (id == R.id.Sair) {
            VerificarLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

    private void ExibirIconeDoCarrinho() {
        // Toolbar & Carrinho icone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);
        actionBar.setCustomView(view);

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        CarrinhoPersonalizadoNumero = (TextView)findViewById(R.id.CarrinhoPersonalizadoNumero);

        PaginaTitulo.setText("Favoritos");
        DefinirNumeroItensIconeCarrinho();

        CarrinhoPersonalizadoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CarrinhoActivity.class));
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
                    FirebaseDatabase.getInstance().getReference().child("carrinho").child(UsuarioId).child("pretoTotal").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }
}