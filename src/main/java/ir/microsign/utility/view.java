package ir.microsign.utility;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import ir.microsign.R;


/**
 * Created by Mohammad on 9/25/14.
 */
public class view {
    static LayoutInflater mLayoutInflater = null;

//    public static LayoutInflater getLayoutInflater() {
//        return getLayoutInflater(activity.getContext());
//    }

    public static LayoutInflater getLayoutInflater(Context context) {
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return mLayoutInflater;
    }

    public static int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    public static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static boolean setVisibility(View view, boolean visible) {
        if (view == null) return false;
        int visibility = visible ? View.VISIBLE : View.GONE;
//		if (view.getVisibility()!=visibility)
        view.setVisibility(visibility);
        return visible;
    }

//	@TargetApi(Build.VERSION_CODES.CUPCAKE)
//	public static void bindDigitInput(View _root) {
//		ViewGroup root = (ViewGroup) _root;
//		for (int i = 0; i < root.getChildCount(); ++i) {
//			View child = root.getChildAt(i);
//
//			if (child instanceof EditText) {
//				EditText et = ((EditText) child);
//				Boolean hasDot0 = false;
//				int inputType0 = et.getInputType();
//				if (inputType0 == InputType.TYPE_CLASS_NUMBER) hasDot0 = false;
//				else if (inputType0 == InputType.TYPE_NUMBER_FLAG_DECIMAL || (inputType0 == (InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER)))
//					hasDot0 = true;
//
//				Object tag = et.getTag();
//				if (tag != null && !(tag instanceof Pair)) continue;
//
//				if (hasDot0) et.setTag(new Object[]{true, tag});
//				else et.setTag(new Object[]{false, tag});
//				et.setKeyListener(null);
//				et.setFocusableInTouchMode(true);
//				et.setOnTouchListener(new View.OnTouchListener() {
//					Intent InputIntent;
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						if (event.getAction() != MotionEvent.ACTION_UP) return false;
//						if (InputIntent == null) {
//							InputIntent = new Intent(activity.getContext(), net.tarnian.activity.Activity_InputDigits.class);
//							InputIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						}
//						try {
//							InputIntent.putExtra("value", ((EditText) v).getText().toString());
//							net.tarnian.activity.Activity_InputDigits.SecondEt = (EditText) v;
//							v.getContext().startActivity(InputIntent);
//						} catch (Exception ex) {
//							ShowMessage(ex.getMessage());
//						}
//						return false;
//					}
//				});
//			} else if (child instanceof ViewGroup) {
//				bindDigitInput(child);
//			}
//		}
//	}


    //		public static void setVisibility(int viewId, boolean visible) {
//			View v=getContext().vie
//			view.setVisibility(visible ? View.VISIBLE : View.GONE);
//		}
    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static boolean invertVisibility(View view) {
        return setVisibility(view, !isVisible(view));
    }

    public static List<View> getAllChilds(View root, Class<?> _class) {
        List<View> childs = new ArrayList<View>();
        List<View> result = new ArrayList<View>();
        if (root instanceof ViewGroup)
            childs.addAll(getAllChilds(root));
        for (View child : childs)
            if (child.getClass().equals(_class)) result.add(child);
        return result;
    }

    public static List<View> getAllChilds(View root) {
        ViewGroup viewGroup = (ViewGroup) root;
        List<View> childs = new ArrayList<View>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            childs.add(child);
            if (child instanceof ViewGroup)
                childs.addAll(getAllChilds(child));
        }
        return childs;
    }

    public static void releaseIamges(View root) {
        List<View> imageViews = getAllChilds(root, ImageView.class);
        for (View imageView : imageViews)
            ((ImageView) imageView).setImageBitmap(null);
    }

    public static void BindTouchToAllImageViews(ViewGroup viewGroup, View.OnTouchListener touchListener) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View nextChild = viewGroup.getChildAt(i);
            if (nextChild instanceof ImageView) {
                nextChild.setOnTouchListener(touchListener);

            } else if (nextChild instanceof ViewGroup) {
                BindTouchToAllImageViews((ViewGroup) nextChild, touchListener);
            }
        }
    }

    public static void BindClickToAllViews(View root, Class<?> type, View.OnClickListener onClickListener) {
        ViewGroup _root = (ViewGroup) root;
        int childCount = _root.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View nextChild = _root.getChildAt(i);
            if (type == null || nextChild.getClass().equals(type)) {
                nextChild.setOnClickListener(onClickListener);

            }
            if (nextChild instanceof ViewGroup) {
                BindClickToAllViews(nextChild, type, onClickListener);
            }
        }
    }

    public static void BindClickToAllImageViews(ViewGroup viewGroup, View.OnClickListener onClickListener) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View nextChild = viewGroup.getChildAt(i);
            if (nextChild instanceof ImageView) {
                nextChild.setOnClickListener(onClickListener);

            } else if (nextChild instanceof ViewGroup) {
                BindClickToAllImageViews((ViewGroup) nextChild, onClickListener);
            }
        }
    }

    public static void AddClicListenerToBtns(ViewGroup viewGroup, View.OnClickListener listener) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View nextChild = viewGroup.getChildAt(i);
            if (nextChild instanceof Button) {
                nextChild.setOnClickListener(listener);
            } else if (nextChild instanceof ViewGroup) {
                AddClicListenerToBtns((ViewGroup) nextChild, listener);
            }
        }
    }


    private static void show_toast(Context context, String msg) {
        TextView textView = new TextView(context);
        Font font = Font.getFont(context,Font.TextPos.h1);
        font.setColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.back_toast);
        Text.setText(textView, msg, font,1, false);
        Toast toast = new Toast(context);
        toast.setView(textView);
        toast.setGravity(Gravity.CENTER, 0, Display.getHeight(context) / 4);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showToast(Context context,String msg) {
        show_toast(context,Text.reshape(context,msg));
    }

    public static void showToast(Context context,int reId) {
        show_toast(context, Text.reshape(context,reId));
    }

    public static void ReshapeTextViews(View _root, Font font) {
        ViewGroup root = (ViewGroup) _root;
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View child = root.getChildAt(i);
            if (child instanceof TextView) {
                Text.setText(child, ((TextView) child).getText().toString(), font, false);
            } else if (child instanceof ViewGroup)
                ReshapeTextViews(child, font);
        }
    }
}
