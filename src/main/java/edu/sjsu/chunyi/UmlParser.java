package edu.sjsu.chunyi;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import static java.lang.System.out;

import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.metaInfo.FieldMeta;
import edu.sjsu.chunyi.util.Arrows;
import edu.sjsu.chunyi.util.MapAndSet;
import edu.sjsu.chunyi.util.ReadFiles;
import edu.sjsu.chunyi.util.Uml;
import edu.sjsu.chunyi.visitors.*;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by jilongsun on 10/19/15.
 */
public class UmlParser {


    public static void main(String args[]) throws Exception {

        String classPath = args[0];
        String outPutFile = args[0] + "/" + args[1] + ".png";


        final File file = new File(classPath);
        ReadFiles.listFilesForFolder(file);

        MapAndSet.umlStringBuilder.setLength(0);
        MapAndSet.umlStringBuilder.append("@startuml" + "\n");
        MapAndSet.umlStringBuilder.append("skinparam classAttributeIconSize 0 \n");
        MapAndSet.umlStringBuilder.append("title Chunyi Li 010052341 \n");

        for (int i = 0; i < MapAndSet.classes.size(); i++) {
            if (MapAndSet.classes.get(i).endsWith(".class")) {

                String str = MapAndSet.classes.get(i).toString();
                String[] strings = str.split("\\.");
                String name = strings[0];
                MapAndSet.classSet.add(name);
                ClassMeta currentMeta;
                FieldMeta fieldMeta;


                if (MapAndSet.classMetaMap.containsKey(name)) {
                    currentMeta = MapAndSet.classMetaMap.get(name);
                } else {
                    currentMeta = new ClassMeta();
                    currentMeta.classNames = name;
                }

                try {
                    URL url = file.toURI().toURL();
                    URL[] urls = new URL[]{url};

                    ClassLoader loader = new URLClassLoader(urls);

                    Class cls = loader.loadClass(name);

                    boolean found = false;

                    Field[] flds = cls.getDeclaredFields();

                    for (Field f : flds) {


                        Class<?> c = f.getType();
                        String fieldMetaKey = f.getName();


                        Type type = f.getGenericType();

                        if (type instanceof ParameterizedType) {

                            ParameterizedType pType = ((ParameterizedType) type);


                            String typeName = pType.getRawType().getTypeName();


                            Class typeClass = Class.forName(typeName);


                            if (Collection.class.isAssignableFrom(typeClass) || typeClass.equals(Collection.class)) {


                                String actualType = ((ParameterizedType) type).getActualTypeArguments()[0].toString();


                                String[] a = actualType.toString().split(" ");
                                String name2 = a[1];


                                System.out.println(name + " " + name2 + "  is name 2");
                                if (currentMeta.fieldMetasMap.containsKey(fieldMetaKey)) {
//
                                    fieldMeta = currentMeta.fieldMetasMap.get(fieldMetaKey);
                                } else {
                                    fieldMeta = new FieldMeta();

                                }

                                fieldMeta.trueType = name2;

                                fieldMeta.attriAccessModifier = Modifier.toString(f.getModifiers());
                                currentMeta.fieldMetasMap.put(fieldMetaKey, fieldMeta);

//
                                String key;
                                String value;
                                Map<String, String> localMap = new HashMap<String, String>();
                                localMap.put(name, " ");
                                localMap.put(name2, "*");
                                if (name.compareTo(name2) < 0) {
                                    key = name + "," + name2;
                                } else {
                                    key = name2 + "," + name;
                                }

                                System.out.println(key +  " is a key");


                                String[] keys = key.split(",");
                                if (MapAndSet.associateMap.containsKey(key)) {
                                    value = MapAndSet.associateMap.get(key);


                                    String[] values = value.split(":");
                                    values[0] = MapAndSet.update(values[0], localMap.get(name));
                                    values[1] = MapAndSet.update(values[1], localMap.get(name2));
                                    value = values[0] + ":" + values[1];
                                    MapAndSet.associateMap.put(key, value);

                                } else {
                                    value = localMap.get(keys[0]) + ":" + localMap.get(keys[1]);
                                    MapAndSet.associateMap.put(key, value);

                                }
//                                }

                            } else if (Map.class.isAssignableFrom(typeClass) || typeClass.equals(Map.class)) {
                                for (int j = 0; j < ((ParameterizedType) type).getActualTypeArguments().length; j++) {
                                    String a = ((ParameterizedType) type).getActualTypeArguments()[j].toString();

                                    String[] strings1 = a.split(" ");
                                    String actualName = strings1[1];

                                    if (currentMeta.fieldMetasMap.containsKey(fieldMetaKey)) {
//
                                        fieldMeta = currentMeta.fieldMetasMap.get(fieldMetaKey);
                                    } else {
                                        fieldMeta = new FieldMeta();

                                    }

                                    fieldMeta.trueType = actualName;

                                    fieldMeta.attriAccessModifier = Modifier.toString(f.getModifiers());
                                    currentMeta.fieldMetasMap.put(fieldMetaKey, fieldMeta);


                                    String key;
                                    String value;
                                    Map<String, String> localMap = new HashMap<String, String>();
                                    localMap.put(name, " ");
                                    localMap.put(actualName, "*");
                                    if (name.compareTo(actualName) < 0) {
                                        key = name + "," + actualName;
                                    } else {
                                        key = actualName + "," + name;
                                    }


//                                        String[] keys = key.split(",");
                                    if (MapAndSet.associateMap.containsKey(key)) {
                                        value = MapAndSet.associateMap.get(key);


                                        String[] values = value.split(":");
                                        values[0] = MapAndSet.update(values[0], localMap.get(name));
                                        values[1] = MapAndSet.update(values[1], localMap.get(actualName));
                                        value = values[0] + ":" + values[1];
                                        MapAndSet.associateMap.put(key, value);

                                    } else {
                                        value = localMap.get(name) + ":" + localMap.get(actualName);
                                        MapAndSet.associateMap.put(key, value);

                                    }
                                }


                            }


                        } else {
                            if (currentMeta.fieldMetasMap.containsKey(fieldMetaKey)) {
//
                                fieldMeta = currentMeta.fieldMetasMap.get(fieldMetaKey);
                            } else {
                                fieldMeta = new FieldMeta();

                            }
                            fieldMeta.trueType = type.getTypeName();
                            currentMeta.fieldMetasMap.put(fieldMetaKey, fieldMeta);
                        }


                        if (c.isArray()) {
                            found = true;
                            String componentType = c.getComponentType().toString();

                            String[] componentTypes = componentType.split(" ");


                            if (componentTypes.length > 1) {
                                String key;
                                String value;
                                Map<String, String> localMap = new HashMap<String, String>();
                                localMap.put(name, " ");
                                localMap.put(componentTypes[1], "*");

                                if (name.compareTo(componentTypes[1]) < 0) {
                                    key = name + "," + componentTypes[1];
                                } else {
                                    key = componentTypes[1] + "," + name;
                                }

                                String[] keys = key.split(",");
                                if (MapAndSet.associateMap.containsKey(key)) {
                                    value = MapAndSet.associateMap.get(key);


                                    String[] values = value.split(":");
                                    values[0] = MapAndSet.update(values[0], localMap.get(keys[0]));
                                    values[1] = MapAndSet.update(values[1], localMap.get(keys[1]));
                                    value = values[0] + ":" + values[1];
                                    MapAndSet.associateMap.put(key, value);

                                } else {
                                    value = localMap.get(keys[0]) + ":" + localMap.get(keys[1]);
                                    MapAndSet.associateMap.put(key, value);

                                }
                            }

                        }

                        if (!found) {
                            System.out.println("Array is not found");
                        }

                    }
                } catch (MalformedURLException e) {
                } catch (ClassNotFoundException e) {
                }

                MapAndSet.classMetaMap.put(name, currentMeta);


            } else if (MapAndSet.classes.get(i).endsWith(".java")) {
                MapAndSet.className = MapAndSet.classes.get(i).split("\\.")[0];
                MapAndSet.classSet.add(MapAndSet.className);


                ClassMeta currentMeta;
                if (MapAndSet.classMetaMap.containsKey(MapAndSet.className)) {
                    currentMeta = MapAndSet.classMetaMap.get(MapAndSet.className);
                } else {
                    currentMeta = new ClassMeta();
                    currentMeta.classNames = MapAndSet.className;
                }


                FileInputStream in = new FileInputStream(classPath + "/" + MapAndSet.classes.get(i));


                CompilationUnit cu;

                try {
                    // parse the file
                    cu = JavaParser.parse(in);
                } finally {
                    in.close();
                }

                // visit and print the methods names
                ClassVisitor classVisitor = new ClassVisitor();
                classVisitor.setCurrentMeta(currentMeta);
                classVisitor.visit(cu, null);
                MapAndSet.classMetaMap.put(MapAndSet.className, currentMeta);


                FieldVisitor fieldVisitor = new FieldVisitor();
                fieldVisitor.setCurrentMeta(currentMeta);
                fieldVisitor.visit(cu, null);

                MethodVisitor methodVisitor = new MethodVisitor();
                methodVisitor.setCurrentMeta(currentMeta);
                methodVisitor.visit(cu, null);


                ConstructorVisitor constructorVisitor = new ConstructorVisitor();
                constructorVisitor.setCurrentMeta(currentMeta);
                constructorVisitor.visit(cu, null);

                StatementVisitor statementVisitor = new StatementVisitor();
                statementVisitor.setCurrentMeta(currentMeta);
                statementVisitor.visit(cu, null);


                MapAndSet.classMetaMap.put(MapAndSet.className, currentMeta);
            }
        }

        Uml uml = new Uml();
        uml.drawUMl();

        MapAndSet.umlStringBuilder.append("@enduml" + "\n");
        OutputStream png = new FileOutputStream(outPutFile);


        SourceStringReader reader = new SourceStringReader(MapAndSet.umlStringBuilder.toString());
        System.out.println(MapAndSet.umlStringBuilder.toString());

        String desc = reader.generateImage(png);
    }


}
