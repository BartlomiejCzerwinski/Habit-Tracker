package com.example.myapplication.myapplication.ui.tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.myapplication.databinding.FragmentTutorialBinding;

public class TutorialFragment extends Fragment {

    private FragmentTutorialBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TutorialViewModel tutorialViewModel =
                new ViewModelProvider(this).get(TutorialViewModel.class);

        binding = FragmentTutorialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTutorial;
        tutorialViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}