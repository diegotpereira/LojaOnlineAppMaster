package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import br.java.lojaonlineappmaster.R;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class UsuarioPerfilActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolBar;

    private TextView mPessoa_nome;
    private CircleImageView mPessoa_imagem;

    // ----------------------------------
    private CircleImageView UsuarioImagem;
    private TextView UsuarioNome;
    private TextView UsuarioEmail;
    private TextView UsuarioTelefone;
    private TextView UsuarioFavoritos;
    private TextView UsuarioPedidos;
    private ProgressBar progressBar;
    private final int GALERIA_FOTOS = 1;

    //--------------------Firebase ------
    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsuario;
    private String UsuarioId;
    private StorageReference mStorageReference;

    //Personalizar Xml Views (cart Icon)
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo, CarrinhoPersonalizadoNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser();
        UsuarioId =  AtualUsuario.getUid();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        // Toolbar
        mToolBar = findViewById(R.id.UsuarioPerfilToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UsuarioImagem = (CircleImageView) findViewById(R.id.UsuarioImagem);
        UsuarioNome = (TextView) findViewById(R.id.UsuarioNome);
        UsuarioEmail = (TextView) findViewById(R.id.UsuarioEmail);
        UsuarioTelefone = (TextView) findViewById(R.id.UsuarioTelefone);
        UsuarioFavoritos = (TextView) findViewById(R.id.UsuarioFavorito);
        UsuarioPedidos = (TextView) findViewById(R.id.UsuarioPerdidos);
        progressBar = (ProgressBar) findViewById(R.id.PerfilprogressBar);

        // obter dados do perfil do usuário
        obterDadosDoPerfilDoUsuario();

        UsuarioImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarImagem();
            }
        });

        UsuarioFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        UsuarioPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // definir o Navigation Viewer e obter seus dados
        DefinirNavegacao();

        // Ícone de atualização do carrinho


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void obterDadosDoPerfilDoUsuario() {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("usuarios").child(UsuarioId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Nome = snapshot.child("Nome").getValue().toString();
                    String Imagem = snapshot.child("Imagem").getValue().toString();
                    String Telefone = snapshot.child("Telefone").getValue().toString();

                    UsuarioNome.setText(Nome);
                    UsuarioTelefone.setText(Telefone);
                    UsuarioEmail.setText(AtualUsuario.getEmail().toString());

                    if (Imagem.equals("default")) {
                        Picasso.get().load(R.drawable.profile).into(UsuarioImagem);
                    } else
                        Picasso.get().load(Imagem).placeholder(R.drawable.profile).into(UsuarioImagem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    private void DefinirNavegacao(){
        drawerLayout = (DrawerLayout) findViewById(R.id.UsuarioPerfilDrawer);
        navigationView = (NavigationView) findViewById(R.id.UsuarioPerfilNavigation);

        // Navegação no cabeçalho
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        mPessoa_nome = view.findViewById(R.id.pessoanome);
        mPessoa_imagem = view.findViewById(R.id.circimage);

        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Abrir, R.string.Fechar);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        obterDadosDoCabecalhoNavegacaoo();

    }

    private void obterDadosDoCabecalhoNavegacaoo() {
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);

    }

    private void carregarImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione Imagem"), GALERIA_FOTOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // para cortar a imagem
        if (requestCode == GALERIA_FOTOS && resultCode == RESULT_OK) {
            Uri ImagemUri = data.getData();
            CropImage.activity(ImagemUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                progressBar.setVisibility(View.VISIBLE);

                CarregarImagemNoBancoDeDadosDeArmazenamento(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void CarregarImagemNoBancoDeDadosDeArmazenamento(Uri resultUri) {
        final StorageReference FilePath = mStorageReference.child("usuarios_imagem").child(UsuarioId + "jpg");

        FilePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference mUsuarioDatabase = FirebaseDatabase.getInstance().getReference().child("usuarios").child(UsuarioId);
                        mUsuarioDatabase.child("Imagem").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Picasso.get().load(uri.toString()).placeholder(R.drawable.profile).into(UsuarioImagem);
                                progressBar.setVisibility(View.GONE);

                                obterDadosDoCabecalhoNavegacaoo();
                            }
                        });
                    }
                });
            }
        });
    }
}