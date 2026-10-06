package com.example.zapisnayakniga;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SozPapki extends AppCompatActivity {
    long userId=0;
    String text;
    SQLiteDatabase db;
    com.example.zapisnayakniga.DatabaseHelper databaseHelper;
    private static final int Print_Words = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soz_papki);

        databaseHelper = new com.example.zapisnayakniga.DatabaseHelper( getApplicationContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    public void save(View view) {
        // Получаем текстовое поле в текущей Activity
        EditText editText = (EditText) findViewById(R.id.et);
        //Проверка  EditText на пустоту
        if (editText.getText().toString().isEmpty()) {
            CustomDialog dialog = new CustomDialog();
            dialog.show(getSupportFragmentManager(), "custom");
        } else {
            // открытие файла
            FileInputStream fin = null;

            try {
                fin = openFileInput(Glav.FILE_NAME);
                byte[] bytes = new byte[fin.available()];
                fin.read(bytes);
                text = new String(bytes);
            } catch (IOException ex) {

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {

                try {
                    if (fin != null)
                        fin.close();
                } catch (IOException ex) {

                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            //Вставка времени и даты
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd-MM-yyyy");
            String date = sdf.format(Calendar.getInstance().getTime());

            // открываем подключение
            databaseHelper = new DatabaseHelper(this);
            db = databaseHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.COLUMN_NAME, editText.getText().toString());
            cv.put(DatabaseHelper.COLUMN_ZAPISI,date + "\n" + text);
            db.insert(DatabaseHelper.TABLE, null, cv);
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            goHome();
        }
    }

    private void goHome(){
        // закрываем подключение
        db.close();
        Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        // переход к главной activity
        Intent intent = new Intent(this, Glav.class);
        intent.putExtra("klu4","");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
    }

    public void gol(View view) {

        //Вызываем RecognizerIntent для голосового ввода и преобразования голоса в текст:
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Скажите слово для распознавания");
        startActivityForResult(intent, Print_Words);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Проверяем успешность получения обратного ответа:
        if (requestCode==Print_Words && resultCode==RESULT_OK) {
            //Как результат получаем строковый массив слов, похожих на произнесенное:
            ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //Переводим в строку(для получения одного слова)
            //и отображаем  в элементе EditText:
            String a= result.get(0);
            EditText e=(EditText)findViewById(R.id.et);
            e.append(a);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
