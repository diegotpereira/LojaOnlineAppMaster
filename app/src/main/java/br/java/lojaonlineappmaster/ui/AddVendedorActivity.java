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
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
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
    private TextInputLayout nomeLayout;
    private TextInputLayout salarioLayout;
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

                    carregarDados();
                    carregarQr();
                    Toast.makeText(AddVendedorActivity.this, "Carregado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        escolher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirImagem();
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
        NaoExibirIconeCarrinho();
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
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, RegistrarActivity.GALERIA_FOTO);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public Bitmap QrGenerate(String x) {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 2 / 4;
        QRGEncoder encoder = new QRGEncoder(x, null, QRGContents.Type.TEXT, smallerDimension);

        return encoder.getBitmap();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegistrarActivity.GALERIA_FOTO && resultCode == Activity.RESULT_OK && data.getData() != null && data != null) {
            imgUri = data.getData();

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