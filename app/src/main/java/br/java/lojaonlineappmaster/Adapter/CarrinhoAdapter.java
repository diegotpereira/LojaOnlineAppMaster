package br.java.lojaonlineappmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.CarrinhoItemModelo;

public class CarrinhoAdapter extends RecyclerView.Adapter {

    private List<CarrinhoItemModelo> carrinhoItemModeloLista;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void emDeletarClique(int position);
        void atualizarPrecoTotal(String str);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CarrinhoAdapter(List<CarrinhoItemModelo> carrinhoItemModeloLista) {
        this.carrinhoItemModeloLista = carrinhoItemModeloLista;
    }

    @Override
    public int getItemViewType(int position) {
        switch (carrinhoItemModeloLista.get(position).getTipo()) {
            case 0:
                return CarrinhoItemModelo.carrinho_item;

            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View carrinhoItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carrinho_item_layout, parent, false);

        return new carrinhoItemViewHolder(carrinhoItemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String recurso =  carrinhoItemModeloLista.get(position).getProdutoImagem();
        String titulo = carrinhoItemModeloLista.get(position).getProdutoTitulo();
        int livreCupons = carrinhoItemModeloLista.get(position).getCupon();
        int produtoPreco = carrinhoItemModeloLista.get(position).getPreco();

        String reduzidoPreco = String.valueOf(carrinhoItemModeloLista.get(position).getPrecoReduzido());
        int ofertaAplicada = carrinhoItemModeloLista.get(position).getOfertaAplicada();
        int quantidade = carrinhoItemModeloLista.get(position).getQuantidade();
        ((carrinhoItemViewHolder) holder).definirItemDetalhe(
                recurso,
                titulo,
                livreCupons,
                produtoPreco,
                quantidade,
                reduzidoPreco,
                ofertaAplicada);
    }

    @Override
    public int getItemCount() {
        return carrinhoItemModeloLista.size();
    }

    class carrinhoItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView produtoImagem;
        private TextView produtoTitulo;
        private TextView livreCupon;
        private TextView produtoPreco;
        private TextView reduzidoPreco;
        private TextView ofertaAplicada;
        private TextView cuponAplicado;
        private TextView produtoQuantidade;
        private ImageView cuponIcone;
        private ImageView PlusIcon;
        private ImageView MinusIcon;
        private ImageView CarrinhoItemDeletar;
        public boolean deletadoItem = false;

        int precoTotalValor;
        DatabaseReference root;
        String AtualUsuario;
        private FirebaseAuth mAuth;

        public carrinhoItemViewHolder(@NonNull View itemView,
                                      final CarrinhoAdapter.OnItemClickListener listener) {
            super(itemView);

            produtoImagem = itemView.findViewById(R.id.produto_imagem);
            produtoTitulo = itemView.findViewById(R.id.produto_titulo);
            livreCupon = itemView.findViewById(R.id.copon_txt);
            produtoPreco = itemView.findViewById(R.id.preco);
            reduzidoPreco = itemView.findViewById(R.id.cut_preco);
            ofertaAplicada = itemView.findViewById(R.id.oferta_texto);
            cuponAplicado = itemView.findViewById(R.id.cupon_aplicado);
            produtoQuantidade = itemView.findViewById(R.id.quan);
            cuponIcone = itemView.findViewById(R.id.coupon);
            PlusIcon = itemView.findViewById(R.id.PlusIcon);
            MinusIcon = itemView.findViewById(R.id.MinusIcon);

            CarrinhoItemDeletar = itemView.findViewById(R.id.Carrinho_Item_Deletar);

            root = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            AtualUsuario = mAuth.getCurrentUser().getUid();

            CarrinhoItemDeletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int posicao = getAdapterPosition();

                        if (posicao != RecyclerView.NO_POSITION) {
                            listener.emDeletarClique(posicao);

                            contarPrecoTotal();
                        }
                    }
                }
            });
        }
        void definirItemDetalhe(String recurso, final String titulo, int livreCuponsNo,
                                final int produtoPrecoTexto,
                                int quantidade,
                                String precoCorte, int ofertaAplicadaNo) {
            Picasso.get().load(recurso).into(produtoImagem);
            produtoTitulo.setText(titulo);

            if (livreCuponsNo > 0) {
                cuponIcone.setVisibility(View.VISIBLE);
                livreCupon.setVisibility(View.VISIBLE);
                cuponAplicado.setVisibility(View.VISIBLE);

                if (livreCuponsNo == 1) {
                    livreCupon.setText("1 cupom grátis");
                } else {
                    livreCupon.setText("livre" + livreCuponsNo + "cupons");
                }
            } else {
                cuponIcone.setVisibility(View.INVISIBLE);
                livreCupon.setVisibility(View.INVISIBLE);
                cuponAplicado.setVisibility(View.INVISIBLE);
            }
            produtoPreco.setText("R$: " + "Preço: " + String.valueOf(produtoPrecoTexto * quantidade));

            if (Integer.parseInt(precoCorte) > 0) {
                reduzidoPreco.setText(precoCorte);
                reduzidoPreco.setVisibility(View.VISIBLE);
            } else {
                reduzidoPreco.setVisibility(View.INVISIBLE);
            }
            produtoQuantidade.setText(String.valueOf(quantidade));

            PlusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    produtoQuantidade.setText(String.valueOf(Integer.parseInt(produtoQuantidade
                            .getText().toString()) + 1));
                    produtoPreco.setText("Preço: " + String.valueOf(produtoPrecoTexto *
                            Integer.parseInt(produtoQuantidade.getText().toString())) + " R$");

                    root.child("carrinho").child(AtualUsuario).child("quantidade")
                            .child("produtoQuantidade").setValue(produtoQuantidade
                            .getText().toString());

                    contarPrecoTotal();
                }
            });

            MinusIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.valueOf(produtoQuantidade.getText().toString()) > 1) {
                        produtoQuantidade.setText(String.valueOf(Integer.parseInt(produtoQuantidade
                                .getText().toString()) - 1));

                        produtoPreco.setText("Preço: " + String.valueOf(produtoPrecoTexto *
                                Integer.parseInt(produtoQuantidade.getText().toString())));

                        contarPrecoTotal();
                    }
                }
            });
            if (ofertaAplicadaNo > 0) {
                ofertaAplicada.setVisibility(View.VISIBLE);
                ofertaAplicada.setText(ofertaAplicadaNo + "ofertas aplicadas");
            } else {
                ofertaAplicada.setVisibility(View.INVISIBLE);
            }
        }
    }
    class valorTotalDoCarrinhoViewHolder extends RecyclerView.ViewHolder {

        private TextView totalItensTitulo;
        private TextView totalItensPreco;
        private TextView precoDeEntrega;
        private TextView valorTotal;
        private TextView quantidadeSalva;

        public valorTotalDoCarrinhoViewHolder(@NonNull View itemView) {
            super(itemView);

            totalItensTitulo = itemView.findViewById(R.id.total_titulos_item);
            totalItensPreco = itemView.findViewById(R.id.total_preco_item);
            precoDeEntrega = itemView.findViewById(R.id.preco_de_entrega);
            valorTotal = itemView.findViewById(R.id.preco_total);

            quantidadeSalva = itemView.findViewById(R.id.quantidade_salva);
        }

        private void definirQuantidadeTotal(String totalItensTexto,
                                            String totalItensPrecoTexto,
                                            String precoEntregaTexto,
                                            String quantidadeTotalTexto,
                                            String quantidadeSalvaTexto) {
            totalItensTitulo.setText(totalItensTexto);
            totalItensPreco.setText(totalItensPrecoTexto);
            precoDeEntrega.setText(precoEntregaTexto);
            valorTotal.setText(quantidadeTotalTexto);
            quantidadeSalva.setText(quantidadeSalvaTexto);
        }
    }

    public void contarPrecoTotal() {
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference m = root.child("carrinho");
        final String AtualUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ValueEventListener valueEventListener = new ValueEventListener() {

            int precoTotalValor = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.child(AtualUsuario).getChildren()) {
                        if (!dataSnapshot.getKey().equals("precoTotal")) {
                            String carrinhoItemPreco = dataSnapshot.child("produtoPreco")
                                    .getValue(String.class).toString();
                            String quantidade = dataSnapshot.child("quantidade")
                                    .getValue(String.class).toString();

                            precoTotalValor += Integer.parseInt(carrinhoItemPreco) *
                                    Integer.parseInt(quantidade);
                        }
                    }
                    root.child("carrinho").child(AtualUsuario).child("precoTotal")
                            .setValue(String.valueOf(precoTotalValor));
                    mListener.atualizarPrecoTotal(String.valueOf(precoTotalValor) + " R$");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }
}
