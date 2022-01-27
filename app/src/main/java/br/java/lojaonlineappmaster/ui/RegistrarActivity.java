package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;

import br.java.lojaonlineappmaster.R;

public class RegistrarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ImageView imagem;
    private Bitmap bitmap;
    public static final int GALERIA_FOTO = 1;
    private StorageReference storageReference;
    private Uri ResultadoURI;
    private String uId;
    private RelativeLayout relativeLayout;
    private Animation animation;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText nome = (EditText) findViewById(R.id.nome);
        final EditText pass1 = (EditText) findViewById(R.id.pass1);
        final EditText pass2 = (EditText) findViewById(R.id.pass2);
        final EditText num = (EditText) findViewById(R.id.num);

        // Cabeçalho
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Animação
        relativeLayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        relativeLayout.setAnimation(animation);

        imagem = findViewById(R.id.image);
        final Button finalizar = (Button) findViewById(R.id.finalizar);
        TextView entrar = (TextView) findViewById(R.id.login);

        imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolherFoto();
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegistrarActivity.this, EntrarActivity.class);
                startActivity(i);
                finish();
            }
        });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || nome.getText().toString().isEmpty() ||
                    pass1.getText().toString().isEmpty() || pass2.getText().toString().isEmpty()) {

                    Toast.makeText(RegistrarActivity.this, "Células vazias", Toast.LENGTH_LONG).show();
                    
                } else if (!pass1.getText().toString().equals(pass2.getText().toString())) {

                    Toast.makeText(RegistrarActivity.this, "você deve escrever a senha em duas caixas", Toast.LENGTH_LONG).show();
                    pass1.setText("");
                    pass2.setText("");

                } else {
                    String mailtxt = email.getText().toString(), passtxt = pass1.getText().toString();
                    mAuth.createUserWithEmailAndPassword(mailtxt, passtxt).addOnCompleteListener(RegistrarActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Nome", nome.getText().toString());
                                hashMap.put("Imagem", "default");
                                hashMap.put("Telefone", num.getText().toString());

                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                uId = currentUser.getUid();

                                DatabaseReference z = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                z.child(uId).setValue(hashMap);

                                if (ResultadoURI != null) CarregarImagemLocalStorageArmazenamento(ResultadoURI);

                                Toast.makeText(RegistrarActivity.this, "Registrado com Sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegistrarActivity.this, "O cadastro falhou", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void escolherFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), GALERIA_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERIA_FOTO && resultCode == RESULT_OK) {

            try {
                Uri ImageUri = data.getData();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(this);

                ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(ImageUri, "r");
                FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                descriptor.close();
            } catch (IOException e) {
                Log.e("B2ala", "Arquivo não encontrado", e);
            }
            imagem.setImageBitmap(bitmap);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ResultadoURI = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void CarregarImagemLocalStorageArmazenamento(Uri resultadoURI) {
        FirebaseUser atualUsuario = mAuth.getCurrentUser();
        uId = atualUsuario.getUid();

        final StorageReference FilePath = storageReference.child("usuarios_imagens").child(uId + "jpg");
        FilePath.putFile(resultadoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference mUsuarioDatabse = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uId);
                        mUsuarioDatabse.child("Imagem").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }
}