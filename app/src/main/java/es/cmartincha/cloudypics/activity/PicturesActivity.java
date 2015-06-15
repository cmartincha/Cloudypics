package es.cmartincha.cloudypics.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.adapter.GridPicturesAdapter;
import es.cmartincha.cloudypics.db.PictureDb;
import es.cmartincha.cloudypics.db.PictureDbHelper;
import es.cmartincha.cloudypics.lib.UploadPictureScheduler;
import es.cmartincha.cloudypics.lib.UserCredentials;
import es.cmartincha.cloudypics.service.UploadPictureService;
import es.cmartincha.cloudypics.task.PictureTask;
import es.cmartincha.cloudypics.task.PicturesCollectionTask;


public class PicturesActivity extends AppCompatActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public GridView gridPictures;
    public int mPage = 0;
    public GridPicturesAdapter mGridPicturesAdapter = null;
    public boolean mLoadingPictures = false;
    public int mPreviousFirstVisibleItem = 0;
    public String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        gridPictures = (GridView) findViewById(R.id.gridPictures);
        gridPictures.setOnScrollListener(this);
        gridPictures.setOnItemClickListener(this);

        new PicturesCollectionTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_pictures, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    try {
                        File photoFile = createImageFile();

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (IOException ex) {
                        Toast.makeText(this, "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "cloudypics_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            galleryAddPic();

            //TODO añadir a la base de datos
            PictureDbHelper pictureDbHelper = new PictureDbHelper(this);
            SQLiteDatabase db = pictureDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(PictureDb.PictureEntry.COLUMN_NAME_PATH, mCurrentPhotoPath);

            long rowId = db.insert(PictureDb.PictureEntry.TABLE_NAME, null, values);

            Log.d("Nueva fila", String.valueOf(rowId));

            //TODO lanzar servicio para subir
            UploadPictureScheduler.scheduleNewStart(this, UploadPictureService.class, new UserCredentials(this).getToken());
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount <= 0) {
            return;
        }

        if (mLoadingPictures) {
            return;
        }

        if (mPreviousFirstVisibleItem >= firstVisibleItem) {
            return;
        }

        if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            mPage++;
            mPreviousFirstVisibleItem = firstVisibleItem;

            new PicturesCollectionTask(this).execute();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new PictureTask(this)
                .execute(mGridPicturesAdapter.getItem(position));
    }

}
