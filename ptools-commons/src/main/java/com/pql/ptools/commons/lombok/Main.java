package com.pql.ptools.commons.lombok;

import com.pql.ptools.commons.lombok.processor.AgentProcessor;
import com.pql.ptools.commons.lombok.processor.AnnotationProcessor;
import com.pql.ptools.commons.lombok.processor.EnumDescProcessor;

import java.lang.reflect.Constructor;


public class Main {
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {

        String property = System.getProperty("java.specification.version");
        System.out.println(property);
       /* AnnotationProcessor annotationProcessor = new AnnotationProcessor();

        Module jdkCompilerModule = (Module) AgentProcessor.getJdkCompilerModule();
        Module module = jdkCompilerModule.addOpens("com.sun.tools.javac.tree", AgentProcessor.class.getModule());
        System.out.println();

        EnumDescProcessor enumDescProcessor = new EnumDescProcessor();
        enumDescProcessor.init(null);
        System.out.println();*/
    }
}
