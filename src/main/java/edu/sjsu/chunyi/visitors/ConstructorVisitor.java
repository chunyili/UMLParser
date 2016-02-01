package edu.sjsu.chunyi.visitors;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.metaInfo.ConstructorMeta;
import edu.sjsu.chunyi.util.Arrows;
import edu.sjsu.chunyi.util.MapAndSet;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jilongsun on 10/20/15.
 */
public class ConstructorVisitor extends VoidVisitorAdapter {

    public void setCurrentMeta(ClassMeta currentMeta) {
        this.currentMeta = currentMeta;
    }

    private ClassMeta currentMeta;

    ConstructorMeta currentConstructorMeta;

    @Override
    public void visit(ConstructorDeclaration n, Object arg) {

        Set<String> consParaSet = new HashSet<String>();
        List<String> consParalist = new ArrayList<String>();

        currentConstructorMeta = new ConstructorMeta();
        currentConstructorMeta.constructorName = n.getName();
        currentConstructorMeta.constructorModifier = Modifier.toString(n.getModifiers());

        if (n.getParameters() != null) {
            for (int i = 0; i < n.getParameters().size(); i++) {

                String consPara = n.getParameters().get(i).toString();
                consParalist.add(consPara);
                String[] strings = consPara.split(" ");

                String consParaKey = currentConstructorMeta.constructorName;

                if (MapAndSet.constructorMap.containsKey(consParaKey)) {

                    MapAndSet.constructorMap.get(consParaKey).add(consPara);
                    MapAndSet.constructorMap.put(consParaKey, consParaSet);


                } else {
                    consParaSet.add(consPara);
                    MapAndSet.constructorMap.put(consParaKey, consParaSet);

                }


                String usesOrInterfaceKey = strings[0] + "," + currentConstructorMeta.constructorName;


                if (MapAndSet.usesMap.containsKey(usesOrInterfaceKey)) {


//                    MapAndSet.usesOrInterfaceValue = MapAndSet.usesMap.get(usesOrInterfaceKey);
//                    MapAndSet.usesOrInterfaceValue = MapAndSet.arrowsUpdate(MapAndSet.usesOrInterfaceValue, Arrows.uses.toString());
//                    MapAndSet.usesOrInterfaceMap.put(usesOrInterfaceKey, MapAndSet.usesOrInterfaceValue);

                } else if (!MapAndSet.usesMap.containsKey(usesOrInterfaceKey)) {


                    MapAndSet.usesMap.put(usesOrInterfaceKey, Arrows.uses.toString());
                }


            }


            currentConstructorMeta.parameterList = consParalist;
            currentMeta.constructorMetas.add(currentConstructorMeta);


        } else {

            System.out.println("to check the ecnomy");
            currentMeta.constructorMetas.add(currentConstructorMeta);

        }

        MapAndSet.classMetaMap.put(MapAndSet.className, currentMeta);

    }
}
