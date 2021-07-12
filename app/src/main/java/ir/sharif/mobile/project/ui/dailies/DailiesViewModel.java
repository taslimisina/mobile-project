package ir.sharif.mobile.project.ui.dailies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailiesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DailiesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is daily fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}