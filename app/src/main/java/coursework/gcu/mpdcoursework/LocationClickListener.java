//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;


public class LocationClickListener implements DialogInterface.OnClickListener {

    private String location;
    private Activity activity;
    private String title;

    public LocationClickListener(String location, Activity activity, String title){
        this.location=location;
        this.activity = activity;
        this.title = title;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        //split the string by the space then store into a string array
        String[] split = location.split("\\s+");
        String latitude = split[0];
        String longitude = split[1];

        //Set the string to google maps and add coordinates and open in the google maps app
        String uri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude+"("+title+")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        activity.startActivity(intent);


    }


}
