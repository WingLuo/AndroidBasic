package tech.bootloader.androidbasic.collector;

import android.app.Activity;


import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void romoveActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();

        }
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();

            }
        }
    }

//    public static void finishAllRuleOutMain() {
//        for (Activity activity : activities) {
//            if (activity != null && !activity.isFinishing() && !(activity instanceof MainActivity)) {
//                activity.finish();
//
//            }
//        }
//    }

    public static Activity getTopActivity() {
        if (activities.isEmpty()) {
            return null;
        }
        return activities.get(activities.size() - 1);
    }

    public static <T extends Activity> void stayMainActivity(Class<T> clazz) {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                if (activity.getClass() != clazz) {
                    activity.finish();
                }

            }


        }
    }

}