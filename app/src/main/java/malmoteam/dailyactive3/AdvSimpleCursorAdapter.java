package malmoteam.dailyactive3;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Amer on 24-Mar-16.
 * */

public class AdvSimpleCursorAdapter extends SimpleCursorAdapter {
    protected int[] mTo;
    protected int[] mFrom;

    String[] mOriginalFrom;
/*
    @Deprecated
    public AdvSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
    }
*/

    public AdvSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
    }

    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewBinder binder = getViewBinder();
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {
                    String text = cursor.getString(from[i]);
                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    } else if (v instanceof RelativeLayout) {
                        RelativeLayout rl = (RelativeLayout) v;
                        switch (text) {
                            case "1":
                                rl.setBackgroundColor(Color.RED);
                                break;
                            case "2":
                                rl.setBackgroundColor(Color.YELLOW);
                                break;
                            case "3":
                                rl.setBackgroundColor(Color.GREEN);
                                break;
                            case "4":
                                rl.setBackgroundColor(Color.BLUE);
                                break;
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
        }
    }

}
