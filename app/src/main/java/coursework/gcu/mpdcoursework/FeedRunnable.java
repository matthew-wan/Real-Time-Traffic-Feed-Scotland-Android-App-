//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import java.util.List;

class FeedRunnable implements Runnable {

    //String for the url feed
    private String url;

    //using GetItems() will return the items in the list
    public List<RSSItem> getItems() {
        return items;
    }

    //List to store the items from the RSS feed
    private List<RSSItem> items;

    public FeedRunnable(String url) {
        this.url = url;
    }

    //Parse the RSS feed with the provided URL and store it into the items list
    @Override
    public synchronized void run() {
        XMLFeedParser rssFeed = new XMLFeedParser(url);
        items = rssFeed.parseFeed();
    }

}
