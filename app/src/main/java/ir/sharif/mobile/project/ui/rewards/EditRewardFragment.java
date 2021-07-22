package ir.sharif.mobile.project.ui.rewards;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.R;
import ir.sharif.mobile.project.ui.model.Reward;
import ir.sharif.mobile.project.ui.utils.HideSoftKeyboardHelper;


public class EditRewardFragment extends Fragment {
    private Reward reward;
    private Reward editingReward;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            reward = (Reward) getArguments().getSerializable("reward");
        }
        if (reward == null) {
            reward = Reward.getEmptyReward();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_reward, container, false);

        init_form_values(view);
        init_reward_section(view);

        view.findViewById(R.id.up_button).setOnClickListener(v -> getActivity().onBackPressed());

        view.findViewById(R.id.save_button).setOnClickListener(v -> {
            editingReward = new Reward();
            editingReward.setId(reward.getId());
            editingReward.setTitle(((TextInputEditText) view.findViewById(R.id.input_title)).getText().toString());
            editingReward.setDescription(((TextInputEditText) view.findViewById(R.id.input_description)).getText().toString());
            editingReward.setAmount(Integer.parseInt(((TextInputEditText) view.findViewById(R.id.input_reward)).getText().toString()));

            if (editingReward.getTitle().equals("")) {
                Toast.makeText(getContext(), "Title cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Executor.getInstance().saveReward(editingReward);
            getActivity().onBackPressed();
        });

        HideSoftKeyboardHelper.setupUI(view, getActivity());
        return view;
    }

    private void init_reward_section(View view) {
        TextInputEditText rewardEditText = view.findViewById(R.id.input_reward);
        rewardEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        view.findViewById(R.id.inc_reward_button).setOnClickListener(v -> {
            String reward = rewardEditText.getText().toString();
            int val = reward.trim().length() == 0 ? 0 : Integer.parseInt(reward);
            rewardEditText.setText(String.valueOf(val + 1));
        });

        view.findViewById(R.id.dec_reward_button).setOnClickListener(v -> {
            String reward = rewardEditText.getText().toString();
            int val = reward.trim().length() == 0 ? 0 : Integer.parseInt(reward);
            rewardEditText.setText(String.valueOf(val > 0 ? val - 1 : 0));
        });
    }

    void init_form_values(View view) {
        ((TextInputEditText) view.findViewById(R.id.input_title)).setText(reward.getTitle());
        ((TextInputEditText) view.findViewById(R.id.input_description)).setText(reward.getDescription());
        ((TextInputEditText) view.findViewById(R.id.input_reward)).setText(String.valueOf(reward.getAmount()));
    }

}
