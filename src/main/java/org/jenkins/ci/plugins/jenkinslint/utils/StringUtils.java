package org.jenkins.ci.plugins.jenkinslint.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Victor Martinez
 */
public class StringUtils {

    public static boolean isEmptyOrBlank (String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s+");
        Matcher m = p.matcher(line);
        found = m.matches();
        return found || line.isEmpty();
    }

    public static boolean isShellComment(String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s*#\\s*.*");
        Matcher m = p.matcher(line);
        found = m.matches();
        return found;
    }

    public static boolean isBatchComment(String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s*REM\\s*.*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(line);
        found = m.matches();
        return found;
    }

    public static boolean isH (String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s*h.*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(line);
        found = m.matches();
        return found;
    }

    public static boolean isAt (String line) {
        boolean found = false;
        Pattern p = Pattern.compile("^\\s*@.*");
        Matcher m = p.matcher(line);
        found = m.matches();
        return found;
    }
}
