package es.cmartincha.cloudypics.task;

import android.os.AsyncTask;
import android.widget.Toast;

import es.cmartincha.cloudypics.activity.PicturesActivity;
import es.cmartincha.cloudypics.adapter.GridPicturesAdapter;
import es.cmartincha.cloudypics.lib.PictureCollection;
import es.cmartincha.cloudypics.lib.Server;

/**
 * Created by Carlos on 14/06/2015.
 */
public class PicturesCollectionTask extends AsyncTask<Void, Void, PictureCollection> {
    private PicturesActivity mPicturesActivity;
    private String mToken;

    public PicturesCollectionTask(PicturesActivity picturesActivity, String token) {
        mPicturesActivity = picturesActivity;
        mToken = token;
    }

    @Override
    protected PictureCollection doInBackground(Void... params) {
        PictureCollection response = null;
        mPicturesActivity.mLoadingPictures = true;

        try {
            response = Server.getPictures(mPicturesActivity.mPage++, mToken);
        } catch (Exception ignored) {
        }

        return response;
    }

    @Override
    protected void onPostExecute(PictureCollection response) {
        if (response.isOk()) {
            if (mPicturesActivity.mGridPicturesAdapter == null) {
                mPicturesActivity.mGridPicturesAdapter = new GridPicturesAdapter(mPicturesActivity, response);

                mPicturesActivity.gridPictures.setAdapter(mPicturesActivity.mGridPicturesAdapter);
            } else {
                mPicturesActivity.mGridPicturesAdapter.addAll(response.getPicturesArray());
            }
        } else {
            Toast.makeText(mPicturesActivity, "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                    .show();
        }

        mPicturesActivity.mLoadingPictures = false;
    }
}
