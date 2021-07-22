package ir.sharif.mobile.project.ui.dailies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.ChecklistItem;
import ir.sharif.mobile.project.ui.model.Daily;
import ir.sharif.mobile.project.ui.model.utils.DateUtil;
import ir.sharif.mobile.project.ui.utils.TwoLayerView;

public class DailyViewAdaptor extends RecyclerView.Adapter<DailyViewAdaptor.DailyViewHolder>{
    private List<Daily> dailyList;
    private final Context context;
    private final View rootView;

    public DailyViewAdaptor(Context context, View rootView) {
        this.dailyList = new ArrayList<>();
        this.context = context;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public DailyViewAdaptor.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_dailies, parent, false);
        return new DailyViewAdaptor.DailyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull DailyViewAdaptor.DailyViewHolder holder, int position) {
        final Daily daily = dailyList.get(position);
        holder.title.setText(daily.getTitle());
        holder.description.setText(daily.getDescription());
        holder.reward.setText(String.valueOf(daily.getReward()));

        // Init checklist
        holder.checklist.removeAllViews();
        for (ChecklistItem item : daily.getChecklistItems()) {
            View checklistItemView = LayoutInflater.from(context).inflate(R.layout.layout_checklist_item, (ViewGroup)holder.checklist, false);
            ((TextView)checklistItemView.findViewById(R.id.checklist_item_title)).setText(item.getName());
            CheckBox itemCheckBox = checklistItemView.findViewById(R.id.checklist_item_box);
            itemCheckBox.setChecked(item.isChecked());
            itemCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {item.setChecked(isChecked);
                Executor.getInstance().saveChecklistItem(item);
            });
            holder.checklist.addView(checklistItemView);
        }

        holder.checklist.setVisibility(View.GONE);
        // Init expand button
        if (daily.getChecklistItems().size() == 0) {
            holder.expandButton.setVisibility(View.GONE);
        } else {
            holder.expandButton.setVisibility(View.VISIBLE);
            holder.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
            holder.expandButton.setOnClickListener(v -> {
                if (holder.checklist.getVisibility() == View.GONE) {
                    holder.expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                    holder.checklist.setVisibility(View.VISIBLE);
                } else {
                    holder.expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    holder.checklist.setVisibility(View.GONE);
                }
            });
        }

        // Todo replace with start/every
        if (daily.getStart() != null) {
            holder.startDate.setText(DateUtil.formatDate(daily.getStart(), DateUtil.SHORT));
            Date today = Calendar.getInstance().getTime();
            if (daily.getStart().after(today)) {
                holder.startDate.setTextColor(context.getResources().getColor(R.color.red_icon));
            }
        } else {
            holder.startDateIcon.setVisibility(View.GONE);
            holder.startDate.setVisibility(View.GONE);
        }

        // Checked listener
        holder.checkBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                checkDaily(holder);
            }
        });


        holder.viewBackground.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("daily", daily);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editDailyFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return dailyList.size();
    }

    public void addAll(List<Daily> list) {
        for (Daily daily : list)
            if (!dailyList.contains(daily))
                dailyList.add(daily);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        dailyList.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    public void restoreItem(Daily daily, int position) {
        dailyList.add(position, daily);
        // notify task added by position
        notifyItemInserted(position);
    }

    public Daily getItem(int position) {
        return dailyList.get(position);
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder implements TwoLayerView {

        public final TextView title;
        public final TextView description;
        public final TextView reward;
        public final CheckBox checkBox;
        public final RelativeLayout viewBackground, viewForeground;
        public final LinearLayout checklist;
        public final ImageButton expandButton;
        public final TextView startDate;
        public final ImageView startDateIcon;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            reward = itemView.findViewById(R.id.item_reward);
            checkBox = itemView.findViewById(R.id.item_checkbox);
            viewBackground = itemView.findViewById(R.id.item_background);
            viewForeground = itemView.findViewById(R.id.item_foreground);
            checklist = itemView.findViewById(R.id.checklist);
            expandButton = itemView.findViewById(R.id.expand_button);
            startDate = itemView.findViewById(R.id.start_date);
            startDateIcon = itemView.findViewById(R.id.start_logo);
        }

        @Override
        public View getViewForeground() {
            return this.viewForeground;
        }
    }

    void checkDaily(DailyViewAdaptor.DailyViewHolder holder) {
        String name = dailyList.get(holder.getAdapterPosition()).getTitle();

        // backup of removed item for undo purpose
        final Daily doneTask = dailyList.get(holder.getAdapterPosition());
        final int doneIndex = holder.getAdapterPosition();

        // add reward
        Executor.getInstance().addCoin(doneTask.getReward());

        // remove the item from recycler view
        removeItem(holder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(this.rootView, name + " is done!", Snackbar.LENGTH_LONG);
        snackbar.setAction("UNDO", view -> {
            // undo is selected, restore the deleted item
            snackbar.setAction("UNDO", v -> {});
            holder.checkBox.setChecked(false);
            restoreItem(doneTask, doneIndex);
            Executor.getInstance().undoCoin();
        });
        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
                    Executor.getInstance().deleteTask(doneTask);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
    }
}
