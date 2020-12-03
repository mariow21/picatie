package com.kelvinadithya.picatie.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kelvinadithya.picatie.R;
import com.kelvinadithya.picatie.ui.main.MainPhotoFragment;
import com.kelvinadithya.picatie.ui.slideshow.SlideshowFragment;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        final BottomNavigationView bottomNavigationView =root.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        fragment = new GalleryFragment();
                        replaceFragment(fragment);
                        Toast.makeText(getActivity(), "About Us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites:
                        fragment = new SlideshowFragment();
                        replaceFragment(fragment);
                        Toast.makeText(getActivity(), "Contact Us", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });

        final BottomNavigationView topNavigationView =root.findViewById(R.id.top_navigation);
        topNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_recents1:
                        fragment = new GalleryFragment();
                        replaceFragment(fragment);
                        Toast.makeText(getActivity(), "Credit", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites2:
                        fragment = new MainPhotoFragment();
                        replaceFragment(fragment);
                        Toast.makeText(getActivity(), "API", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });


        return root;
    }
    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container1, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}