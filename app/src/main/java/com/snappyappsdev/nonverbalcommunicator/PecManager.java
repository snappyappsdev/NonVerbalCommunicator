package com.snappyappsdev.nonverbalcommunicator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.snappyappsdev.nonverbalcommunicator.database.PecBaseHelper;
import com.snappyappsdev.nonverbalcommunicator.database.PecDbSchema.PecTable;
import com.snappyappsdev.nonverbalcommunicator.database.PecCursorWrapper;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lrocha on 12/25/17.
 */

import static com.snappyappsdev.nonverbalcommunicator.database.PecDbSchema.PecTable.Cols.*;

public class PecManager {
    private static PecManager sPecManager;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PecManager get(Context context){
        if (sPecManager == null){
            sPecManager = new PecManager(context);
        }
        return sPecManager;
    }

    public PecManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PecBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addPec(Pec pec) {
        ContentValues values = getContentValues(pec);
        mDatabase.insert(PecTable.NAME,null,values);
    }

    public void deletePec(Pec pec){
        String uuidString = pec.getId().toString();
        mDatabase.delete(PecTable.NAME,
                UUID + " = ?",
                new String[]{uuidString});
    }

    public List<Pec> getPecs() {
        List<Pec> pecs = new ArrayList<>();
        PecCursorWrapper cursor = queryPec(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                pecs.add(cursor.getPec());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return pecs;
    }

    public Pec getPec(UUID id){
        PecCursorWrapper cursor = queryPec(
                PecTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPec();
        } finally {
            cursor.close();
        }
    }


    public File getPhotoFile(Pec pec){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, pec.getPhotoFileName());
    }

    public File getSoundFile(Pec pec){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, pec.getSoundFileName());
    }


    public void updatePec(Pec pec){
        String uuidString = pec.getId().toString();
        ContentValues values = getContentValues(pec);
        mDatabase.update(PecTable.NAME, values,
                UUID + " = ?",
                new String[]{uuidString});
    }

    private PecCursorWrapper queryPec(String whereClause,String[] whereArgs){
        Cursor cursor = mDatabase.query(
                PecTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PecCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Pec pec){
        ContentValues values = new ContentValues();
        values.put(UUID, pec.getId().toString());
        values.put(TITLE, pec.getTitle());
        return values;
    }
}
