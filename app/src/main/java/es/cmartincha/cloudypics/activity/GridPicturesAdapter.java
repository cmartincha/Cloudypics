package es.cmartincha.cloudypics.activity;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.lib.PictureCollectionResponse;
import es.cmartincha.cloudypics.lib.PictureResponse;

/**
 * Created by Carlos on 07/06/2015.
 */
public class GridPicturesAdapter extends ArrayAdapter<PictureResponse> {
    public GridPicturesAdapter(Context context, PictureCollectionResponse pictureCollection) {
        super(context, 0, pictureCollection.getPicturesArray());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PictureResponse pictureResponse = getItem(position);
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grid_pictures, parent, false);
            holder = new ViewHolder();

            holder.imgPicture = (ImageView) convertView.findViewById(R.id.imgPicture);
            holder.labelPictureUser = (TextView) convertView.findViewById(R.id.labelPictureUser);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imgPicture.setImageURI(Uri.parse(pictureResponse.getThumbnail().toString()));
        holder.labelPictureUser.setText(pictureResponse.getUsername());

        return convertView;
    }

    public static class ViewHolder {
        public ImageView imgPicture;
        public TextView labelPictureUser;
    }
}
