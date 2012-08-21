
package com.androidservicewrapper.test.mockobjects;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.test.mock.MockContext;

public class DatabaseContext extends MockContext {
    
    // isolated test db file generation and clean-up related
    private static final String FILE_PREFIX = "test_";
    private static final String JOURNAL_SUFFIX = "-journal";
    
    private Context mAndroidContext;
    // created db list
    private final List<String> mDbList = new LinkedList<String>();

    public DatabaseContext(Context context) {
        mAndroidContext = context;
    }

    public void cleanup() {
        // delete all database files created by this test context
        for (String db : mDbList) {
            mAndroidContext.deleteDatabase(db);
            mAndroidContext.deleteDatabase(db + JOURNAL_SUFFIX);
        }
    }
    
    @Override
    public SQLiteDatabase openOrCreateDatabase(String file, int mode, CursorFactory factory,
                    DatabaseErrorHandler errorHandler) {
        // isolate test database
        file = FILE_PREFIX + file;
        mDbList.add(file);
        return mAndroidContext.openOrCreateDatabase(file, mode, factory, errorHandler);
    }

}
