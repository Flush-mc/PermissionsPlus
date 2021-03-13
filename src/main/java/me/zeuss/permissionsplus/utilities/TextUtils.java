package me.zeuss.permissionsplus.utilities;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextUtils {

    public static String formatText(String text) {
        return formatText(text, false);
    }

    public static String formatText(String text, boolean capitalize) {
        if (text == null || text.equals(""))
            return "";
        if (capitalize)
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> formatText(List<String> list) {
        return list.stream().map(TextUtils::formatText).collect(Collectors.toList());
    }

    public static String removeColors(String msg) {
        return msg.replace("§", "&").replace("&o", "").replace("&0", "")
                .replace("&1", "").replace("&2", "").replace("&3", "")
                .replace("&4", "").replace("&5", "").replace("&6", "")
                .replace("&7", "").replace("&8", "").replace("&9", "")
                .replace("&a", "").replace("&b", "").replace("&c", "")
                .replace("&d", "").replace("&e", "").replace("&f", "")
                .replace("&6", "").replace("&k", "").replace("&m", "")
                .replace("&n", "").replace("&r", "");
    }

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean containsIgnoreCase(String find, String... param) {
        Stream<String> result = Arrays.stream(param).filter(p -> p.toLowerCase().contains(find.toLowerCase()));
        return result.findFirst().isPresent();
    }

    public static String formatEconomy(double amt) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String format = formatter.format(amt);
        if (format.startsWith(",")) {
            format = "0,00";
        }
        return format;
    }

    public static String upperCaseFirst(String input) {
        return WordUtils.capitalize(input);
    }

    public static String upperCaseAllFirst(String input) {
        return WordUtils.capitalizeFully(input);
    }

    public static String toRoman(int input) {
        if (input < 1 || input > 3999) {
            return "Invalid Roman Number Value";
        }
        StringBuilder s = new StringBuilder();
        while (input >= 1000) {
            s.append("M");
            input -= 1000;
        }
        while (input >= 900) {
            s.append("CM");
            input -= 900;
        }
        while (input >= 500) {
            s.append("D");
            input -= 500;
        }
        while (input >= 400) {
            s.append("CD");
            input -= 400;
        }
        while (input >= 100) {
            s.append("C");
            input -= 100;
        }
        while (input >= 90) {
            s.append("XC");
            input -= 90;
        }
        while (input >= 50) {
            s.append("L");
            input -= 50;
        }
        while (input >= 40) {
            s.append("XL");
            input -= 40;
        }
        while (input >= 10) {
            s.append("X");
            input -= 10;
        }
        while (input >= 9) {
            s.append("IX");
            input -= 9;
        }
        while (input >= 5) {
            s.append("V");
            input -= 5;
        }
        while (input >= 4) {
            s.append("IV");
            input -= 4;
        }
        while (input >= 1) {
            s.append("I");
            --input;
        }
        return s.toString();
    }

}

