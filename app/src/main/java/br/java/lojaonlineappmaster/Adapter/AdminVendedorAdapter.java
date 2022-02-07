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
import br.java.lojaonlineappmaster.model.AdminVendedor;

public class AdminVendedorAdapter extends RecyclerView.Adapter<AdminVendedorAdapter.VendedorViewHolder> {

    private Context context;
    private List<AdminVendedor> vendedorLista;
    private AdminOfertaAdapter.onItemClickListener itemClickListener;
    private AdminOfertaAdapter.onLongClickListener longClickListener;

    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    public interface onLongClickListener {
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(AdminOfertaAdapter.onItemClickListener listener) {

        itemClickListener = listener;
    }

    public  void setOnLongClickListener(AdminOfertaAdapter.onLongClickListener listener) {

        longClickListener = listener;
    }

    public AdminVendedorAdapter(Context context, List<AdminVendedor> vendedorLista) {
        this.context = context;
        this.vendedorLista = vendedorLista;
    }

    public void addList(List<AdminVendedor> lista) {
        vendedorLista.clear();
        Collections.copy(vendedorLista, lista);
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VendedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_vendedor_lista, parent, false);

        return new VendedorViewHolder(v, itemClickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VendedorViewHolder holder, int position) {
        holder.img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));

        Picasso.get().load(vendedorLista.get(position).getImg()).centerCrop().fit().into(holder.img);

        holder.nome.setText("Nome: " + vendedorLista.get(position).getNome());
        holder.salario.setText("Sálario: " + vendedorLista.get(position).getSalario() + " R$");
    }

    @Override
    public int getItemCount() {
        return vendedorLista.size();
    }
    public static class VendedorViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView nome;
        TextView salario;
        CardView cardView;

        public VendedorViewHolder(@NonNull View itemView,
                                  final AdminOfertaAdapter.onItemClickListener itemClickListener,
                                  final AdminOfertaAdapter.onLongClickListener longClickListener) {
            super(itemView);

            img = itemView.findViewById(R.id.adapter_vendedor_imagem);
            nome = itemView.findViewById(R.id.adapter_vendedor_nome);
            salario = itemView.findViewById(R.id.adapter_vendedor_salario);
            cardView = itemView.findViewById(R.id.vendedor_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int posicao = getAdapterPosition();

                        if (posicao != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(posicao);
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
