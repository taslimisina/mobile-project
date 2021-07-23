package ir.sharif.mobile.simple_task_management;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        BottomNavigationView navView = view.findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_habits, R.id.navigation_dailies, R.id.navigation_todo)
                .build();
//        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavController navController = Navigation.findNavController(view.findViewById(R.id.nav_host_fragment));
//        NavigationUI.setupActionBarWithNavController((MainActivity)getActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        return view;
    }
}