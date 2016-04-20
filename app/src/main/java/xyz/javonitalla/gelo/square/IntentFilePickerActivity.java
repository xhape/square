package xyz.javonitalla.gelo.square;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntentFilePickerActivity extends AppCompatActivity {

  private final static String TAG = IntentFilePickerActivity.class.getName();

  private final static FileMeta[] MOCK_FILE = {
      new FileMeta("csv", R.raw.sample_csv), new FileMeta("doc", R.raw.sample_doc),
      new FileMeta("docx", R.raw.sample_docx), new FileMeta("pdf", R.raw.sample_pdf),
      new FileMeta("html", R.raw.sample_html), new FileMeta("txt", R.raw.sample_txt),
      new FileMeta("xml", R.raw.sample_xml)
  };
  private static int lastMockFile = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    pickFile();
    finish();
  }

  private void pickFile() {
    FileMeta nextFile =  getNextFile();
    File destination = getOutputMediaFile(nextFile.type);

    try {
      if (destination != null) {
        copyFile(destination, nextFile.resource);

        Intent intent = new Intent();
        intent.setData(Uri.fromFile(destination));
        setResult(RESULT_OK, intent);
      } else {
        Log.w(TAG, "No picture path specified in request");
        setResult(RESULT_CANCELED);
      }

    } catch (IOException | InterruptedException e) {
      Log.e(TAG, "Can't copy file");
    }
  }

  @Nullable
  private static File getOutputMediaFile(String fileExtension) {
    String externalStorageState = Environment.getExternalStorageState();
    if (!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
      Log.e(TAG, "SD Card is not mounted.");
      return null;
    }

    File mediaStorageDir =
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "fake-files");
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
      Log.e(TAG, "Cannot create holmes pictures directory.");
      return null;
    }

    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    return new File(mediaStorageDir.getPath() + File.separator + "FAKE_" + timestamp + "." + fileExtension);
  }

  private FileMeta getNextFile() {
    if (lastMockFile == MOCK_FILE.length - 1) {
      lastMockFile = -1;
    }

    return MOCK_FILE[++lastMockFile];
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

    //some file not reflecting adding waiting time to allow device write file completely
    Thread.sleep(1000);
  }

  private static class FileMeta {
    private String type;
    private int resource;

    public FileMeta(String type, int resource) {
      this.type = type;
      this.resource = resource;
    }
  }
}
