package com.example.projeto.ui.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.projeto.R;
import com.example.projeto.ui.MyDBHelper;
import com.google.android.material.snackbar.Snackbar;

import java.security.AccessController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MyDBHelper myDBHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        myDBHelper = new MyDBHelper(container.getContext());

        Button btn = (Button)root.findViewById(R.id.btnInf);
        btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Spinner carro = (Spinner)root.findViewById(R.id.spinCarInf);
            TextView text = (TextView)root.findViewById(R.id.txtInfo);


            if(carro.getCount() != 0 )
            {
                String car = String.valueOf(carro.getSelectedItem().toString());
                myDBHelper.openDB();
                Cursor cursor1 = myDBHelper.getRecords(car);
                StringBuffer finalData = new StringBuffer();
                StringBuffer Modelo = new StringBuffer();
                StringBuffer Marca = new StringBuffer();
                StringBuffer Ano = new StringBuffer();
                StringBuffer Mes = new StringBuffer();
                StringBuffer Combustivel = new StringBuffer();
                Double DinheiroGasto = null;



                for(cursor1.moveToFirst(); !cursor1.isAfterLast(); cursor1.moveToNext())
                {
                    finalData.append(cursor1.getString(cursor1.getColumnIndex(myDBHelper.MATRICULA)));
                    finalData.append("  -  ");
                    finalData.append(cursor1.getString(cursor1.getColumnIndex(myDBHelper.DINHERIOGASTO)));

                    Modelo.append(cursor1.getString((cursor1.getColumnIndex(myDBHelper.MODELO))));
                    Marca.append(cursor1.getString((cursor1.getColumnIndex(myDBHelper.MARCA))));
                    Ano.append(cursor1.getString((cursor1.getColumnIndex(myDBHelper.ANO))));
                    Mes.append(cursor1.getString((cursor1.getColumnIndex(myDBHelper.MES))));
                    Combustivel.append(cursor1.getString((cursor1.getColumnIndex(myDBHelper.COMBUSTIVEL))));
                    DinheiroGasto = Double.parseDouble(cursor1.getString((cursor1.getColumnIndex(myDBHelper.DINHERIOGASTO))));
                }
                String Dinherio = String.valueOf(DinheiroGasto);
                text.setText("");
                text.setText("Marca : "+Marca+"\nModelo : "+Modelo+"\nCombustivel : "+Combustivel+"\nMes Ano : "+Mes+"/"+Ano );
                Snackbar.make(v, ""+car+" já gastou "+ Dinherio + " Euros em combustivel", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();


                Calendar calendar = Calendar.getInstance();
                int thisMonth = calendar.get(Calendar.MONTH);
                thisMonth = thisMonth + 1 ;
                String mesAtual = String.valueOf(thisMonth);

                Integer MesAtual = Integer.parseInt(mesAtual);
                Integer MesCarro = Integer.parseInt(Mes.toString());

                if(MesAtual == MesCarro)
                {
                    Notificacao(container.getContext(),"Este mês o seu carro deve ir fazer a inspeçao.", "Inspeçao periodica");
                }
                myDBHelper.closeDB();
            }
            else
            {
                Snackbar.make(v, "Não tem carros adicionados \nAdicione um carro primeiro", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }});

        myDBHelper.openDB();

        Cursor cursor = myDBHelper.getAllRecords();
        StringBuffer finalData = new StringBuffer();
        List<String> Carros= new ArrayList<String>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            finalData.append(cursor.getString(cursor.getColumnIndex(myDBHelper.DINHERIOGASTO)));
            Carros.add(cursor.getString(cursor.getColumnIndex(myDBHelper.MATRICULA)));
        }
        Spinner spinnercarro = (Spinner) root.findViewById(R.id.spinCarInf);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(container.getContext(),   android.R.layout.simple_spinner_item, Carros);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercarro.setAdapter(spinnerArrayAdapter);



        myDBHelper.closeDB();

        return root;
    }
    public void Notificacao(Context context0, String Texto, String Notificacao)
    {
        AccessController container = null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context0,"Not")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(Notificacao)
                .setContentText(Texto)
                //.setColor(222)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("Not","Name",importance);
        channel.setDescription("");

        NotificationManager notificationManager = getSystemService(context0,NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context0);
        notificationManagerCompat.notify(100, builder.build());
    }
}
