package es.cmartincha.cloudypics.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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

    private class PictureTask extends AsyncTask<Picture, Void, Bitmap> {
        private Activity mActivity;

        public PictureTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected Bitmap doInBackground(Picture... params) {
            Bitmap bitmap = null;

            try {
                bitmap = Server.getPictureBitmap(params[0].getUrl());
            } catch (Exception ignored) {
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

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
                response = Server.getPictures(mPage);
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
