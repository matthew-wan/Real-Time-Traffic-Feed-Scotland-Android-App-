//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrentIncidentsFragment extends Fragment {
    private List<RSSItem> items;
    private ItemAdapter adapter;
    private ListView listView;
    private List<RSSItem> sendList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //URL to be parsed for current incidents
        FeedRunnable feed = new FeedRunnable("https://trafficscotland.org/rss/feeds/currentincidents.aspx");

        //Starts a new thread to to parse the feed. It also wait for the other thread to finish before starting the thread
        try {
            Thread thread = new Thread(feed);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        items = feed.getItems();
        sendList = items;


        RelativeLayout layout =(RelativeLayout)inflater.inflate(R.layout.fragment_currentincidents,container,false);
        listView = (ListView) layout.findViewById(R.id.currentincidents_list);
        adapter = new ItemAdapter(layout.getContext(), R.layout.item_layout, items);
        listView.setAdapter(adapter);
        final EditText locationFilter = (EditText) layout.findViewById(R.id.currentLocation);

        locationFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentText = String.valueOf(locationFilter.getText());
                Log.e ("MyTag", currentText);
                List<RSSItem> dummyList = new ArrayList<>();
                for(RSSItem currentItem: items){
                    String title = currentItem.getTitle();
                    if(title.toLowerCase().contains(currentText.toLowerCase())){
                        dummyList.add(currentItem);
                    }
                }
                sendList.clear();
                sendList.addAll(dummyList);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        /*Checks the amount of items in the list. If there are items, update the text to display the number of incidents. If there are no items text will say "No Current Incidents!".
        Also updates the text size to be large when there no incidents to fill the screen. */
        TextView incidentNum = (TextView) layout.findViewById(R.id.incidentNumber);
        if (items.size() > 0) {
            incidentNum.setTextSize(14);
            incidentNum.setText("Number of Current Incidents: "+ String.valueOf(items.size()));

        } else {
            incidentNum.setTextSize(30);
            incidentNum.setText("No Current Incidents!");
        }

        listView.setOnItemClickListener(new ListItemClickListener(getActivity(),adapter));

        return layout;
    }
}
