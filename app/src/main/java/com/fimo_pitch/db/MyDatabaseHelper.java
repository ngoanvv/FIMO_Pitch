package com.fimo_pitch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.fimo_pitch.CONSTANT;
import com.fimo_pitch.model.SystemPitch;

import java.util.ArrayList;

/**
 * Created by diep1 on 3/19/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Contacts table name
    private static final String TABLE_CONTACTS = "systempitchs";
    Context mContext;
    // Contacts Table Columns names
    private static String id="id";
    private static String name="name";
    private static String address="address";
    private static String ownerID="user_id";
    private static String description="description";
    private static String phone="phone";
    private static String lat="lat";
    private static String lng="lng";
    private static String comment="comment";
    private static String rating="rating";
    private static String ownerName="ownerName";

    public MyDatabaseHelper(Context context)  {
        super(context, CONSTANT.DBNAME, null,1);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB = "CREATE TABLE " + CONSTANT.SYSTEM_TABLE +" ( "
                + id +" Integer primary key, "
                + name + " String, "
                + address + " String, "
                + ownerID + " String, "
                + description + " String, "
                + phone + " String, "
                + lat + " String, "
                + lng + " String, "
                + comment + " String, "
                + rating + " String, "
                + ownerName + " String )";
        Log.d("query " ,CREATE_DB);
        db.execSQL(CREATE_DB);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONSTANT.SYSTEM_TABLE);

        // Create tables again
        onCreate(db);
    }
    public int countRecord()
    {
        String countQuery = "SELECT  * FROM " + CONSTANT.SYSTEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    public void addSystem(SystemPitch systemPitch )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(id,Integer.getInteger(systemPitch.getId()));
        contentValues.put(name,systemPitch.getName());
        contentValues.put(address,systemPitch.getAddress());
        contentValues.put(ownerID,systemPitch.getOwnerID());
        contentValues.put(description,systemPitch.getDescription());
        contentValues.put(phone,systemPitch.getPhone());
        contentValues.put(lat,systemPitch.getLat());
        contentValues.put(lng,systemPitch.getLng());
        contentValues.put(comment,systemPitch.getComment());
        contentValues.put(rating,systemPitch.getRating());
        contentValues.put(ownerName,systemPitch.getOwnerName());
        if(db.insert(CONSTANT.SYSTEM_TABLE,null,contentValues) != -1 )
            Toast.makeText(mContext, "New row added, row id: " , Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "Something wrong", Toast.LENGTH_SHORT).show();
        Log.d("count ",DatabaseUtils.queryNumEntries(db,CONSTANT.SYSTEM_TABLE)+" ");

        db.close();

    }
    public ArrayList<SystemPitch> getSystemByName(String inputAddress)
    {
        ArrayList<SystemPitch> list = new ArrayList<>();
        String sql = "Select * from "+CONSTANT.SYSTEM_TABLE +" where "+address+" like %"+inputAddress+"%;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do {
                SystemPitch item = new SystemPitch();
                item.setId(cursor.getString(0));
                item.setName(cursor.getString(1));
                item.setAddress(cursor.getString(2));
                item.setOwnerID(cursor.getString(3));
                item.setDescription(cursor.getString(4));
                item.setPhone(cursor.getString(5));
                item.setLat(cursor.getString(6));
                item.setLng(cursor.getString(7));
                item.setComment(cursor.getString(8));
                item.setRating(cursor.getString(9));
                item.setOwnerName(cursor.getString(10));

                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;


    }

}
