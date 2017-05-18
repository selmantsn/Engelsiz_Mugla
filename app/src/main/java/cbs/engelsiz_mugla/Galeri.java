package cbs.engelsiz_mugla;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * Created by Asus on 18.03.2017.
 */
public class Galeri extends ListActivity {
    private DBHelper dbh;

    public void init(){
        dbh =new DBHelper(this);
    }

    private void BindData(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from photos" , null);
        BlobAdapter adb = new BlobAdapter(this,cursor);
        this.setListAdapter(adb);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        BindData();
    }
}
