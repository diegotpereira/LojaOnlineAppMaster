package br.java.lojaonlineappmaster.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.fragment.PedidoFragmento;
import br.java.lojaonlineappmaster.model.Usuario;

public class ScannerQRCodeActivity extends AppCompatActivity {

    private CodeScanner codigoScanner;
    private CodeScannerView codigoScannerView;
    private TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsuario;
    private String UsuarioId;
    private String PedidoId;
    private Toolbar mTollbar;
    private RelativeLayout CarrinhoPersonalizadoContainer;
    private TextView PaginaTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_qrcode);

        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser();
        UsuarioId = AtualUsuario.getUid();

        // ToolBar
        mTollbar = (Toolbar) findViewById(R.id.QRScanner_TooBar);
        setSupportActionBar(mTollbar);
        getSupportActionBar().setTitle("Scanner QR Code");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NaoExibriIconeCarrinho();

        codigoScannerView = findViewById(R.id.ScannerView);
        textView = (TextView) findViewById(R.id.texto);
        codigoScanner = new CodeScanner(this, codigoScannerView);

        codigoScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PesquisarSobreVendedor(result.getText());
                        Vibrator vibrator = (Vibrator) getApplicationContext()
                                .getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                    }
                });
            }
        });

        codigoScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigoScanner.startPreview();
                textView.setText("por favor, foque a câmera no código QR");
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        solicitarAhCamera();
    }

    private void solicitarAhCamera() {
        Dexter.withActivity(ScannerQRCodeActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
              codigoScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ScannerQRCodeActivity.this,
                        "A permissão da câmera é solicitada", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void PesquisarSobreVendedor(String nome) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = root.child("vendedor").child(nome);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().child("pedido").child(UsuarioId)
                            .child(PedidoId).child("EhVerificado").setValue("true");

                    Toast.makeText(ScannerQRCodeActivity.this,
                            "Pedido recebido com sucesso", Toast.LENGTH_LONG).show();
                    PedidoFragmento.fa.finish();
                    finish();
                } else {
                    textView.setText("Esta entrega não existe em nosso sistema\nTente novamente");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        x.addListenerForSingleValueEvent(valueEventListener);
    }

    private void NaoExibriIconeCarrinho() {
        //Toolbar & carrinho icone
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main2_toolbar, null);

        // Ação Personalizada itens xml
        CarrinhoPersonalizadoContainer = (RelativeLayout) findViewById(R.id.CarrinhoPersonalizadoContainer);
        PaginaTitulo = (TextView) findViewById(R.id.PaginaTitulo);
        PaginaTitulo.setVisibility(View.GONE);
        CarrinhoPersonalizadoContainer.setVisibility(View.GONE);
    }
}