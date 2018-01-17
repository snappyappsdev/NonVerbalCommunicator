package com.snappyappsdev.nonverbalcommunicator.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.snappyappsdev.nonverbalcommunicator.Pec;

import java.util.UUID;

import com.snappyappsdev.nonverbalcommunicator.database.PecDbSchema.PecTable.*;
/**
 * Created by lrocha on 12/25/17.
 */

public class PecCursorWrapper extends CursorWrapper {

    public PecCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Pec getPec() {
        String uuidString = getString(getColumnIndex(Cols.UUID));
        String name = getString(getColumnIndex(Cols.TITLE));

        Pec pec = new Pec(UUID.fromString(uuidString));
        pec.setTitle(name);

        return pec;
    }
}

