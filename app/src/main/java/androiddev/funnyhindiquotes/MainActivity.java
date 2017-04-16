package androiddev.funnyhindiquotes;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    /**
     * The images.
     */
    public ArrayList<String> urlResponse;
    GridView gallery;
    Boolean check;
    ProgressDialog loading;
    AlertDialog.Builder builder;
    public String Data_Url = "http://gurbaaj.16mb.com/photos.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = checkWriteExternalPermission();
        getData();
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Required");
        builder.setMessage("Please Give Storage Permissions to the App...");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        if (!check) {
            builder.show();
        }
        gallery = (GridView) findViewById(R.id.gridview);

        //Action On item click
        gallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != urlResponse && !urlResponse.isEmpty()) {
                    Intent i = new Intent(MainActivity.this, ImageCheckActivity.class);
                    i.putExtra("imageArrayList", urlResponse);
                    i.putExtra("imagePosition", position);
                    startActivity(i);
                }
            }
        });
    }

    private boolean checkWriteExternalPermission() {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    public void getData() {
        loading = ProgressDialog.show(MainActivity.this, "Data is Loading", "Please wait...", false, false);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Data_Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loading.dismiss();
                parseData(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
// Creating RequestQueue
        CustomVolleyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }


    private void parseData(JSONArray response) {
        urlResponse = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject;
            try {
                jsonObject = response.getJSONObject(i);
                String Images = jsonObject.getString("Image");
                urlResponse.add(Images);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        gallery.setAdapter(new ImageAdapter(this, urlResponse));
    }

    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {

        /**
         * The context.
         */
        private Activity context;
        ArrayList<String> urlImages;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */

        public ImageAdapter(Activity localContext, ArrayList<String> urlImages) {
            context = localContext;
            this.urlImages = urlImages;
        }

        public int getCount() {
            return urlImages.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }
            try {
                Glide.with(context).load(urlImages.get(position))
                        .placeholder(R.drawable.loading).centerCrop()
                        .into(picturesView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return picturesView;
        }

    }
}