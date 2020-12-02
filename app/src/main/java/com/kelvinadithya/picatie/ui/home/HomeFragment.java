package com.kelvinadithya.picatie.ui.home;


import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.api.Order;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;
import com.kelvinadithya.picatie.MyAdapter;
import com.kelvinadithya.picatie.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private final String CLIENT_ID = "611b6a517cafa69285e513282257c6516061c205974a8a39a641cdb408f4d4ca";
    private final String code = "98f3eabb12eed312575028ded4bfec7c9e10ec10192c7f9a4e66a67faa6d985f";
    private final String secret = "128fd0de2c81798597c416c83cfd320b5e9029a45e43d74a0e659d2b1bfb5864";
    private final String url = "urn:ietf:wg:oauth:2.0:oob";
    @BindView(R.id.recyclerView12)
    public RecyclerView recyclerView12;
    Unsplash unsplash = new Unsplash(CLIENT_ID);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView12 = (RecyclerView) root.findViewById(R.id.recyclerView12);
        //ButterKnife.bind(getActivity());
        ButterKnife.bind(recyclerView12);
        recyclerView12.setLayoutManager(new LinearLayoutManager(getActivity()));
        Photos(1, 100, Order.POPULAR);
        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        final SearchView searchView =(SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
       // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQuery(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }




/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchQuery(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.oldest:
                Photos(1, 40, Order.OLDEST);
                Toast.makeText(getActivity(), "Oldest", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.latest:
                Photos(1, 40, Order.LATEST);
                Toast.makeText(getActivity(), "Latest", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.popular:
                Photos(1, 40, Order.POPULAR);
                Toast.makeText(getActivity(), "Popular", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void SearchQuery(String query) {
        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                List<Photo> photos = results.getResults();
                MyAdapter adapter = new MyAdapter(photos, getActivity());
                recyclerView12.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
