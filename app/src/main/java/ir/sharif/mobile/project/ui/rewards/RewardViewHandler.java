package ir.sharif.mobile.project.ui.rewards;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import ir.sharif.mobile.project.ui.model.Reward;
import ir.sharif.mobile.project.ui.repository.RepositoryHolder;
import ir.sharif.mobile.project.ui.repository.RewardRepository;


public class RewardViewHandler extends Handler {

    public static final int DELETE_REWARD = 0;
    public static final int DONE_REWARD = 1;
    public static final int SAVE_REWARD = 2;


    public RewardViewHandler() {
        super(Looper.myLooper());
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        RewardRepository helper = RepositoryHolder.getRewardRepository();
        if (helper == null) {
            return;
        }

        switch (msg.what) {
            case DELETE_REWARD:
                helper.delete(((Reward) msg.obj).getId());
                break;
            case DONE_REWARD:
                // TODO: 7/21/21 add reward
                break;
            case SAVE_REWARD:
                helper.save((Reward) msg.obj);
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
