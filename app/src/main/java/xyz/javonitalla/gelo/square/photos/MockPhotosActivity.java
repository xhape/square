package xyz.javonitalla.gelo.square.photos;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import xyz.javonitalla.gelo.square.R;

/**
 * screen to add photos to be used for mocking
 */
public class MockPhotosActivity extends AppCompatActivity {

  private MockPhotosContract.Presenter presenter;
  private MockPhotosContract.View view;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    this.view = new MockPhotosFragment();
    this.presenter = new MockPhotosPresenter(view);

    if (savedInstanceState != null) {
      String currentPhotoPath = savedInstanceState.getString(MockPhotosContract.Presenter.CURRENT_PHOTO_PATH);
      presenter.setCurrentPhotoPath(currentPhotoPath);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    outState.putSerializable(MockPhotosContract.Presenter.CURRENT_PHOTO_PATH, presenter.getCurrentPhotoPath());

    super.onSaveInstanceState(outState, outPersistentState);
  }
}
