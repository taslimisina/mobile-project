package ir.sharif.mobile.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ir.sharif.mobile.project.repository.RepositoryHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RepositoryHolder.init(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}