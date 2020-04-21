//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

public class ListItemClickListener implements AdapterView.OnItemClickListener {

    private Activity activity;
    private ItemAdapter adapter;

    public ListItemClickListener(Activity activity, ItemAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Get the get the item from the specific list position that was clicked on
        RSSItem item = adapter.getItem(i);

        //Get the title, link, and location from the item
        String title = item.getTitle();
        String url = item.getLink();
        String location = item.getLocation();

        //Create a dialog box to allow the user to open link in a browser or location in google maps
        AlertDialog dialog = new AlertDialog.Builder(activity).create();

        //setCancelable makes allows dialog box to be closed if clicked outside
        dialog.setCancelable(true);
        dialog.setTitle(item.getTitle());
        DialogInterface.OnClickListener linkListener = new LinkClickListener(url, activity);

        //Create the buttons to open the link to browser, google maps or cancel
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Open Link in Browser", linkListener);
        DialogInterface.OnClickListener mapListener = new LocationClickListener(location,activity,title);
        dialog.setButton(Dialog.BUTTON_NEGATIVE, "View Location in Google Maps", mapListener);
        dialog.setButton(Dialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}