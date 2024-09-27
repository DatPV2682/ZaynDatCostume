package com.example.zayndatcostume.fragments;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zayndatcostume.acivities.MainActivity;
import com.example.zayndatcostume.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateProfileFragment extends Fragment {
    private EditText nameUpdateEditText, repasswordUpdateEditText, passwordUpdateEditText;
    private ImageView avatarUpdateImageView;
    private Button updateButton, updatePasswordButton;
    private Uri mUri;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    public static UpdateProfileFragment newInstance() {
        return new UpdateProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        nameUpdateEditText = view.findViewById(R.id.name_update_edt);
        passwordUpdateEditText = view.findViewById(R.id.password_update_edt);
        repasswordUpdateEditText = view.findViewById(R.id.repassword_update_edt);
        avatarUpdateImageView = view.findViewById(R.id.avatar_update_img);
        updateButton = view.findViewById(R.id.update_button);
        updatePasswordButton = view.findViewById(R.id.update_password_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();
            nameUpdateEditText.setText(name);
            if (photoUrl != null) {
                Glide.with(view).load(photoUrl).error(R.drawable.defaultavatar).into(avatarUpdateImageView);
            } else {
                avatarUpdateImageView.setImageResource(R.drawable.defaultavatar);
            }
        }

        avatarUpdateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }

            private void onClickRequestPermission() {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity == null) {
                    return;
                }
                mainActivity.setUpdateProfileFragment(UpdateProfileFragment.this); // Set updateProfileFragment
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mainActivity.openGallery();
                    return;
                }
                if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    mainActivity.openGallery();
                } else {
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    getActivity().requestPermissions(permissions, MainActivity.MY_REQUEST_CODE);
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdatePassword();
            }
        });

        return view;
    }

    private void onClickUpdatePassword() {
        String newPassword = passwordUpdateEditText.getText().toString().trim();
        String rePassword = repasswordUpdateEditText.getText().toString().trim();

        if (newPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Mật khẩu mới không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(rePassword)) {
            Toast.makeText(getActivity(), "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                                Toast.makeText(getActivity(), "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void setBitmapImageView(Bitmap bitmap) {
        avatarUpdateImageView.setImageBitmap(bitmap);
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String strFullName = nameUpdateEditText.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Thông tin đã được cập nhật.");
                            Toast.makeText(getActivity(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
