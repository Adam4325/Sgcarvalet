package sgcarvalet.adam.com.MySQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import sgcarvalet.adam.com.sgcarvalet.adam.com.model.User;

/**
 * Created by adam on 1/1/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSI = 1;
    private static final String DB_NAME = "sgcavarlet.db";
    public static final String TABLE_USER = "tbl_user";
    public static final String TABLE_TEMP = "tbl_temp";
    private static final String KEY_TEMP_ID = "id_temp";
    private static final String KEY_TEMP = "temp";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TELP = "telpon";
    private static final String KEY_STATUS = "status_logout";
    private static final String KEY_STATUS_LOGOUT = "1";
    Context context;

    private static final String[] COLUMNS = {KEY_USER_ID,KEY_NAMA,KEY_EMAIL,KEY_PASSWORD,KEY_STATUS};

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSI);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = " CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAMA + " TEXT NOT NULL,"
                + KEY_EMAIL + " TEXT NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_TELP + " TEXT NOT NULL,"
                + KEY_STATUS + " CHAR(5)" + ")";

        String CREATE_TABLE_TEMP = " CREATE TABLE IF NOT EXISTS " + TABLE_TEMP + "("
                + KEY_TEMP_ID + " CHAR(2),"
                + KEY_TEMP + "  CHAR(5)" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_TABLE_TEMP);
        db.execSQL("INSERT INTO "+TABLE_TEMP+" VALUES('1', 'no')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_user");
        db.execSQL("DROP TABLE IF EXISTS tbl_temp");
        this.onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_NAMA,user.getNama());
        cv.put(KEY_EMAIL,user.getEmail());
        cv.put(KEY_PASSWORD,user.getPassword());
        cv.put(KEY_TELP,user.getTelp());
        cv.put(KEY_STATUS,user.getStatus_logout());
        long i = db.insert(TABLE_USER, null, cv);
        if(i>0){
            Log.d("Ok","Data sudah masuk");
        }else{
            Log.d("Ok","Data gagal masuk");
        }
        db.close();
    }

    public User getUser(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_USER, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        User user = new User();
        if (cursor!=null){
            cursor.moveToFirst();

            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setNama(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setTelp(cursor.getString(4));
            user.setStatus_logout(cursor.getString(5));

        }
        return user;
    }

    public User getDataUser(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_USER, // a. table
                        COLUMNS, // b. column names
                        " email = ? AND password=?", // c. selections
                        new String[] { email,password}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        User user = new User();
        if (cursor!=null){
            cursor.moveToFirst();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setNama(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setTelp(cursor.getString(4));
            user.setStatus_logout(cursor.getString(5));

        }
        return user;
    }

    public List<User> getAllUser(){
        List<User> users = new LinkedList<User>();
        String Query = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(Query,null);
        User user = null;
        if (cursor.moveToFirst()){
            do{
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setNama(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                user.setTelp(cursor.getString(4));
                user.setStatus_logout(cursor.getString(5));
                users.add(user);
            }while(cursor.moveToNext());

        }
        return users;
    }

    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAMA,user.getNama());
        cv.put(KEY_EMAIL,user.getEmail());
        cv.put(KEY_PASSWORD,user.getPassword());
        cv.put(KEY_TELP,user.getTelp());
        cv.put(KEY_STATUS,user.getStatus_logout());
        int i = db.update(TABLE_USER,cv, KEY_USER_ID +"=?",new String[]{String.valueOf(user.getId())});
        db.close();
        return i;
    }

    public int updateTemp(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TEMP,"no");
        int i = db.update(TABLE_TEMP,cv, KEY_TEMP_ID +"=?",new String[]{"1"});
        db.close();
        return i;
    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_USER_ID +"=?",new String[]{String.valueOf(user.getId())});
        db.close();

    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */


     public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();


        int del2 = updateTemp();

        if (del2>0){
            Log.e("RESET :","Sukses RESET");
        }else{
            Log.e("RESET :","Gagal di RESET");
        }
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+ TABLE_USER +"'");
        db.execSQL("DELETE FROM "+TABLE_USER);
        db.close();
    }
}
