//Name: Chun Ting Matthew Wan
//Matric Number: S1625394
package coursework.gcu.mpdcoursework;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    //on create set the layout to activity_main and enable the bottom navigation listener
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomnav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //Start the app in the current incident fragment as the saved instance is null otherwise use the saved fragment
        if (savedInstanceState == null) {
            CurrentIncidentsFragment currentFragment = new CurrentIncidentsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, currentFragment, "saved_fragment").commit();
        } else {
            CurrentIncidentsFragment  currentFragment = (CurrentIncidentsFragment  ) getSupportFragmentManager().findFragmentByTag("saved_fragment");
        }

    }

    //Listener to create a new fragment when item is clicked on the bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            //Switch statement used to match the navigation ItemID to its fragment
            switch (menuItem.getItemId()){
                case R.id.bottomnav_roadworks:
                    selectedFragment = new RoadworksFragment();
                    break;
                case R.id.bottomnav_plannedroadworks:
                    selectedFragment = new PlannedRoadworksFragment();
                    break;
                case R.id.bottomnav_currentincidents:
                    selectedFragment = new CurrentIncidentsFragment();
                    break;
            }

            //Updates the fragment manager to the selected fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer, selectedFragment).commit();

            return true;
        }
    };
}