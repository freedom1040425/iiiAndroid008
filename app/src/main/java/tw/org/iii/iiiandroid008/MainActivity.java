package tw.org.iii.iiiandroid008;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp; //外部存取
    private SharedPreferences.Editor editor; //內部存取類別
    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //系統上要有的物件 直接get 不用new
        sp = getSharedPreferences("brad", MODE_PRIVATE);//針對檔名brad的data進行讀取
        editor = sp.edit();//內部存取的物件 抓出來
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

    public void test3(View view) { //內存一個資料
        try { //完整輸出一個檔案
            FileOutputStream fout = openFileOutput("brad.txt", MODE_PRIVATE);//覆蓋
                                                                    // MODE_APPEND--->跟隨後面繼續寫
            fout.write("Hello,World\n".getBytes()); //要string轉換成byte 網路上都是byte傳遞
            fout.flush();
            fout.close();
            Toast.makeText(this, "save OK", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.v("brad",e.toString());

        }
    }


    public void test4(View view) {
        try (FileInputStream fin = openFileInput("brad.txt")){
            StringBuffer sb= new StringBuffer();
            int c; //如果讀的值 不等於-1 繼續讀
            while ((c = fin.read())!=-1){
               // Log.v("brad","==>"+(char)c); //強制轉型 沒有的話就是用數字呈現
            sb.append((char)c);
            }
        content.setText(sb.toString());
        }catch (Exception e){
         Log.v("brad",e.toString());
        }//自動關閉機制
    }


//


}

//user 偏好設定 要儲存
//user 實體檔案儲存--網際網路觀念相同 但+上通訊協定
//內存空間 user 可以自行清除
