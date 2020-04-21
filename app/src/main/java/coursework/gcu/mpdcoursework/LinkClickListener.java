//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class LinkClickListener implements DialogInterface.OnClickListener {

    private String url;
    Activity activity;

    //sets the url and activity to the one currently used
    public LinkClickListener(String url, Activity activity){
        this.url=url;
        this.activity = activity;
    }

    //When the dialog button for the link is clicked on, it sends an intent to the browser with the url
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Log.e("MyTag",url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.url));
        activity.startActivity(browserIntent);
    }


}
