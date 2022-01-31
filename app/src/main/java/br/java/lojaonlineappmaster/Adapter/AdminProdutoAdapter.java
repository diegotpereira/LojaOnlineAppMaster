package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.AdminProduto;

public class AdminProdutoAdapter extends RecyclerView.Adapter<AdminProdutoAdapter.ProdutoViewHolder> {

    private Context context;
    private List<AdminProduto> adminProdutos;
    private AdminOfertaAdapter.onItemClickListener itemListener;
    private AdminOfertaAdapter.onLongClickListener longListener;

    // no Ouvinte de cliques do item
    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    public interface onLongClickListener {
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(AdminOfertaAdapter.onItemClickListener listener) {
        itemListener = listener;
    }

    public void setOnLongClickListener(AdminOfertaAdapter.onLongClickListener listener) {
        longListener = listener;
    }

    public AdminProdutoAdapter(Context context, List<AdminProduto> adminProdutos) {
        this.context = context;
        this.adminProdutos = adminProdutos;
    }

    public void addList(List<AdminProduto> lista) {
        adminProdutos.clear();
        Collections.copy(adminProdutos, lista);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_produto_lista, parent, false);

        return new ProdutoViewHolder(v, itemListener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        Picasso.get().load(adminProdutos.get(position).getImagem()).centerCrop().fit().into(holder.img);

        holder.nome.setText(adminProdutos.get(position).getNome());
        holder.categoria.setText("Categoria: " + adminProdutos.get(position).getCategoria());
        holder.quantidade.setText("Quantidade Disponivél: " + adminProdutos.get(position).getQuantidade());
        holder.preco.setText("R$" + "Preço: " + adminProdutos.get(position).getPreco());

        if (adminProdutos.get(position).getDataVencimento().equalsIgnoreCase("null"))
            holder.dataVencimento.setVisibility(View.GONE);
        else holder.dataVencimento.setVisibility(View.VISIBLE);

        holder.dataVencimento.setText("Data de Vencimento: " + adminProdutos.get(position).getDataVencimento());
    }

    @Override
    public int getItemCount() {
        return adminProdutos.size();
    }


    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView nome;
        TextView categoria;
        TextView quantidade;
        TextView preco;
        TextView dataVencimento;
        CardView cardView;

        public ProdutoViewHolder(@NonNull View itemView,
                                 final AdminOfertaAdapter.onItemClickListener itemlistener,
                                 final AdminOfertaAdapter.onLongClickListener longClickListener) {
            super(itemView);
            img = itemView.findViewById(R.id.adapterProdutoImagem);
            nome = itemView.findViewById(R.id.adapterProdutoNome);
            categoria = itemView.findViewById(R.id.adapterProdutoCategoria);
            quantidade = itemView.findViewById(R.id.AdapterProdutoQuantidade);
            preco = itemView.findViewById(R.id.adapterProdutoPreco);
            dataVencimento = itemView.findViewById(R.id.adapterProdutoDataVencimento);
            cardView = itemView.findViewById(R.id.ProductCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemlistener != null) {
                        int posicao = getAdapterPosition();

                        if (posicao != RecyclerView.NO_POSITION) {
                            itemlistener.onItemClick(posicao);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener != null) {
                        int posicao = getAdapterPosition();

                        if (posicao != RecyclerView.NO_POSITION)
                            longClickListener.onItemLongClick(posicao);
                    }
                    return false;
                }
            });
        }
    }
}
