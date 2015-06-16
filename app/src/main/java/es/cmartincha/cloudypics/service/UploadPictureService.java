package es.cmartincha.cloudypics.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;

import es.cmartincha.cloudypics.db.PictureDb;
import es.cmartincha.cloudypics.db.PictureDbHelper;
import es.cmartincha.cloudypics.lib.Server;
import es.cmartincha.cloudypics.lib.UploadPictureScheduler;

/**
 * Created by Carlos on 14/06/2015.
 */
public class UploadPictureService extends IntentService {
    String mToken;

    public UploadPictureService() {
        super("UploadPictureService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Subo foto", "");
        mToken = intent.getStringExtra("token");

        if (!connectionReady()) {
            UploadPictureScheduler.scheduleNewStart(this, getClass(), mToken);
        }

        PictureDbHelper pictureDbHelper = new PictureDbHelper(this);
        SQLiteDatabase db = pictureDbHelper.getWritableDatabase();
        String[] projection = {
                PictureDb.PictureEntry._ID,
                PictureDb.PictureEntry.COLUMN_NAME_PATH,
        };

        Cursor cursor = db.query(
                PictureDb.PictureEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String path = cursor.getString(cursor.getColumnIndex(PictureDb.PictureEntry.COLUMN_NAME_PATH));
                File picture = new File(path);

                try {
                    Server.uploadPicture(picture, mToken);

                    db.delete(PictureDb.PictureEntry.TABLE_NAME, PictureDb.PictureEntry._ID + "=" + cursor.getInt(cursor.getColumnIndex(PictureDb.PictureEntry._ID)), null);
                } catch (Exception e) {
                    UploadPictureScheduler.scheduleNewStart(this, getClass(), mToken);
                }

                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();
    }

    private boolean connectionReady() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
