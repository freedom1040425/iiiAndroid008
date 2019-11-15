package tw.org.iii.iiiandroid008;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp; //外部存取
    private SharedPreferences.Editor editor; //內部存取類別
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //系統上要有的物件 直接get 不用new
        sp = getSharedPreferences("brad",MODE_PRIVATE);//針對檔名brad的data進行讀取
        editor = sp.edit();//內部存取的物件 抓出來
    }

    public void test1(View view) {
        editor.putString("username","brad");
        editor.putBoolean("sound",false);
        editor.putInt("stage",4);
        editor.commit();
        Toast.makeText(this,"save OK",Toast.LENGTH_SHORT).show();

    }

    public void test2(View view) {
        boolean isSound = sp.getBoolean("sound",true);
        String username = sp.getString("username","nobody");
        int stage = sp.getInt("stage",1);
        Log.v("brad",username+":"+stage+":"+isSound);
    }
}
//user 偏好設定 要儲存
//user 實體檔案儲存--網際網路觀念相同 但+上通訊協定
//內存空間 user 可以自行清除
