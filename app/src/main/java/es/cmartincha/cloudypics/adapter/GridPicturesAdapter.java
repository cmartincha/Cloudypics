package es.cmartincha.cloudypics.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.lib.Picture;
import es.cmartincha.cloudypics.lib.PictureCollection;
import es.cmartincha.cloudypics.task.ThumbnailTask;

/**
 * Created by Carlos on 07/06/2015.
 */
public class GridPicturesAdapter extends ArrayAdapter<Picture> {
    public Context mContext;

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
        new ThumbnailTask(this, position, holder)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, picture.getThumbnail());
        holder.labelPictureUser.setText(picture.getUsername());

        return convertView;
    }

    public static class ViewHolder {
        public ImageView imgPicture;
        public TextView labelPictureUser;
        public int position;
    }

}
