package com.example.zapisnayakniga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {
    private Removable removable;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        removable = (Removable) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Эта переменная для получения id из Udalit(ListView) в диалоговое окно для базы данных
        final long phone = getArguments().getLong("phone");
        //Эта переменная для получения текста из Udalit(ListView) в диалоговое окно
        String phon = getArguments().getString("phon");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Вы уверены?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("БУДЕТ УДАЛЕНО: \n   " + phon + " ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sqlHelper=new DatabaseHelper(getContext());
                        db = sqlHelper.getWritableDatabase();
                        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(phone)});
                        db.close();
                        // переход к главной activity
                        Intent intent = new Intent(getContext(), Glav.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Отмена",null)
                .create();
    }
}
