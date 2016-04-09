package xyz.javonitalla.gelo.fakecamera;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TakePhotoActivity extends AppCompatActivity {

  private final static int[] MOCK_PHOTOS = {
      R.drawable.mock_1, R.drawable.mock_2, R.drawable.mock_3, R.drawable.mock_4,
      R.drawable.mock_5, R.drawable.mock_6, R.drawable.mock_7, R.drawable.mock_8};
  static int lastPhotoIndex = -1;

  private final static String TAG = TakePhotoActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    prepareToSnapPicture();
    finish();
  }

  private void prepareToSnapPicture() {
    checkSdCard();
    Intent intent = getIntent();

    if (intent.getExtras() != null) {
      snapPicture(intent);
      setResult(RESULT_OK);
    } else {
      Log.w(TAG, "Unable to capture photo. Missing Intent Extras.");
      setResult(RESULT_CANCELED);
    }
  }

  private void checkSdCard() {
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
      Toast.makeText(this, "External SD card not mounted", Toast.LENGTH_LONG).show();
      Log.w(TAG, "External SD card not mounted");
    }
  }

  private void snapPicture(Intent intent) {
    try {
      this.copyFile(getPicturePath(intent));
      Toast.makeText(this, "Fake Photo Returned!", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      Log.e(TAG, "Can't copy photo");
    } catch (InterruptedException e) {
      Log.e(TAG, "Can't copy photo");
    }
  }

  private File getPicturePath(Intent intent) {
    Uri uri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
    return new File(uri.getPath());
  }

  private void copyFile(File destination) throws IOException, InterruptedException {
    InputStream in = getResources().openRawResource(getNextPhoto());
    OutputStream out = new FileOutputStream(destination);
    byte[] buffer = new byte[1024];
    int length;

    if (in != null) {
      while ((length = in.read(buffer)) > 0) {
        out.write(buffer, 0, length);
      }

      in.close();
    }

    out.close();

    //some image not reflecting adding waiting time to allow device write file completely
    Thread.sleep(1000);
  }

  private int getNextPhoto() {
    if (lastPhotoIndex == MOCK_PHOTOS.length - 1) {
      lastPhotoIndex = -1;
    }

    return MOCK_PHOTOS[++lastPhotoIndex];
  }
}
