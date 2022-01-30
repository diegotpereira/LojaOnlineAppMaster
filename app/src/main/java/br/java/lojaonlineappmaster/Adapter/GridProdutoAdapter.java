package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.FavoritaClasse;
import br.java.lojaonlineappmaster.model.HorizontalProdutoModel;
import br.java.lojaonlineappmaster.ui.ProdutoInfoActivity;

public class GridProdutoAdapter extends BaseAdapter {
    List<HorizontalProdutoModel> horizontalProdutoModelLista;
    ImageView produtoImagem;
    TextView produtoTitulo;
    TextView produtoPreco;
    ImageView checkBox;
    List<FavoritaClasse> favoritos;
    ConstraintLayout Container;
    Context context;

    public GridProdutoAdapter(List<HorizontalProdutoModel> horizontalProdutoModelLista,
                              List<FavoritaClasse> favoritos, Context context) {
        this.horizontalProdutoModelLista = horizontalProdutoModelLista;
        this.favoritos = favoritos;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (horizontalProdutoModelLista == null) {
            return 0;
        }
        return horizontalProdutoModelLista.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, null);
            Container = view.findViewById(R.id.PrincipalProdutoID);
            produtoImagem = view.findViewById(R.id.item_imagem);
            produtoTitulo = view.findViewById(R.id.item_titulo);
            produtoPreco = view.findViewById(R.id.item_preco);
            checkBox = view.findViewById(R.id.check_box);

            try {
                Picasso.get().load(horizontalProdutoModelLista.get(position).getProdutoImagem())
                        .into(produtoImagem);
            } catch (Exception e) {
                e.printStackTrace();
            }



            produtoTitulo.setText(horizontalProdutoModelLista.get(position).getProdutoTitulo());
            produtoPreco.setText("EGP " + horizontalProdutoModelLista.get(position).getProdutoPreco());

            boolean ehFavorito = false;

            for(int index = 0; index < favoritos.size(); index++) {
                if (horizontalProdutoModelLista.get(position).getProdutoTitulo()
                        .equals(favoritos.get(index).getProdutoTitulo())) {

                    ehFavorito = true;

                    horizontalProdutoModelLista.get(position).setVerificado(true);

                    break;
                }
            }
            if (ehFavorito) {
                checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else
                checkBox.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
        } else {
            view = convertView;
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("favoritos")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                HorizontalProdutoModel hz = horizontalProdutoModelLista.get(position);

                if (!(horizontalProdutoModelLista.get(position).isVerificado())) {
                    horizontalProdutoModelLista.get(position).setVerificado(true);
                    checkBox = view.findViewById(R.id.check_box);
                    checkBox.setImageResource(R.drawable.ic_baseline_favorite_24);
                    ref.child(horizontalProdutoModelLista.get(position).getProdutoTitulo()).setValue(hz);
                } else {
                    horizontalProdutoModelLista.get(position).setVerificado(false);
                    checkBox = view.findViewById(R.id.check_box);
                    checkBox.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    ref.child(horizontalProdutoModelLista.get(position).getProdutoTitulo()).setValue(null);
                }
            }
        });
        Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProdutoInfoActivity.class);
                intent.putExtra("Produto Nome", horizontalProdutoModelLista.get(position).getProdutoTitulo());
                intent.putExtra("Produto Preço", horizontalProdutoModelLista.get(position).getProdutoPreco());
                intent.putExtra("Produto Imagem", horizontalProdutoModelLista.get(position).getProdutoImagem());
                intent.putExtra("Produto Data Vencimento", horizontalProdutoModelLista.get(position).getDataVencimento());
                intent.putExtra("Produto Favorito", String.valueOf(horizontalProdutoModelLista.get(position).isVerificado()));
                intent.putExtra("Eh Oferecido", "não");

                context.startActivity(intent);
            }
        });
        return view;
    }
}
