package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import br.java.lojaonlineappmaster.model.Produto;

public class AddProdutoActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText quantidade;
    private TextInputEditText preco;
    private TextInputEditText dataVencimento;
    private Button addBtn;
    private Button escolherBtn;
    private ImageView img;
    private Uri imagemUri;
    private String categoria;
    private StorageReference mStorageRef;
    private Spinner spinner;
    private StorageTask mUploadTask;
    private TextInputLayout nomeLayout;
    private TextInputLayout quantidadeLayout;
    private TextInputLayout precoLayout;
    private TextInputLayout dataVencimentoLayout;
    private Toolbar mToolbar;
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.addProduto_ToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Adicionar Produto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = (TextInputEditText) findViewById(R.id.editarTextoProdutoNome);
        quantidade = (TextInputEditText) findViewById(R.id.editarTextoProdutoNumero);
        preco = (TextInputEditText) findViewById(R.id.editarTextoProdutoPreco);
        dataVencimento = (TextInputEditText) findViewById(R.id.editarTextoProdutoDataVencimento);
        img = (ImageView) findViewById(R.id.imgProduto);


        addBtn = (Button) findViewById(R.id.addBtn);
            escolherBtn = (Button) findViewById(R.id.escolherImgbtn);


        spinner = (Spinner) findViewById(R.id.spinner);

        nomeLayout = (TextInputLayout) findViewById(R.id.editarTextoProdutoNomeLayout);
        quantidadeLayout = (TextInputLayout) findViewById(R.id.editarTextoProdutoNumeroLayout);
        precoLayout = (TextInputLayout) findViewById(R.id.editarTextoProdutoPrecoLayout);
        dataVencimentoLayout = (TextInputLayout)
                findViewById(R.id.editarTextoProdutoDataVencimentoLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.produtosTipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoria = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference("produtos");

        nomeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (nome.getText().toString().trim().isEmpty()){
                    nomeLayout.setErrorEnabled(true);
                    nomeLayout.setError("Digite o nome da oferta");
                } else {
                    nomeLayout.setErrorEnabled(false);
                }

            }
        });

        nome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (nome.getText().toString().trim().isEmpty()){
                    nomeLayout.setErrorEnabled(true);
                    nomeLayout.setError("Digite o nome da oferta");
                } else {
                    nomeLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        quantidadeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (quantidade.getText().toString().trim().isEmpty()){
                    quantidadeLayout.setErrorEnabled(true);
                    quantidade.setError("Digite a quantidade da oferta");
                } else {
                    quantidadeLayout.setErrorEnabled(false);
                }

            }
        });

        quantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (quantidade.getText().toString().trim().isEmpty()){
                    quantidadeLayout.setErrorEnabled(true);
                    quantidadeLayout.setError("Digite a quantidade da oferta");
                } else {
                    quantidadeLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        precoLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (preco.getText().toString().trim().isEmpty()){
                    precoLayout.setErrorEnabled(true);
                    precoLayout.setError("Digite o preço da oferta");
                } else {
                    precoLayout.setErrorEnabled(false);
                }

            }
        });

        preco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (preco.getText().toString().trim().isEmpty()) {
                    precoLayout.setErrorEnabled(true);
                    precoLayout.setError("Digite o preço da oferta");
                } else {
                    precoLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dataVencimentoLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (dataVencimento.getText().toString().trim().isEmpty()){
                    dataVencimentoLayout.setErrorEnabled(true);
                    dataVencimentoLayout.setError("Digite a data de vencimento da oferta");
                } else {
                    dataVencimentoLayout.setErrorEnabled(false);
                }

            }
        });

        dataVencimento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (dataVencimento.getText().toString().trim().isEmpty()){
                    dataVencimentoLayout.setErrorEnabled(true);
                    dataVencimentoLayout.setError("Digite a data de vencimento da oferta");
                } else {
                    dataVencimentoLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        escolherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirImagem();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())

                    Toast.makeText(
                            AddProdutoActivity.this,
                            "O carregamento está em andamento", Toast.LENGTH_SHORT).show();

                else if(
                        nome.getText().toString().isEmpty() ||
                        quantidade.getText().toString().isEmpty() ||
                        preco.getText().toString().isEmpty() ||
                        imagemUri == null) {

                    Toast.makeText(AddProdutoActivity.this, "Campos Vazios", Toast.LENGTH_SHORT).show();
                } else {

                    carregarDados();
                    Toast.makeText(AddProdutoActivity.this, "Carregado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
                }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        NaoExibirIconeCarrinho();
    }
    public void carregarDados() {
        if (
            nome.getText().toString().isEmpty() ||
            quantidade.getText().toString().isEmpty() ||
            preco.getText().toString().isEmpty() ||
            imagemUri == null) {

            Toast.makeText(AddProdutoActivity.this, "Campos Vazios", Toast.LENGTH_SHORT).show();
        } else {
            SubirImagem();
        }
    }
    public void SubirImagem() {
        if (imagemUri != null) {
            StorageReference fileReference = mStorageRef.child(nome.getText().toString() + "." +
                    obterExtensaoArquivo(imagemUri));

            mUploadTask = fileReference.putFile(imagemUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                    while (!urlTask.isSuccessful());

                    Uri downloadUrl = urlTask.getResult();

                    Produto produto = new Produto(
                            quantidade.getText().toString().trim(),
                            preco.getText().toString().trim(),
                            dataVencimento.getText().toString().trim(),
                            downloadUrl.toString());

                    DatabaseReference z = FirebaseDatabase.getInstance().getReference()
                            .child("produto")
                            .child(categoria)
                            .child(nome.getText().toString());
                    z.setValue(produto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(AddProdutoActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

    public String obterExtensaoArquivo(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RegistrarActivity.GALERIA_FOTO && resultCode == Activity.RESULT_OK &&
                data.getData() != null && data != null) {
            imagemUri = data.getData();

            try {
                Picasso.get().load(imagemUri).fit().centerCrop().into(img);
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