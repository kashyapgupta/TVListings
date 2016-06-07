package mobi.wrapper.listings.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import mobi.wrapper.listings.constants.UrlConstants;
import mobi.wrapper.listings.controller.network.TVListingNetworkClient;
import mobi.wrapper.listings.model.images.Images;

/**
 * Created by Rohit on 4/27/2016.
 */
public class GalleryImageAdapter extends BaseAdapter
{
    private Context mContext;
    public Images mImages;


    public GalleryImageAdapter(Context context, Images mImages)
    {
        mContext = context;
        this.mImages = mImages;
    }

    public int getCount() {
        if (mImages == null) {
            return 0;
        }else {
            if (mImages.getBackdrops() == null && mImages.getPosters() == null) {
                return mImages.getProfiles().size();
            }else {
                return mImages.getBackdrops().size() + mImages.getPosters().size();
            }
        }
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int index, View view, ViewGroup viewGroup)
    {
        NetworkImageView i = new NetworkImageView(mContext);
        if (mImages.getProfiles() != null) {
            String profile = String.format(UrlConstants.IMAGE_URLW_500, mImages.getProfiles().get(index).getFile_path());
            i.setImageUrl(profile, TVListingNetworkClient.getInstance().getImageLoader());
            i.setLayoutParams(new Gallery.LayoutParams(270, 380));
            i.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            if (index < mImages.getBackdrops().size()) {
                String backdrop = String.format(UrlConstants.IMAGE_URLW_500, mImages.getBackdrops().get(index).getFile_path());
                i.setImageUrl(backdrop, TVListingNetworkClient.getInstance().getImageLoader());
                i.setLayoutParams(new Gallery.LayoutParams(550, 380));
                i.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                int newPosition = index - mImages.getBackdrops().size();
                String poster = String.format(UrlConstants.IMAGE_URLW_500, mImages.getPosters().get(newPosition).getFile_path());
                i.setImageUrl(poster, TVListingNetworkClient.getInstance().getImageLoader());
                i.setLayoutParams(new Gallery.LayoutParams(270, 380));
                i.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        return i;
    }
}
