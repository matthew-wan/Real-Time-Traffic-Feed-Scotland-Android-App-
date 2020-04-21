//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RSSItem {

    private String title="Title Not Available";
    private String description = "Description Not Available";
    private String location="Location Not Available";
    private String link="Link Not Available";
    private Date date;

    public RSSItem(){

    }

    public RSSItem(String title,String description, String location, String link, Date date){
        this.title = title;
        this.description = description;
        this.location = location;
        this.link = link;
        this.date = date;
    }

    //Used to get the start date from the description
    public Date getStartDate(){
        if(description.indexOf("Start Date")!=-1){
            int firstIndex = description.indexOf("Start Date:")+12;
            String dt = description.substring(firstIndex);
            if (dt.indexOf("\n") != -1) {
                dt = dt.substring(dt.indexOf(",") + 2, dt.indexOf("\n")-8 );
                DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                Date startDate;
                try {
                    startDate = formatter.parse(dt);
                    return startDate;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //Used to get the end date from the from the description
    public Date getEndDate(){
        if(description.indexOf("End Date")!=-1) {
            int firstIndex = description.indexOf("End Date:") + 10;
            String dt = description.substring(firstIndex);
            if (dt.indexOf("\n") != -1) {
                dt = dt.substring(dt.indexOf(",") + 2, dt.indexOf("\n") - 8);
                DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                Date endDate;
                try {
                    endDate = formatter.parse(dt);
                    return endDate;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public long getDateDifference(Date date){
        TimeUnit timeUnit = TimeUnit.DAYS;
        long diff = date.getTime() - new Date().getTime();
        return Math.abs(timeUnit.convert(diff,TimeUnit.MILLISECONDS));
    }

    //Set and get methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
