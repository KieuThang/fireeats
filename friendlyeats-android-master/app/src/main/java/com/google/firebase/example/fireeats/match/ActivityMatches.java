package com.google.firebase.example.fireeats.match;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.example.fireeats.FilterDialogFragment;
import com.google.firebase.example.fireeats.Filters;
import com.google.firebase.example.fireeats.MainActivity;
import com.google.firebase.example.fireeats.R;
import com.google.firebase.example.fireeats.databinding.ActivityMatchesBinding;
import com.google.firebase.example.fireeats.viewmodel.FilterViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class ActivityMatches extends AppCompatActivity implements MatchesAdapter.OnMatchSelectedListener, FilterDialogFragment.FilterListener {
    private static final String TAG = "ActivityMatches";
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ActivityMatchesBinding mBinding;

    private MatchesAdapter mAdapter;

    private FilterViewModel mViewModel;
    private String mFilterLeague;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_matches);

        mFilterLeague = getIntent().getStringExtra(MainActivity.KEY_LEAGUE_ID);
        mViewModel = ViewModelProviders.of(this).get(FilterViewModel.class);
        initFirestore();
        initRecyclerView();
    }

    private void initFirestore() {
        // TODO(developer): Implement
        mFirestore = FirebaseFirestore.getInstance();

        // Get the 50 highest rated restaurants
        mQuery = mFirestore.collection("365football_test").document("Ekmi7j5Cx5UG1oOTtWv3").collection("matches")
                .orderBy("time", Query.Direction.ASCENDING)
                .limit(LIMIT);

    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new MatchesAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mBinding.recyclerRestaurants.setVisibility(View.GONE);
                    mBinding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    mBinding.recyclerRestaurants.setVisibility(View.VISIBLE);
                    mBinding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mBinding.recyclerRestaurants.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerRestaurants.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Apply filters
        onFilter(mViewModel.getFilters());
        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onMatchSelected(DocumentSnapshot restaurant) {

    }

    @Override
    public void onFilter(Filters filters) {
        // Construct query basic query
        Query query = mFirestore.collection("365football_test").document("Ekmi7j5Cx5UG1oOTtWv3").collection("matches")
                .orderBy("time", Query.Direction.ASCENDING);
//
//        // Cateogory (equality filter)
        if (!TextUtils.isEmpty(mFilterLeague)) {
            query = query.whereEqualTo("leagueId", mFilterLeague);
        }
        
//
//        if(filters.hasCity()){
//            query = query.whereEqualTo("city",filters.getCity());
//        }
//
//        if(filters.hasPrice()){
//            query = query.whereEqualTo("price",filters.getPrice());
//        }
//
//        // Sort by (orderBy with direction)
//        if(filters.hasSortBy()){
//            query = query.orderBy(filters.getSortBy(), filters.getSortDirection());
//        }
//
//        // Limit items
        query = query.limit(LIMIT);
//
//        // Update the query
        mQuery = query;
        mAdapter.setQuery(query);

        // Save filters
        mViewModel.setFilters(filters);
    }
}
