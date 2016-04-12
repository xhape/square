package xyz.javonitalla.gelo.square.photos;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.List;
import xyz.javonitalla.gelo.square.R;
import xyz.javonitalla.gelo.square.data.MockPhoto;

/**
 * Created by ajavonitalla on 4/10/2016.
 */
public class MockPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Activity host;
  private LayoutInflater layoutInflater;
  private List<MockPhoto> mockPhotos;

  private int imageSize;

  public MockPhotosAdapter(Activity host, int columns) {
    this.host = host;
    this.layoutInflater = LayoutInflater.from(host);
    this.mockPhotos = new ArrayList<>();

    DisplayMetrics metrics = new DisplayMetrics();
    host.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    this.imageSize = metrics.widthPixels / columns;
  }

  //region Overriden Methods

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    PhotosHolder holder = new PhotosHolder(layoutInflater.inflate(R.layout.photo_list_item, parent, false));
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    PhotosHolder photosHolder = (PhotosHolder) holder;
    MockPhoto photo = mockPhotos.get(position);
    Glide.with(host)
        .load(photo.getPhotoPath())
        .override(imageSize, imageSize)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .into(photosHolder.imageView);
  }

  @Override
  public int getItemCount() {
    return mockPhotos.size();
  }

  //endregion

  //region Public Methods

  public void showPhotos(@NonNull  List<MockPhoto> photos) {
    if (!photos.isEmpty()) {
      mockPhotos.addAll(photos);
      notifyDataSetChanged();
    }
  }

  public void showPhoto(@NonNull MockPhoto photo) {
    //add at the start of the list
    mockPhotos.add(0, photo);
    notifyDataSetChanged();
  }

  //endregion

  //region Inner Classes

  private static class PhotosHolder extends RecyclerView.ViewHolder {

    ImageView imageView;

    public PhotosHolder(View itemView) {
      super(itemView);
      this.imageView = (ImageView) itemView.findViewById(R.id.image);
    }
  }

  //endregion
}
