package cbs.engelsiz_mugla;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private Button btnShowLocation;
    private TextView konumText;
    private Tracker gps;
    private SurfaceView surfaceV;
    private SurfaceHolder surfaceH;
    private android.hardware.Camera cam;
    private Button photoButton;
    private Button againButton;
    private DBHelper dbh;
    private EditText textarea;
    private Button gonderButton;


    private void init(){
        surfaceV =(SurfaceView)findViewById(R.id.sView);
        surfaceH = surfaceV.getHolder();
        surfaceH.addCallback(this);
        surfaceH.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceH.setFixedSize(300,300);


        dbh = new DBHelper(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();



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

                /**SaveImage**/

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


        konumText =(TextView)findViewById(R.id.text_konum);
        btnShowLocation = (Button) findViewById(R.id.buton_konum);


        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent iMaps = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(iMaps);



   /**
                gps = new Tracker(MainActivity.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    konumText.setText("Lat: " + latitude + "\nLong: "
                            + longitude);
    //                Toast.makeText(
    //                        getApplicationContext(),
    //                        "Your Location is -\nLat: " + latitude + "\nLong: "
    //                                + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }
    */
            }
        });
    }

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
}
