package ir.microsign.apphelper.interfaces;

import androidx.fragment.app.Fragment;

import ir.microsign.apphelper.object.MenuItem;
import ir.microsign.dbhelper.object.BaseObject;

//import net.tarnian.content.object.BaseObject;

/**
 * Created by Mohammad on 6/18/14.
 */
public class Listener {

    public interface EventRequestListener {
        public void onEventRequest(int code, Object... args);
    }

    public interface DbItemSelectedListener {
        public void onItemSelected(BaseObject selectedItem);
    }

    public interface MenuItemSelectedListener {
        public void onItemSelected(MenuItem selectedItem);
    }

    public interface FragmentListener {
        public void onStart(Fragment fragment);
    }

}
