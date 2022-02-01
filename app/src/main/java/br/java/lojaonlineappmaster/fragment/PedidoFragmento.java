package br.java.lojaonlineappmaster.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.java.lojaonlineappmaster.Adapter.PedidoAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.MeuPedidoModelo;

public class PedidoFragmento extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<MeuPedidoModelo> pedidoItensLista;
    PedidoAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference root;
    private DatabaseReference m;
    private String AtualUsuario;

    private static Activity fa;


    public PedidoFragmento() {
        // Required empty public constructor
    }

    private RecyclerView PedidoItemRecyclerView;

    public static PedidoFragmento newInstance(String param1, String param2) {
        PedidoFragmento fragment = new PedidoFragmento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmento_pedido, container, false);
        fa = getActivity();
        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser().getUid();

        PedidoItemRecyclerView = view.findViewById(R.id.pedido_recycler);
        pedidoItensLista = new ArrayList<MeuPedidoModelo>();
        adapter = new PedidoAdapter(getActivity(), pedidoItensLista);

        PedidoItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PedidoItemRecyclerView.setAdapter(adapter);

        DatabaseReference roott = FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = roott.child("pedido").child(AtualUsuario);

        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String Data = dataSnapshot.child("Data").getValue().toString();
                        int nums = ((int) (dataSnapshot.child("produtosPedido").getChildrenCount()));
                        String precoTotal = dataSnapshot.child("precoTotal").getValue().toString();
                        String verificarPedido = dataSnapshot.child("EhVerificado").getValue().toString();

                        String produtos = "Produtos :\n";
                        for(DataSnapshot data : dataSnapshot.child("produtosPedido")
                                .getChildren()) {

                            produtos += " #" + data.getKey() + "\n Preço: " + data
                                    .child("produtoPreco").getValue()
                                    .toString() + " R$\n Quantidade: " + data
                                    .child("quantidade").getValue().toString() + "\n";
                        }
                        pedidoItensLista.add(new MeuPedidoModelo(dataSnapshot
                                .getKey(), " Data: "
                                + Data, " Produtos Número: "
                                + String.valueOf(nums), " Preço Total: "
                                + precoTotal + " R$", " "
                                + produtos, verificarPedido));
                    }
                } else {
                    pedidoItensLista.clear();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        x.addListenerForSingleValueEvent(valueEventListener1);
        return view;
    }
}