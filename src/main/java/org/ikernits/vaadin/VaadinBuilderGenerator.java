package org.ikernits.vaadin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ikernits on 24/10/15.
 */
class VaadinBuilderGenerator {
    private final List<Class<? extends AbstractComponent>> sources = ImmutableList.of(
            VerticalLayout.class,
            HorizontalLayout.class,
            Button.class,
            TextField.class,
            TextArea.class,
            Label.class,
            Panel.class
    );

    private static final String builderPackage = "org.ikernits.vaadin";
    private static final String path = "./src/main/java/" + builderPackage.replace(".", "/");

    private static class IndentedPrinter {
        private int indent = 0;
        private PrintStream out;
        private static final String indentString = "    ";

        public IndentedPrinter(PrintStream out) {
            this.out = out;
        }

        public void increaseIndent() {
            indent++;
        }

        public void decreaseIndent() {
            indent--;
        }

        public void println(String line) {
            for (int i = 0; i < indent; ++i) {
                out.print(indentString);
            }
            out.println(line);
        }
    }

    private final Map<Class<?>, ComponentModel> componentMap = new HashMap<>();

    private static class ComponentModel {
        private Class<?> clazz;
        private List<Method> setters;

        public static ComponentModel scanClass(Class<?> clazz) {
            ComponentModel model = new ComponentModel();
            model.clazz = clazz;
            model.setters = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> m.getName().startsWith("set") || m.getName().startsWith("add"))
                    .filter(m -> (m.getModifiers() & Modifier.PUBLIC) != 0
                            && (m.getModifiers() & ~Modifier.methodModifiers()) == 0)
                    .collect(Collectors.toList());
            return model;
        }

        Map<String, Map<List<Class<?>>, List<String>>> paramNameMap =
                ImmutableMap.<String, Map<List<Class<?>>, List<String>>>builder()
                        .put("setWidth", ImmutableMap.of(
                                ImmutableList.of(float.class, Sizeable.Unit.class),
                                ImmutableList.of("width", "unit")
                        ))
                        .put("setHeight", ImmutableMap.of(
                                ImmutableList.of(float.class, Sizeable.Unit.class),
                                ImmutableList.of("height", "unit")
                        ))
                        .build();

        private List<String> resolveParameterNames(Method m) {
            if (m.getParameterTypes().length == 0) {
                return ImmutableList.of();
            }

            Map<List<Class<?>>, List<String>> methodMap = paramNameMap.get(m.getName());
            if (methodMap != null) {
                List<String> names = methodMap.get(Arrays.asList(m.getParameterTypes()));
                if (names != null) {
                    return names;
                }
            }

            if (m.getParameterTypes().length == 1) {
                if (m.getName().startsWith("set") || m.getName().startsWith("add")) {
                    return ImmutableList.of(m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4));
                } else {
                    return ImmutableList.of(m.getName());
                }
            }

            AtomicInteger index = new AtomicInteger();
            return Stream.generate(() -> "param" + index.incrementAndGet())
                    .limit(m.getParameterCount())
                    .collect(Collectors.toList());
        }

        public void printMethodImplementation(IndentedPrinter p, Method m) {
            p.println("/**");
            p.println(" * @see " + clazz.getName() + "#" + m.getName());
            p.println(" */");

            StringBuilder sb = new StringBuilder();
            sb.append("public B ");
            sb.append(m.getName());
            sb.append("(");

            Parameter[] params = m.getParameters();
            List<String> names = resolveParameterNames(m);
            for (int i = 0; i < params.length; ++i) {
                sb.append(params[i].getType().getSimpleName());
                sb.append(" ");
                sb.append(names.get(i));
                if (i != params.length - 1) {
                    sb.append(", ");
                }
            }

            sb.append(") {");
            p.println(sb.toString());

            p.increaseIndent();
            p.println("delegate." + m.getName() + "(" + names.stream().collect(Collectors.joining(", ")) + ");");
            p.println("return self;");
            p.decreaseIndent();

            p.println("}");
            p.println("");
        }

        List<String> getMethodImports() {
            return setters.stream()
                    .flatMap(m -> Arrays.stream(m.getParameterTypes()))
                    .map(Class::getName)
                    .map(name -> name.replace("$", "."))
                    .distinct()
                    .filter(name -> !name.startsWith("java.lang."))
                    .filter(name -> name.contains("."))
                    .sorted()
                    .collect(Collectors.toList());
        }



        public void generate() throws FileNotFoundException {
            String path = "./src/main/java/" + builderPackage.replace(".", "/");
            File file = new File(path + "/" + clazz.getSimpleName() + "Builder.java");
            try (PrintStream printStream = new PrintStream(file)) {
                print(new IndentedPrinter(printStream));
            }
        }

        public void print(IndentedPrinter out) {
            String className = clazz.getSimpleName();
            String builderName = className + "Builder";
            String superName;

            List<String> imports = new ArrayList<>();
            if (clazz.equals(AbstractComponent.class)) {
                superName = "ComponentBuilder";
            } else {
                superName = clazz.getSuperclass().getSimpleName() + "Builder";
            }

            imports.add(clazz.getName());
            imports.addAll(getMethodImports());

            out.println("package " + builderPackage + ";");
            out.println("");
            imports.forEach(i -> out.println("import " + i + ";"));
            out.println("");

            out.println(String.format("public class %s<T extends %s, B extends %s<T, B>> extends %s<T, B> {",
                    builderName, className, builderName, superName));
            out.println("");

            out.increaseIndent();


            out.println("public " + builderName + "(T delegate) {");
            out.increaseIndent();
            out.println("super(delegate);");
            out.decreaseIndent();
            out.println("}");
            out.println("");


            setters.forEach(s -> printMethodImplementation(out, s));
            out.decreaseIndent();
            out.println("}");
        }
    }

    public void scanHierarchy(Class<? extends AbstractComponent> componentClass) {
        for (Class<?> clazz = componentClass; !clazz.equals(AbstractComponent.class); clazz = clazz.getSuperclass()) {
            if (!componentMap.containsKey(clazz)) {
                componentMap.put(clazz, ComponentModel.scanClass(clazz));
            }
        }
    }

    public VaadinBuilderGenerator build() {
        componentMap.put(AbstractComponent.class, ComponentModel.scanClass(AbstractComponent.class));
        sources.forEach(this::scanHierarchy);
        return this;
    }

    private static List<String> generateImports(List<? extends Class<?>> classes) {
        return classes.stream()
                .map(Class::getName)
                .map(name -> name.replace("$", "."))
                .distinct()
                .filter(name -> !name.startsWith("java.lang."))
                .filter(name -> name.contains("."))
                .sorted()
                .collect(Collectors.toList());
    }

    public void printBuilderClass(IndentedPrinter out) {
        List<String> imports = generateImports(sources);

        out.println("package " + builderPackage + ";");
        out.println("");
        imports.forEach(i -> out.println("import " + i + ";"));
        out.println("");

        out.println("public class VaadinBuilders {");
        out.println("");
        out.increaseIndent();

        sources.forEach(s -> {
            String className = s.getSimpleName();
            String smallName = className.substring(0, 1).toLowerCase() + className.substring(1);
            String builderName = s.getSimpleName() + "Builder";
            out.println(String.format("public static %s<%s, ? extends %s<%s, ?>> %s() {",
                    builderName, className, builderName, className, smallName
            ));
            out.increaseIndent();
            out.println(String.format("return new %s<>(new %s());", builderName, className));
            out.decreaseIndent();
            out.println("}");
            out.println("");
        });

        out.decreaseIndent();
        out.println("}");

    }

    public void generate() throws FileNotFoundException {
        componentMap.values().forEach((componentModel) -> {
            try {
                componentModel.generate();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        File file = new File(path + "/VaadinBuilders.java");
        try (PrintStream printStream = new PrintStream(file)) {
            printBuilderClass(new IndentedPrinter(printStream));
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        new VaadinBuilderGenerator().build().generate();
    }
}
