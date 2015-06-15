package es.cmartincha.cloudypics.db;

import android.provider.BaseColumns;

/**
 * Created by Carlos on 14/06/2015.
 */
public final class PictureDb {
    public PictureDb() {

    }

    public static abstract class PictureEntry implements BaseColumns {
        public static final String TABLE_NAME = "Picture";
        public static final String COLUMN_NAME_PATH = "path";
    }
}
