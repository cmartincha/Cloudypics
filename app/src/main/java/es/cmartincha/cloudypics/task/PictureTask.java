package es.cmartincha.cloudypics.task;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.URL;

import es.cmartincha.cloudypics.activity.PicturesActivity;
import es.cmartincha.cloudypics.lib.Picture;
import es.cmartincha.cloudypics.lib.Server;

/**
 * Created by Carlos on 14/06/2015.
 */
public class PictureTask extends AsyncTask<Picture, Void, File> {
    private PicturesActivity mPicturesActivity;

    public PictureTask(PicturesActivity picturesActivity) {
        mPicturesActivity = picturesActivity;
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
            Toast.makeText(mPicturesActivity, "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(picture), "image/*");
            mPicturesActivity.startActivity(intent);
        }
    }
}
