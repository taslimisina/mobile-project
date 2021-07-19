package ir.sharif.mobile.project.ui.dailies;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Daily;

public class DailyViewAdaptor extends RecyclerView.Adapter<DailyViewAdaptor.DailyViewHolder>{
    private List<Daily> dailies;
    private final DailyViewHandler dailyViewHandler;
    private final Context context;

    public DailyViewAdaptor(List<Daily> dailys, DailyViewHandler dailyViewHandler, Context context) {
        this.dailies = dailys;
        this.dailyViewHandler = dailyViewHandler;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyViewAdaptor.DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_dailies, parent, false);
        return new DailyViewAdaptor.DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewAdaptor.DailyViewHolder holder, int position) {
        final Daily daily = dailies.get(position);

        holder.viewBackground.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("daily", daily);
            Navigation.findNavController(v.getRootView().findViewById(R.id.fragment))
                    .navigate(R.id.action_mainFragment_to_editDailyFragment, bundle);
        });

        //Todo complete function
    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground;
        //Todo other fields

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.viewBackground = itemView.findViewById(R.id.item_background);
        }
    }
}
