package ir.microsign.content.activity;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import ir.microsign.content.database.DataSource;
import ir.microsign.utility.Display;

/**
 * Created by Mohammad on 6/24/14.
 */
public class ActivityBase extends FragmentActivity {
//    private DataSource mDataSource = null;

    public ActivityBase() {
    }

    public DataSource getDataSource() {
        return DataSource.getDataSource();
//        if (mDataSource == null) mDataSource = new DataSource(this);
//        return mDataSource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(0);
    }

//	@Override
//	public void finish() {
//		super.finish();
//	}

    public boolean isLandscape() {
        return Display.isLandscape(this);
    }
}
