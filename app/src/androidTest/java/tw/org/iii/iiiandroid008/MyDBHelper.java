package tw.org.iii.iiiandroid008;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String creatTable =
            "CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT+" +
                    ",username TEXT,birthday DATE)";
    public MyDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }//factory 指標移動模式 名字庫名

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creatTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
