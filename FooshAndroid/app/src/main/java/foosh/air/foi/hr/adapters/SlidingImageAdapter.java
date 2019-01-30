package foosh.air.foi.hr.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import foosh.air.foi.hr.R;

/**
 * Adapter za prikaz slika u klizaču slika.
 */
public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;


    /**
     * Konstruktor.
     * @param context
     * @param images
     */
    public SlidingImageAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images =images;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Briše sliku iz klizača slika.
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * Broji slike.
     * @return
     */
    @Override
    public int getCount() {
        return (images !=null? images.size():0);
    }

    /**
     * Instancira novu sliku.
     * @param view
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.viewpager_image, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.viewPagerItem_image1);

        Picasso.get().load(images.get(position)).error(R.drawable.ic_launcher_foreground).centerCrop().fit().into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
