package ir.sharif.mobile.project.ui.rewards;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Reward;
import ir.sharif.mobile.project.ui.utils.TwoLayerView;

public class RewardViewAdapter extends RecyclerView.Adapter<RewardViewAdapter.RewardViewHolder> {

    private List<Reward> rewards;
    private final Context context;

    public class RewardViewHolder extends RecyclerView.ViewHolder implements TwoLayerView {

        public final TextView title;
        public final TextView description;
        public final TextView reward;
        public final ImageButton actionButton;
        public RelativeLayout viewBackground, viewForeground;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            reward = itemView.findViewById(R.id.item_reward);
            actionButton = itemView.findViewById(R.id.item_action_button);
            viewBackground = itemView.findViewById(R.id.item_background);
            viewForeground = itemView.findViewById(R.id.item_foreground);
        }

        @Override
        public View getViewForeground() {
            return viewForeground;
        }
    }

    public RewardViewAdapter(Context context) {
        this.rewards = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        final Reward reward = rewards.get(position);
        holder.title.setText(reward.getTitle());
        holder.description.setText(reward.getDescription());
        holder.reward.setText(String.valueOf(reward.getAmount()));

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("reward", reward);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editRewardFragment, bundle);
        });

        holder.actionButton.setOnClickListener(v -> Executor.getInstance().subCoin(reward.getAmount()));

    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public void addAll(List<Reward> list) {
        for (Reward reward : list)
            if (!rewards.contains(reward))
                rewards.add(reward);
        notifyDataSetChanged();
    }

    public Reward getItem(int position) {
        return rewards.get(position);
    }

    public void removeItem(int position) {
        rewards.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void restoreItem(Reward reward, int position) {
        rewards.add(position, reward);
        // notify task added by position
        notifyItemInserted(position);
    }

    public void clearList() {
        rewards.clear();
    }
}
