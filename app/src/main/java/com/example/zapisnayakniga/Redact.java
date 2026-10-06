package com.example.zapisnayakniga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Redact extends AppCompatActivity {
    EditText e;
    Button delButton;
    Button saveButton;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redact);
        //Принимаем данные из Prosmotr(ListView)
        Bundle arguments = getIntent().getExtras();
        userId = arguments.getLong("id");

        e = (EditText) findViewById(R.id.Et);
        saveButton = (Button) findViewById(R.id.b1);
        delButton = (Button) findViewById(R.id.b2);

        sqlHelper = new DatabaseHelper( this);
        db = sqlHelper.getWritableDatabase();

        // получаем элемент по id из бд
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        userCursor.moveToFirst();
        e.setText(userCursor.getString(2));
    }

    public void save(View view) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ZAPISI, e.getText().toString());

        //При обновлении в метод update() передается название таблицы, объект ContentValues и критерий,
        // по которому происходит обновление
        // (в данном случае столбец id):
        db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        goHome();
    }

    public void del(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        Toast.makeText(this, "Файл успешно удален", Toast.LENGTH_SHORT).show();
        goHome();
    }

    public void shareText(View view) {
        String txt = e.getText().toString();
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(R.string.share_text_with)
                .setText(txt)
                .startChooser();
    }

    private void goHome() {
        // закрываем подключение
        userCursor.close();
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, Glav.class);
        intent.putExtra("klu4","");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
    }
}
