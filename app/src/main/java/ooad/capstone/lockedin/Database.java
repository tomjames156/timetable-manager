package ooad.capstone.lockedin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Database";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i1, int i2) {
    }


    public void checkTable(String date) {
        String create = "CREATE TABLE IF NOT EXISTS `"+date+
                "` (`ID` integer, `TASK` text, `From` text, `To` text, `Color` text );";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(create);
    }

    public void addTask(Task t, String date) {
        checkTable(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO `"+date+"` (`ID`, `Task`, `From`, `To`, `Color`) VALUES " +
                "( '"+t.getID()+"', '"+t.getTask()+"', '"+t.getFromToString()+"', '"+t.getToToString()+
                "', '"+t.getColor()+"' );";
        db.execSQL(insert);
    }

    public ArrayList<Task> getAllTasks(String date){
        checkTable(date);
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Task> tasks = new ArrayList<>();
        String select = "SELECT * FROM `" + date + "`;";
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            do{
                Task t = new Task();
                t.setID(cursor.getInt(0));
                t.setTask(cursor.getString(1));
                t.setFrom(cursor.getString(2));
                t.setTo(cursor.getString(3));
                t.setColor(cursor.getString(4));
                tasks.add(t);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public int getNextID(String date){
        ArrayList<Task> tasks = this.getAllTasks(date);
        int id = 0;
        int size = this.getAllTasks(date).size();
        if (size != 0){
            int lastIndex = tasks.size() - 1;
            id = tasks.get(lastIndex).getID() + 1;
        }
        return id;
    }

    public void updateTask(Task t, String date) {
        String update = "UPDATE `" + date + "` SET `TASK` = '" + t.getTask() + "', `From` = '"+
                t.getFromToString() + "', `To` = '" + t.getToToString() + "', `Color` = '" + t.getColor()
                + "' WHERE `ID` = " + t.getID() + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }

    public void deleteTask(int id, String date) {
        String delete = " DELETE FROM `" + date + "` WHERE `ID` = '" + id + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(delete);
    }
}
