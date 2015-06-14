package es.cmartincha.cloudypics.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.net.URL;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.adapter.GridPicturesAdapter;
import es.cmartincha.cloudypics.lib.Picture;
import es.cmartincha.cloudypics.lib.PictureCollection;
import es.cmartincha.cloudypics.lib.Server;


public class PicturesActivity extends AppCompatActivity implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    protected GridView gridPictures;
    protected int mPage = 0;
    protected GridPicturesAdapter mGridPicturesAdapter = null;
    protected boolean mLoadingPictures = false;
    protected int mPreviousFirstVisibleItem = 0;

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

    private class PictureTask extends AsyncTask<Picture, Void, File> {
        private Activity mActivity;

        public PictureTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected File doInBackground(Picture... params) {
            File picture = null;

            try {
                URL pictureUrl = params[0].getUrl();
                String pictureName = Uri.parse(pictureUrl.toString()).getLastPathSegment();
                picture = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), pictureName);

                if (!picture.exists()) {
                    picture = Server.getPicture(params[0].getUrl(), Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES));

                    Log.d("Foto guardada", picture.getAbsolutePath());
                }
            } catch (Exception ignored) {
            }

            return picture;
        }

        @Override
        protected void onPostExecute(File picture) {
            if (picture == null) {
                Toast.makeText(mActivity, "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(picture), "image/*");
                startActivity(intent);
            }
        }
    }

    private class PicturesCollectionTask extends AsyncTask<Void, Void, PictureCollection> {
        Activity mActivity;

        public PicturesCollectionTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected PictureCollection doInBackground(Void... params) {
            PictureCollection response = null;
            mLoadingPictures = true;

            try {
                response = Server.getPictures(mPage++);
            } catch (Exception ignored) {
            }

            return response;
        }

        @Override
        protected void onPostExecute(PictureCollection response) {
            if (response.isOk()) {
                if (mGridPicturesAdapter == null) {
                    mGridPicturesAdapter = new GridPicturesAdapter(mActivity, response);

                    gridPictures.setAdapter(mGridPicturesAdapter);
                } else {
                    mGridPicturesAdapter.addAll(response.getPicturesArray());
                }
            } else {
                Toast.makeText(mActivity, "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                        .show();
            }

            mLoadingPictures = false;
        }
    }
}
