package edu.sjsu.chunyi.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.sjsu.chunyi.metaInfo.FieldMeta;
import edu.sjsu.chunyi.util.Arrows;
import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.util.MapAndSet;
import edu.sjsu.chunyi.metaInfo.MethodMeta;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jilongsun on 10/20/15.
 */
public class MethodVisitor extends VoidVisitorAdapter {
    public void setCurrentMeta(ClassMeta currentMeta) {
        this.currentMeta = currentMeta;
    }

    private ClassMeta currentMeta;

    MethodMeta currentMethodMeta;


    @Override
    public void visit(MethodDeclaration n, Object arg) {


        Set<String> parameterSet = new HashSet<String>();
        currentMethodMeta = new MethodMeta();
        currentMethodMeta.methodName = n.getName();

        currentMethodMeta.methodReturnType = n.getType().toString();
        currentMethodMeta.methodAccessModifier = Modifier.toString(n.getModifiers());

        List<String> parameterList = new ArrayList<String>();

        //this is a getter or setter
        if(MapAndSet.memberSet.contains(n.getName())){
            String filedName = n.getName().substring(3);
            char first = Character.toLowerCase(filedName.charAt(0));

            filedName = first + filedName.substring(1);
            FieldMeta fieldMeta = currentMeta.fieldMetasMap.get(filedName);
            if (n.getName().startsWith("get")){
                fieldMeta.isGetPublic = true;
            }
            if(n.getName().startsWith("set")){
                fieldMeta.isSetPublic = true;
            }
        }
        else if (n.getParameters() != null) {


            for (int i = 0; i < n.getParameters().size(); i++) {
                String parameterName = n.getParameters().get(i).toString();
                String[] paraClassName = parameterName.split(" ");
                String usesName = paraClassName[0];
                String usesName1 = paraClassName[1];

                parameterList.add(parameterName);

                parameterName = usesName1 + " " + usesName;

                String parameterKey;

                    parameterKey =  currentMethodMeta.methodAccessModifier+ ","+MapAndSet.className + "," + n.getName() + ","+ n.getType();
                    if (MapAndSet.parameterMap.containsKey(parameterKey)) {
                        MapAndSet.parameterMap.get(parameterKey).add(parameterName);
                        MapAndSet.parameterMap.put(parameterKey, parameterSet);

                    } else  {
                        parameterSet.add(parameterName);
                        MapAndSet.parameterMap.put(parameterKey, parameterSet);

                    }

                MapAndSet.usesOrInterfaceKey = usesName + "," + MapAndSet.className;


                if (MapAndSet.usesMap.containsKey(MapAndSet.usesOrInterfaceKey)) {

//
//                    MapAndSet.usesOrInterfaceValue = MapAndSet.usesOrInterfaceMap.get(MapAndSet.usesOrInterfaceKey);
//                    MapAndSet.usesOrInterfaceValue = MapAndSet.arrowsUpdate(MapAndSet.usesOrInterfaceValue, Arrows.uses.toString());
//                    MapAndSet.usesOrInterfaceMap.put(MapAndSet.usesOrInterfaceKey, MapAndSet.usesOrInterfaceValue);


                } else if (!MapAndSet.usesMap.containsKey(MapAndSet.usesOrInterfaceKey)) {


                    MapAndSet.usesMap.put(MapAndSet.usesOrInterfaceKey, Arrows.uses.toString());


                }

            }
            currentMethodMeta.parameterName = parameterList;
            currentMeta.methodMetas.add(currentMethodMeta);

//            setCurrentMeta(currentMeta);


        } else {

            currentMeta.methodMetas.add(currentMethodMeta);

//            setCurrentMeta(currentMeta);
        }


        MapAndSet.classMetaMap.put(MapAndSet.className, currentMeta);


    }


}
