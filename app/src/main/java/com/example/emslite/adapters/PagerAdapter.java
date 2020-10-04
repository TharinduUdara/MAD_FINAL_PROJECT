package com.example.emslite.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.emslite.DepartmentFragment;
import com.example.emslite.EmployeeFragment;

public class PagerAdapter extends FragmentStateAdapter {
    private int tabCount;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        this.tabCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return EmployeeFragment.newInstance();
            case 1:
                return DepartmentFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
