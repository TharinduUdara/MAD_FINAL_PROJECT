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
import com.example.emslite.adapters.DepartmentAdapter;
import com.example.emslite.models.Department;
import com.example.emslite.utils.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DepartmentFragment extends Fragment {
    private List<Department> departmentList = new ArrayList<>();
    private DepartmentAdapter departmentAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private final String DEPARTMENT = "department";

    public DepartmentFragment() {
        // Required empty public constructor
    }

    public static DepartmentFragment newInstance() {
        DepartmentFragment fragment = new DepartmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        recyclerView = view.findViewById(R.id.departmentRecyclerView);
        departmentAdapter = new DepartmentAdapter(departmentList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(DEPARTMENT);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(departmentAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Department department = departmentList.get(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), DepartmentEmployeeActivity.class);
                intent.putExtra("id", department.getId());
                intent.putExtra("name", department.getName());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));

        departmentData();

        return view;
    }

    private void departmentData() {
        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Department departmentItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        departmentItem = item.getValue(Department.class);
                        departmentItem.setId(item.getKey());

                        if (departmentItem != null) {
                            departmentList.add(departmentItem);
                        }
                    }
                    departmentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}