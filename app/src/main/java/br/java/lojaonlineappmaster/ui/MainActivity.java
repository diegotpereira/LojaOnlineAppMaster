package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
import br.java.lojaonlineappmaster.model.Usuario;
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
    private DatabaseReference m;
    private static List<FavoritaClasse> favoritos;

    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;
    private ImageView CarrinhoPersonalizadoIcone;

    private String Uid;
    private String nome;
    private String foto;
    private CircleImageView imagem;


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
        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
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
    public void Dados_cabecalho_visualizacao_navegacao() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(Uid);

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
                    mpessoa_nome.setText(nome);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);

    }

    public void Recuperar_favoritos() {}

    public void Recuperar_eletronicos() {
        LinearLayout meuLayout = (LinearLayout) findViewById(R.id.meu_cardView);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.grid_product_layout, meuLayout, false);

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
                            meu_usuario.getCategoria(), meu_usuario.getPreco(), false, meu_usuario.getExpirado()));
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

    public void Recuperar_frutas() {}

    public void Recuperar_Carnes() {}

    public void Recuperar_vegetais() {}

    public void Recuperar_ofertas() {}

    public void exibirIconeCarrinho() {

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
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
}