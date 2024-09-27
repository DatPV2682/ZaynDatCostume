package com.example.zayndatcostume.acivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.zayndatcostume.R;
import com.example.zayndatcostume.fragments.CartFragment;
import com.example.zayndatcostume.fragments.HomeFragment;
import com.example.zayndatcostume.fragments.SettingFragment;
import com.example.zayndatcostume.fragments.UpdateProfileFragment;
import com.example.zayndatcostume.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int MY_REQUEST_CODE = 10;
    private UpdateProfileFragment updateProfileFragment;

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                Log.d("MainActivity", "Intent received: " + intent);
                if (intent == null) {
                    Log.e("MainActivity", "Intent is null");
                    return;
                }
                Uri uri = intent.getData();
                Log.d("MainActivity", "URI received: " + uri);
                if (uri != null && updateProfileFragment != null) {
                    updateProfileFragment.setUri(uri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        updateProfileFragment.setBitmapImageView(bitmap);
                    } catch (IOException e) {
                        Log.e("MainActivity", "Error loading image", e);
                    }
                } else {
                    Log.e("MainActivity", "URI is null or updateProfileFragment is null");
                }
            } else {
                Log.e("MainActivity", "Result not OK");
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Danh sách các costume



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                transaction.replace(R.id.container, new HomeFragment());
            } else if (itemId == R.id.navigation_cart) {
                transaction.replace(R.id.container, new CartFragment());
            } else if (itemId == R.id.navigation_user) {
                transaction.replace(R.id.container, new UserFragment());
            } else if (itemId == R.id.navigation_setting) {
                transaction.replace(R.id.container, new SettingFragment());
            }

            transaction.commit();
            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Log.e("MainActivity", "Permission denied");
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    public void setUpdateProfileFragment(UpdateProfileFragment fragment) {
        this.updateProfileFragment = fragment;
    }
    private Map<String, Object> createCostume(String id, String name, String imageLink, String price, String rentalFee, List<String> category, String publisher, List<String> size, int availableQuantity) {
        Map<String, Object> costume = new HashMap<>();
        costume.put("id", id);
        costume.put("name", name);
        costume.put("imageLink", imageLink);
        costume.put("price", price);
        costume.put("rentalFee", rentalFee);
        costume.put("category", category);
        costume.put("publisher", publisher);
        costume.put("size", size);
        costume.put("availableQuantity", availableQuantity);
        return costume;
    }


}
/* Map<String, Object>[] costumes = new Map[]{
                createCostume("cos012", "Akame (Akame ga Kill!)", "https://i.pinimg.com/originals/be/ee/6f/beee6f577aa5d2f689314408856f9ab4.jpg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 38),
                createCostume("cos013", "Inuyasha (Inuyasha)", "https://laz-img-sg.alicdn.com/p/bca3827d3ad9dadf72b1607d19bf2eb6.png", "160,99", "7,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 60),
                createCostume("cos014", "Rukia Kuchiki (Bleach)", "https://i.pinimg.com/736x/45/5a/24/455a24f1a9dd76c34a17b7b01dc1486d.jpg", "115,99", "7,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 15),
                createCostume("cos015", "Hisoka Morow (Hunter x Hunter)", "https://i.ebayimg.com/images/g/-SYAAOSwmuhezH73/s-l1600.jpg", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 48),
                createCostume("cos016", "Kakashi Hatake (Naruto)", "https://i.ebayimg.com/images/g/mzQAAOSwTV1h2BT-/s-l1200.jpg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 62),
                createCostume("cos017", "Sasuke Uchiha (Naruto)", "https://i.ebayimg.com/images/g/MqwAAOSwP45dpnAr/s-l1600.jpg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 44),
                createCostume("cos018", "Ryuk (Death Note)", "https://i0.wp.com/cdn.makezine.com/uploads/2012/09/ryuk-costume.jpg?resize=598%2C588&ssl=1", "144,99", "8,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 58),
                createCostume("cos019", "Ken Kaneki (Tokyo Ghoul)", "https://img.ws.mms.shopee.vn/cc7bc002c1c8c9157a8cb6583b19f8dc", "134,99", "6,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 37),
                createCostume("cos020", "Hinata Hyuga (Naruto)", "https://i.ebayimg.com/images/g/Vu0AAOSw4uFi0A8d/s-l1200.webp", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 81),
                createCostume("cos021", "Saber (Fate series)", "https://mooncosya.com/cdn/shop/products/il_1588xN.3248620204_2lwl.jpg?v=1630574813&width=600", "144,99", "8,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 25),
                createCostume("cos022", "Nezuko Kamado (Demon Slayer: Kimetsu no Yaiba)", "https://m.media-amazon.com/images/I/512cbtKdaaL._AC_UY1000_.jpg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 25),
                createCostume("cos023", "Zoro Roronoa (One Piece)", "https://images-na.ssl-images-amazon.com/images/I/61zmchGGvsL.jpg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 16),
                createCostume("cos024", "Light Yagami (Death Note)", "https://i.ebayimg.com/images/g/MZkAAOSwCp1fmNG4/s-l1200.jpg", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 49),
                createCostume("cos025", "Rimuru Tempest (That Time I Got Reincarnated as a Slime)", "https://i.ebayimg.com/images/g/x8sAAOSwD4xgBOcD/s-l400.jpg", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 70),
                createCostume("cos026", "Spider-Man", "https://images.fun.com/media/159/spiderman-costumes/mens-spiderman-costumes.jpg", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 59),
                createCostume("cos027", "Iron Man", "https://i.ebayimg.com/images/g/vu4AAOSwDRNeluFU/s-l1600.jpg", "134,99", "6,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 57),
                createCostume("cos028", "Captain America", "https://m.media-amazon.com/images/I/71kR6o6oQQL._AC_UY1000_.jpg", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 57),
                createCostume("cos029", "Wolverine", "https://m.media-amazon.com/images/I/71CeiHKYadL._AC_UY1000_.jpg", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 62),
                createCostume("cos030", "Thor", "https://i5.walmartimages.com/asr/3c4d36f4-4f3e-4acb-a095-328f798048df_1.c0bf05ba5de9823c434d76bc6c0e1a01.jpeg", "162,99", "7,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 62),
                createCostume("cos031", "Hulk", "https://m.media-amazon.com/images/I/81fzMDRDIUL._AC_UY1000_.jpg", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 84),
                createCostume("cos032", "Deadpool", "https://i.ebayimg.com/images/g/-wkAAOSwEWJixBD9/s-l1200.webp", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 70),
                createCostume("cos033", "Black Widow", "https://cdn.costumewall.com/wp-content/plugins/image-hot-spotter/images/BlackWidow.jpg", "134,99", "6,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 48),
                createCostume("cos034", "Hawkeye", "https://i.ebayimg.com/images/g/MckAAOSw3VBhn0ei/s-l1200.webp", "128,99", "7,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 28),
                createCostume("cos035", "Captain Marvel", "https://images-na.ssl-images-amazon.com/images/I/71OCj6SuP9L.jpg", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 38),
                createCostume("cos036", "Ant-Man", "https://images.halloweencostumes.eu/products/54300/1-1/ant-man-grand-heritage-adult-costume.jpg", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 82),
                createCostume("cos037", "Doctor Strange", "https://i.ebayimg.com/images/g/8nsAAOSwoU1c7KMx/s-l1600.jpg", "144,99", "8,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 31),
                createCostume("cos038", "The Punisher", "https://trendsincosplay.com/cdn/shop/products/15007253-cosplaystyle2014.jpg?v=1571717294", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 91),
                createCostume("cos037", "Black Panther", "https://img.fruugo.com/product/4/10/377008104_max.jpg", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 37),
                createCostume("cos040", "Scarlet Witch", "https://m.media-amazon.com/images/I/61SrKAb6lsL._AC_SL1500_.jpg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 55),
                createCostume("cos041", "Vision", "https://cdn-amz.woka.io/images/I/313MhiuTTqL._SR575,575_.jpg", "134,99", "6,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 98),
                createCostume("cos042", "Loki", "https://i.ebayimg.com/images/g/RuUAAOSwaU1fQMX2/s-l1200.jpg", "144,99", "8,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 72),
                createCostume("cos043", "Thanos", "https://i.ebayimg.com/images/g/HPsAAOSwSldgpN4Y/s-l1200.webp", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 67),
                createCostume("cos044", "Star-Lord", "https://ultimateapparels.com/image/cache/data/men/Guardians-of-galaxy-star-lord-costume-coat-875x1000.jpg", "134,99", "6,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 20),
                createCostume("cos045", "Gamora", "https://i.ebayimg.com/images/g/V44AAOSw8CRkbdya/s-l1200.webp", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 21),
                createCostume("cos046", "Groot", "https://cdn.media.amplience.net/i/partycity/P964486?$large$&fmt=auto&qlt=default", "149,99", "9,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 69),
                createCostume("cos047", "Rocket Raccoon", "https://i5.walmartimages.com/seo/Guardians-of-the-Galaxy-Deluxe-Rocket-Raccoon-Child-Halloween-Costume_2ad60568-506b-4ad3-881d-3be2fc059bed_1.34edeb34fc2d69a030113641b4acd256.jpeg?odnHeight=640&odnWidth=640&odnBg=FFFFFF", "134,99", "6,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 44),
                createCostume("cos048", "Drax", "https://costumeworld.co.nz/cdn/shop/products/Drax-The-Destroyer-Costume-for-Kids-Marvel-Guardians-Of-The-Galaxy-Rubies-Kids-Boys_540x.jpg?v=1631270821", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 95),
                createCostume("cos049", "Mantis", "https://i.ebayimg.com/images/g/w4sAAOSwvhZiaPBR/s-l1600.jpg", "129,99", "5,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 95),
                createCostume("cos050", "Nick Fury", "https://i5.walmartimages.com/seo/Nick-Fury-Avengers-Adult-50-52_77f56df9-fdb1-4f71-84f9-7894c8a31b77.c5744851a7786f4fcef51e141795a2a2.jpeg", "124,99", "4,99", Arrays.asList("Anime", "Anime Costume"), "Anime", Arrays.asList("S", "M", "L", "XL", "XXL", "XXXL"), 68),
                // Thêm tất cả các costume còn lại tương tự như trên
        };

        // Thêm từng tài liệu vào collection "costume"
        for (Map<String, Object> costume : costumes) {
            String id = (String) costume.get("id");
            db.collection("costume")
                    .document(id) // Sử dụng ID tùy chỉnh
                    .set(costume)
                    .addOnSuccessListener(aVoid -> {
                        // Xử lý khi thêm thành công
                        System.out.println("DocumentSnapshot successfully written with ID: " + id);
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý khi thêm thất bại
                        System.err.println("Error writing document: " + e.getMessage());
                    });}*/