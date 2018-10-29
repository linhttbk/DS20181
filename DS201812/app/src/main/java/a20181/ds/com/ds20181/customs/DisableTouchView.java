package a20181.ds.com.ds20181.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DisableTouchView extends RelativeLayout {
    public DisableTouchView(Context context) {
        super(context);
    }

    public DisableTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
