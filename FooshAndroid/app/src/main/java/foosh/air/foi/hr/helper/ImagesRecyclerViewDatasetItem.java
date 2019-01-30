package foosh.air.foi.hr.helper;

import android.net.Uri;

/**
 * Klasa koja služi za pohranjivanje elemenata ImagesRecyclerViewDataset-a
 * kod edita oglasa
 */
public class ImagesRecyclerViewDatasetItem {

    private Uri imageUri;
    private boolean inDatabase;

    public ImagesRecyclerViewDatasetItem(Uri uri) {
        this.imageUri = uri;
        this.inDatabase = false;
    }
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isInDatabase() {
        return inDatabase;
    }

    public void setInDatabase(boolean inDatabase) {
        this.inDatabase = inDatabase;
    }
}
