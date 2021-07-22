package ir.sharif.mobile.project.ui.rewards;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import ir.sharif.mobile.project.Executor;
import ir.sharif.mobile.project.ui.model.Reward;


public class RewardViewHandler extends Handler {

    public static final int LOAD_DONE = 0;
    public static final int UPDATE_SCORE = 2;
    public static final int SHOW_TOAST = Executor.SHOW_TOAST;

    private WeakReference<RewardFragment> rewardFragment;

    public RewardViewHandler(RewardFragment rewardFragment) {
        this.rewardFragment = new WeakReference<>(rewardFragment);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (rewardFragment == null)
            return;

        switch (msg.what) {
            case LOAD_DONE:
                rewardFragment.get().getAdapter().addAll(((List<Reward>) msg.obj));
                break;
            case SHOW_TOAST:
                String text = (String) msg.obj;
                Toast.makeText(rewardFragment.get().getContext(), text, Toast.LENGTH_LONG)
                        .show();
                break;
            case UPDATE_SCORE:
                // TODO: 7/22/21 update score
                break;
            default:
                Log.e("Handler", "Unknown Message");
        }
    }
}
