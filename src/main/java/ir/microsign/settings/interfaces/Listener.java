package ir.microsign.settings.interfaces;

import androidx.fragment.app.Fragment;

import java.util.List;

import ir.microsign.dbhelper.object.BaseObject;
import ir.microsign.settings.view.SettingItemBaseView;

/**
 * Created by Mohammad on 6/18/14.
 */
public class Listener {
    public interface ReceiveCompletedListener {
        public void onReceiveCompleted(Object result, int resultState);
    }

    public interface ReceiveObjectCompletedListener {
        public void onReceiveObjectCompleted(List<BaseObject> objects, int resultState);

        public void onReceiveStepObjectCompleted(List<BaseObject> objects, int resultState);
    }

    public interface EventRequestListener {
        public void onEventRequest(int code, Object... args);
    }

    public interface DbItemSelectedListener {
        public void onItemSelected(BaseObject selectedItem);
    }

    //	public interface MenuItemSelectedListener {
//		public void onItemSelected(MenuItem selectedItem);
//	}
    public interface ResultListener {
        public void onResult(Object o, int i);
    }

    public interface FragmentListener {
        public void onStart(Fragment fragment);
    }

    public interface SettingItemClickListener {
        public void onItemChangeClick(SettingItemBaseView selectedItem);

        public void onItemDefaultClick(SettingItemBaseView selectedItem);
    }

}
