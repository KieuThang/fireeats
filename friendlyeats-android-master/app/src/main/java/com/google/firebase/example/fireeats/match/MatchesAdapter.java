package com.google.firebase.example.fireeats.match;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.example.fireeats.R;
import com.google.firebase.example.fireeats.adapter.FirestoreAdapter;
import com.google.firebase.example.fireeats.databinding.MatchItemBinding;
import com.google.firebase.example.fireeats.model.Match;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class MatchesAdapter extends FirestoreAdapter<MatchesAdapter.ViewHolder> {
    public interface OnMatchSelectedListener {
        void onMatchSelected(DocumentSnapshot restaurant);
    }

    private OnMatchSelectedListener mListener;

    public MatchesAdapter(Query query) {
        super(query);
    }

    public MatchesAdapter(Query query, OnMatchSelectedListener listener) {
        super(query);

        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MatchItemBinding binding = DataBindingUtil.inflate(inflater,R.layout.match_item,parent,false);
        ViewHolder holder = new ViewHolder(binding.getRoot());
        holder.binding = binding;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        MatchItemBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
        }

        void bind(final DocumentSnapshot snapshot, final OnMatchSelectedListener mListener) {
            Match match = snapshot.toObject(Match.class);
            Log.d("KieuThang","snapShot id:"+snapshot.getId());

            Glide.with(binding.imgHomeLogo.getContext())
                    .load(match.homeImage)
                    .into(binding.imgHomeLogo);

            Glide.with(binding.imgAwayLogo.getContext())
                    .load(match.awayImage)
                    .into(binding.imgAwayLogo);

            binding.tvAwayName.setText(match.awayName);
            binding.tvHomeName.setText(match.homeName);
            binding.tvDate.setText(match.time);
            binding.tvScore.setText(String.format("%s : %s", match.homeScore, match.awayScore));

//
//            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onMatchSelected(snapshot);
                    }
                }
            });
        }
    }
}
