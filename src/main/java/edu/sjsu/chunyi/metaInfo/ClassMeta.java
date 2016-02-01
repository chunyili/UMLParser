package edu.sjsu.chunyi.metaInfo;


import java.util.*;

/**
 * Created by jilongsun on 10/18/15.
 */
public class ClassMeta {


    public String classNames;




    public List<StatementMeta> statementMetas = new ArrayList<StatementMeta>();
//
    public Map<String, FieldMeta> fieldMetasMap = new HashMap<String, FieldMeta>();
//
    public List<MethodMeta> methodMetas = new ArrayList<MethodMeta>();
//
    public List<ConstructorMeta> constructorMetas = new ArrayList<ConstructorMeta>();

    public List<String> implementList = new ArrayList<String>();
    public List<String> extendList = new ArrayList<String>();

}
