package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import br.java.lojaonlineappmaster.R;

public class EntrarActivity extends AppCompatActivity {

    TextInputEditText mPassword;
    EditText mEmail;
    Button mEntrar;
    TextView mEsqueceuSenha, tvEntrar;
    ImageButton mCriarBtn;
    FirebaseAuth fAuth;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);

        mEmail = (EditText) findViewById(R.id.EmailLogin);
        mPassword = findViewById(R.id.PasswordLogin);
        fAuth = FirebaseAuth.getInstance();
        tvEntrar = findViewById(R.id.tvEntrar);
        mEntrar = (Button) findViewById(R.id.Login);

        mCriarBtn = (ImageButton) findViewById(R.id.SignUPtext);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mEsqueceuSenha = (TextView) findViewById(R.id.EsqueceuSenha);

        if (fAuth.getCurrentUser() != null) {
            if (fAuth.getCurrentUser().getEmail().equals("admin@gmail.com")) {
                startActivity(new Intent(EntrarActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(EntrarActivity.this, MainActivity.class));
                finish();
            }
        }
        mEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = mEmail.getText().toString().trim();
                final String Password = mPassword.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError("O e-mail é obrigatório");

                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    mPassword.setError("A senha é obrigatória");

                    return;
                }

                if (Password.length() < 6) {
                    mPassword.setError("A senha deve ser maior ou igual a 6 caracteres");

                    return;
                }
                // progresso em segundo plano e eu torná-lo aqui visível.
                mProgressBar.setVisibility(View.VISIBLE);

                // Autenticação
                fAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (Email.equals("admin@gmail.com") && Password.equals("password")) {

                                Toast.makeText(EntrarActivity.this, "Bem vindo meu criador", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EntrarActivity.this, AdminActivity.class));
                                finish();
                            } else {
                                Toast.makeText(EntrarActivity.this, "Conectado com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EntrarActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(EntrarActivity.this, "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mCriarBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntrarActivity.this, RegistrarActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvEntrar, "tvEntrar");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(EntrarActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
            }
        });
        mEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EntrarActivity.this, EsqueceuSenhaActivity.class));
            }
        });
    }
}