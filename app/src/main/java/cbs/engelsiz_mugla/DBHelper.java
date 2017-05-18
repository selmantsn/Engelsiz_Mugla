package cbs.engelsiz_mugla;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Asus on 17.03.2017.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context , String name , SQLiteDatabase.CursorFactory factory , int version){
        super(context,name,factory,version);

    }
    public DBHelper(Context context){
        this(context,"Galeri.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table photos(_id INTEGER PRIMARY KEY, photo BLOB, comment TEXT);");

        db.execSQL("create table comments(_id INTEGER PRIMARY KEY, comment TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
