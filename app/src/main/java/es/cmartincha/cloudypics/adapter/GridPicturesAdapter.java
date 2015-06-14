package es.cmartincha.cloudypics.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.URL;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.lib.Picture;
import es.cmartincha.cloudypics.lib.PictureCollection;
import es.cmartincha.cloudypics.lib.Server;

/**
 * Created by Carlos on 07/06/2015.
 */
public class GridPicturesAdapter extends ArrayAdapter<Picture> {
    Context mContext;

    public GridPicturesAdapter(Context context, PictureCollection pictureCollection) {
        super(context, 0, pictureCollection.getPicturesArray());

        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Picture picture = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid_pictures, parent, false);
            holder = new ViewHolder();

            holder.imgPicture = (ImageView) convertView.findViewById(R.id.imgPicture);
            holder.labelPictureUser = (TextView) convertView.findViewById(R.id.labelPictureUser);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        new ThumbnailTask(position, holder)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, picture.getThumbnail());
        holder.labelPictureUser.setText(picture.getUsername());

        return convertView;
    }

    private static class ViewHolder {
        public ImageView imgPicture;
        public TextView labelPictureUser;
        public int position;
    }

    private class ThumbnailTask extends AsyncTask<URL, Void, File> {
        private int mPosition;
        private ViewHolder mViewHolder;

        public ThumbnailTask(int position, ViewHolder viewHolder) {
            mPosition = position;
            mViewHolder = viewHolder;
        }

        @Override
        protected File doInBackground(URL... params) {
            File picture = new File(mContext.getFilesDir(), Uri.parse(params[0].toString()).getLastPathSegment());

            try {
                if (!picture.exists()) {
                    picture = Server.getPicture(params[0], mContext.getFilesDir());

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
}
