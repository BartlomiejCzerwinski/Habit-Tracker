package com.example.myapplication.myapplication.ui.settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

        SettingsManager settingsManager = new SettingsManager(binding, getActivity().findViewById(R.id.user_name_text), getContext());
        switchColourTheme(Color.parseColor("#00FF00"), Color.parseColor("#00FF00"), Color.parseColor("#00FF00"));
        return root;
    }

    public void switchColourTheme(int colorStart, int colorCenter, int colorEnd) {
        int resourceId = R.drawable.side_nav_bar;
        GradientDrawable drawable = (GradientDrawable) getContext().getDrawable(resourceId);
        drawable.setColors(new int[]{colorStart, colorCenter, colorEnd});
        drawable.invalidateSelf();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
