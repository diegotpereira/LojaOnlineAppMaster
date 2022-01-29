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
import br.java.lojaonlineappmaster.model.AdminOferta;

public class AdminOfertaAdapter extends RecyclerView.Adapter<AdminOfertaAdapter.ViewHolder> {
    private Context context;
    private onItemClickListener itemListener;
    private onLongClickListener longListener;
    private List<AdminOferta> ofertas;

    // no Ouvinte de cliques do item
    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    public interface onLongClickListener {
        void onItemLongClick(int pos);
    }

    public void setItemListener(onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setLongListener(onLongClickListener longListener) {
        this.longListener = longListener;
    }

    public AdminOfertaAdapter(Context context, List<AdminOferta> ofertas) {
        this.context = context;
        this.ofertas = ofertas;
    }

    public void addLista(List<AdminOferta> lista) {
        ofertas.clear();
        Collections.copy(ofertas, lista);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_opcoes_lista, parent, false);

        return new ViewHolder(v, itemListener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

        Picasso.get().load(ofertas.get(position).getOfertaImg()).centerCrop().fit().into(holder.img);

        holder.nome.setText(ofertas.get(position).getOfertaNome());
        holder.descricao.setText(ofertas.get(position).getOfertaDescricao());
    }

    @Override
    public int getItemCount() {
        return ofertas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nome;
        TextView descricao;
        CardView cardView;

        public ViewHolder(@NonNull View itemView, final onItemClickListener itemListener,
                          final onLongClickListener longClickListener) {
            super(itemView);

            img = itemView.findViewById(R.id.adapterOfertaImagem);
            nome = itemView.findViewById(R.id.adapterOfertaNome);
            descricao = itemView.findViewById(R.id.adapterOfertaDescricao);

            cardView = itemView.findViewById(R.id.OfertaCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemListener != null) {
                        int posicao = getAdapterPosition();

                        if (posicao != RecyclerView.NO_POSITION) {
                            itemListener.onItemClick(posicao);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener != null) {
                        int posicao = getAdapterPosition();

                        if (posicao != RecyclerView.NO_POSITION) {
                            longClickListener.onItemLongClick(posicao);
                        }
                    }
                    return false;
                }
            });
        }
    }
}
