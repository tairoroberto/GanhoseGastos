package br.com.trmasolucoes.ganhosegastos.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by tairo on 07/04/15.
 *
 */
public class DisplayUtil {

    public static String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }

    public static String getScreenOrientation(Activity activity)
    {
        Display display = ((WindowManager) activity.getSystemService(activity.WINDOW_SERVICE))
                .getDefaultDisplay();
        int orientation = display.getRotation();

        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            return "landscape";
        }else {
            return "portrait";
        }
    }
}
