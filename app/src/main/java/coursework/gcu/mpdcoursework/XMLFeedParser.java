//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import android.text.Html;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XMLFeedParser {

    private String rssUrl;
    private List<RSSItem> items;
    private RSSItem rssItem;
    private String text;
    private InputStream stream;

    //Use this to start the parser in another class
    public XMLFeedParser(String rssUrl){
        this.rssUrl = rssUrl;
        items = new ArrayList<>();
        rssItem = new RSSItem();
        this.initializeStream();
    }

    //Used to start a connection to the url
    private void initializeStream(){
        URL aurl;
        URLConnection yc;
        try{
            aurl = new URL(rssUrl);
            yc = aurl.openConnection();
            this.stream = yc.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<RSSItem> getItems(){
        return items;
    }

    //used to parse the rss feed
    public List<RSSItem> parseFeed(){
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try{
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(stream, null);

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = parser.getName();



                switch (eventType){
                    //create new item list
                    case XmlPullParser.START_TAG:
                        if(tagName.equalsIgnoreCase("item")){
                            rssItem = new RSSItem();
                        }
                    break;

                    case XmlPullParser.TEXT:
                        text=parser.getText();
                    break;

                    //parses the feed. adds an item to the list each time and uses setters to store the information into the appropirate variable based on the tag
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase("item")){
                            items.add(rssItem);
                        }else if(tagName.equalsIgnoreCase("title")){
                            rssItem.setTitle(text);
                        }else if(tagName.equalsIgnoreCase("description")){
                            String des = Html.fromHtml(text).toString();
                            rssItem.setDescription(des);
                        }else if(tagName.equalsIgnoreCase("point")){
                            rssItem.setLocation(text);
                        }else if(tagName.equalsIgnoreCase("link")){
                            rssItem.setLink(text);
                        }else if(tagName.equalsIgnoreCase("pubDate")){
                            rssItem.setDate(new Date(text));
                        }
                    break;
                        default:
                            break;
                }
                eventType =  parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return items;
    }

}
