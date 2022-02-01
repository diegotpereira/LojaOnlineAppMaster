package br.java.lojaonlineappmaster.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.java.lojaonlineappmaster.Adapter.CarrinhoAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.CarrinhoItemModelo;
import br.java.lojaonlineappmaster.ui.CarrinhoVerificarActivity;


public class MeuCarrinhoFragmento extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<CarrinhoItemModelo> carrinhoItemModeloLista;
    private LinearLayout Carrinho_container;
    private TextView SemItem;
    public static TextView precoTotal;

    CarrinhoAdapter carrinhoAdapter;
    private FirebaseAuth mAuth;
    private String AtualUsuario;
    private DatabaseReference m;
    private DatabaseReference root;

    public int precoTotalValor = 0;

    public MeuCarrinhoFragmento() {
        // Required empty public constructor
    }

    private RecyclerView CarrinhoItemRecyclerView;
    private Button CarrinhoConfirmar;
    private Button CarrinhoLimpar;

    public static MeuCarrinhoFragmento newInstance(String param1, String param2) {
        MeuCarrinhoFragmento fragment = new MeuCarrinhoFragmento();
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
        mAuth = FirebaseAuth.getInstance();
        AtualUsuario = mAuth.getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmento_meu_carrinho, container, false);
        CarrinhoItemRecyclerView = view.findViewById(R.id.carrinho_recycle);
        CarrinhoConfirmar = view.findViewById(R.id.carrinho_confirmarBtn);
        CarrinhoLimpar = view.findViewById(R.id.carrinho_limparBtn);
        precoTotal = view.findViewById(R.id.precoTotal);
        Carrinho_container = view.findViewById(R.id.carrinho_container);

        SemItem = view.findViewById(R.id.sem_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        CarrinhoItemRecyclerView.setLayoutManager(layoutManager);

        carrinhoItemModeloLista = new ArrayList<CarrinhoItemModelo>();

        root = FirebaseDatabase.getInstance().getReference();
        m = root.child("carrinho");

        carrinhoAdapter = new CarrinhoAdapter(carrinhoItemModeloLista);
        CarrinhoItemRecyclerView.setAdapter(carrinhoAdapter);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.child(AtualUsuario).getChildren()) {
                        if (!dataSnapshot.getKey().equals("precoTotal")) {
                            Log.d("kk", dataSnapshot.getKey());

                            String carrinhoItemTitulo = dataSnapshot.getKey();
                            String carrinhoItemImagem = dataSnapshot.child("produtoImagem")
                                    .getValue(String.class).toString();
                            String carrinhoItemPreco = dataSnapshot.child("produtoPreco")
                                    .getValue(String.class).toString();
                            String quantidade = dataSnapshot.child("quantidade")
                                    .getValue(String.class).toString();

                            carrinhoItemModeloLista.add(new CarrinhoItemModelo(
                                    0,
                                    carrinhoItemImagem,
                                    carrinhoItemTitulo,
                                    0,
                                    Integer.parseInt(carrinhoItemPreco),
                                    0,
                                    Integer.parseInt(quantidade),
                                    0,
                                    0));
                        }
                    }
                    precoTotalDaConta();
                    carrinhoAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);

        CarrinhoConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CarrinhoVerificarActivity.class));
            }
        });

        CarrinhoLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carrinhoItemModeloLista.clear();
                root.child("carrinho").child(AtualUsuario).removeValue();
                carrinhoAdapter.notifyDataSetChanged();
                Carrinho_container.setVisibility(View.GONE);
                SemItem.setVisibility(View.VISIBLE);
            }
        });

        carrinhoAdapter.setOnItemClickListener(new CarrinhoAdapter.OnItemClickListener(){
            @Override
            public void atualizarPrecoTotal(String str) {
                precoTotal.setText(str);
            }

            @Override
            public void emDeletarClique(int position) {
                String x  = carrinhoItemModeloLista.get(position).getProdutoTitulo();
                m.child(AtualUsuario).child(x).removeValue();
                carrinhoItemModeloLista.remove(position);
                carrinhoAdapter.notifyItemRemoved(position);
            }
        });

        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference x = root.child("carrinho").child(AtualUsuario);

        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() <= 1) {
                    if (snapshot.exists()) {
                        Carrinho_container.setVisibility(View.GONE);
                        SemItem.setVisibility(View.VISIBLE);
                    } else {
                        Carrinho_container.setVisibility(View.VISIBLE);
                        SemItem.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        x.addListenerForSingleValueEvent(valueEventListener1);

        return view;
    }

    public void precoTotalDaConta() {

        precoTotalValor = 0;
        m = root.child("carrinho");

        ValueEventListener valueEventListener = new ValueEventListener() {
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
                    precoTotal.setText("R$: " + String.valueOf(precoTotalValor));
                    carrinhoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }
}