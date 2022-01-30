package br.java.lojaonlineappmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.FavoritaClasse;

public class MeuAdapter_Recycler_View extends RecyclerView.Adapter<MeuAdapter_Recycler_View.ViewHolder> {

    private List<FavoritaClasse> horizontalProdutoModelLista;

    public MeuAdapter_Recycler_View(List<FavoritaClasse> horizontalProdutoModelLista) {
        this.horizontalProdutoModelLista = horizontalProdutoModelLista;
    }

    @NonNull
    @Override
    public MeuAdapter_Recycler_View.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuAdapter_Recycler_View.ViewHolder holder, int position) {
        FavoritaClasse horizontalProdutoModel = horizontalProdutoModelLista.get(position);

        holder.produtoTitulo.setText(horizontalProdutoModel.getProdutoTitulo());
        holder.produtoPreco.setText(horizontalProdutoModel.getProdutoPreco());

        Picasso.get().load(horizontalProdutoModel.getProdutoImagem()).into(holder.produtoImagem);
        holder.checkbox.setImageResource(R.drawable.ic_baseline_favorite_24);

    }

    @Override
    public int getItemCount() {
        return horizontalProdutoModelLista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView produtoImagem;
        TextView produtoTitulo;
        TextView produtoPreco;
        ImageView checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            produtoImagem = itemView.findViewById(R.id.item_imagem);
            produtoTitulo = itemView.findViewById(R.id.item_titulo);
            produtoPreco = itemView.findViewById(R.id.item_preco);
            checkbox = itemView.findViewById(R.id.check_box);
        }
    }
}
