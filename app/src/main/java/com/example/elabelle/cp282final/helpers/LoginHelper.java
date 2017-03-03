package com.example.elabelle.cp282final.helpers;

/**
 * Created by elabelle on 3/3/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CP282LoginDatabase";
    private static final int DATABASE_VERSION = 1;

    public LoginHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE users (username TEXT PRIMARY KEY, password TEXT," +
                "email TEXT, phone TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean saveUser (String username, String password, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("phone", phone);

        contentValues.put("username", username);
        result = db.insert("users", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public String getUser (String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username, password FROM users";

        Cursor cursor = db.rawQuery(query,null);

        String tbl_username, tbl_password;
        tbl_password = "not found";

        if (cursor.moveToFirst()) {
            do {
                tbl_username = cursor.getString(0);

                if (tbl_username.equals(username)){
                    tbl_password = cursor.getString(1);
                    break;
                }
            } while (cursor.moveToNext());
        }

        return tbl_password;
    }

}