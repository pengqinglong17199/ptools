package com.pql.ptools.commons;

import com.sun.source.tree.*;
import com.sun.source.util.DocTrees;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class PqlProcessor extends AbstractProcessor {

    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = JavacTrees.instance(processingEnv);
        Method[] declaredMethods = processingEnv.getClass().getDeclaredMethods();
        Context context  = null;
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("getContext")) {
                try {
                    context = (Context) declaredMethod.invoke(processingEnv, null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> rootElements = roundEnvironment.getElementsAnnotatedWith(PqlTest.class);
        for (Element rootElement : rootElements) {
            JCTree tree = trees.getTree(rootElement);
            tree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    jcClassDecl.defs.stream()
                            // 过滤，只处理变量类型
                            .filter(it -> it.getKind().equals(Tree.Kind.VARIABLE))
                            // 类型强转
                            .map(it -> (JCTree.JCVariableDecl) it)
                            .forEach(it -> {

                                // 添加get方法
                                jcClassDecl.defs = jcClassDecl.defs.prepend(genGetterMethod(it));
                            });

                    super.visitClassDef(jcClassDecl);
                }
            });

        }
        return true;
    }


    private JCTree.JCMethodDecl genGetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
        // 生成return语句，return this.xxx
        JCTree.JCReturn returnStatement = treeMaker.Return(
                treeMaker.Conditional(
                        treeMaker.Binary(JCTree.Tag.NE, treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName()), treeMaker.Literal(TypeTag.BOT, 0)),
                        treeMaker.Apply(List.nil(), treeMaker.Select(treeMaker.Ident(names.fromString("pql")), names.fromString("getDesc")), List.nil()),
                        treeMaker.Literal("")
                )
        );


        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(returnStatement);

        // public 方法访问级别修饰
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名 getXXX ，根据字段名生成首字母大写的get方法
        Name getMethodName = createGetMethodName(jcVariableDecl.getName());
        // 返回值类型，get类型的返回值类型与字段类型一致
        JCTree.JCIdent string = treeMaker.Ident(names.fromString("String"));
        // 生成方法体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameterList = List.nil();
        // 异常抛出列表
        List<JCTree.JCExpression> throwCauseList = List.nil();

        // 生成方法定义树节点
        return treeMaker.MethodDef(
                // 方法访问级别修饰符
                modifiers,
                // get 方法名
                getMethodName,
                // 返回值类型
                string,
                // 泛型参数列表
                methodGenericParamList,
                //参数值列表
                parameterList,
                // 异常抛出列表
                throwCauseList,
                // 方法默认体
                body,
                // 默认值
                null
        );

    }

    private Name createGetMethodName(Name variableName) {
        String fieldName = variableName.toString();
        return names.fromString("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)+ "Desc");

    }

    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(PqlTest.class.getCanonicalName());
    }

    public SourceVersion getSupportedSourceVersion() {

        return SourceVersion.RELEASE_11;
    }
}
