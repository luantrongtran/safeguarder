package ifn701.safeguarder.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ifn701.safeguarder.Constants;
import ifn701.safeguarder.R;
import ifn701.safeguarder.backend.myApi.model.Accident;
import ifn701.safeguarder.entities.google_places.Place;
import ifn701.safeguarder.webservices.ImageViewUrlLoader;
import ifn701.safeguarder.webservices.google_web_services.GoogleImageService;
import ifn701.safeguarder.webservices.google_web_services.GooglePlaceDetailService;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class customizes the content of window info when click on a marker.
 * There are more than 1 types of marker. Therefore, marker's title will be the type of marker which
 * could be CustomWindowInfoAdapter.ACCIDENT_TYPE or CustomWindowInfoAdapter.GOOGLE_PLACE_TYPE
 */
public class CustomWindowInfoAdapter implements InfoWindowAdapter {

    TextView titleView;
    LinearLayout contentBeforeImage;
    ImageView imageView;
    TextView textAfterImage;

    SimpleDateFormat simpleDateFormat ;

    ProgressDialog processDialog;

    public static String ACCIDENT_TYPE = "ACCIDENT_TYPE";
    public static String GOOGLE_PLACE_TYPE = "GOOGLE_PLACE_TYPE";

    public static final int TIME_OUT_PER_IMAGE = 5000;
    public static final int GOOGLE_PLACE_DETAIL_TIME_OUT = 5000;

    private JacksonFactory jacksonFactory = new JacksonFactory();

    LinearLayout.LayoutParams informationLayoutParams;

    View view;
    Context context;
    public CustomWindowInfoAdapter(Context context) {
        this.context = context;

        processDialog = new ProgressDialog(context);
        processDialog.setMessage("loading");
        simpleDateFormat = new SimpleDateFormat(context.getResources()
                .getString(R.string.time_format));
        setUpLayout();
    }

    /**
     *
     * @param type should be CustomWindowInfoAdapter.ACCIDENT_TYPE
     *             or CustomWindowInfoAdapter.GOOGLE_PLACE_TYPE
     * @return a new MarkerOptions which has title is a type. The type will decide how
     * the info window content displayed.
     */
    public static MarkerOptions createMarkerOptions(String type) {
        MarkerOptions markerOptions = new MarkerOptions().title(type);

        return markerOptions;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        String title = marker.getTitle();
        if(title.equals(ACCIDENT_TYPE)){
            getInfoWindowForAccidentMarker(marker);
        } else if (title.equals(GOOGLE_PLACE_TYPE)) {
            getInfoWindowForGooglePlaceMarker(marker);
        }

        return view;
    }

    private void setUpLayout() {
        LayoutInflater layoutInflater
                = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custome_window_info, null);

        titleView = (TextView)view.findViewById(R.id.window_info_title);
        contentBeforeImage = (LinearLayout)view.findViewById(R.id.window_info_content_before_image);
        imageView = (ImageView)view.findViewById(R.id.window_info_image);
        textAfterImage = (TextView)view.findViewById(R.id.window_info_text_after_image);

        informationLayoutParams = (LinearLayout.LayoutParams) titleView.getLayoutParams();
    }

    /**
     * If a marker's type is GOOGLE_PLACE_TYPE
     * @param marker
     */
    public void getInfoWindowForGooglePlace(Marker marker) {
        String snippet = marker.getSnippet();

        Place accident = new Place();
        try {
            jacksonFactory.createJsonParser(snippet).parse(accident);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * The marker's snippet should be google place id
     * @param marker the marker snippet contains the place_id of the clicked place
     */
    public void getInfoWindowForGooglePlaceMarker(Marker marker) {
        reset();
        String snippet = marker.getSnippet();

        Place place = null;
        GooglePlaceDetailService googlePlaceDetailService = new GooglePlaceDetailService(context);
        try {
            place = googlePlaceDetailService.execute(snippet)
                    .get(GOOGLE_PLACE_DETAIL_TIME_OUT, TimeUnit.MILLISECONDS);

            if(place != null && place.photos != null && place.photos.size()>0) {
                String photoRef = place.photos.get(0).photo_reference;
                if(!photoRef.isEmpty()) {
                    GoogleImageService googleImageService = new GoogleImageService(context);
                    Bitmap bm = googleImageService.execute(photoRef)
                            .get(TIME_OUT_PER_IMAGE, TimeUnit.MILLISECONDS);
                    imageView.setImageBitmap(bm);
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        if(place == null) {
            return;
        }

        String title = place.name;
        titleView.setText(title);

        String address = "<b>" + context.getResources()
                .getString(R.string.window_info_google_place_address)
                + "</b> " + place.formatted_address;
        addInformationBeforeImage(address);

        String phone = "<b>"
                + context.getResources().getString(R.string.window_info_google_place_phone)
                + "</b> " + place.formatted_phone_number;
        addInformationBeforeImage(phone);
    }

    /**
     * If a marker's type is ACCIDENT_TYPE
     * @param marker the marker snippet contain a json string of Accident object
     */
    public void getInfoWindowForAccidentMarker(Marker marker) {
        reset();

        String snippet = marker.getSnippet();

        Accident accident = new Accident();
        try {
            jacksonFactory.createJsonParser(snippet).parse(accident);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String title = accident.getName() ;
        titleView.setText(title);

        String beforeImage = "";
        String time = String.format("<b>"
                + context.getResources().getString(R.string.window_info_accident_time) + "</b> "
                + simpleDateFormat.format(new Timestamp(accident.getTime())));
        addInformationBeforeImage(time);

        String description = "<b>"
                + context.getResources().getString(R.string.window_info_accident_description)
                + "</b> " + accident.getDescription();
        addInformationBeforeImage(description);

        String image1 = accident.getImage1();
        Bitmap bitmap = null;
        try {
            if(image1 != null && !image1.isEmpty()) {
                bitmap = new ImageViewUrlLoader(imageView)
                        .execute(image1)
                        .get(TIME_OUT_PER_IMAGE, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        if(bitmap == null) {
            imageView.setImageResource(R.drawable.default_image);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void reset() {
        titleView.setText("Failed to load information");
        contentBeforeImage.removeAllViews();
        imageView.setImageResource(R.drawable.default_image);
        textAfterImage.setText("");
    }

    /**
     * Add a new information such as an address, phone number of a place or time of an accident
     * @param content can be html string
     */
    protected void addInformationBeforeImage(String content){
        TextView textView = new TextView(view.getContext());
        textView.setLayoutParams(informationLayoutParams);
        textView.setTextColor(context.getResources()
                .getColor(R.color.custom_window_info_text_color));
        textView.setText(Html.fromHtml(content));
        ((LinearLayout)view.findViewById(R.id.window_info_content_before_image)).addView(textView);
    }
}