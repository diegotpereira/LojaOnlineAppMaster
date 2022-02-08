package br.java.lojaonlineappmaster.fragment;

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

import java.util.ArrayList;
import java.util.List;

import br.java.lojaonlineappmaster.Adapter.AdminOfertaAdapter;
import br.java.lojaonlineappmaster.Adapter.AdminVendedorAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.AdminVendedor;
import br.java.lojaonlineappmaster.ui.AddVendedorActivity;

public class VendedorFragmento extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    // minhas variaveis
    private RecyclerView VendedorRecycler;
    private AdminVendedorAdapter adapter;
    private FloatingActionButton VendedorFloatingActionButton;
    private List<AdminVendedor> adminVendedorLista;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;

    public VendedorFragmento() {
        // Required empty public constructor
    }

    public static VendedorFragmento newInstance(String param1, String param2) {
        VendedorFragmento fragment = new VendedorFragmento();
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

        view = inflater.inflate(R.layout.fragmento_vendedor, container, false);

        VendedorRecycler = (RecyclerView) view.findViewById(R.id.vendedor_recyclerview);
        VendedorFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.vendedor_floating_btn_id);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("vendedor");
        bar = view.findViewById(R.id.vendedor_progress_bar);

        adminVendedorLista = new ArrayList<>();

        bar.setVisibility(View.VISIBLE);
        adapter = new AdminVendedorAdapter(getActivity(), adminVendedorLista);
        VendedorRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        VendedorRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminVendedorLista.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    adminVendedorLista.add(new AdminVendedor(snapshot1.getKey(),
                            snapshot1.child("img").getValue(String.class),
                            snapshot1.child("qrimagem").getValue(String.class),
                            snapshot1.child("salario").getValue(String.class)));
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

            }
        });

        adapter.setOnLongClickListener(new AdminOfertaAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(int pos) {

            }
        });

        VendedorFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddVendedorActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}