package com.example.projeto.ui.notifications;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private MyDBHelper myDBHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        myDBHelper = new MyDBHelper(container.getContext());



        Button btn = (Button)root.findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox checkBox = (CheckBox)root.findViewById(R.id.checkBox);
                EditText edtLitros = (EditText)root.findViewById(R.id.edtLitros);
                EditText edtPreco = (EditText)root.findViewById(R.id.edtPreco);
                EditText edkilimetros = (EditText)root.findViewById(R.id.edtKm);
                TextView txtResult = (TextView)root.findViewById(R.id.txtResultado);
                Spinner spnCarro = (Spinner)root.findViewById(R.id.spnCarros);


                if(spnCarro.getCount() != 0)
                {

                    if(edtLitros.length() == 0 || edtPreco.length() == 0 || edkilimetros.length() == 0 )
                    {
                        //txtResult.setText("Tem de Prencher todos os campos");
                        Snackbar.make(v, "Tem de Prencher todos os campos", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    else
                    {
                        if(checkBox.isChecked())
                        {

                            Double litros;
                            Double preco;
                            Double kilometros;
                            Double result;
                            litros = Double.valueOf(edtLitros.getText().toString());
                            kilometros = Double.valueOf(edkilimetros.getText().toString());
                            preco = Double.valueOf(edtPreco.getText().toString());

                            String carro = String.valueOf(spnCarro.getSelectedItem().toString());
                            result = (litros / kilometros) * 100;



                            myDBHelper.openDB();

                            StringBuffer Modelo = new StringBuffer();
                            StringBuffer Marca = new StringBuffer();
                            StringBuffer Ano = new StringBuffer();
                            StringBuffer Mes = new StringBuffer();
                            StringBuffer Combustivel = new StringBuffer();

                            Double DinheiroGasto = null;
                            Cursor cursorDinheiro = myDBHelper.getRecords(carro);
                            StringBuffer finalDataDinehrip = new StringBuffer();
                            List<String> CarrosDinheiro= new ArrayList<String>();
                            for(cursorDinheiro.moveToFirst(); !cursorDinheiro.isAfterLast(); cursorDinheiro.moveToNext())
                            {
                                CarrosDinheiro.add(cursorDinheiro.getString(cursorDinheiro.getColumnIndex(myDBHelper.DINHERIOGASTO)));

                                DinheiroGasto = Double.parseDouble(cursorDinheiro.getString((cursorDinheiro.getColumnIndex(myDBHelper.DINHERIOGASTO))));
                                Modelo.append(cursorDinheiro.getString((cursorDinheiro.getColumnIndex(myDBHelper.MODELO))));
                                Marca.append(cursorDinheiro.getString((cursorDinheiro.getColumnIndex(myDBHelper.MARCA))));
                                Ano.append(cursorDinheiro.getString((cursorDinheiro.getColumnIndex(myDBHelper.ANO))));
                                Mes.append(cursorDinheiro.getString((cursorDinheiro.getColumnIndex(myDBHelper.MES))));
                                Combustivel.append(cursorDinheiro.getString((cursorDinheiro.getColumnIndex(myDBHelper.COMBUSTIVEL))));
                            }

                            String Combustivel1 = Combustivel.toString();
                            String Marca1 = Marca.toString();
                            String Modelo1 = Modelo.toString();
                            String Mes1 = Mes.toString();
                            String Ano1 = Ano.toString();


                            Double precoTotal = DinheiroGasto + litros;




                            long ResultUpdate = myDBHelper.update(Combustivel1, Marca1, Modelo1, carro, Mes1, Ano1, precoTotal);



                            if(ResultUpdate == -1)
                            {
                                //NAO ATUALIZOU
                            }
                            else
                            {
                                Snackbar.make(v, "Comsumiu "+result.toString()+" litros aos 100KM  " + carro, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                            myDBHelper.closeDB();


                        }
                        else
                        {
                            Snackbar.make(v, "Tem de atestar o deposito", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
                else
                {
                    Snackbar.make(v, "Ainda nao tem um carro adicionado", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        myDBHelper.openDB();
        Cursor cursor = myDBHelper.getAllRecords();
        StringBuffer finalData = new StringBuffer();
        List<String> Carros= new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            finalData.append(cursor.getString(cursor.getColumnIndex(myDBHelper.MATRICULA)));
            Carros.add(cursor.getString(cursor.getColumnIndex(myDBHelper.MATRICULA)));
        }
        Spinner car = (Spinner) root.findViewById(R.id.spnCarros);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(container.getContext(),   android.R.layout.simple_spinner_item, Carros);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        car.setAdapter(spinnerArrayAdapter);
        myDBHelper.closeDB();


        return root;
    }
}