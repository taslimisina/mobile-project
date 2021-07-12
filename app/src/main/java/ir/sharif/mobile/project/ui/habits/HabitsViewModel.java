package ir.sharif.mobile.project.ui.habits;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HabitsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HabitsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is habit fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}