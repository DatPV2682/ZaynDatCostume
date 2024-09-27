package com.example.zayndatcostume.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.zayndatcostume.adapter.SlideAdapter;
import com.example.zayndatcostume.R;
import com.example.zayndatcostume.models.Costume;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button listbtn;
    private FirebaseFirestore db;
    private ImageButton reloadbtn;
    private ViewPager vpg;
    private CircleIndicator ci;
    List<Costume> list = new ArrayList<>();
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (vpg.getCurrentItem() == 6) {
                vpg.setCurrentItem(0);
            } else {
                vpg.setCurrentItem(vpg.getCurrentItem() + 1);
            }
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        vpg = view.findViewById(R.id.viewpager);
        ci = view.findViewById(R.id.circle_indicator);

        FirebaseFirestore.getInstance().collection("costume")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String name = document.getString("name");
                                String imageLink = document.getString("imageLink");
                                String price = document.getString("price");
                                String rentalFee = document.getString("rentalFee");
                                List<String> category = (List<String>) document.get("category");
                                String publisher = document.getString("publisher");
                                List<String> size = (List<String>) document.get("size");
                                Long availableQuantity = document.getLong("availableQuantity");

                                // Kiểm tra và xử lý trường hợp availableQuantity bị null
                                if (availableQuantity != null) {
                                    String id = document.getId();
                                    Costume costume = new Costume(id, name, imageLink, price, rentalFee, category, publisher, size, availableQuantity.intValue());
                                    System.out.println(costume);
                                    list.add(costume);
                                }
                            }

                            Collections.shuffle(list);
                            List<Costume> randomCostumes = list.subList(0, Math.min(list.size(), 7));
                            SlideAdapter sld = new SlideAdapter(randomCostumes);
                            vpg.setAdapter(sld);
                            ci.setViewPager(vpg);
                            mHandler.postDelayed(mRunnable, 5000);
                            vpg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    mHandler.removeCallbacks(mRunnable);
                                    mHandler.postDelayed(mRunnable, 5000);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
      /*  reloadbtn = view.findViewById(R.id.reloadbtn);
        reloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new HomeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });*/
        listbtn = view.findViewById(R.id.allcostumebutton);
        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new AllCostumeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}
