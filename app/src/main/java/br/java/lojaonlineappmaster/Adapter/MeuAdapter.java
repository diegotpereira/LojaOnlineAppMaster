package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.Modelo;
import br.java.lojaonlineappmaster.ui.ProdutoInfoActivity;

public class MeuAdapter extends PagerAdapter {

    private List<Modelo> modelos;
    private LayoutInflater layoutInflater;
    private Context context;

    private String ProdutoNome;
    private String ProdutoPreco;
    private String ProdutoImagem;
    private String ProdutoNDataVencimento;
    private String ProdutoEhFavorito;

    private MeuAdapter(List<Modelo> modelos, Context context) {
        this.modelos = modelos;
        this.context = context;
    }
    @Override
    public int getCount() {
        return modelos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView titulo;
        TextView desc;
        CardView OfertaCardContainer;

        OfertaCardContainer = view.findViewById(R.id.OfertaCardContainer);
        imageView = view.findViewById(R.id.contentImagem);
        titulo = view.findViewById(R.id.contentTitulo);
        desc = view.findViewById(R.id.contentDesc);

        Picasso.get().load(modelos.get(position).getImagem()).into(imageView);
        titulo.setText(modelos.get(position).getTitulo() + "Ofertas");
        desc.setText(modelos.get(position).getDesc());
        container.addView(view);

        OfertaCardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProdutoEhFavorito = "false";
                buscarDados(modelos.get(position).getTitulo());
            }
        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void buscarDados(final String ProdutoNomee) {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("produto");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.child("Frutas").getChildren()) {
                        if (dataSnapshot.getKey().equals(ProdutoNomee)) {
                            ProdutoNome = ProdutoNomee;
                            ProdutoPreco = dataSnapshot.child("preco").getValue().toString();
                            ProdutoImagem = dataSnapshot.child("imagem").getValue().toString();
                            ProdutoNDataVencimento = dataSnapshot.child("dataVencimento").getValue().toString();

                            break;
                        }
                    }
                    for(DataSnapshot dataSnapshot : snapshot.child("Eletronicos").getChildren()) {
                        if (dataSnapshot.getKey().equals(ProdutoNomee)) {
                            ProdutoNome = ProdutoNomee;
                            ProdutoPreco = dataSnapshot.child("preco").getValue().toString();
                            ProdutoImagem = dataSnapshot.child("imagem").getValue().toString();
                            ProdutoNDataVencimento = dataSnapshot.child("dataVencimento").getValue().toString();

                            break;
                        }
                    }

                    for(DataSnapshot dataSnapshot : snapshot.child("Carnes").getChildren()) {
                        if (dataSnapshot.getKey().equals(ProdutoNomee)) {
                            ProdutoNome = ProdutoNomee;
                            ProdutoPreco = dataSnapshot.child("preco").getValue().toString();
                            ProdutoImagem = dataSnapshot.child("imagem").getValue().toString();
                            ProdutoNDataVencimento = dataSnapshot.child("dataVencimento").getValue().toString();

                            break;
                        }
                    }
                    for(DataSnapshot dataSnapshot : snapshot.child("Vegetais").getChildren()) {
                        if (dataSnapshot.getKey().equals(ProdutoNomee)) {
                            ProdutoNome = ProdutoNomee;
                            ProdutoPreco = dataSnapshot.child("preco").getValue().toString();
                            ProdutoImagem = dataSnapshot.child("imagem").getValue().toString();
                            ProdutoNDataVencimento = dataSnapshot.child("dataVencimento").getValue().toString();

                            break;
                        }
                    }
                    buscarEhFavorito(ProdutoNomee);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }
    private void buscarEhFavorito(final String ProdutoNomee) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UsuarioId = mAuth.getCurrentUser().getUid();

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("favoritos").child(UsuarioId);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(ProdutoNomee)) {
                        ProdutoEhFavorito = "true";

                        break;
                    }
                    else ProdutoEhFavorito = "false";
                }
                IrParaProduto();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }
    private void IrParaProduto() {
        Intent intent = new Intent(context, ProdutoInfoActivity.class);
        intent.putExtra("Produto Nome", ProdutoNome);
        intent.putExtra("Produto Preco", ProdutoPreco);
        intent.putExtra("Produto Imagem", ProdutoImagem);
        intent.putExtra("Produto Data Vencimento", ProdutoNDataVencimento);
        intent.putExtra("Produto EhFavorito", ProdutoEhFavorito);
        intent.putExtra("Eh Oferecido", "Sim");
        context.startActivity(intent);
    }
}
