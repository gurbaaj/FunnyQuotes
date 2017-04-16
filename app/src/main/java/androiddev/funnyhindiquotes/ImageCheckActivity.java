package androiddev.funnyhindiquotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImageCheckActivity extends AppCompatActivity {
    ArrayList<String> newList;
    ViewPager viewPager;
    ImageSwipeAdapter adapter;
    int imagePosition;
    FloatingActionButton Share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_check);
        viewPager = (ViewPager)findViewById(R.id.imageViewPager);
        final CoordinatorLayout screen  =(CoordinatorLayout) findViewById(R.id.CL_imageShare);
        Share = (FloatingActionButton)findViewById(R.id.fabShare);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageCheckActivity.this, "Starting Intent", Toast.LENGTH_SHORT).show();
                Share.setVisibility(View.GONE);
                Bitmap mBitmap = screenShot(screen);
                Share.setVisibility(View.VISIBLE);
                saveScreenshot(mBitmap);
            }
        });
        try {
            Intent i = getIntent();
            newList = i.getStringArrayListExtra("imageArrayList");
            imagePosition = i.getIntExtra("imagePosition",0);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("An_error","This is a network error");
        }
        adapter = new ImageSwipeAdapter(this,newList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagePosition);
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void saveScreenshot(Bitmap bm) {
        ByteArrayOutputStream bao = null;
        File file = null;

        try {
            bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG,40,bao);

            file = new File(Environment.getExternalStorageDirectory()+ File.separator + "Image.jpg");
            file.createNewFile();

            FileOutputStream fos  = new FileOutputStream(file);
            fos.write(bao.toByteArray());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ImageCheckActivity.this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }
}
