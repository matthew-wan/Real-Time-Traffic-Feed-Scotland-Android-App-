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

public class PlannedRoadworksFragment extends Fragment {

    private List<RSSItem> items;
    private ItemAdapter adapter;
    private ListView listView;
    private Date fromDate;
    private Date toDate;
    private RelativeLayout layout;
    private List<RSSItem> sendList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        //set the URL to be parsed
        FeedRunnable feed = new FeedRunnable("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");

        //Starts a new thread to to parse the feed. It also wait for the other thread to finish before starting the thread
        try {
            Thread thread = new Thread(feed);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        items = feed.getItems();

        RelativeLayout layout =(RelativeLayout)inflater.inflate(R.layout.fragment_plannedroadworks,container,false);
        this.layout = layout;

        //Get the button ids
        Button from = (Button) layout.findViewById(R.id.from_plannedroadworks);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFromDatePicker();
            }
        });
        Button to = (Button) layout.findViewById(R.id.to_plannedroadworks);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToDatePicker();
            }
        });
        Button search = (Button) layout.findViewById(R.id.search_plannedroadworks);

        final EditText locationFilter = (EditText) layout.findViewById(R.id.plannedLocation);

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

        //if the to date or from date is null disable the search button
        if(toDate==null || fromDate==null){
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
                    Date startDate = currentItem.getStartDate();
                    Date endDate = currentItem.getEndDate();
                    if(startDate.after(fromDate) && endDate.before(toDate)){
                        dummyList.add(currentItem);
                    }
                }
                sendList.clear();
                sendList.addAll(dummyList);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
            }
        });


        listView = (ListView) layout.findViewById(R.id.plannedroadworks_list);

        sendList = items;
        adapter = new ItemAdapter(layout.getContext(), R.layout.item_layout, sendList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new ListItemClickListener(getActivity(),adapter));

        return layout;
    }

    Calendar fromCalender = Calendar.getInstance();

    public void showFromDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), from, fromCalender.get(Calendar.YEAR), fromCalender.get(Calendar.MONTH), fromCalender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showToDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), to, fromCalender.get(Calendar.YEAR), fromCalender.get(Calendar.MONTH), fromCalender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /*Listeners for the date picker dialog. Store the dates chosen from the date picker into "from" and "to".
      Set the date format to day/month/year
      then store into a string
     */
    DatePickerDialog.OnDateSetListener from = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fromCalender.set(Calendar.YEAR, year);
            fromCalender.set(Calendar.MONTH, monthOfYear);
            fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            fromDate = fromCalender.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String str = dateFormat.format(fromDate);
            Button from = (Button) layout.findViewById(R.id.from_plannedroadworks);
            from.setText(str);

            Button search = (Button) layout.findViewById(R.id.search_plannedroadworks);
            if(toDate==null || fromDate==null){
                search.setEnabled(false);
            }else{
                search.setEnabled(true);
            }

        }
    };
    DatePickerDialog.OnDateSetListener to = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fromCalender.set(Calendar.YEAR, year);
            fromCalender.set(Calendar.MONTH, monthOfYear);
            fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            toDate = fromCalender.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            String str = dateFormat.format(toDate);
            Button to = (Button) layout.findViewById(R.id.to_plannedroadworks);
            to.setText(str);
            Button search = (Button) layout.findViewById(R.id.search_plannedroadworks);
            if(toDate==null || fromDate==null){
                search.setEnabled(false);
            }else{
                search.setEnabled(true);
            }
        }
    };
}
