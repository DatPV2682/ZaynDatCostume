package com.example.zayndatcostume.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.zayndatcostume.R;
import com.example.zayndatcostume.fragments.DetailCostumeFragment;
import com.example.zayndatcostume.models.Costume;

import java.util.List;

public class SlideAdapter extends PagerAdapter {

    private List<Costume> mListCostume;

    public SlideAdapter(List<Costume> mListCostume) {
        this.mListCostume = mListCostume;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.suggestion_item, container, false);
        ImageView imgCostume = view.findViewById(R.id.imgsug);
        TextView nameCostume = view.findViewById(R.id.namesug);
        TextView priceCostume = view.findViewById(R.id.pricesug);
        Button detailCostume = view.findViewById(R.id.detailcostumebutton);

        Costume costume = mListCostume.get(position);
        nameCostume.setText(costume.getName());
        priceCostume.setText(costume.getPrice());

        Glide.with(view.getContext())
                .load(costume.getImageLink()) // Đường dẫn hình ảnh từ đối tượng Costume
                .placeholder(R.drawable.zayndatlogo) // Hình ảnh mặc định trong khi đang tải
                .into(imgCostume); // ImageView để hiển thị hình ảnh

        detailCostume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Bundle để truyền dữ liệu
                Bundle bundle = new Bundle();
                bundle.putString("costume_id", costume.getId());
                bundle.putString("costume_name", costume.getName());
                bundle.putString("costume_imglink", costume.getImageLink());
                bundle.putString("costume_price", costume.getPrice());
                bundle.putString("costume_rentalfee", costume.getRentalFee());
                bundle.putString("costume_category", costume.getCategory().toString());
                bundle.putString("costume_publisher", costume.getPublisher());
                bundle.putString("costume_size", costume.getSize().toString());
                bundle.putInt("costume_availableQuantity", costume.getAvailableQuantity());

                // Thêm các dữ liệu khác nếu cần

                // Mở Fragment chi tiết và truyền dữ liệu
                DetailCostumeFragment detailFragment = new DetailCostumeFragment();
                detailFragment.setArguments(bundle);

                // Thay đổi Fragment trong Activity hiện tại
                FragmentTransaction transaction = ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, detailFragment);
                transaction.addToBackStack(null); // Thêm Fragment vào back stack
                transaction.commit();
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (mListCostume != null) {
            return mListCostume.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
