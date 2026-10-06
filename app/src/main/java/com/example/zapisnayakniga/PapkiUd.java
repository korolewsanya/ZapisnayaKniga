package com.example.zapisnayakniga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class PapkiUd extends AppCompatActivity {
    ListView userList;
    com.example.zapisnayakniga.DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.papki_ud);

        TextView t = (TextView) findViewById(R.id.tv);
        t.setText("Выберите папку из которой нужно далить запись");
        t.setTextColor(Color.MAGENTA);

        Button b=(Button) findViewById(R.id.b);
        b.setVisibility(View.GONE);

        userList = (ListView) findViewById(R.id.lv);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // получаем выбранный элемент из списка(ListView)
                TextView textView = (TextView) view;
                selectedItem = (String) textView.getText();

                Intent intent = new Intent(getApplicationContext(), Udalit.class);
                intent.putExtra("i", selectedItem);
                startActivity(intent);
            }
        });
        databaseHelper = new com.example.zapisnayakniga.DatabaseHelper( getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение для записи(не для чтения)
        //ВАЖНО ЧТОБЫ getWritableDatabase() СОВПАДАЛ С НАПИСАННЫМ ВЫШЕЕ. Иначе добавление данныхбудет идти не сразу
        // а после следующего добавления
        db = databaseHelper.getWritableDatabase();
        //получаем данные из бд в виде курсора. Выводим уникальные значения
        userCursor = db.rawQuery(" SELECT * FROM Stranici GROUP BY name ", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{DatabaseHelper.COLUMN_NAME};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}
