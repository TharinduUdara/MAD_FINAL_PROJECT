package com.example.emslite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class DepartmentEmployeeActivity extends AppCompatActivity {
    private List<Employee> employeeList = new ArrayList<>();
    private EmployeeAdapter employeeAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private final String EMPLOYEE = "employee";
    private String departmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_employee);

        recyclerView = findViewById(R.id.deptEmployeeRecyclerView);
        employeeAdapter = new EmployeeAdapter(employeeList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(EMPLOYEE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(employeeAdapter);

        departmentId = getIntent().getStringExtra("id");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Employee employee = employeeList.get(position);

                Intent intent = new Intent(getApplicationContext(), EmployeeEditActivity.class);
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
    }

    private void employeeData() {
        databaseReference.orderByChild("departmentId").equalTo(departmentId).addListenerForSingleValueEvent(new ValueEventListener() {
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