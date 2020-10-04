package com.example.emslite;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.emslite.adapters.EmployeeAdapter;
import com.example.emslite.models.Employee;
import com.example.emslite.utils.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFragment extends Fragment {
    private List<Employee> employeeList = new ArrayList<>();
    private EmployeeAdapter employeeAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private final String EMPLOYEE = "employee";

    public EmployeeFragment() {
        // Required empty public constructor
    }

    public static EmployeeFragment newInstance() {
        EmployeeFragment fragment = new EmployeeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);

        recyclerView = view.findViewById(R.id.employeeRecyclerView);
        employeeAdapter = new EmployeeAdapter(employeeList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(EMPLOYEE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(employeeAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Employee employee = employeeList.get(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), EmployeeEditActivity.class);
                intent.putExtra("id", employee.getId());
                intent.putExtra("departmentId", employee.getDepartmentId());
                intent.putExtra("firstName", employee.getFirstName());
                intent.putExtra("lastName", employee.getLastName());
                intent.putExtra("position", employee.getPosition());
                intent.putExtra("age", employee.getAge());
                intent.putExtra("salary", employee.getSalary());
                intent.putExtra("address", employee.getAddress());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));

        employeeData();

        return view;
    }

    private void employeeData() {
        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Employee employeeItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        employeeItem = item.getValue(Employee.class);
                        employeeItem.setId(item.getKey());

                        if (employeeItem != null) {
                            employeeList.add(employeeItem);
                        }
                    }
                    employeeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}