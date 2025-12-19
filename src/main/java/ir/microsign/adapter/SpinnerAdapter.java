package ir.microsign.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ir.microsign.utility.Display;
import ir.microsign.utility.Font;
import ir.microsign.R;
import ir.microsign.utility.Text;

/**
 * Created with IntelliJ IDEA.
 * User: Mohammad
 * Date: 4/29/13
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    Font mFont = null;

    public SpinnerAdapter(Context context, String[] items, Font font) {
        super(context, R.layout.spinner, items);
        mFont = font;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
//        convertView.setBackgroundResource(R.drawable.back_selectable_spinner);
        ((TextView) convertView).setTextColor(Color.parseColor("#444444"));
        int padding = Display.dpToPx(getContext(), 5);
//        convertView.setPadding(padding * 2, padding*2, (int) (padding * 2), padding*2);
        Text.setText(convertView, ((TextView) convertView).getText().toString(), mFont, false);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = super.getDropDownView(position, convertView, parent);
        ((TextView) convertView).setTextColor(Color.parseColor("#444444"));
        convertView.setBackgroundResource(R.drawable.back_selectable_menu_item);
        int padding = Display.dpToPx(getContext(), 5);
        convertView.setPadding(padding * 2, padding*2, padding * 2, padding*2);
        Text.setText(convertView, ((TextView) convertView).getText().toString(), mFont, false);
        return convertView;
    }
}
