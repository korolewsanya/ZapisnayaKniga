package com.example.zapisnayakniga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;

public class Prosmotr extends AppCompatActivity {
    ListView l;
    Cursor userCursor;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    SimpleCursorAdapter userAdapter;
    String company;
    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prosmotr);

         l = (ListView) findViewById(R.id.list);

        //Принимаем данные из PapkiPros(ListView)
        Bundle arguments = getIntent().getExtras();
        company = arguments.getString("i");

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // получаем выбранный элемент из списка(ListView)
                TextView textView = (TextView) view;
                selectedItem = (String) textView.getText();

                Intent intent = new Intent(getApplicationContext(), Redact.class);
intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        databaseHelper = new com.example.zapisnayakniga.DatabaseHelper( getApplicationContext());
    }

        @Override
        public void onResume() {
            super.onResume();

            // открываем подключение
            db = databaseHelper.getReadableDatabase();
            String[]s={company};
            //получаем данные из бд в виде курсора
            userCursor = db.query(DatabaseHelper.TABLE,null,DatabaseHelper.COLUMN_NAME +"=?" ,s,null,null,null);
            // определяем, какие столбцы из курсора будут выводиться в ListView

            String[] headers = new String[]{ DatabaseHelper.COLUMN_ZAPISI,DatabaseHelper.COLUMN_NAME};
            // создаем адаптер, передаем в него курсор
            userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                    userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
            l.setAdapter(userAdapter);
        }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}