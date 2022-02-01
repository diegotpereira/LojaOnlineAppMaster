package br.java.lojaonlineappmaster.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import br.java.lojaonlineappmaster.R;

public class TelaDeAberturaActivity extends AppCompatActivity {

    private TextView texto;
    private TextView appTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_abertura);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAGS_CHANGED,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOpcoes = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOpcoes);
        }
        appTexto = findViewById(R.id.appSplashNome);
        texto = findViewById(R.id.bem_vindo);

        YoYo.with(Techniques.FlipInX)
                .duration(3000)
                .repeat(0)
                .playOn(appTexto);

        YoYo.with(Techniques.ZoomIn)
                .duration(3000)
                .repeat(0)
                .playOn(texto);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(TelaDeAberturaActivity.this, EntrarActivity.class));
                finish();
            }
        }, 3500);
    }
}