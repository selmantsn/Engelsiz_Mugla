package cbs.engelsiz_mugla;

import android.*;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karan.churi.PermissionManager.PermissionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity{ //implements SurfaceHolder.Callback{

    private PermissionManager permissionManager;
    private static final int REQUEST_PERMISSION = 10;
    private static final int ACTTIVITY_START_CAMERA_APP = 0;
    private Button btnShowLocation;
    private SurfaceView surfaceV;
    private SurfaceHolder surfaceH;
    private android.hardware.Camera cam = null;
    private Button photoButton;
    private Button againButton;
    private DBHelper dbh;
    private EditText textarea;
    private Button gonderButton;
    public TextView konumText;
    private Tracker gps;
    private ImageView mTakenPhotoView;
    private String mImageFileLocation = "";


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
/**
    private void init(){
        surfaceV =(SurfaceView)findViewById(R.id.sView);
        surfaceH = surfaceV.getHolder();
        surfaceH.addCallback(this);
        surfaceH.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceH.setFixedSize(300,300);


        dbh = new DBHelper(this);
    }

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init();

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        mTakenPhotoView = (ImageView)findViewById(R.id.takenPhotoView);
        photoButton=(Button)findViewById(R.id.btn_photo);




        gonderButton = (Button)findViewById(R.id.buton_gonder);
        gonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textarea = (EditText) findViewById(R.id.editText);
                String comment = textarea.getText().toString();

                SQLiteDatabase db = dbh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("comment",comment);
                // insert(sutun adı,sutuna yerleştirilecek veri, içerik degeri)
                long r = db.insert("comments", null, cv);

                // r>-1  primary key yani rowID
                if (r > -1)
                    Log.i("tag","mission complete!!!");
                else
                    Log.e("tag","mission Failed '!^!");

                db.close();
                Log.i("Yorum kaydedildi : ",comment );

            }
        });




/**
        photoButton = (Button)findViewById(R.id.btn_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deklansör sesi
                cam.enableShutterSound(true);
                android.hardware.Camera.Parameters param = cam.getParameters();
                //efekt uygula
                param.setColorEffect(Camera.Parameters.EFFECT_AQUA);
                android.hardware.Camera.ShutterCallback shutter = new android.hardware.Camera.ShutterCallback(){
                    @Override
                    public void onShutter(){
                        Log.i("cam","Deklansör kapandı");
                    }
                };
                //SaveImage
                //JPEG olarak yaz
                Camera.PictureCallback jpeg = new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {

                            //File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/cam" + String.valueOf(fCount++) + ".jpeg");

                            //String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+ NameOfFolder;
                            //File dir = new File(file_path);

                            String CurrentDateAndTime = getCurrentDateAndTime();
                            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).
                                    getAbsolutePath() + File.separator + CurrentDateAndTime+ ".jpeg");

                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(data,0,data.length);
                            fos.close();
                            //Veri tabanına kaydet
                            SQLiteDatabase db = dbh.getWritableDatabase();
                            ContentValues cv = new ContentValues();
                            cv.put("photo", data);
                            db.insert("photos", null,cv);
                            db.close();
                            Log.i("Fotoraf çekildi Adı : ", CurrentDateAndTime);
                        }catch (IOException e){
                            Log.e("foto",e.getMessage());
                        }
                    }
                    public String getCurrentDateAndTime() {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                        String formattedDate = df.format(c.getTime());
                        return formattedDate;
                    }
                };
                cam.takePicture(shutter,null,jpeg);
            }
        });
        againButton = (Button)findViewById(R.id.btn_again);
        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cam = android.hardware.Camera.open();
                    cam.setPreviewDisplay(surfaceH);
                    //canlı görüntüyü göster
                    cam.startPreview();
                    cam.enableShutterSound(true);
                }catch (IOException e){
                    Log.e("foto", e.getMessage());
                }

            }
        });

 */

        btnShowLocation = (Button) findViewById(R.id.buton_konum);


        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iMaps = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(iMaps);

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);

    }

    /**
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("Camera test", "surface is Created");
        //kameranın açılması ve canlı görüntünün üstüne yazılcagı surfaceholder ın belirlenmesi
        try {
            cam = android.hardware.Camera.open();
            // make any resize, rotate or reformatting changes here
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                cam.setDisplayOrientation(90);
            }
            cam.setPreviewDisplay(surfaceH);
            //canlı görüntüyü göster
            cam.startPreview();
            cam.enableShutterSound(true);
        }catch (IOException e){
            Log.e("foto", e.getMessage());
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(cam!=null){
            cam.stopPreview();
            cam.setPreviewCallback(null);
            cam.release();
        }
    }
*/

    public void takePhoto(View view){
        Intent callCameraApp = new Intent();
        callCameraApp.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile=createImageFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
        callCameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApp,ACTTIVITY_START_CAMERA_APP);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==ACTTIVITY_START_CAMERA_APP && resultCode==RESULT_OK){
            Toast.makeText(this,"Picture taken",Toast.LENGTH_SHORT).show();
            //Bundle extras = data.getExtras();
            //Bitmap takenPhotoBitmap = (Bitmap) extras.get("data");
            //Bitmap takenPhotoBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            //mTakenPhotoView.setImageBitmap(takenPhotoBitmap);
            setReducedImageSize();
        }
    }


    File createImageFile() throws IOException {
        String CurrentDateAndTime = getCurrentDateAndTime();
        String imageFileName ="Image_" + CurrentDateAndTime ;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        mImageFileLocation = image.getAbsolutePath();
        return image;

    }


    public String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    /**
    private void savaPhotoSdCard(Bitmap bitmap){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String pname = sdf.format(new Date());
        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root + "/Engelsiz_Mugla");
        folder.mkdirs();
        File myFile = new File(folder,pname+".jpg")
        try {
            FileOutputStream stream = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
    void setReducedImageSize(){
        int targetViewWidth = mTakenPhotoView.getWidth();
        int targetViewHeight = mTakenPhotoView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation,bmOptions);
        int cameraImageWidth =bmOptions.outWidth;
        int cameraImageHeight =bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetViewWidth, cameraImageHeight/targetViewHeight);
        bmOptions.inSampleSize =scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mImageFileLocation,bmOptions);
        mTakenPhotoView.setImageBitmap(photoReducedSizeBitmap);

    }
}
