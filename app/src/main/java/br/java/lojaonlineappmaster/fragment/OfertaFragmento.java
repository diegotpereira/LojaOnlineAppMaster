package br.java.lojaonlineappmaster.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import br.java.lojaonlineappmaster.Adapter.AdminOfertaAdapter;
import br.java.lojaonlineappmaster.R;
import br.java.lojaonlineappmaster.model.AdminOferta;
import br.java.lojaonlineappmaster.ui.AdminOfertaActivity;
import br.java.lojaonlineappmaster.ui.EditarOfertaActivity;

public class OfertaFragmento extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Variaveis
    private RecyclerView OfertaRecycler;
    private AdminOfertaAdapter adapter;
    private FloatingActionButton OfertaFloatingActionButton;
    private List<AdminOferta> OfertaLista;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;

    public OfertaFragmento() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OfertaFragmento newInstance(String param1, String param2) {
        OfertaFragmento fragment = new OfertaFragmento();
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

        view = inflater.inflate(R.layout.fragmento_oferta, container, false);

        bar = view.findViewById(R.id.oferta_progressbar);
        OfertaRecycler = (RecyclerView) view.findViewById(R.id.oferta_recyclerview);
        OfertaFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.oferta_floating_btn_id);

        mDataBaseRef = FirebaseDatabase.getInstance().getReference("ofertas");
        OfertaLista = new ArrayList<>();

        adapter = new AdminOfertaAdapter(getActivity(), OfertaLista);
        OfertaRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        OfertaRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OfertaLista.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    OfertaLista.add(new AdminOferta(snapshot1.getKey(),
                            snapshot1.child("descricao").getValue(String.class),
                            snapshot1.child("img").getValue(String.class)));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                bar.setVisibility(View.INVISIBLE);
            }
        });
        adapter.setOnItemClickListener(new AdminOfertaAdapter.onItemClickListener() {

            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity(), EditarOfertaActivity.class);
                Bundle b = new Bundle();
                b.putString("img", OfertaLista.get(pos).getOfertaImg());
                b.putString("nome", OfertaLista.get(pos).getOfertaNome());
                b.putString("descricao", OfertaLista.get(pos).getOfertaDescricao());
                i.putExtras(b);
                startActivity(i);

            }
        });

        adapter.setOnLongClickListener(new AdminOfertaAdapter.onLongClickListener() {

            @Override
            public void onItemLongClick(int pos) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmação").setMessage("Tem certeza de que deseja excluir ?!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference reference = mDataBaseRef.child(OfertaLista.get(pos).getOfertaNome());
                                reference.removeValue();


                                StorageReference z = FirebaseStorage.getInstance()
                                        .getReference("offers").child(OfertaLista.get(pos)
                                                .getOfertaNome() + ".jpg");
                                z.delete();

                                StorageReference x = FirebaseStorage.getInstance().getReference("ofertas")
                                        .child(OfertaLista.get(pos).getOfertaNome());
                                x.delete();
                            }
                        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });
        OfertaFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AdminOfertaActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}