//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<RSSItem> {

    private Context context;
    private int resource;
    private int lastPosition = -1;
    List<RSSItem> items;

    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<RSSItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.items = objects;
    }

    //used to return the position of an item
    public RSSItem getItem(int position){
        return items.get(position);
    }


    private static class ViewHolder {
        TextView title;
        TextView description;
        TextView location;
        TextView link;
        TextView date;
        TextView color;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the information that is to be displayed
         String title = getItem(position).getTitle();
         String description =  getItem(position).getDescription();
         String location =  "Geo Location: " + getItem(position).getLocation();
         String link =  "Link: " + getItem(position).getLink();;
         Date date =  getItem(position).getDate();;

         RSSItem item = new RSSItem(title,description,location,link,date);

         final View result;
         ViewHolder holder;

         //Used to store object information that is to be loaded in the next few positions
         if(convertView == null) {
             LayoutInflater inflater = LayoutInflater.from(context);
             convertView = inflater.inflate(resource, parent, false);

             //Update the viewholder with the apprpriate text and store it into the layout
             holder = new ViewHolder();
             holder.title = (TextView) convertView.findViewById(R.id.item_layout_title);
             holder.description = (TextView) convertView.findViewById(R.id.item_layout_description);
             holder.location = (TextView) convertView.findViewById(R.id.item_layout_location);
             holder.link = (TextView) convertView.findViewById(R.id.item_layout_link);
             holder.date = (TextView) convertView.findViewById(R.id.item_layout_date);
             holder.color = (TextView) convertView.findViewById(R.id.item_layout_color);

             result = convertView;
             convertView.setTag(holder);
         }else{
             holder = (ViewHolder) convertView.getTag();
             result = convertView;
         }

         //Create an animation. Check if current position is greater than last position used animation loading otherwise use loading up. Then the start animation.
        Animation animation = AnimationUtils.loadAnimation(context, (position>lastPosition)? R.anim.loading_down:R.anim.loading_up);
        result.startAnimation(animation);
        lastPosition = position;

        holder.title.setText(title);
        holder.description.setText(description);
        holder.location.setText(location);
        holder.link.setText(link);
        holder.date.setText(date.toString());

        holder.color.setTextColor(Color.WHITE);

        /*Used to set the background colour to to green, yellow or red.
          If the date difference is less than a week make it green
          between a week and 28 days make it yellow
          over 28 days make it red*/
        try{
            Date startDate = item.getStartDate();
            Date endDate = item.getEndDate();
            long dateDifference = item.getDateDifference(endDate);
            if(dateDifference<=7){
                holder.color.setBackgroundColor(Color.GREEN);
                holder.color.setTextColor(Color.GREEN);
            }else if(dateDifference>7 && dateDifference<28){
                holder.color.setBackgroundColor(Color.YELLOW);
                holder.color.setTextColor(Color.YELLOW);
            }else {
                holder.color.setBackgroundColor(Color.RED);
                holder.color.setTextColor(Color.RED);
            }
        }catch (Exception e){
            Log.e("MyTag","invalid date difference");
        }



        return convertView;
    }
}
