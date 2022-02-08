package br.java.lojaonlineappmaster.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.Oferta;

public class AddOfertaActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText descricao;

    private Button btnAdd;
    private Button btnEscolher;

    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private StorageTask mEnviarTarefa;

    private TextInputLayout nomeTextoInputLayout;
    private TextInputLayout descricaoTextoInputLayout;
    private Toolbar mToolbar;

    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_oferta);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.add_oferta_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Offer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = findViewById(R.id.editar_texto_oferta_nome);
        descricao = findViewById(R.id.editar_texto_oferta_descricao);
        btnAdd = findViewById(R.id.btn_add_oferta);
        btnEscolher = findViewById(R.id.btn_escolher_oferta_imagem);
        img = findViewById(R.id.oferta_imagem);
        nomeTextoInputLayout = findViewById(R.id.editar_texto_oferta_layout);
        descricaoTextoInputLayout = findViewById(R.id.editar_texto_oferta_descricao_layout);

        mStorageRef = FirebaseStorage.getInstance().getReference("ofertas");

        nomeTextoInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (nome.getText().toString().trim().isEmpty()) {
                    nomeTextoInputLayout.setErrorEnabled(true);
                    nomeTextoInputLayout.setError("Por favor digite nome da oferta");
                } else {
                    nomeTextoInputLayout.setErrorEnabled(false);
                }
            }
        });

        descricaoTextoInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (descricao.getText().toString().trim().isEmpty()) {
                    descricaoTextoInputLayout.setErrorEnabled(true);
                    descricaoTextoInputLayout.setError("Por favor digite nome da oferta");
                } else {
                    descricaoTextoInputLayout.setErrorEnabled(false);
                }
            }
        });

        nome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nome.getText().toString().trim().isEmpty()) {
                    nomeTextoInputLayout.setErrorEnabled(true);
                    nomeTextoInputLayout.setError("Por favor digite nome da oferta");
                } else {
                    nomeTextoInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        descricao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (descricao.getText().toString().trim().isEmpty()) {
                    descricaoTextoInputLayout.setErrorEnabled(true);
                    descricaoTextoInputLayout.setError("Por favor digite nome da oferta");
                } else {
                    descricaoTextoInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEnviarTarefa != null && mEnviarTarefa.isInProgress())
                    Toast.makeText(AddOfertaActivity.this, "O carregamento está em andamento", Toast.LENGTH_SHORT).show();
                else if (nome.getText().toString().isEmpty() || descricao.getText().toString().isEmpty() || imgUri == null) {

                    Toast.makeText(AddOfertaActivity.this, "Células vazias", Toast.LENGTH_SHORT).show();
                } else {

                    try {

                        carregarDados();

                        Toast.makeText(AddOfertaActivity.this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {

                        Toast.makeText(AddOfertaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnEscolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirImagem();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        NaoExibirIconeCarrinho();

    }

    public void carregarDados() {
        if (nome.getText().toString().isEmpty() ||
                 descricao.getText().toString().isEmpty()||
        imgUri == null) {

            Toast.makeText(AddOfertaActivity.this, "Células vazias", Toast.LENGTH_SHORT).show();
        } else {
            carregarImagem();
        }
    }

    public void carregarImagem() {
        if (imgUri != null) {

            StorageReference arquivoReferencia = mStorageRef.child(nome.getText().toString() + "." + getFileExtension(imgUri));
            mEnviarTarefa = arquivoReferencia.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTarefa = taskSnapshot.getStorage().getDownloadUrl();

                    while (!urlTarefa.isSuccessful());
                    Uri downloadUrl = urlTarefa.getResult();
                    Oferta oferta = new Oferta(descricao.getText().toString().trim(),
                            downloadUrl.toString());
                    DatabaseReference z = FirebaseDatabase.getInstance().getReference("ofertas");
                    z.child(nome.getText().toString().trim()).setValue(oferta);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    public void abrirImagem() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, RegistrarActivity.GALERIA_FOTO);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RegistrarActivity.GALERIA_FOTO && resultCode == Activity.RESULT_OK) {
            imgUri = data.getData();
            Log.e("uri", imgUri.toString());

            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {

                Log.e(this.toString(), e.getMessage().toString());
            }
        }
    }

    private void NaoExibirIconeCarrinho() {
        // Toolbar && Icone Carrinho
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);

        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        PaginaTitulo.setVisibility(View.GONE);

        CarrinhoPersonalizadoContainer.setVisibility(View.GONE);
    }
}