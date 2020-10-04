package com.example.emslite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.emslite.models.Department;
import com.example.emslite.models.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import es.dmoral.toasty.Toasty;

public class EmployeeEditActivity extends AppCompatActivity {
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText positionField;
    private EditText ageField;
    private EditText salaryField;
    private EditText addressField;
    private Spinner departmentField;
    private Button editButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseDepartmentReference;
    private DatabaseReference databaseEmployeeReference;
    private Employee employeeForSave;
    private boolean isEdit = false;
    private boolean isAdd = false;
    private ArrayAdapter<Department> departmentArrayAdapter;
    private List<Department> departmentList = new ArrayList<>();

    private final String EMPLOYEE = "employee";
    private final String DEPARTMENT = "department";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit);

        firstNameField = findViewById(R.id.employeeEditFirstName);
        lastNameField = findViewById(R.id.employeeEditLastName);
        positionField = findViewById(R.id.employeeEditPosition);
        ageField = findViewById(R.id.employeeEditAge);
        salaryField = findViewById(R.id.employeeEditSalary);
        addressField = findViewById(R.id.employeeEditAddress);
        departmentField = findViewById(R.id.employeeEditDepartment);
        editButton = findViewById(R.id.employeeEditEditButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseDepartmentReference = firebaseDatabase.getReference(DEPARTMENT);
        databaseEmployeeReference = firebaseDatabase.getReference(EMPLOYEE);

        departmentArrayAdapter = new ArrayAdapter<Department>(this, android.R.layout.simple_spinner_item, departmentList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setText(departmentList.get(position).getName());
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setText(departmentList.get(position).getName());
                return view;
            }
        };
        departmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        departmentField.setAdapter(departmentArrayAdapter);

        employeeForSave = new Employee();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id.equals("")) {
            isAdd = true;
            editButton.setText("Add");
        } else {
            String departmentId = intent.getStringExtra("departmentId");
            String firstName = intent.getStringExtra("firstName");
            String lastName = intent.getStringExtra("lastName");
            String position = intent.getStringExtra("position");
            int age = intent.getIntExtra("age", 0);
            double salary = intent.getDoubleExtra("salary", 0);
            String address = intent.getStringExtra("address");

            employeeForSave.setId(id);
            employeeForSave.setDepartmentId(departmentId);
            employeeForSave.setFirstName(firstName);
            employeeForSave.setLastName(lastName);
            employeeForSave.setPosition(position);
            employeeForSave.setAge(age);
            employeeForSave.setSalary(salary);
            employeeForSave.setAddress(address);

            updateUI();

            firstNameField.setEnabled(false);
            lastNameField.setEnabled(false);
            positionField.setEnabled(false);
            ageField.setEnabled(false);
            salaryField.setEnabled(false);
            addressField.setEnabled(false);
            departmentField.setEnabled(false);
        }

        loadDepartments();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    employeeForSave.setFirstName(firstNameField.getText().toString());
                    employeeForSave.setLastName(lastNameField.getText().toString());
                    employeeForSave.setPosition(positionField.getText().toString());
                    employeeForSave.setAge(Integer.parseInt(ageField.getText().toString()));
                    employeeForSave.setSalary(Double.parseDouble(salaryField.getText().toString()));
                    employeeForSave.setAddress(addressField.getText().toString());

                    if (((Department) departmentField.getSelectedItem()).getId() != null) {
                        employeeForSave.setDepartmentId(((Department) departmentField.getSelectedItem()).getId());
                    }

                    databaseEmployeeReference.child(databaseEmployeeReference.push().getKey()).setValue(employeeForSave);

                    Toasty.success(EmployeeEditActivity.this, "Employee added successfully.", Toasty.LENGTH_SHORT).show();

                    Intent backIntent = new Intent(EmployeeEditActivity.this, MainActivity.class);
                    startActivity(backIntent);
                } else {
                    if (isEdit) {
                        employeeForSave.setFirstName(firstNameField.getText().toString());
                        employeeForSave.setLastName(lastNameField.getText().toString());
                        employeeForSave.setPosition(positionField.getText().toString());
                        employeeForSave.setAge(Integer.parseInt(ageField.getText().toString()));
                        employeeForSave.setSalary(Double.parseDouble(salaryField.getText().toString()));
                        employeeForSave.setAddress(addressField.getText().toString());

                        if (((Department) departmentField.getSelectedItem()).getId() != null) {
                            employeeForSave.setDepartmentId(((Department) departmentField.getSelectedItem()).getId());
                        }

                        databaseEmployeeReference.child(employeeForSave.getId()).setValue(employeeForSave);

                        Toasty.success(EmployeeEditActivity.this, "Employee updated successfully.", Toasty.LENGTH_SHORT).show();
                        Intent backIntent = new Intent(EmployeeEditActivity.this, MainActivity.class);
                        startActivity(backIntent);
                    } else {
                        isEdit = true;

                        firstNameField.setEnabled(true);
                        lastNameField.setEnabled(true);
                        positionField.setEnabled(true);
                        ageField.setEnabled(true);
                        salaryField.setEnabled(true);
                        addressField.setEnabled(true);
                        departmentField.setEnabled(true);
                        editButton.setText("Save");
                    }
                }
            }
        });
    }

    private void loadDepartments() {
        databaseDepartmentReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Department departmentItem;

                departmentList.add(new Department("Select a Department"));

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        departmentItem = item.getValue(Department.class);
                        departmentItem.setId(item.getKey());

                        if (departmentItem != null) {
                            departmentList.add(departmentItem);
                        }
                    }

                    departmentArrayAdapter.notifyDataSetChanged();

                    for (Department item: departmentList) {
                        if (employeeForSave.getDepartmentId() != null && item.getId() != null && item.getId().equals(employeeForSave.getDepartmentId())) {
                            departmentField.setSelection(departmentList.indexOf(item));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void updateUI() {
        firstNameField.setText(employeeForSave.getFirstName());
        lastNameField.setText(employeeForSave.getLastName());
        positionField.setText(employeeForSave.getPosition());
        ageField.setText(Integer.toString(employeeForSave.getAge()));
        salaryField.setText(Double.toString(employeeForSave.getSalary()));
        addressField.setText(employeeForSave.getAddress());
    }
}