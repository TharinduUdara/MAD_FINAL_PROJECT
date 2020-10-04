package com.example.emslite.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.emslite.R;
import com.example.emslite.models.Department;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyViewHolder> {

    private List<Department> departmentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.departmentName);
        }
    }

    public DepartmentAdapter(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    @Override
    public DepartmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_department, parent, false);

        return new DepartmentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DepartmentAdapter.MyViewHolder holder, int position) {
        Department department = departmentList.get(position);

        holder.name.setText(department.getName());
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }
}
