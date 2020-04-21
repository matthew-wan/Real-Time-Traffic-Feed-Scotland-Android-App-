//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;


import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoadworksFragment extends Fragment {

    private List<RSSItem> items;
    private ItemAdapter adapter;
    private ListView listView;
    private Date toDate;
    private RelativeLayout layout;
    private List<RSSItem> sendList;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //set the url to be parsed
        FeedRunnable feed = new FeedRunnable("https://trafficscotland.org/rss/feeds/roadworks.aspx");

        //Starts a new thread to to parse the feed. It also wait for the other thread to finish before starting the thread
        try {
            Thread thread = new Thread(feed);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Get the items from the feed
        items = feed.getItems();

        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_roadworks,container,false);
        this.layout = layout;

        listView = (ListView) layout.findViewById(R.id.roadworks_list);

        final EditText locationFilter = (EditText) layout.findViewById(R.id.roadworkLocation);

        locationFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //items = feed.getItems();
                //locationFilter.getText();
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

        Button to = (Button) layout.findViewById(R.id.to_roadworks);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToDatePicker();
            }
        });
        Button search = (Button) layout.findViewById(R.id.search_roadworks);

        //If the date has not been set or the date chosen is before the current date the search button will not be enabled
        if(toDate==null || toDate.before(new Date())){
            search.setEnabled(false);
        }else{
            search.setEnabled(true);
        }

        //When the search button is clicked, create a list to only store the items with dates between the input dates and update the list view.
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<RSSItem> dummyList = new ArrayList<>();
                for(RSSItem currentItem: sendList){
                    try {
                        Date endDate = currentItem.getEndDate();
                        Log.e("MyTag",endDate.toString());
                        if(endDate.before(toDate)){
                            dummyList.add(currentItem);
                        }
                    }catch (Exception e){

                    }

                }
                sendList.clear();
                sendList.addAll(dummyList);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
            }
        });
        sendList = items;
        adapter = new ItemAdapter(layout.getContext(), R.layout.item_layout, sendList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListItemClickListener(getActivity(),adapter));

        return layout;
    }



    public void showToDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), to, fromCalender.get(Calendar.YEAR), fromCalender.get(Calendar.MONTH), fromCalender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    Calendar fromCalender = Calendar.getInstance();

    /*Listeners for the date picker dialog. Store the dates chosen from the date picker "to".
     Set the date format to day/month/year
     then store into a string
    */
    DatePickerDialog.OnDateSetListener to = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fromCalender.set(Calendar.YEAR, year);
            fromCalender.set(Calendar.MONTH, monthOfYear);
            fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            toDate = fromCalender.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String str = dateFormat.format(toDate);
            Button to = (Button) layout.findViewById(R.id.to_roadworks);
            to.setText(str);
            Button search = (Button) layout.findViewById(R.id.search_roadworks);

            //If the date has not been set or the date chosen is before the current date the search button will not be enabled
            if(toDate==null || toDate.before(new Date())){
                search.setEnabled(false);
            }else{
                search.setEnabled(true);
            }
        }
    };
}
