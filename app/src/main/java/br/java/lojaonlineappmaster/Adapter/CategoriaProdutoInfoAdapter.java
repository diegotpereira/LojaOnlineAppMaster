package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.CategoriaProdutoInfo;

public class CategoriaProdutoInfoAdapter extends RecyclerView.Adapter<CategoriaProdutoInfoAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener listener;

        private RelativeLayout PrContainer;
        private ImageView ProdutoImagem;
        private TextView ProdutoNome;
        private TextView ProdutoPreco;
        private TextView ProdutoDataVencimento;
        private ImageView PrFavoritoImagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PrContainer = (RelativeLayout) itemView.findViewById(R.id.PrContainer);
            ProdutoImagem = (ImageView) itemView.findViewById(R.id.PrImagem);
            ProdutoNome = (TextView) itemView.findViewById(R.id.PrNome);
            ProdutoPreco = (TextView) itemView.findViewById(R.id.PrPreco);
            ProdutoDataVencimento = (TextView) itemView.findViewById(R.id.PrDataVencimento);
            PrFavoritoImagem = (ImageView) itemView.findViewById(R.id.PrFavoritaImagem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }


    private Context context;
    private List<CategoriaProdutoInfo> ProdutoLista;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categoria_produtos_lista, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaProdutoInfoAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
