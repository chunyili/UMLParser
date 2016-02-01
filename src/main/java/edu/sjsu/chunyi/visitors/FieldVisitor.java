package edu.sjsu.chunyi.visitors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.metaInfo.FieldMeta;
import edu.sjsu.chunyi.util.Arrows;
import edu.sjsu.chunyi.util.MapAndSet;

import java.lang.reflect.Modifier;

/**
 * Created by jilongsun on 10/20/15.
 */
public class FieldVisitor extends VoidVisitorAdapter {


    private ClassMeta currentMeta;

    public void setCurrentMeta(ClassMeta currentMeta) {
        this.currentMeta = currentMeta;
    }

    @Override
    public void visit(FieldDeclaration n, Object arg) {


        FieldMeta fieldMeta;

        String id = n.getVariables().get(0).getId().toString();

        char first = Character.toUpperCase(id.charAt(0));

        String memberName = first + id.substring(1);

        String getMethod = "get" + memberName;
        String setMethod = "set" + memberName;

        MapAndSet.memberSet.add(getMethod);
        MapAndSet.memberSet.add(setMethod);


        if (currentMeta.fieldMetasMap.containsKey(id)) {
            fieldMeta = currentMeta.fieldMetasMap.get(id);

        } else {
            fieldMeta = new FieldMeta();
        }
        fieldMeta.attriType = n.getType().toString();

        fieldMeta.attriAccessModifier = Modifier.toString(n.getModifiers());


        currentMeta.fieldMetasMap.put(id, fieldMeta);


//
        setCurrentMeta(currentMeta);

        MapAndSet.classMetaMap.put(MapAndSet.className, currentMeta);


        if (n.toString().contains("new")) {
            MapAndSet.usesOrInterfaceKey = n.getType() + "," + MapAndSet.className;
        }


        StringBuilder sb = new StringBuilder();

        String a = sb.append(n.getType().toString()).append(".java").toString();

        MapAndSet.anotherClassName = n.getType().toString();



        if (MapAndSet.className.compareTo(MapAndSet.anotherClassName) < 0) {
            MapAndSet.key = MapAndSet.className + "," + MapAndSet.anotherClassName;

        } else if (MapAndSet.className.compareTo(MapAndSet.anotherClassName) >= 0) {
            MapAndSet.key = MapAndSet.anotherClassName + "," + MapAndSet.className;

        }


        for (int i = 0; i < MapAndSet.classes.size(); i++) {




            if (a.equals(MapAndSet.classes.get(i).toString())) {

                if (MapAndSet.associateMap.containsKey(MapAndSet.key)) {
                    continue;

                } else {
                    MapAndSet.associateMap.put(MapAndSet.key, " : ");
                }
            }


        }


    }


}
