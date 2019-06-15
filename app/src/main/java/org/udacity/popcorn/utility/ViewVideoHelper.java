package org.udacity.popcorn.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ViewVideoHelper {
    private static final String YOUTUBE_APPLICATION_BASE_LINK = "vnd.youtube:";
    private static final String YOUTUBE_BROWSER_BASE_LINK = "http://www.youtube.com/watch?v=";

    public static void watchYouTubeVideo(Context context, String id) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APPLICATION_BASE_LINK + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BROWSER_BASE_LINK + id));
            try {
                context.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(webIntent);
            }
        }

}
