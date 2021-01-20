package tech.bootloader.androidbasic.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;

import java.util.ArrayList;
import java.util.Calendar;

public class SetAlarm {
    public static void createAlarm(Context context, String message, int hour, int minutes, boolean everDay, int resId) {
        ArrayList<Integer> testDays = new ArrayList<>();
        testDays.add(Calendar.MONDAY);//周一
        testDays.add(Calendar.TUESDAY);//周二
        testDays.add(Calendar.WEDNESDAY);//周三
        testDays.add(Calendar.THURSDAY);//周四
        testDays.add(Calendar.FRIDAY);//周五
        testDays.add(Calendar.SATURDAY);//周六
        testDays.add(Calendar.SUNDAY);//周日

        String packageName = context.getPackageName();

        //action为AlarmClock.ACTION_SET_ALARM
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                //闹钟的小时
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                //闹钟的分钟
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                //响铃时提示的信息
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                //用于指定该闹铃触发时是否振动
                .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                //如果为true，则调用startActivity()不会进入手机的闹钟设置界面
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (everDay) {
            //对于一次性闹铃，无需指定此 extra
            //一个 ArrayList，其中包括应重复触发该闹铃的每个周日。
            // 每一天都必须使用 Calendar 类中的某个整型值（如 MONDAY）进行声明。
            intent.putExtra(AlarmClock.EXTRA_DAYS, testDays);
        }

        if (resId != 0) {
            //一个 content: URI，用于指定闹铃使用的铃声，也可指定 VALUE_RINGTONE_SILENT 以不使用铃声。
            Uri ringtoneUri = Uri.parse("android.resource://" + packageName + "/" + resId);
            //如需使用默认铃声，则无需指定此 extra。
            intent.putExtra(AlarmClock.EXTRA_RINGTONE, ringtoneUri);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (intent.resolveActivity(context.getPackageManager()) != null) {
//            context.startActivity(intent);
//        }
    }

}
