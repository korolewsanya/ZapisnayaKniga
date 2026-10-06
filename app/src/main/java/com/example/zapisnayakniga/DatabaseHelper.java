package com.example.zapisnayakniga;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Bloknot1.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "Stranici"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ZAPISI = "zapisi";

    public DatabaseHelper(Context context) {
        super( context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Stranici (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + COLUMN_NAME + " TEXT ," + COLUMN_ZAPISI + " TEXT);");
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME + ") VALUES ('Без категории');");
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME + ") VALUES ('Список дел на сегодня');");
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME + ") VALUES ('Список покупок');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}

