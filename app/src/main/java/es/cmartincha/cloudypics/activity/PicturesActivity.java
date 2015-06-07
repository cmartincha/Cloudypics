package es.cmartincha.cloudypics.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.lib.PictureCollectionResponse;
import es.cmartincha.cloudypics.lib.Server;


public class PicturesActivity extends AppCompatActivity {

    protected GridView gridPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        gridPictures = (GridView) findViewById(R.id.gridPictures);

        new PicturesCollectionTask(this).execute();
    }

    private class PicturesCollectionTask extends AsyncTask<Void, Void, PictureCollectionResponse> {
        Activity mActivity;

        public PicturesCollectionTask(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected PictureCollectionResponse doInBackground(Void... params) {
            PictureCollectionResponse response = null;

            try {
                response = Server.getPictures(1);
            } catch (Exception ignored) {
            }

            return response;
        }

        @Override
        protected void onPostExecute(PictureCollectionResponse response) {
            if (response.isOk()) {
                gridPictures.setAdapter(new GridPicturesAdapter(mActivity, response));
            } else {
                Toast.makeText(mActivity, "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
