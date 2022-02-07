package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.java.lojaonlineappmaster.R;

public class AddVendedorActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText salario;
    private Button add;
    private Button escolher;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private StorageTask mEnviarTarefa;
    private TextInputEditText nomeLayout;
    private TextInputEditText salarioLayout;
    private Toolbar mToolbar;
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendedor);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.add_vendedor_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Adicionar Vendedor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorageRef = FirebaseStorage.getInstance().getReference("vendedor");
        mDataBaseRef = FirebaseDatabase.getInstance().getReference("vendedor");

        nome = findViewById(R.id.editar_texto_vendedor_nome);
        salario = findViewById(R.id.editar_texto_vendedor_salario);
        add = findViewById(R.id.btn_add_vendedor);
        escolher = findViewById(R.id.btn_escolher_vendedor_imagem);
        img = findViewById(R.id.vendedor_imagem);
        nomeLayout = findViewById(R.id.editar_texto_vendedor_nomeLayout);
        salarioLayout = findViewById(R.id.editar_texto_vendedor_salarioLayout);
        
        
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEnviarTarefa != null && mEnviarTarefa.isInProgress())

                    Toast.makeText(AddVendedorActivity.this, "O carregamento está em andamento", Toast.LENGTH_SHORT).show();
                else if (nome.getText().toString().isEmpty()
                        || salario.getText().toString().isEmpty()
                        || imgUri == null) {

                    Toast.makeText(AddVendedorActivity.this, "Células vazias", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(AddVendedorActivity.this, "Carregado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        escolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        salarioLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (salario.getText().toString().trim().isEmpty()) {

                    salarioLayout.setErrorEnabled(true);
                    salarioLayout.setError("Please Enter Offer Name");
                } else {
                    salarioLayout.setErrorEnabled(false);
                }
            }
        });

        salario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (salario.getText().toString().trim().isEmpty()) {
                    salarioLayout.setErrorEnabled(true);
                    salarioLayout.setError("Digite o nome da oferta");
                } else {
                    salarioLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nomeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (nome.getText().toString().trim().isEmpty()) {

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

                if (nome.getText().toString().trim().isEmpty()) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void carregarDados() {
        if (nome.getText().toString().isEmpty() || salario.getText().toString().isEmpty()
           || imgUri == null) {

            Toast.makeText(AddVendedorActivity.this, "Células Vazias", Toast.LENGTH_SHORT).show();
        } else {

            carregarImagem();
        }
    }
    public void carregarQr() {

        Bitmap bitmap = QrGenerate(nome.getText().toString().trim());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        StorageReference fileReference = mStorageRef.child(nome.getText().toString().trim() + "qr." + "jpg");
        fileReference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTarefa = taskSnapshot.getStorage().getDownloadUrl();

                while (!urlTarefa.isSuccessful());
                Uri downloadUrl = urlTarefa.getResult();
                DatabaseReference z = mDataBaseRef.child(nome.getText().toString().trim())
                        .child("qrImagem");
                z.setValue(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AddVendedorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void carregarImagem() {
        if (imgUri != null) {

            StorageReference fileReference = mStorageRef.child(nome.getText().toString().trim() + "." + getFileExtension(imgUri));
            mEnviarTarefa = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                    while(!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    DatabaseReference z = mDataBaseRef.child(nome.getText().toString().trim())
                            .child("img");
                    z.setValue(downloadUrl.toString());
                    DatabaseReference x = mDataBaseRef.child(nome.getText().toString().trim())
                            .child("salario");
                    x.setValue(salario.getText().toString().trim());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(AddVendedorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void abrirImagem() {
        Intent i = new Intent();
    }
}