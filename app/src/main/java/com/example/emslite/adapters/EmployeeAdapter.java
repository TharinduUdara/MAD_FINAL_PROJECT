package com.example.emslite.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.emslite.R;
import com.example.emslite.models.Employee;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {

    private List<Employee> employeeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView position;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemEmployeeName);
            position = (TextView) view.findViewById(R.id.itemEmployeePosition);
        }
    }

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public EmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_employee, parent, false);

        return new EmployeeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmployeeAdapter.MyViewHolder holder, int position) {
        Employee employee = employeeList.get(position);

        String name = employee.getFirstName() +" "+ employee.getLastName();
        holder.name.setText(name);
        holder.position.setText(employee.getPosition());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
