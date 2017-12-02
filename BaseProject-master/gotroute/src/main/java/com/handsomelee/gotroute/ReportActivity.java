package com.handsomelee.gotroute;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class ReportActivity extends Fragment {
  Spinner spinner;
  private Button Btn;
  private EditText editText;
  
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.activity_report, container, false);
    
    spinner = (Spinner) rootView.findViewById(R.id.spinner);
    editText = (EditText) rootView.findViewById(R.id.editText);
    Btn = (Button) rootView.findViewById(R.id.Btn_Send_Report);
    String[] array_Spiner = getResources().getStringArray(R.array.reports_array);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.support_simple_spinner_dropdown_item, array_Spiner);

    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
  
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    
      }
  
      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
    
      }
    });
    
    Btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getContext(), "{type : " + spinner.getSelectedItem().toString() + ", comment : "+ editText.getText().toString() +" }", Toast.LENGTH_LONG).show();
      }
    });
    return rootView;
  }
  
}
