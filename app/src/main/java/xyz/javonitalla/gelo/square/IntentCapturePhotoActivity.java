package xyz.javonitalla.gelo.square;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * No UI
 * activity that returns mock photo
 */
public class IntentCapturePhotoActivity extends AppCompatActivity {

  private final static String TAG = IntentCapturePhotoActivity.class.getName();

  private final static int[] MOCK_PHOTOS = {
      R.drawable.mock_1, R.drawable.mock_2, R.drawable.mock_3, R.drawable.mock_4, R.drawable.mock_5, R.drawable.mock_6,
      R.drawable.mock_7, R.drawable.mock_8
  };
  private static int lastPhotoIndex = -1;

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
      File file = getPicturePath(intent);
      if (file != null) {
        copyFile(file, getNextPhoto());
        Toast.makeText(this, "Fake Photo Returned!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
      } else {
        Log.w(TAG, "No picture path specified in request");
        setResult(RESULT_CANCELED);
      }
    } catch (IOException | InterruptedException e) {
      Log.e(TAG, "Can't copy photo");
    }
  }

  @Nullable
  private File getPicturePath(Intent intent) {
    Uri uri = (Uri) intent.getExtras().get(MediaStore.EXTRA_OUTPUT);
    return uri != null ? new File(uri.getPath()) : null;
  }

  private void copyFile(File destination, int resource) throws IOException, InterruptedException {
    InputStream in = getResources().openRawResource(resource);
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
