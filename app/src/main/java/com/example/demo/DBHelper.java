package com.example.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo.Models.OrdersModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    final static String DBNAME="mynewdatabase.db";
    final static int DBVERSION=1;


    public DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Create_Table="CREATE TABLE neworders " +
                            "(id integer PRIMARY KEY autoincrement," +
                            "name text,"+
                            "phone text,"+
                            "price int,"+
                            "image int,"+
                            "quantity int,"+
                            "description text,"+
                            "foodname text)";
        sqLiteDatabase.execSQL(Create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS neworders");
        onCreate(sqLiteDatabase);
    }

    public Boolean insertOrder(String name, String phone, int price, int image, String desc, String foodname, int quantity){
        ContentValues value=new ContentValues();
        value.put("name",name);
        value.put("phone",phone);
        value.put("price",price);
        value.put("image",image);
        value.put("description",desc);
        value.put("foodname",foodname);
        value.put("quantity",quantity);

        SQLiteDatabase SqlDB=this.getReadableDatabase();
        long id=SqlDB.insert("neworders",null,value);
        SqlDB.close();

        if(id<=0){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<OrdersModel> getRecords(){
        SQLiteDatabase SqlDB=this.getWritableDatabase();
        ArrayList<OrdersModel> result=new ArrayList<OrdersModel>();
        String selectQuery="SELECT id,foodname,image,price FROM neworders " ;
        Cursor currentCursor= SqlDB.rawQuery(selectQuery,null);

//        if(currentCursor.moveToFirst()) {
            while (currentCursor.moveToNext()) {
                OrdersModel currentOrder=new OrdersModel();
                currentOrder.setOrderNumber(currentCursor.getInt(0)+"");
                currentOrder.setSoldItemName(currentCursor.getString(1));
                currentOrder.setOrderImage(currentCursor.getInt(2));
                currentOrder.setPrice(currentCursor.getInt(3)+"");

                result.add(currentOrder);
            }
//        }
        currentCursor.close();
        SqlDB.close();
        return result;
    }

    public Cursor getOrderById(int id){
        SQLiteDatabase SqlDB=this.getWritableDatabase();
        String selectQuery="SELECT * FROM neworders where id = "+id ;
        Cursor currentCursor= SqlDB.rawQuery(selectQuery,null);

        if(currentCursor!=null) {
            currentCursor.moveToFirst();
        }

        return currentCursor;
    }


    public Boolean updateOrder(String name, String phone, int price, int image, String desc, String foodname, int quantity,int id){
        ContentValues value=new ContentValues();
        value.put("name",name);
        value.put("phone",phone);
        value.put("price",price);
        value.put("image",image);
        value.put("description",desc);
        value.put("foodname",foodname);
        value.put("quantity",quantity);

        SQLiteDatabase SqlDB=this.getReadableDatabase();
        long row=SqlDB.update("neworders ",value,"id= "+id,null);
        SqlDB.close();

        if(row<=0){
            return false;
        }else{
            return true;
        }
    }

    public int deleteOrder(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete("neworders","id="+id,null);
    }
}
