package com.example.heailth_30;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestFragment extends Fragment {

    ImageView upload_file_btn, check_result_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);

        upload_file_btn = v.findViewById(R.id.upload_file_btn);
        check_result_btn = v.findViewById(R.id.check_result_btn);
        //do procedure for upload btn

        check_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new InfectedFragment()).commit();
            }
        });

        return v;
    }
}
