package tech.bootloader.androidbasic.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatUtils {

    /**
     * 强制两位小数
     *
     * @param number 不允许为null
     * @return
     */
    public static String forciblyFormat(@NonNull String number) {
        return formatTwoDecimalPlaces(number, true, false);
    }

    /**
     * 有小数显示小数 无小数显示整数
     *
     * @param number
     * @return
     */
    public static String formatTwoDecimalPlaces(@NonNull String number) {
        return formatTwoDecimalPlaces(number, true, true);
    }

    /**
     * @param number
     * @param format    是否格式化
     * @param removeDot 格式化是否移除小数点
     * @return
     */
    public static String formatTwoDecimalPlaces(@NonNull String number, boolean format, boolean removeDot) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        if (format) {
            try {
                double v = Double.parseDouble(number);
                BigDecimal bg = new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                double num = bg.doubleValue();
                if (removeDot && Math.round(num) - num == 0) {
                    return String.valueOf((long) num);
                }
                DecimalFormat df = new DecimalFormat("0.00");
                return df.format(num);
            } catch (Exception e) {
                throw e;
            }
        } else {
            return number;
        }


    }

    public static String formatName(@NonNull String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        if (name.length() == 11) {
            return name.substring(0, 3) + "****" + name.substring(7);
        } else {
            return name.substring(0, 1) + "****" + name.substring(name.length() - 1);
        }

    }

}
