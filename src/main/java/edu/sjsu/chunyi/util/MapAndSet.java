package edu.sjsu.chunyi.util;

import edu.sjsu.chunyi.metaInfo.ClassMeta;

import java.util.*;

/**
 * Created by jilongsun on 10/20/15.
 */
public class MapAndSet {

    public static List<String> classes = new ArrayList<String>();

    public static StringBuilder umlStringBuilder = new StringBuilder();
    public static Map<String, ClassMeta> classMetaMap = new HashMap<String, ClassMeta>();


    public static Map<String, String> associateMap = new HashMap<String, String>();
    public static Map<String, String> usesMap = new HashMap<String, String>();
    public static Map<String, String> interfaceMap = new HashMap<String, String>();
    public static Set<String> interfaceSet = new HashSet<String>();
    public static Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
    public static Map<String, Set<String>> constructorMap = new HashMap<String, Set<String>>();
    public static Set<String> memberSet = new HashSet<String>();
    public static Set<String> classSet = new HashSet<String>();
    public static boolean isOne = false;

    public static String className, anotherClassName, key, usesOrInterfaceKey, interfaceName;
    public static boolean isInterface = false;

    public static String update(String v1, String v2) {
        if (v1.equals("1")) {
            return v2;
        } else {
            return v1;
        }
    }

}
