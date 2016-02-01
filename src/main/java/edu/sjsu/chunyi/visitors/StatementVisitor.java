package edu.sjsu.chunyi.visitors;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.sjsu.chunyi.metaInfo.ClassMeta;
import edu.sjsu.chunyi.metaInfo.StatementMeta;
import edu.sjsu.chunyi.util.Arrows;
import edu.sjsu.chunyi.util.MapAndSet;

/**
 * Created by jilongsun on 10/21/15.
 */
public class StatementVisitor extends VoidVisitorAdapter {
    public void setCurrentMeta(ClassMeta currentMeta) {
        this.currentMeta = currentMeta;
    }

    private ClassMeta currentMeta;
    StatementMeta currentStatementMeta;

    @Override
    public void visit(BlockStmt n, Object args) {

        currentStatementMeta = new StatementMeta();
        if (n.getStmts() != null ) {
            for (int i = 0; i < n.getStmts().size(); i++) {

                currentStatementMeta.statement = n.getStmts().get(i).toString();

                String[] strings = currentStatementMeta.statement.split(" ");

                MapAndSet.usesOrInterfaceKey = strings[0] + "," + MapAndSet.className;
                if(MapAndSet.usesMap.containsKey(MapAndSet.usesOrInterfaceKey)){

                }else{

                    MapAndSet.usesMap.put(MapAndSet.usesOrInterfaceKey, Arrows.uses.toString());
                }

            }
        currentMeta.statementMetas.add(currentStatementMeta);


        MapAndSet.classMetaMap.put(MapAndSet.className, currentMeta);
        }
    }
}
