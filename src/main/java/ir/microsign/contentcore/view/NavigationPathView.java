package ir.microsign.contentcore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ir.microsign.contentcore.database.DataSource;
import ir.microsign.contentcore.object.BookIndexItem;
import ir.microsign.contentcore.object.Content;
import ir.microsign.utility.Font;
import ir.microsign.utility.Text;

/**
 * Created by Mohammad on 26/10/2015.
 */
public class NavigationPathView extends TextView {


    public NavigationPathView(Context context) {
        super(context);
    }

    public NavigationPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationPathView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPath(DataSource dataSource, BookIndexItem bookIndexItem, Content content) {
        String path = null;
        if (content != null) path = content.getPath(dataSource);
        else if (bookIndexItem != null) path = bookIndexItem.getPath(dataSource);
        Text.setText(this, path, Font.TextPos.h3);
    }


}
