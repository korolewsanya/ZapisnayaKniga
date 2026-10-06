package com.example.zapisnayakniga;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.O)

public class Papki extends AppCompatActivity {
    ListView userList;
    com.example.zapisnayakniga.DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    String company;
    String selectedItem;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papki);
        databaseHelper = new com.example.zapisnayakniga.DatabaseHelper( getApplicationContext());
        userList = (ListView) findViewById(R.id.lv);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Принимаем данные из Glav(EditText)
        Bundle arguments = getIntent().getExtras();
        company = arguments.getString(Glav.AGE_KEY);

        // получаем выбранный элемент из списка(ListView)
        TextView textView = (TextView) view;
        selectedItem = (String) textView.getText();

        //Вставка времени и даты
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd-MM-yyyy");
        String date = sdf.format(Calendar.getInstance().getTime());

        // открываем подключение для записи(не для чтения)
        //ВАЖНО ЧТОБЫ getWritableDatabase() СОВПАДАЛ С НАПИСАННЫМ НИЖЕ. Иначе добавление данныхбудет идти не сразу
        // а после следующего добавления
        db = databaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, selectedItem);
        cv.put(DatabaseHelper.COLUMN_ZAPISI,date + "\n" + company);
        db.insert(DatabaseHelper.TABLE, null, cv);

        Toast.makeText(getApplicationContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), Glav.class);
        intent.putExtra("klu4","");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
            }
          });
        }

         @Override
        public void onResume() {
        super.onResume();
        // открываем подключение для записи(не для чтения)
        //ВАЖНО ЧТОБЫ getWritableDatabase() СОВПАДАЛ С НАПИСАННЫМ ВЫШЕЕ. Иначе добавление данныхбудет идти не сразу
        // а после следующего добавления
        db = databaseHelper.getWritableDatabase();
        //получаем данные из бд в виде курсора. Выводим уникальные значения
        userCursor = db.rawQuery("select * from Stranici GROUP BY name ", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{DatabaseHelper.COLUMN_NAME};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
        userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

        userList.setAdapter(userAdapter);
        }

        public void add (View view){
        // действия, совершаемые после нажатия на кнопку
        Toast.makeText(getApplicationContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, SozPapki.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // запуск activity
        startActivity(intent);
        }

          @Override
          public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
        }
}