package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_Name="Todo.db";
    private static final String TABLE_NAME ="task_details";
    private static final int Version =2;
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_DESCRIPTION="Description";
    private static final String COLUMN_TIME_STAMP="Time";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TIME_STAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP," + COLUMN_DESCRIPTION + " TEXT" + ")";
    private Context context;
    SQLiteDatabase sqLiteDatabase;

    public DBHelper( Context context) {
        super(context, DB_Name, null, Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            db.execSQL(CREATE_TABLE); }
        catch (Exception e)
        {
            Toast.makeText(context,"Something wrong!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try{
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }

        catch (Exception e)
        {

        }

    }

    public long insert_todo(Task task)
    {
        sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_TIME_STAMP,task.getTimestamp());
        contentValues.put(COLUMN_DESCRIPTION,task.getDescription());
        long id= sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
        return id;

    }

    public void delete_todo(Task task)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID  + " = ?",new String[] {String.valueOf(task.getId())});

    }

    public int update_todo(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }



    public ArrayList<Task> ListAllTasks()
    {
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<Task> arr_list=new ArrayList<>();
        Cursor cursor= sqLiteDatabase.rawQuery(query,null);
        if (cursor.moveToFirst())
        {
            do
                {
                    int id=Integer.parseInt(cursor.getString(0));
                    String time=cursor.getString(1);
                    String des=cursor.getString(2);
                    arr_list.add(new Task(id,time,des));
                }
            while (cursor.moveToNext());
        }
        cursor.close();
        return arr_list;
    }
}
