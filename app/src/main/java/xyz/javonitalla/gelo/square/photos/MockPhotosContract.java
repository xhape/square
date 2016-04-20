package xyz.javonitalla.gelo.square.photos;

import java.util.List;
import xyz.javonitalla.gelo.square.data.MockPhoto;

/**
 * Created by ajavonitalla on 4/11/2016.
 */
public interface MockPhotosContract {

  interface View {

    int REQUEST_IMAGE_CAPTURE = 926;

    /**
     * Shows all photo
     * @param photos
     */
    void showPhotos(List<MockPhoto> photos);

    /**
     * Show newly added photo
     * @param photo
     */
    void showPhoto(MockPhoto photo);

    /**
     * Called to show error when there is no app to handle intent that takes photo
     */
    void showNoCameraError();

    void showUnableToCreateFileError();

    void setPresenter(Presenter presenter);
  }

  /**
   * Listens to user actions from the UI ({@link View}), retrieves the data and updates the
   * UI as required.
   */
  interface Presenter {

    String CURRENT_PHOTO_PATH = "xyz.javonitalla.gelo.square.photos.CURRENT_PHOTO_PATH";

    /**
     * Called after taking a picture
     * @param requestCode
     */
    void result(int requestCode);

    /**
     * Called when there is no application available to take pictures
     */
    void handleNoAppToHandleCameraIntent();

    void handleUnableToCreateFile();

    void setCurrentPhotoPath(String photoPath);

    String getCurrentPhotoPath();
  }
}
