package xyz.javonitalla.gelo.square.photos;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import xyz.javonitalla.gelo.square.R;
import xyz.javonitalla.gelo.square.data.MockPhoto;

/**
 * Created by ajavonitalla on 4/11/2016.
 */
public class MockPhotosFragment extends Fragment implements MockPhotosContract.View {

  private static final String TAG = MockPhotosFragment.class.getName();

  private MockPhotosContract.Presenter presenter;

  private RecyclerView pictures;
  private FloatingActionButton fab;

  private MockPhotosAdapter adapter;
  private int columns = 2;

  //region Overriden Methods

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.pictures = (RecyclerView) getActivity().findViewById(R.id.pictures_grid);
    this.adapter = new MockPhotosAdapter(getActivity(), columns);
    pictures.setAdapter(adapter);
    pictures.setLayoutManager(new GridLayoutManager(getActivity(), columns));

    this.fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
    if (fab != null) {
      fab.setOnClickListener(new TakePhotoListener());
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      presenter.result(requestCode);
    }
  }

  @Override
  public void showPhotos(List<MockPhoto> photos) {
    adapter.showPhotos(photos);
  }

  @Override
  public void showPhoto(MockPhoto photo) {
    adapter.showPhoto(photo);
  }

  @Override
  public void showNoCameraError() {
    //no need to check for fab if null since it can't be clicked if null
    Snackbar.make(fab, "No App installed to take pictures", Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void showUnableToCreateFileError() {
    Snackbar.make(fab, "Unable to create file. Make sure you have enough space or SD card is available",
        Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void setPresenter(MockPhotosContract.Presenter presenter) {
    this.presenter = presenter;
  }

  //endregion

  //region Inner Classes

  private class TakePhotoListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
      Intent intent = createTakePictureIntent();
      if (intent == null) {
        presenter.handleUnableToCreateFile();
      } else if (isCameraAppAvailable(intent)) {
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
      } else {
        presenter.handleNoAppToHandleCameraIntent();
      }
    }

    @Nullable
    private Intent createTakePictureIntent() {
      File photoFile = createImageFile();
      if (photoFile != null) {
        // Save a file: path for use with ACTION_VIEW intents
        String currentPhotoPath = "file:" + photoFile.getAbsolutePath();
        presenter.setCurrentPhotoPath(currentPhotoPath);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        return takePictureIntent;
      }

      return null;
    }

    private boolean isCameraAppAvailable(Intent intent) {
      return intent != null && intent.resolveActivity(getActivity().getPackageManager()) != null;
    }

    //Create the File where the photo should go
    @Nullable
    private File createImageFile() {
      // Create an image file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = timeStamp + "_";
      File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

      File image = null;
      try {
        image = File.createTempFile(imageFileName, ".jpg", storageDir);
      } catch (IOException e) {
        Log.e(TAG, "Unable to create image file", e);
      }

      return image;
    }
  }

  //endregion
}
