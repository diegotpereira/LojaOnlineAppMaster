package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.CategoriaProdutoInfo;

public class CategoriaProdutoInfoAdapter extends RecyclerView.Adapter<CategoriaProdutoInfoAdapter.ViewHolder> {

    private RecyclerViewClickListener listener;
    private FirebaseAuth mAuth;
    private FirebaseUser AtualUsuario;
    private String UsuarioId;

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

    public CategoriaProdutoInfoAdapter(Context context, List<CategoriaProdutoInfo> ProdutoLista, RecyclerViewClickListener listener) {
        this.context = context;
        this.ProdutoLista = ProdutoLista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categoria_produtos_lista, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaProdutoInfoAdapter.ViewHolder holder, int position) {
        final CategoriaProdutoInfo produto = ProdutoLista.get(position);

        Picasso.get().load(produto.getProdutoImagem()).into(holder.ProdutoImagem);
        holder.ProdutoNome.setText(produto.getProdutoNome());
        holder.ProdutoPreco.setText("Preço: " + produto.getProdutoPreco() + " EGP");
        holder.ProdutoDataVencimento.setText("Data de Vencimento: " + produto.getProdutoDataVencimento());

        if (produto.getProdutoDataVencimento().equalsIgnoreCase("null")) holder.ProdutoDataVencimento.setVisibility(View.INVISIBLE);
        else holder.ProdutoDataVencimento.setVisibility(View.VISIBLE);

        if (produto.isEhFavorito()) holder.PrFavoritoImagem.setImageResource(R.drawable.ic_baseline_favorite_24);
        else holder.PrFavoritoImagem.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);

        // ao clicar no ícone favorito
        holder.PrFavoritoImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                AtualUsuario = mAuth.getCurrentUser();
                UsuarioId = AtualUsuario.getUid();

                if (produto.isEhFavorito()) {
                    holder.PrFavoritoImagem.setImageResource(R.drawable.ic_baseline_favorite_shadow_24);
                    produto.setEhFavorito(false);

                    // aqui exclua isFavorite do firebase
                    DatabaseReference x = FirebaseDatabase.getInstance().getReference().child("favoritos").child(UsuarioId);
                    x.child(produto.getProdutoNome()).removeValue();
                } else {
                    holder.PrFavoritoImagem.setImageResource(R.drawable.ic_baseline_favorite_24);
                    produto.setEhFavorito(true);

                    // aqui salve isFavorite no firebase
                    DatabaseReference x = FirebaseDatabase.getInstance().getReference("favoritos").child(UsuarioId).child(produto.getProdutoNome());
                    x.child("verificado").setValue(true);
                    x.child("produtoimagem").setValue(produto.getProdutoImagem());
                    x.child("produtopreco").setValue("EGP " + produto.getProdutoPreco());
                    x.child("produtotitulo").setValue(produto.getProdutoNome());
                }
            }
        });

        // Animação
        holder.PrContainer.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.PrFavoritoImagem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.ProdutoNome.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.ProdutoPreco.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.ProdutoDataVencimento.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.ProdutoImagem.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
    }

    @Override
    public int getItemCount() {
        return ProdutoLista.size();
    }
}
