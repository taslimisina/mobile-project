package ir.sharif.mobile.project.ui.rewards;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Reward;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.utils.RecyclerItemTouchHelper;

public class RewardFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<Reward> rewards;
    private RewardViewAdaptor mAdaptor;
    private RewardViewHandler handler;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        handler = new RewardViewHandler();
        rewards = RepositoryHolder.getRewardRepository().findAll();
        mAdaptor = new RewardViewAdaptor(rewards, handler, getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdaptor);

        // adding item touch helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        rewards.clear();
        rewards.addAll(RepositoryHolder.getRewardRepository().findAll());
        getActivity().findViewById(R.id.new_button).setOnClickListener(v -> {
            Navigation.findNavController(getActivity().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editRewardFragment);
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RewardViewAdaptor.RewardViewHolder) {
            // get the removed item name to display it in snack bar
            String name = rewards.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Reward deleted = rewards.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdaptor.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(getView(), name + " removed from Rewards!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    snackbar.setAction("UNDO", v -> {});
                    mAdaptor.restoreItem(deleted, deletedIndex);
                }
            });
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        Message message = new Message();
                        message.what = RewardViewHandler.DELETE_REWARD;
                        message.obj = deleted;
                        handler.sendMessage(message);
                    }
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


}