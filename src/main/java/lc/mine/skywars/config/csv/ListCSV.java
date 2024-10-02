package lc.mine.skywars.config.csv;

import org.apache.commons.lang3.StringUtils;

public class ListCSV {

    public static String[] deserialize(final String input) {
        final int length = input.length();
        if (input.charAt(0) != '[' || input.charAt(length - 1) != ']') {
            return null;
        }
        final String substring = input.substring(1, length-1);
        final String[] variables = StringUtils.split(substring, ';');
        return variables;
    }
}
