package com.example.projeto.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

public class MyDBHelper extends SQLiteOpenHelper {
    private static  final String DBNAME = "mydb.db";
    private static  final int VERSION = 1;

    private static  final String TABLE_NAME = "Carro";
    private static  final String ID = "id";
    public static  final String COMBUSTIVEL = "combustivel";
    public static  final String MARCA = "marca";
    public static  final String MODELO = "modelo";
    public static  final String MATRICULA = "matricula";
    public static  final String MES = "mes";
    public static  final String ANO = "ano";
    public static  final String DINHERIOGASTO = "DinheroGasto";
    public static  final String CARRO = "carro";
    public static  final Integer KILOMETROS = 0;


    private SQLiteDatabase myDB;

    public MyDBHelper(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTable = "CREATE TABLE " + TABLE_NAME +
                " ("+
                COMBUSTIVEL + " TEXT NOT NULL, " +
                MARCA + " TEXT NOT NULL, " +
                MODELO + " TEXT NOT NULL, " +
                MATRICULA + " TEXT NOT NULL PRIMARY KEY, " +
                MES + " TEXT NOT NULL, " +
                ANO + " TEXT NOT NULL, " +
                DINHERIOGASTO + " DOUBLE NOT NULL" +
                ")";
        db.execSQL(queryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public  void openDB()
    {
        myDB = getWritableDatabase();
    }
    public  void closeDB()
    {
        if(myDB != null && myDB.isOpen()) {
            myDB.close();
        }
    }

    public long insert(String Combustivel,String Marca, String Modelo, String Mes, String Ano, String Matricula,
                       Double DinheiroGasto)
    {
        ContentValues values = new ContentValues();

        values.put(COMBUSTIVEL, Combustivel);
        values.put(MARCA, Marca);
        values.put(MODELO, Modelo);
        values.put(MATRICULA, Matricula);
        values.put(MES, Mes);
        values.put(ANO, Ano);
        values.put(DINHERIOGASTO, DinheiroGasto);


        return myDB.insert(TABLE_NAME, null, values);
    }



    public long update(String Combustivel, String Marca, String Modelo, String matricula, String Mes,
                String Ano , Double euros)
    {
        ContentValues values = new ContentValues();

        values.put(DINHERIOGASTO, euros);

        String where =MATRICULA +"= '"+matricula+"'";

        return myDB.update(TABLE_NAME, values, where, null);
    }

    public long delete(int Id)
    {
        String where = ID + " = " + Id;

        return myDB.delete(TABLE_NAME, where, null);
    }


    public Cursor getAllRecords()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        return  myDB.rawQuery(query, null);
    }

    public Cursor getRecords(String carro)
    {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE MATRICULA = " + "'"+ carro+"'";
        return  myDB.rawQuery(query, null);
    }
}
