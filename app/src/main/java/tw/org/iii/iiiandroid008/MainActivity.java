package tw.org.iii.iiiandroid008;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp; //外部存取
    private SharedPreferences.Editor editor; //內部存取類別
    // 看到openFile 就是玩內存
    private TextView content;
    private File sdroot, approot;

    private MyDBHelper myDBHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //檢察自我權限 是否沒有得到授權
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 9487);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9487) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                finish();
            }
        }
    }

    private void init() {
        //系統上要有的物件 直接get 不用new
        content = findViewById(R.id.content);
        sp = getSharedPreferences("brad", MODE_PRIVATE);//針對檔名brad的data進行讀取
        editor = sp.edit();//內部存取的物件 抓出來
        sdroot = Environment.getExternalStorageDirectory();
        Log.v("brad", sdroot.getAbsolutePath());
        approot = new File(sdroot, "Android/data/" + getPackageName());
        if (!approot.exists()) {
            approot.mkdirs();
        }
        myDBHelper = new MyDBHelper(this, "mydb", null, 1);
        db = myDBHelper.getReadableDatabase();
    }


    public void test1(View view) {
        editor.putString("username", "brad");
        editor.putBoolean("sound", false);
        editor.putInt("stage", 4);
        editor.commit();
        Toast.makeText(this, "save OK", Toast.LENGTH_SHORT).show();

    }

    public void test2(View view) {
        boolean isSound = sp.getBoolean("sound", true);
        String username = sp.getString("username", "nobody");
        int stage = sp.getInt("stage", 1);
        Log.v("brad", username + ":" + stage + ":" + isSound);
    }

    ///test3+4 沒有路徑表示固定存取  輸出比讀取容易
    public void test3(View view) { //內存一個資料
        try { //完整輸出一個檔案
            FileOutputStream fout = openFileOutput("brad.txt", MODE_PRIVATE);//覆蓋
            // MODE_APPEND--->跟隨後面繼續寫
            fout.write("Hello,World\n".getBytes()); //要string轉換成byte 網路上都是byte傳遞
            fout.flush();
            fout.close();
            Toast.makeText(this, "save OK", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("brad", e.toString());

        }
    }

    //先讀buf 將資料弄成 --->宣告成1024byte  在讀 在建立字串物件實體
    public void test4(View view) {
        try (FileInputStream fin = openFileInput("brad.txt")) {
            StringBuffer sb = new StringBuffer();
           /*int c; //如果讀的值 不等於-1 繼續讀
            while ((c = fin.read())!=-1){
            sb.append((char)c);改良成下面*/
            byte[] buf = new byte[1024]; //一次讀比較多byte 速度比較快
            int len;
            while ((len = fin.read(buf)) != -1) {
                // Log.v("brad","==>"+(char)c); //強制轉型 沒有的話就是用數字呈現
                sb.append(new String(buf, 0, len));
            }
            content.setText(sb.toString());
        } catch (Exception e) {
            Log.v("brad", e.toString());
        }//自動關閉機制
    }

    // 重點在串流的建立 用JAVA 原生地建立方式 宣告父FILE 位置
    public void test5(View view) {
        File file1 = new File(sdroot, "brad.ok");
        try {
            FileOutputStream fout =
                    new FileOutputStream(file1);
            fout.write("Hello brad".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "Save OK1",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.v("brad", e.toString());
        }
    }

//user 偏好設定 要儲存
//user 實體檔案儲存--網際網路觀念相同 但+上通訊協定
//內存空間 user 可以自行清除

    public void test6(View view) {
        File file2 = new File(approot, "brad.ok");
        try {
            FileOutputStream fout =
                    new FileOutputStream(file2);
            fout.write("Hello brad".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "Save OK2",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.v("brad", e.toString());

        }
    }

    // Query
    public void test7(View view) {
        // select * from cust where ... group by.... having... order by...
        Cursor c = db.query("user", null, null,
                null, null, null, null);
        while (c.moveToNext()) {
            String id = c.getString(0);
            String username = c.getString(1);
            String tel = c.getString(2);
            String birthday = c.getString(3);
            Log.v("brad", id + ":" + username + ":" + tel + ":" + birthday);
        }
    }

    public void test8(View view) {
        // String sql = "insert into user  (username, tel, birthday) values (''aa,''bb,'cc')";
        //  db.execute(sql);
        ContentValues values = new ContentValues();
        values.put("username", "brad");
        values.put("tel", "1234567");
        values.put("birthday", "2000-01-02");
        db.insert("user", null, values);

        test7(null);
    }

    public void test9(View view) {
        // delete from user where id = 2 and username='brad'
        db.delete("user", "id = ? and username = ? ", new String[]{"3", "brad"});
        test7(null);
    }

    public void test10(View view) {
        // update user set username='peter', tel='0912-123456' where id = 4;
        ContentValues values = new ContentValues();
        values.put("username", "peter");
        values.put("tel", "0912-123456");
        db.update("user", values, "id = ?", new String[]{"4"});
        test7(null);
    }
}

