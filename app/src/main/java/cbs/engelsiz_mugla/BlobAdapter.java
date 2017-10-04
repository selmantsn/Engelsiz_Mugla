package cbs.engelsiz_mugla;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Asus on 18.03.2017.
 */
public class BlobAdapter extends BaseAdapter {
    private Context context;    private Cursor cursor;


    public BlobAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        FotoEntity entity = null;

        if (cursor.moveToPosition(position)){
            entity = new FotoEntity(cursor.getInt(0) , cursor.getBlob(1));
        }
        return entity;
    }

    @Override
    public long getItemId(int position) {
        return ((FotoEntity) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FotoEntity entity = (FotoEntity) getItem(position);
        byte arr[] = entity.getData();
        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0 , arr.length);
        ImageView img = new ImageView(context);
        img.setImageBitmap(bmp);
        return img;
    }

}
