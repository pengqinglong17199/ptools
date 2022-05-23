module commons {
    requires jdk.compiler;
    requires jdk.unsupported;

    exports com.pql.ptools.commons.lombok.processor;
    exports com.pql.ptools.commons.lombok.annotations;
    exports com.pql.ptools.commons.lombok.constants;

}