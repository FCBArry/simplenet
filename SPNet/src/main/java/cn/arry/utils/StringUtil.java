package cn.arry.utils;

import cn.arry.Log;
import cn.arry.meta.SplitData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * string工具类
 */
public class StringUtil {
    private static final int[] EMPTY_INT_ARR = new int[0];

    private static final int[][] EMPTY_INT_ARR2 = new int[0][0];

    private static final float[] EMPTY_FLOAT_ARR = new float[0];

    private static final float[][] EMPTY_FLOAT_ARR2 = new float[0][0];

    private static final String[] EMPTY_STRING_ARR = new String[0];

    private static final String[][] EMPTY_STRING_ARR2 = new String[0][0];

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^-?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;,※и\\[\\].<>/?￥…（）—【】‘；：”“’。，、？\\s]";
        Matcher m = Pattern.compile(regEx).matcher(str);
        return m.replaceAll("").trim();
    }

    public static String[] splitString(String str) {
        if (str == null || str.trim().isEmpty() || str.trim().equals("-1"))
            return EMPTY_STRING_ARR;

        try {
            return str.split(",");
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitString error", e);
            return null;
        }
    }

    public static String[] splitString(String str, String spStr) {
        if (str == null || str.trim().isEmpty() || str.trim().equals("-1"))
            return EMPTY_STRING_ARR;

        try {
            return str.split(spStr);
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitString error", e);
            return null;
        }
    }

    public static String[][] splitString2(String str) {
        if (str == null || str.trim().isEmpty() || str.trim().equals("-1"))
            return EMPTY_STRING_ARR2;

        try {
            String[] str1 = str.split("\\|");
            int len1 = str1.length;
            int len2 = str1[0].split(",").length;
            String[] str2;
            String[][] strs = new String[len1][len2];
            for (int i = 0; i < len1; i++) {
                str2 = str1[i].split(",");
                System.arraycopy(str2, 0, strs[i], 0, len2);
            }

            return strs;
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitString2 error", e);
            return null;
        }
    }

    public static List<Integer> splitToList(String str) {
        return splitToList(str, ",");
    }

    public static List<Integer> splitToList(String str, String spStr) {
        List<Integer> resultList = new ArrayList<>();
        if (str == null || str.trim().length() == 0 || str.trim().equals("-1"))
            return resultList;

        try {
            String[] temps = str.split(spStr);
            for (String temp : temps) {
                resultList.add(Integer.parseInt(temp.trim()));
            }
        } catch (Exception e) {
            Log.error("splitToList error", e);
        }

        return resultList;
    }

    public static int[] splitToInt(String str) {
        return splitToInt(str, ",");
    }

    public static int[] splitToInt(String str, String spStr) {
        if (str == null || str.trim().isEmpty() || str.trim().equals("-1"))
            return EMPTY_INT_ARR;

        try {
            String[] temps = str.split(spStr);
            int len = temps.length;
            int[] results = new int[len];
            for (int i = 0; i < len; i++) {
                results[i] = Integer.parseInt(temps[i].trim());
            }

            return results;
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitToInt error", e);
            return EMPTY_INT_ARR;
        }
    }

    public static int[][] splitToInt2(String data) {
        return splitToInt2(data, "\\|", ",");
    }

    public static int[][] splitToInt2(String data, String spStr1, String spStr2) {
        if (data == null || data.trim().length() == 0 || data.trim().equals("-1"))
            return EMPTY_INT_ARR2;

        try {
            String[] strs = data.split(spStr1);
            int[][] result = new int[strs.length][];
            for (int i = 0; i < strs.length; i++) {
                result[i] = splitToInt(strs[i], spStr2);
                if (result[i] == null) {
                    return EMPTY_INT_ARR2;
                }
            }

            return result;
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitToInt2 error", e);
            return EMPTY_INT_ARR2;
        }
    }

    public static float[] splitToFloat(String str) {
        return splitToFloat(str, ",");
    }

    public static float[] splitToFloat(String str, String spStr) {
        if (str == null || str.trim().length() == 0 || str.trim().equals("-1"))
            return EMPTY_FLOAT_ARR;

        try {
            String[] temps = str.split(spStr);
            int len = temps.length;
            float[] results = new float[len];
            for (int i = 0; i < len; i++) {
                results[i] = Float.parseFloat(temps[i].trim());
            }

            return results;
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitToFloat error", e);
            return EMPTY_FLOAT_ARR;
        }
    }

    public static float[][] splitToFloat2(String data) {
        return splitToFloat2(data, "\\|", ",");
    }

    public static float[][] splitToFloat2(String data, String spStr1, String spStr2) {
        if (data == null || data.trim().length() == 0 || data.trim().equals("-1"))
            return EMPTY_FLOAT_ARR2;

        try {
            String[] strs = data.split(spStr1);
            int len = splitToFloat(strs[0], spStr2).length;
            float[][] result = new float[strs.length][len];
            for (int i = 0; i < strs.length; i++) {
                result[i] = splitToFloat(strs[i], spStr2);
                if (result[i] == null) {
                    return EMPTY_FLOAT_ARR2;
                }
            }

            return result;
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitToFloat2 error", e);
            return EMPTY_FLOAT_ARR2;
        }
    }

    public static Map<Integer, Integer> splitToIntIntMap(String str, String regex1, String regex2) {
        Map<Integer, Integer> map = new HashMap<>();
        try {
            String[] temps = str.split(regex1);
            for (String temp : temps) {
                try {
                    if (temp.isEmpty())
                        continue;

                    String[] data = temp.split(regex2);
                    if (data.length != 2)
                        continue;

                    int dataKey = Integer.valueOf(data[0]);
                    if (map.containsKey(dataKey)) {
                        int tmp = map.get(dataKey);
                        map.put(dataKey, Integer.valueOf(data[1]) + tmp);
                    } else {
                        map.put(dataKey, Integer.valueOf(data[1]));
                    }
                } catch (Exception e) {
                    Log.error("StringSplitUtil->splitToIntIntMap error", e);
                }
            }
        } catch (Exception e) {
            Log.error("StringSplitUtil->splitToIntIntMap error", e);
        }

        return map;
    }

    public static String concatToStr(int[] ints) {
        if (ints == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int anInt : ints) {
            sb.append(anInt).append(",");
        }

        if (sb.length() == 0) {
            return null;
        }

        return sb.substring(0, sb.length() - 1);
    }

    public static String concatToStr(Object... args) {
        StringBuilder sb = new StringBuilder();
        if (args != null) {
            for (Object object : args) {
                sb.append(object).append(",");
            }
        }

        if (sb.length() > 0)
            return sb.substring(0, sb.length() - 1);
        else
            return "";
    }

    public static SplitData splitMethod(String str) {
        return splitMethod(str, 0);
    }

    public static SplitData splitMethod(String str, int type) {
        if (str.equalsIgnoreCase("0"))
            return null;

        if (!str.matches("[-0-9(:,)a-zA-Z| ]+"))
            Log.warn("StringSplitUtil->splitMethod warn, {} not match", str);

        String[] temp = type == 0 ? str.split("[(,)]") : str.split("[(|)]");
        if (temp.length < 1)
            return null;

        SplitData data = new SplitData(temp.length - 1);
        data.method = temp[0].trim();
        for (int i = 1; i < temp.length; i++) {
            if (!temp[i].isEmpty()) {
                try {
                    if (isNumeric(temp[i].trim()))
                        data.values[i - 1] = Integer.valueOf(temp[i].trim());
                } catch (Exception e) {
                    Log.error("StringSplitUtil->splitMethod error, str:{} idx:{} value:{}", str, i, temp[i]);
                }

                data.strValues[i - 1] = temp[i].trim();
            }
        }

        return data;
    }

    public static boolean checkRange(String limit, int value) {
        return checkRange(splitToInt(limit), value);
    }

    public static boolean checkRange(int[] limit, int value) {
        return limit.length == 2 && limit[0] <= value && value <= limit[1];
    }
}
