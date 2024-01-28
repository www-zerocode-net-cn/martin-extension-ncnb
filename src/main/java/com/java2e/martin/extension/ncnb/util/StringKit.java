package com.java2e.martin.extension.ncnb.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 狮少
 * @version 1.0
 * @date 2021/4/26
 * @describtion StringKit
 * @since 1.0
 */
public abstract class StringKit extends StringUtils {
    public StringKit() {
    }

    public static String nvl(Object s, String s1) {
        String ss = "";
        if (s instanceof String) {
            ss = (String)s;
        } else {
            if (s == null) {
                return s1;
            }

            ss = String.valueOf(s);
        }

        return StringUtils.isEmpty(ss) ? s1 : ss;
    }

    public static String decodeKeyValue(String expression, String... args) {
        if (args != null && args.length != 0) {
            if (args.length % 2 != 1) {
                throw new IllegalArgumentException("调用StringKit.decodeKeyValue方法，参数个数必需为奇数个");
            } else {
                for(int i = 0; i < args.length; i += 2) {
                    if (args[i].equals(expression)) {
                        return args[i + 1];
                    }
                }

                return args[args.length - 1];
            }
        } else {
            throw new IllegalArgumentException("调用StringKit.decodeKeyValue方法，参数为空");
        }
    }

    public static String join(String[] strList, String delimiter) {
        return join(Arrays.asList(strList), delimiter);
    }

    public static String join(CharSequence... elements) {
        return join8(elements, "");
    }

    public static String join8(CharSequence[] array, String separator) {
        StringJoiner joiner = new StringJoiner(separator);
        CharSequence[] var3 = array;
        int var4 = array.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            CharSequence object = var3[var5];
            joiner.add(object);
        }

        return joiner.toString();
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String ltrim(String str) {
        return str == null ? null : str.replaceAll("^\\\\s+", "");
    }

    public static String rtrim(String str) {
        return str == null ? null : str.replaceAll("\\\\s+$", "");
    }

    public static String clearSpace(String str) {
        return str == null ? null : str.replaceAll("\\\\s+", "");
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection == null ? null : (String[])collection.toArray(new String[collection.size()]);
    }

    public static String chomp(String str) {
        if (str != null && str.length() != 0) {
            if (str.length() == 1) {
                char ch = str.charAt(0);
                return ch != '\r' && ch != '\n' ? str : "";
            } else {
                int lastIdx = str.length() - 1;
                char last = str.charAt(lastIdx);
                if (last == '\n') {
                    if (str.charAt(lastIdx - 1) == '\r') {
                        --lastIdx;
                    }
                } else if (last != '\r') {
                    ++lastIdx;
                }

                return str.substring(0, lastIdx);
            }
        } else {
            return str;
        }
    }

    public static String chop(String str) {
        if (str == null) {
            return null;
        } else {
            int strLen = str.length();
            if (strLen < 2) {
                return "";
            } else {
                int lastIdx = strLen - 1;
                String ret = str.substring(0, lastIdx);
                char last = str.charAt(lastIdx);
                return last == '\n' && ret.charAt(lastIdx - 1) == '\r' ? ret.substring(0, lastIdx - 1) : ret;
            }
        }
    }

    public static String format(String tpl, Object... args) {
        return MessageFormat.format(tpl, args);
    }

    public static String camelToUnderline(String property) {
        return camelTo(property, "_");
    }

    public static String camelTo(String property, String delimiter) {
        if (isBlank(property)) {
            return property;
        } else {
            char[] chars = property.toCharArray();
            StringBuffer sb = new StringBuffer();
            char[] var4 = chars;
            int var5 = chars.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                char c = var4[var6];
                if (CharUtils.isAsciiAlphaUpper(c)) {
                    sb.append(delimiter + StringUtils.lowerCase(CharUtils.toString(c)));
                } else {
                    sb.append(c);
                }
            }

            String ret = sb.toString();
            ret = ret.replaceAll(delimiter + "+", delimiter);
            ret = removeStart(ret, delimiter);
            ret = removeEnd(ret, delimiter);
            ret = ret.toUpperCase();
            return ret;
        }
    }

    public static String underlineToCamel(String str) {
        return toCamel("_", str);
    }

    public static String toCamel(String delimiter, String str) {
        if (isBlank(str)) {
            return str;
        } else {
            String[] strs = split(str, delimiter);

            for(int i = 0; i < strs.length; ++i) {
                strs[i] = lowerCase(strs[i]);
                if (i > 0) {
                    strs[i] = capitalize(strs[i]);
                }
            }

            return join(strs);
        }
    }

    public static String fillTpl(String tpl, Map<String, String> vars) {
        Set<Map.Entry<String, String>> sets = vars.entrySet();

        Map.Entry entry;
        Matcher matcher;
        for(Iterator var3 = sets.iterator(); var3.hasNext(); tpl = matcher.replaceAll((String)entry.getValue())) {
            entry = (Map.Entry)var3.next();
            String regex = "\\$\\{" + (String)entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            matcher = pattern.matcher(tpl);
        }

        return tpl;
    }

    public static String uuid() {
        return uuid("");
    }

    public static String uuid(String delimiter) {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", delimiter);
        return uuid;
    }

    public static String maxLenLimit(String str, int len) {
        if (isBlank(str)) {
            return str;
        } else {
            return str.length() > len ? str.substring(0, len) : str;
        }
    }

    public static String chineseToUnicode(String cnStr) {
        try {
            StringBuffer out = new StringBuffer("");
            byte[] bytes = cnStr.getBytes("unicode");

            for(int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 255);

                for(int j = str.length(); j < 2; ++j) {
                    out.append("0");
                }

                String str1 = Integer.toHexString(bytes[i] & 255);
                out.append(str1);
                out.append(str);
            }

            return out.toString();
        } catch (UnsupportedEncodingException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static String unicodeToChinese(String unicodeStr) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unicodeStr);

        String cnStr;
        char ch;
        String group1;
        for(cnStr = unicodeStr; matcher.find(); cnStr = cnStr.replace(group1, ch + "")) {
            String group = matcher.group(2);
            ch = (char)Integer.parseInt(group, 16);
            group1 = matcher.group(1);
        }

        return cnStr.replace("\\", "").trim();
    }
}
