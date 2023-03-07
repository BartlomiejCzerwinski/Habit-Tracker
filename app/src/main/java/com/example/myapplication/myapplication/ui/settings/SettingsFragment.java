package com.example.myapplication.myapplication.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.example.myapplication.myapplication.MainActivity;
import com.example.myapplication.myapplication.R;
import com.example.myapplication.myapplication.SettingsManager;
import com.example.myapplication.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.myapplication.ui.home.HomeViewModel;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        SettingsManager settingsManager = new SettingsManager(binding, getActivity().findViewById(R.id.user_name_text));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
