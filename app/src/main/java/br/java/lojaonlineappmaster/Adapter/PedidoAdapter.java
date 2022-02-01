package br.java.lojaonlineappmaster.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.MeuPedidoModelo;
import br.java.lojaonlineappmaster.ui.ScannerQRCodeActivity;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    private List<MeuPedidoModelo> pedidoItensLista;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pedidoData;
        private TextView pedidoNums;
        private TextView pedidoPreco;
        private TextView produtosPedido;
        private TextView verificarPedido;
        private Button ScannerQrCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pedidoData = itemView.findViewById(R.id.pedido_data);
            pedidoNums = itemView.findViewById(R.id.pedido_numeros);
            pedidoPreco = itemView.findViewById(R.id.pedido_preco);
            produtosPedido = itemView.findViewById(R.id.produtos_pedidos);
            verificarPedido = itemView.findViewById(R.id.pedido_verificado);
            ScannerQrCode = itemView.findViewById(R.id.ScanQRCode);
        }
    }

    public PedidoAdapter(Context context, List<MeuPedidoModelo> pedidoItensLista) {
        this.context = context;
        this.pedidoItensLista = pedidoItensLista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pedidoItemView = LayoutInflater.from(context).inflate(R.layout.pedido_item_layout, parent, false);
        return new ViewHolder(pedidoItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoAdapter.ViewHolder holder, int position) {
        final MeuPedidoModelo modelo = pedidoItensLista.get(position);

        holder.pedidoData.setText(modelo.getData());
        holder.pedidoNums.setText(modelo.getPedidoNums());
        holder.pedidoPreco.setText(modelo.getPedidoPreco());
        holder.produtosPedido.setText(modelo.getProdutosPedido());

        if (modelo.getVerificarPedido().equalsIgnoreCase("false")) {
            holder.verificarPedido.setText("Pedido: Pendente");
            holder.ScannerQrCode.setVisibility(View.VISIBLE);
        } else {
            holder.verificarPedido.setText("Pedido: Recebido");
            holder.ScannerQrCode.setVisibility(View.GONE);
        }

        holder.ScannerQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScannerQRCodeActivity.class);
                intent.putExtra("PedidoId", modelo.getPedidoID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidoItensLista.size();
    }
}
