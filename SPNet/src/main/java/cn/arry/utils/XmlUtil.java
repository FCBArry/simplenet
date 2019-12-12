package cn.arry.utils;

import cn.arry.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml工具
 */
public class XmlUtil {
    /**
     * 读取xml
     */
    public static Document readXml(String path) {
        try {
            SAXReader globalReader = new SAXReader();
            return globalReader.read(path);
        } catch (DocumentException e) {
            Log.error("XmlUtil->readXml error, path:{}", path, e);
            return null;
        }
    }

    /**
     * 读取int类型
     */
    public static int readAttrInt(Element element, String name, int defaultVal) {
        String result = element.attributeValue(name);
        if (result == null) {
            Log.info("the attribute '{}' of element is not exist, use default value:{}", name, defaultVal);
            return defaultVal;
        }

        return Integer.parseInt(result);
    }

    /**
     * 读取string类型
     */
    public static String readAttrStr(Element element, String name, String defaultVal) {
        String result = element.attributeValue(name);
        if (result == null) {
            Log.info("the attribute '{}' of element is not exist, use default value:{}", name, defaultVal);
            return defaultVal;
        }

        return result;
    }

    public static Boolean readElementBoolean(Element element, String name, boolean defaultVal) {
        String result = element.elementText(name);
        if (result == null) {
            Log.info("the element '{}' is not exist, use default value:{}", name, defaultVal);
            return defaultVal;
        }

        return Boolean.parseBoolean(result);
    }

    public static int readElementInt(Element element, String name, int defaultVal) {
        String result = element.elementText(name);
        if (result == null) {
            Log.info("the element '{}' is not exist, use default value:{}", name, defaultVal);
            return defaultVal;
        }

        return Integer.parseInt(result);
    }

    public static String readElementStr(Element element, String name, String defaultVal) {
        String result = element.elementText(name);
        if (result == null) {
            Log.info("the element '{}' is not exist, use default value:{}", name, defaultVal);
            return defaultVal;
        }

        return result;
    }
}
