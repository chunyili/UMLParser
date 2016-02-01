package edu.sjsu.chunyi.util;

import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.metaInfo.FieldMeta;
import edu.sjsu.chunyi.metaInfo.MethodMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jilongsun on 10/21/15.
 */
public class Uml {

    public void drawUMl() {

        System.out.println(MapAndSet.classSet + " classSet");
        for (Map.Entry<String, String> entry : MapAndSet.associateMap.entrySet())

        {
            String[] associKeys = entry.getKey().split(",");
            String[] associValues = entry.getValue().split(":");
            if (MapAndSet.classSet.contains(associKeys[1])) {


                MapAndSet.umlStringBuilder.append(associKeys[0] + "\"" + associValues[0] + "\"" + Arrows.association + "\"" + associValues[1] + "\"" + associKeys[1] + "\n");

            }
        }
        for (Map.Entry<String, String> entry : MapAndSet.interfaceMap.entrySet())

        {
            String[] UIkeys = entry.getKey().split(",");

            String UIValue = entry.getValue();

            MapAndSet.umlStringBuilder.append("class " + UIkeys[1] + "\n" + "Interface " + UIkeys[0] + "<<Interface>>" + UIValue + " " + UIkeys[1] + "\n");


        }

        for (Map.Entry<String, String> entry : MapAndSet.usesMap.entrySet())

        {
            String[] UIkeys = entry.getKey().split(",");

            String UIValue = entry.getValue();
            if (MapAndSet.interfaceSet.contains(UIkeys[0]) && !MapAndSet.interfaceSet.contains(UIkeys[1])) {

                MapAndSet.umlStringBuilder.append("class " + UIkeys[1] + "\n" + "Interface " + UIkeys[0] + "<<Interface>>" + UIValue + " " + UIkeys[1]  + "\n");

            }
        }

        for (Map.Entry<String, Set<String>> entry : MapAndSet.parameterMap.entrySet())

        {
            String UIkey = entry.getKey();
            String[] UIkeys = UIkey.split(",");


            Set<String> UIValue = entry.getValue();
            int limit = UIValue.size();
            int number = 0;


            System.out.println(MapAndSet.interfaceSet + " is interfaceset");
            if (!MapAndSet.interfaceSet.contains(UIkeys[1])) {

                if (UIkeys[0].contains("public")) {
                    if (UIkeys[0].contains("static")) {
                        MapAndSet.umlStringBuilder.append("class " + UIkeys[1] + "{ \n")
                                .append("+ {static}" + UIkeys[2] + "(");
                    } else {
                        MapAndSet.umlStringBuilder.append("class " + UIkeys[1] + "{ \n")
                                .append("+" + UIkeys[2] + "(");
                    }
                    for (String string : UIValue) {
                        String[] strings = string.split(" ");
                        if (number == limit - 1) {
                            MapAndSet.umlStringBuilder.append(strings[0] + " : " + strings[1]);
                        } else if (number < limit - 1) {
                            MapAndSet.umlStringBuilder.append(strings[0] + " : " + strings[1] + ",");
                        }
                        number++;
                    }


                    MapAndSet.umlStringBuilder.append(") " + " : " + UIkeys[3] + "\n" + "}" + "\n");
                }
            } else if (MapAndSet.interfaceSet.contains(UIkeys[1])) {


                MapAndSet.umlStringBuilder.append("Interface " + UIkeys[1] + "<<Interface>>" + "{ \n")
                        .append("+" + UIkeys[2] + "(");
                for (String string : UIValue) {
                    String[] strings = string.split(" ");
                    if (number == limit - 1) {
                        MapAndSet.umlStringBuilder.append(strings[0] + " : " + strings[1]);
                    } else if (number < limit - 1) {
                        MapAndSet.umlStringBuilder.append(strings[0] + " : " + strings[1] + ",");
                    }
                    number++;
                }


                MapAndSet.umlStringBuilder.append(") " + " : " + UIkeys[3] + "\n" + "}" + "\n");


            }
        }


        for (String name : MapAndSet.classMetaMap.keySet()) {
            Map<String, String> localMap = new HashMap<String, String>();

            String localKey;

            ClassMeta currentMeta = MapAndSet.classMetaMap.get(name);


            if (currentMeta.extendList != null) {
                for (String string : currentMeta.extendList) {
                    MapAndSet.umlStringBuilder.append(string + Arrows.entends + name).append("\n");
                }
            }

            if (currentMeta.constructorMetas.size() != 0) {

                for (int i = 0; i < currentMeta.constructorMetas.size(); i++) {
                    System.out.println(currentMeta.constructorMetas.get(i).constructorName + " is constructor name");

                    if (currentMeta.constructorMetas.get(i).parameterList.size() != 0) {


                        String consParaKey = currentMeta.constructorMetas.get(i).constructorName;

                        if (MapAndSet.constructorMap.containsKey(consParaKey) && currentMeta.constructorMetas.get(i).constructorModifier.equals("public")) {
                            Set<String> set = MapAndSet.constructorMap.get(consParaKey);
                            int limit = set.size();
                            int start = 0;

                            MapAndSet.umlStringBuilder.append("class " + name + "{ \n")
                                    .append("+" + currentMeta.constructorMetas.get(i).constructorName + "(");
                            for (String paraString : set) {
                                String[] paraStrings = paraString.split(" ");


                                if (start == limit - 1) {
                                    MapAndSet.umlStringBuilder.append(paraStrings[1] + " : " + paraStrings[0]);
                                } else if (start < limit - 1) {
//
                                    MapAndSet.umlStringBuilder.append(paraStrings[1] + " : " + paraStrings[0] + " , ");
                                }
                                start++;


                            }
                            MapAndSet.umlStringBuilder.append(") " + "\n" + "}" + "\n");


                        }
                    } else {
                        System.out.println("to check the ecnomy");
                        if (currentMeta.constructorMetas.get(i).constructorModifier.equals("public")) {
                            MapAndSet.umlStringBuilder.append("class " + name + "{ \n")
                                    .append("+" + currentMeta.constructorMetas.get(i).constructorName + "() \n")
                                    .append("} \n");
                        }
                    }
                }

            }
            if (currentMeta.fieldMetasMap.size() != 0) {
                for (String string : currentMeta.fieldMetasMap.keySet()) {

                    FieldMeta fieldMeta = currentMeta.fieldMetasMap.get(string);
                    String attriType = fieldMeta.attriType;
                    String modifier = fieldMeta.attriAccessModifier;
                    String name1;
                    if (attriType.contains("<")) {
                        int a = attriType.indexOf("<");
                        int b = attriType.indexOf(">");
                        name1 = attriType.substring(a + 1, b);

                    } else {
                        name1 = attriType;

                    }

                    if (!MapAndSet.classSet.contains(name1)) {
                        if (!MapAndSet.interfaceSet.contains(string) && (modifier.equals("public") ||
                                (fieldMeta.isGetPublic && fieldMeta.isSetPublic))) {
                            MapAndSet.umlStringBuilder.append("class " + name + "{ \n")
                                    .append("+" + string + ":" + attriType + "\n")
                                    .append("}  \n ");
                        } else if (!MapAndSet.interfaceSet.contains(string) && modifier.equals("private")) {
                            MapAndSet.umlStringBuilder.append("class " + name + "{ \n")
                                    .append("-" + string + ":" + attriType + "\n")
                                    .append("}  \n ");
                        }
                    }
                }
            }


            if (currentMeta.methodMetas.size() != 0) {
                for (int i = 0; i < currentMeta.methodMetas.size(); i++) {
                    MethodMeta methodMeta = currentMeta.methodMetas.get(i);
                    String modifier = methodMeta.methodAccessModifier;
                    String names = methodMeta.methodName;
                    String returnType = methodMeta.methodReturnType;


                    if (methodMeta.parameterName == null || methodMeta.parameterName.size() == 0) {
                        if (!MapAndSet.interfaceSet.contains(name)) {
                            if (modifier.equals("public")) {
                                MapAndSet.umlStringBuilder.append("class " + name + "{ \n" + "+" + names + "()")
                                        .append(":" + returnType + "\n").append("} \n");
                            }
                        } else {
                            MapAndSet.umlStringBuilder.append("Interface " + name + "<<Interface>>" + "{ \n" + "+" + names + "() ")
                                    .append(":" + returnType + "\n").append("} \n");
                        }
                    }
                }

            }
        }
    }
}