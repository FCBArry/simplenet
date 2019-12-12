package cn.arry.meta;

/**
 * 字符串拆分meta
 */
public class SplitData {
    public String method;

    public int[] values;

    public String[] strValues;

    public SplitData(int n) {
        method = "";
        values = new int[n];
        strValues = new String[n];
    }
}
