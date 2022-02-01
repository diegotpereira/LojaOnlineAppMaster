package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.java.lojaonlineappmaster.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CarrinhoVerificarActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    String ttlPreco;
    String DelPreco;
    String ttlPreco2;
    String salvo;
    String AtualUsuario;

    TextView precoTotal;
    TextView precoEntrega;
    TextView precoTotal2;
    TextView quantidadeSalva;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView mPessoa_nome;
    private CircleImageView mPessoa_imagem;

    DatabaseReference root;
    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsr;
    private String UsuarioId;

    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;
    private TextView CarrinhoPersonalizadoNumero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho_verificar);

        mAuth = FirebaseAuth.getInstance();
        AtualUsr = mAuth.getCurrentUser();
        UsuarioId = AtualUsr.getUid();

        mToolbar = (Toolbar) findViewById(R.id.carrinhoVerificaToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button Concluir = findViewById(R.id.Concluir);
        Button Cancelar = findViewById(R.id.Cancelar);
        precoTotal = findViewById(R.id.total_preco_item);
        precoEntrega = findViewById(R.id.preco_de_entrega);
        precoTotal2 = findViewById(R.id.preco_total);
        quantidadeSalva = findViewById(R.id.quantidade_salva);

        Concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvoData();
                CarrinhoActivity.fa.finish();
                finish();
            }
        });
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        obterDadosDeVerificacao();
    }

    private void obterDadosDeVerificacao() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        String AtualUsuario = mAuth.getCurrentUser().getUid();
        DatabaseReference m = root.child("carrinho").child(AtualUsuario);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ttlPreco = snapshot.child("precoTotal").getValue().toString();
                    Log.d("ttl", ttlPreco);
                    precoTotal.setText(ttlPreco);
                    precoEntrega.setText("Livre");
                    precoTotal2.setVisibility(View.VISIBLE);
                    quantidadeSalva.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void salvoData() {
        root = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser().getUid();

        DatabaseReference x = root.child("carrinho").child(AtualUsuario);

        x.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseDatabase t = FirebaseDatabase.getInstance();
                String chave = t.getReference("pedido").push().getKey();

                root.child("pedido").child(AtualUsuario).child(chave).child("produtosPedidos").setValue(snapshot.getValue());
                root.child("pedido").child(AtualUsuario).child(chave).child("precoTotal").setValue(snapshot.getValue());

                root.child("pedido").child(AtualUsuario).child(chave).child("produtosPedidos").child("precoTotal").removeValue();

                root.child("pedido").child(AtualUsuario).child(chave).child("Data")
                        .setValue(String.valueOf(new SimpleDateFormat("dd MMM yyyy hh:mm a")
                                .format(Calendar.getInstance().getTime())));

                root.child("pedido").child(AtualUsuario).child(chave).child("EhVerificado")
                        .setValue("false");

                Toast.makeText(getApplicationContext(), "Conferido & concluído",
                        Toast.LENGTH_LONG).show();

                root.child("carrinho").child(AtualUsuario).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        drawerLayout = (DrawerLayout) findViewById(R.id.carrinhoVerificarDrawer);
        navigationView = (NavigationView) findViewById(R.id.carrinhoVerificarNavigationViewer);

        // Navegação do Cabeçalho
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mPessoa_nome = view.findViewById(R.id.pessoanome);
        mPessoa_imagem = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        obterDadosDoCabecalhoSeNavegacao();

        ExibirIconeDoCarrinho();

        verificarPrecoTotalZero();

    }

    private void obterDadosDoCabecalhoSeNavegacao() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser().getUid();
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
            startActivity(new Intent(CarrinhoVerificarActivity.this,MainActivity.class));
        }
        if (id == R.id.Perfil) {
            startActivity(new Intent(CarrinhoVerificarActivity.this, UsuarioPerfilActivity.class));
        } else if (id == R.id.MeusPedidos) {
            startActivity(new Intent(CarrinhoVerificarActivity.this, CategoriaActivity.class));
        } else if (id == R.id.Carrinho) {
            startActivity(new Intent(CarrinhoVerificarActivity.this, CarrinhoActivity.class));
        } else if (id == R.id.Frutas) {
            Intent intent = new Intent(CarrinhoVerificarActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Frutas");
            startActivity(intent);
        } else if (id == R.id.Vegetais) {
            Intent intent = new Intent(CarrinhoVerificarActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Vegetais");
            startActivity(intent);
        } else if (id == R.id.Carnes) {
            Intent intent = new Intent(CarrinhoVerificarActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Carnes");
            startActivity(intent);
        } else if (id == R.id.Eletronicos) {
            Intent intent = new Intent(CarrinhoVerificarActivity.this, CategoriaActivity.class);
            intent.putExtra("Categoria Nome", "Eletronicos");
            startActivity(intent);
        } else if (id == R.id.Sair) {
            VerificarLogout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void VerificarLogout() {
        AlertDialog.Builder verifiqueAlerta = new AlertDialog.Builder(CarrinhoVerificarActivity.this);
        verifiqueAlerta.setMessage("Deseja sair?")
                .setCancelable(false).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CarrinhoVerificarActivity.this, EntrarActivity.class);
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
        DefinirNumeroItensIconeCarrinho();

        CarrinhoPersonalizadoNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarrinhoVerificarActivity.this, CarrinhoActivity.class));
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
                    FirebaseDatabase.getInstance().getReference().child("carrinho")
                            .child(UsuarioId).child("precoTotal").setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }
}