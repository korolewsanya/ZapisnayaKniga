package com.example.zapisnayakniga;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Glav extends AppCompatActivity {

    //Это значение мы используем для проверки успеха
    //получения обратной информации в onActivityResult ():
    private static final int Print_Words = 100;

    static final String AGE_KEY = "AGE";
    public final static String FILE_NAME = "content.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glav);

        //Принимаем данные по ключу из Papki, т.е. делаем строку ввода пустой
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            EditText ageView = (EditText) findViewById(R.id.ET);
            String age = extras.getString("klu4");
            ageView.setText(age);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_glav, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.soh:
                // Получаем текстовое поле в текущей Activity
                EditText editText = (EditText) findViewById(R.id.ET);
                // Получае текст данного текстового поля
                String message = editText.getText().toString();

                //Проверка  EditText на пустоту
                if(editText.getText().toString().isEmpty()){
                    CustomDialog dialog = new CustomDialog();
                    dialog.show(getSupportFragmentManager(), "custom");}
                else {
                    // действия, совершаемые после нажатия на кнопку
                    // Создаем объект Intent для вызова новой Activity
                    Intent intent = new Intent(this, Papki.class);
                    // Добавляем для Papki(в ListView) с помощью свойства putExtra объект - первый параметр - ключ,
                    // второй параметр - значение этого объекта
                    intent.putExtra(AGE_KEY, message);

                    // сохранение файла

                    FileOutputStream fos = null;
                    try {
                        EditText textBox = (EditText) findViewById(R.id.ET);
                        String text = textBox.getText().toString();

                        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                        fos.write(text.getBytes());

                    } catch (IOException ex) {

                        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException ex) {

                            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // запуск activity
                    startActivity(intent);}

                    return true;

            case R.id.posm:
                // действия, совершаемые после нажатия на кнопку
                // Создаем объект Intent для вызова новой Activity
                Intent i = new Intent(this,PapkiPros.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // запуск activity
                startActivity(i);

                return true;

            case R.id.ud:
                // действия, совершаемые после нажатия на кнопку
                // Создаем объект Intent для вызова новой Activity
                Intent t = new Intent(this,PapkiUd.class);
                t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // запуск activity
                startActivity(t);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void del(View view) {
        EditText e=(EditText)findViewById(R.id.ET);
        e.setText("");
    }

    public void w(View view) {
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
            EditText e=(EditText)findViewById(R.id.ET);
            e.append(" "+a);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
