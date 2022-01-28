package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.java.lojaonlineappmaster.R;

public class EsqueceuSenhaActivity extends AppCompatActivity {

    private EditText emailTxt;
    private Button enviarEsquecerBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        emailTxt = findViewById(R.id.editTextEmailEndereco);
        enviarEsquecerBtn = findViewById(R.id.esqueceuBtn);

        progressBar = findViewById(R.id.progressBar);
        mFirebaseAuth = FirebaseAuth.getInstance();

        enviarEsquecerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailTxt.getText().toString().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.sendPasswordResetEmail(emailTxt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(EsqueceuSenhaActivity.this, "A senha foi enviada para seu e-mail, verifique sua caixa de entrada", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(EsqueceuSenhaActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                    emailTxt.setError("O campo está vazio");
            }
        });
    }
}