package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import br.java.lojaonlineappmaster.R;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private TextView UsuarioNome, UsuarioEmail, UsuarioTelefone, UsuarioFavoritos, UsuarioPedidos;
    private ProgressBar progressBar;
    private final int GALERIA_FOTOS = 1;

    //--------------------Firebase ------
    private FirebaseAuth mAuth;
    private FirebaseUser UsuarioAtual;
    private String UsuarioId;
    private StorageReference mStorageReference;

    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo, CarrinhoPersonalizadoNumero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}