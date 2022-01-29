package br.java.lojaonlineappmaster.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.java.lojaonlineappmaster.Adapter.AdminOfertaAdapter;
import br.java.lojaonlineappmaster.Adapter.AdminProdutoAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.AdminProduto;
import br.java.lojaonlineappmaster.ui.AddProdutoActivity;


public class ProdutosFragmento extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View view;

    // Variaveis
    private RecyclerView ProdutosRecycler;
    private AdminProdutoAdapter adapter;
    private FloatingActionButton ProdutosFloatingActionButton;
    private List<AdminProduto> adminProdutos;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;

    public ProdutosFragmento() {
        // Required empty public constructor
    }
    public static ProdutosFragmento newInstance(String param1, String param2) {
        ProdutosFragmento fragment = new ProdutosFragmento();
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

        view = inflater.inflate(R.layout.fragmento_produtos, container, false);

        ProdutosRecycler = (RecyclerView) view.findViewById(R.id.ProdutosRecycler);
        ProdutosFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.ProdutosFloatingBtnId);

        bar = view.findViewById(R.id.productProgressBar);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("produto");
        adapter = new AdminProdutoAdapter(getActivity(), adminProdutos);

        ProdutosRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProdutosRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                adminProdutos.clear();

                // Categoria
                for(DataSnapshot snapshot1:snapshot.getChildren()) {

                    // Nome
                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                        adminProdutos.add(new AdminProduto(snapshot2.getKey(),
                                snapshot1.getKey(),
                                snapshot2.child("data vencimento").getValue(String.class),
                                snapshot2.child("imagem").getValue(String.class),
                                snapshot2.child("preco").getValue(String.class),
                                snapshot2.child("quantidade").getValue(String.class)));
                    }
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setOnItemClickListener(new AdminOfertaAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
//                Intent i = new Intent(getActivity(), EditarProduto.class);
//                Bundle b = new Bundle();

            }
        });

        adapter.setOnLongClickListener(new AdminOfertaAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(final int pos) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmação").setMessage("Tem certeza de que deseja excluir ?!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = mDataBaseRef
                                        .child(adminProdutos.get(pos).getCategoria())
                                        .child(adminProdutos.get(pos).getNome());
                            }
                        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });

        ProdutosFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddProdutoActivity.class));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}