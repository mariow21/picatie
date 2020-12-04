package com.kelvinadithya.picatie.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.api.Order;
import com.kc.unsplash.models.Photo;
import com.kelvinadithya.picatie.MyAdapter;
import com.kelvinadithya.picatie.R;

import java.util.List;

import butterknife.BindView;

public class MainPhotoFragment extends Fragment {

    private MainViewModel mViewModel;
    private final String CLIENT_ID = "611b6a517cafa69285e513282257c6516061c205974a8a39a641cdb408f4d4ca";
    private final String code = "98f3eabb12eed312575028ded4bfec7c9e10ec10192c7f9a4e66a67faa6d985f";
    private final String secret = "128fd0de2c81798597c416c83cfd320b5e9029a45e43d74a0e659d2b1bfb5864";
    private final String url = "urn:ietf:wg:oauth:2.0:oob";
    @BindView(R.id.recyclerView12)
    public RecyclerView recyclerView12;
    Unsplash unsplash = new Unsplash(CLIENT_ID);

    public static MainPhotoFragment newInstance() {

        return new MainPhotoFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.main_fragment, container, false);

        return root;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel


    }
    private void Photos(int f, int l, Order order) {
        unsplash.getPhotos(f, l, order, new Unsplash.OnPhotosLoadedListener() {
            @Override
            public void onComplete(List<Photo> photos) {
                MyAdapter adapter = new MyAdapter(photos, getActivity());
                recyclerView12.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
            }
        });
    }
}
