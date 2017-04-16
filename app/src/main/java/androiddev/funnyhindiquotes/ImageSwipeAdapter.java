package androiddev.funnyhindiquotes;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Gurpreet on 12-04-2017.
 */

public class ImageSwipeAdapter extends PagerAdapter {
    ArrayList<String> imagesList;
    private Context ctx;
    private LayoutInflater layoutInflater;


    public ImageSwipeAdapter(Context context, ArrayList<String> imageList) {
        ctx = context;
        imagesList = imageList;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = layoutInflater.inflate(R.layout.swipe_layout, container, false);
        final ImageView funnyImages = (ImageView) itemView.findViewById(R.id.viewpager_image);
        Glide.with(ctx).load(imagesList.get(position))
                .into(funnyImages);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
