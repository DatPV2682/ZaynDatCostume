package com.example.zayndatcostume.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zayndatcostume.R;
import com.example.zayndatcostume.adapter.AllCostumeAdapter;
import com.example.zayndatcostume.models.Costume;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCostumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCostumeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private AllCostumeAdapter allCostumeAdapter;
    private List<Costume> costumeList;
    private FirebaseFirestore db;

    public AllCostumeFragment() {
        // Required empty public constructor
    }

    public static AllCostumeFragment newInstance(String param1, String param2) {
        AllCostumeFragment fragment = new AllCostumeFragment();
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
        db = FirebaseFirestore.getInstance();
        costumeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_costume, container, false);

        recyclerView = view.findViewById(R.id.rcv_all_costume);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allCostumeAdapter = new AllCostumeAdapter(getContext(), costumeList);
        recyclerView.setAdapter(allCostumeAdapter);

        loadCostumes();

        return view;
    }

    private void loadCostumes() {
        db.collection("costume")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        costumeList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String imgLink = document.getString("imageLink");
                            String price = document.getString("price");
                            String rentalFee = document.getString("rentalFee");
                            String publisher = document.getString("publisher");
                            List<String> size = (List<String>) document.get("size");
                            Long availableQuantity = document.getLong("availableQuantity");
                            if (availableQuantity != null) {
                                Costume costume = new Costume(id, name, imgLink, price, rentalFee, null, publisher, size, availableQuantity.intValue());
                                costumeList.add(costume);
                            } else {
                                // Handle the case when availableQuantity is null
                                // You might want to set a default value or skip this document
                                // For example, we can set default available quantity to 0
                                Costume costume = new Costume(id, name, imgLink, price, rentalFee, null, publisher, size, 0);
                                costumeList.add(costume);
                            }
                        }
                        allCostumeAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                    }
                });
    }
}
