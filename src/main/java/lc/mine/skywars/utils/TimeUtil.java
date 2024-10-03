package lc.mine.skywars.utils;

public class TimeUtil {

    public static String getMinutesAndSeconds(final int seconds) {
        final int S = seconds % 60;
        final int M = seconds / 60 % 60;

        return M + ":" + ((S < 9) ? "0"+S : S);
    }
}
