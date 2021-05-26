package org.techtown.dgu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class License extends Fragment {

    RecyclerView license_recycler;
    studylicenseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.license,container,false);

        initUI(rootView);
        Button button1 = (Button)rootView.findViewById(R.id.button2); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
            private void openDialog() {
                DialogFragment InputLicense = new InputLicense();
                // InputSchoolSubject.setTargetFragment(Subject.this, 0);
                InputLicense.show(getFragmentManager(), "License Input");
            }
        });
        return rootView;
    }

    private void initUI(ViewGroup rootView){

        license_recycler = rootView.findViewById(R.id.license_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        license_recycler.setLayoutManager(layoutManager);

        adapter = new studylicenseAdapter();

        adapter.addItem(new study_license("토익","01:24:37"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));
        adapter.addItem(new study_license("정보처리기사","03:56:10"));

        license_recycler.setAdapter(adapter);
    }

}
