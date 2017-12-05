package com.handsomelee.gotroute.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.handsomelee.gotroute.MainActivity;
import com.handsomelee.gotroute.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.EventListener;

public class ReportActivity extends Fragment {
  Spinner spinner;
  private Button Btn;
  private EditText editText;
  public static String reportType = "";
  public static String comment = "";
  
  
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.activity_report, container, false);
      
      spinner = (Spinner) rootView.findViewById(R.id.spinner);
      editText = (EditText) rootView.findViewById(R.id.editText);
      editText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
  
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { comment = charSequence.toString(); }
  
        @Override
        public void afterTextChanged(Editable editable) { }
      });
  
      Btn = (Button) rootView.findViewById(R.id.Btn_Send_Report);
      String[] array_Spiner = getResources().getStringArray(R.array.reports_array);
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.support_simple_spinner_dropdown_item, array_Spiner);
  
      spinner.setAdapter(adapter);
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
  
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          reportType = adapterView.getSelectedItem().toString();
        }
  
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
  
        }
      });
      
  //    Btn.setOnClickListener(new View.OnClickListener() {
  //      @Override
  //      public void onClick(View view) {
  //        Toast.makeText(getContext(), "{type : " + spinner.getSelectedItem().toString() + ", comment : "+ editText.getText().toString() +" }", Toast.LENGTH_LONG).show();
  //      }
  //    });
      return rootView;
    }
  
}
