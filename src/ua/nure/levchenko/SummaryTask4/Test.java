package ua.nure.levchenko.SummaryTask4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        Pattern patternPhone = Pattern.compile("(\\+?((\\s?\\d+)($)?)+$)");
        Matcher matcherPhone = patternPhone.matcher("+380945455");

        if (matcherPhone.find()) {
            System.out.println(matcherPhone.group());
        }

    }
}
