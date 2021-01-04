package com.example.projeto.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.projeto.R;
import com.example.projeto.ui.MyDBHelper;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    Button btn ;
    private MyDBHelper myDBHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        myDBHelper = new MyDBHelper(container.getContext());

        myDBHelper.openDB();
        Cursor cursor = myDBHelper.getAllRecords();
        List<String> TodasMatriculas= new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            TodasMatriculas.add(cursor.getString(cursor.getColumnIndex(myDBHelper.MATRICULA)));
        }
        myDBHelper.closeDB();

        btn = (Button)root.findViewById(R.id.buttonAddCarro);
        btn.setOnClickListener(new View.OnClickListener()

        {

            @Override public void onClick(View v)
            {
                EditText edtmarca = (EditText)root.findViewById(R.id.editTxtMarca);
                EditText edtmodelo = (EditText)root.findViewById(R.id.editTxtModelo);
                EditText edtmatricula = (EditText)root.findViewById(R.id.editTxtMatricula);
                Spinner spcombustivel = (Spinner)root.findViewById(R.id.spinnerCombustivel);
                Spinner spano = (Spinner)root.findViewById(R.id.spinAno);
                Spinner spmes = (Spinner)root.findViewById(R.id.spinMes);

                edtmatricula.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

                String marca = String.valueOf(edtmarca.getText().toString());
                String modelo = String.valueOf(edtmodelo.getText().toString());
                String matricula = String.valueOf(edtmatricula.getText().toString());
                String combustivel = String.valueOf(spcombustivel.getSelectedItem().toString());
                String ano = String.valueOf(spano.getSelectedItem().toString());
                String mes = String.valueOf(spmes.getSelectedItem().toString());
                Double DinheiroGasto = 0.0;


                if(edtmarca.length() != 0 || edtmodelo.length() != 0 || edtmatricula.length() != 0)
                {
                    if (TodasMatriculas.contains(matricula)) {
                        Snackbar.make(v, ""+matricula+" já foi adicionado anteriormente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else
                    {
                        myDBHelper.openDB();
                        myDBHelper.insert(combustivel, marca, modelo, mes, ano, matricula,DinheiroGasto);
                        Snackbar.make(v, "Carro adicionado com sucesso", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        myDBHelper.closeDB();
                    }
                }
                else
                {
                    Snackbar.make(v, "Tem de preencher todos os campos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        List<String> Years= new ArrayList<String>();
        for (int i = 1980; i < year + 1; i++)
        {
            Years.add(String.valueOf(i));
        }
        Spinner spinnerAno = (Spinner) root.findViewById(R.id.spinAno);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(container.getContext(),   android.R.layout.simple_spinner_item, Years);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno.setAdapter(spinnerArrayAdapter);
        return root;
    }
}