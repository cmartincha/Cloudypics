package es.cmartincha.cloudypics.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.net.URL;

import es.cmartincha.cloudypics.adapter.GridPicturesAdapter;
import es.cmartincha.cloudypics.lib.Server;

/**
 * Created by Carlos on 14/06/2015.
 */
public class ThumbnailTask extends AsyncTask<URL, Void, File> {
    private GridPicturesAdapter mGridPicturesAdapter;
    private int mPosition;
    private GridPicturesAdapter.ViewHolder mViewHolder;

    public ThumbnailTask(GridPicturesAdapter gridPicturesAdapter, int position, GridPicturesAdapter.ViewHolder viewHolder) {
        mGridPicturesAdapter = gridPicturesAdapter;
        mPosition = position;
        mViewHolder = viewHolder;
    }

    @Override
    protected File doInBackground(URL... params) {
        File picture = new File(mGridPicturesAdapter.mContext.getFilesDir(), Uri.parse(params[0].toString()).getLastPathSegment());

        try {
            if (!picture.exists()) {
                picture = Server.getPicture(params[0], mGridPicturesAdapter.mContext.getFilesDir());

                Log.d("Foto mini guardada", picture.getAbsolutePath());
            }
        } catch (Exception ignored) {
        }

        return picture;
    }

    @Override
    protected void onPostExecute(File bitmap) {
        if (mViewHolder.position == mPosition) {
            mViewHolder.imgPicture.setImageURI(Uri.parse(bitmap.getAbsolutePath()));
        }
    }
}
