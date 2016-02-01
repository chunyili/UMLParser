package edu.sjsu.chunyi.visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.util.Arrows;
import edu.sjsu.chunyi.util.MapAndSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jilongsun on 10/20/15.
 */
public class ClassVisitor extends VoidVisitorAdapter {

    public void setCurrentMeta(ClassMeta currentMeta) {
        this.currentMeta = currentMeta;
    }

    private ClassMeta currentMeta;

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {

        if (n.isInterface()) {

            MapAndSet.interfaceName = n.getName();
            MapAndSet.interfaceSet.add(MapAndSet.interfaceName);



        }

        List<String> implementsList = new ArrayList<String>();

        String anotherName;
        if (n.getImplements() != null) {
            for (int i = 0; i < n.getImplements().size(); i++) {

                anotherName = n.getImplements().get(i).toString();
                implementsList.add(anotherName);

                MapAndSet.usesOrInterfaceKey = anotherName + "," +  MapAndSet.className;

                if ( MapAndSet.interfaceMap.containsKey( MapAndSet.usesOrInterfaceKey)) {

                } else {
                    MapAndSet.interfaceMap.put( MapAndSet.usesOrInterfaceKey, Arrows.Interface.toString());

                }
            }


            currentMeta.implementList = implementsList;



        }

        if (n.getExtends() == null) {


        } else {
            for (int i = 0; i < n.getExtends().size(); i++) {

                currentMeta.extendList.add(n.getExtends().get(i).toString());


            }

        }

        MapAndSet.classMetaMap.put( MapAndSet.className, currentMeta);

    }
}
