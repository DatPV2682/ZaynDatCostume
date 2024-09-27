package com.example.zayndatcostume.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.zayndatcostume.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailCostumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailCostumeFragment extends Fragment {

    // Parameter arguments
    private static final String ARG_COSTUME_ID = "costume_id";

    private String costumeId;
    private ImageButton btnBack, btnSave;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private TextView nameTextView, priceTextView, rentalFeeTextView, publisherTextView, sizeTextView, availableQuantityTextView;
    private ImageView costumeImageView;
    private String saveCostumeID;

    public DetailCostumeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @param costumeId Costume ID.
     * @return A new instance of fragment DetailCostumeFragment.
     */
    public static DetailCostumeFragment newInstance(String costumeId) {
        DetailCostumeFragment fragment = new DetailCostumeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COSTUME_ID, costumeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            costumeId = getArguments().getString(ARG_COSTUME_ID);
        }
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_costume, container, false);

        // Initialize views
        nameTextView = view.findViewById(R.id.detailname);
        priceTextView = view.findViewById(R.id.detailprice);
        rentalFeeTextView = view.findViewById(R.id.detailrent);
        publisherTextView = view.findViewById(R.id.detailpublisher);
        sizeTextView = view.findViewById(R.id.detailsize);
        availableQuantityTextView = view.findViewById(R.id.detailquan);
        costumeImageView = view.findViewById(R.id.detailimage);
        //btnBack = view.findViewById(R.id.back_btn);
        //btnSave = view.findViewById(R.id.save_btn);

        // Load costume details from Firestore
        loadCostumeDetails(costumeId);

        /*btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveCostumeID == null) {
                    String saveId = UUID.randomUUID().toString().replace("-", "");
                    String userId = SharedPreferencesManager.readUserInfo().getId();
                    SaveItemModel itemModel = new SaveItemModel(saveId, costumeId, userId, new Date().getTime(), 1);
                    saveCostume(itemModel);
                } else {
                    unsaveCostume();
                }
            }
        });*/

        //checkSavedCostume(costumeId);

        return view;
    }

    private void loadCostumeDetails(String costumeId) {
        db.collection("costume").document(costumeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString("name");
                    String price = document.getString("price");
                    String rentalFee = document.getString("rentalFee");
                    String publisher = document.getString("publisher");
                    String imageLink = document.getString("imageLink");
                    List<String> sizes = (List<String>) document.get("size");
                    Long availableQuantity = document.getLong("availableQuantity");

                    nameTextView.setText(name);
                    priceTextView.setText(price);
                    rentalFeeTextView.setText(rentalFee);
                    publisherTextView.setText(publisher);
                    sizeTextView.setText(TextUtils.join(", ", sizes));
                    availableQuantityTextView.setText(String.valueOf(availableQuantity));
                    Glide.with(costumeImageView).load(imageLink).into(costumeImageView);
                } else {
                    Toast.makeText(getContext(), "No such document!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to load costume details!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void saveCostume(SaveItemModel itemModel) {
        db.collection("saved_costumes").document(itemModel.getId()).set(itemModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveCostumeID = itemModel.getId();
                        Toast.makeText(getContext(), "Costume saved successfully!", Toast.LENGTH_SHORT).show();
                        btnSave.setImageResource(R.drawable.icn_unsave);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to save costume!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unsaveCostume() {
        db.collection("saved_costumes").document(saveCostumeID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveCostumeID = null;
                        btnSave.setImageResource(R.drawable.icn_save);
                        Toast.makeText(getContext(), "Costume unsaved successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to unsave costume!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkSavedCostume(String costumeId) {
        db.collection("saved_costumes").whereEqualTo("costumeId", costumeId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (!querySnapshot.isEmpty()) {
                            saveCostumeID = querySnapshot.getDocuments().get(0).getId();
                            btnSave.setImageResource(R.drawable.icn_unsave);
                        } else {
                            btnSave.setImageResource(R.drawable.icn_save);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to check saved costume!", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/
}
