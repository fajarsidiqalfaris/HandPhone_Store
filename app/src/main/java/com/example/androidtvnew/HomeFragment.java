package com.example.androidtvnew;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.example.androidtvnew.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeFragment extends Fragment {
    private TextView title;
    private TextView description;
//    private FragmentContainerView listFragment;
    private ImageView imgBanner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    private void init(View view) {
        initView(view);

        ListFragment listFragment = new ListFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.list_fragment, listFragment);
        transaction.commit();

        Gson gson = new Gson();
        InputStream i = null;
        try {
            i = requireContext().getAssets().open("home.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(i));
            DataModel dataList = gson.fromJson(br, DataModel.class);

            listFragment.bindData(dataList);

            listFragment.setOnContentSelectedListener(this::updateBanner);
            listFragment.setOnContentClickedListener(new ListFragment.OnItemClickListener() {
                @Override
                public void onItemClick(DataModel.Result.Detail item) {
                    Constants constant = new Constants();

                    Intent intent = new Intent(requireContext(), DetailActivity.class);
                    intent.putExtra(constant.ID, item.id);
                    intent.putExtra(constant.BACKDROP, item.backdrop_path);
                    intent.putExtra(constant.TITLE, item.title);
                    intent.putExtra(constant.DESCRIPTION, item.description);
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBanner(DataModel.Result.Detail dataList) {
        title.setText(dataList.title);
        description.setText(dataList.description);

        String url = dataList.backdrop_path;
        Glide.with(this).load(url).into(imgBanner);
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.title);
        TextView language = (TextView) view.findViewById(R.id.language);
        description = (TextView) view.findViewById(R.id.description);
        imgBanner = (ImageView) view.findViewById(R.id.img_Banner);
//        listFragment = (FragmentContainerView) findViewById(R.id.list_fragment);
    }
}