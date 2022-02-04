package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.AdminVendedor;

public class AdminVendedorAdapter extends RecyclerView.Adapter<AdminVendedorAdapter
        .VendedorViewHolder> {

    private Context context;
    private List<AdminVendedor> vendedorLista;
    private AdminOfertaAdapter.onItemClickListener itemListener;
    private AdminOfertaAdapter.onLongClickListener longListener;

    public interface onItemClickListener {
        void onItemClick(int pos);
    }

    public  interface onLongClickListener {
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(AdminOfertaAdapter.onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setOnLongClickListener(AdminOfertaAdapter.onLongClickListener listener) {
        longListener = listener;
    }


    @NonNull
    @Override
    public VendedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_vendedor_lista, parent, false);

        return new VendedorViewHolder(v, itemListener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VendedorViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class VendedorViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView nome;
        TextView salario;
        CardView cardView;

        public VendedorViewHolder(@NonNull View itemView,
                                  final AdminOfertaAdapter.onItemClickListener itemListener,
                                  final AdminOfertaAdapter.onLongClickListener longClickListener) {
            super(itemView);

            img = itemView.findViewById(R.id.adapter_vendedor_imagem);
            nome = itemView.findViewById(R.id.adapter_vendedor_nome);
            salario = itemView.findViewById(R.id.adapter_vendedor_salario);
            cardView = itemView.findViewById(R.id.vendedor_card_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posicao = getAdapterPosition();

                    if (posicao != RecyclerView.NO_POSITION) {
                        itemListener.onItemClick(posicao);
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
