package xyz.javonitalla.gelo.square.photos;

import java.util.ArrayList;
import java.util.List;
import xyz.javonitalla.gelo.square.data.MockPhoto;

/**
 * Created by ajavonitalla on 4/11/2016.
 */
public class MockPhotosPresenter implements MockPhotosContract.Presenter {

  private MockPhotosContract.View view;
  private List<MockPhoto> repo = new ArrayList<>();

  private String currentPhotoPath;

  public MockPhotosPresenter(MockPhotosContract.View view) {
    this.view = view;
    view.setPresenter(this);
  }

  @Override
  public void result(int requestCode) {
    if (requestCode == MockPhotosContract.View.REQUEST_IMAGE_CAPTURE) {
      MockPhoto mockPhoto = new MockPhoto(currentPhotoPath);
      repo.add(mockPhoto);
      view.showPhoto(mockPhoto);
    }
  }

  @Override
  public void handleNoAppToHandleCameraIntent() {
    view.showNoCamera();
  }

  public void setCurrentPhotoPath(String currentPhotoPath) {
    this.currentPhotoPath = currentPhotoPath;
  }

  public String getCurrentPhotoPath() {
    return currentPhotoPath;
  }
}
