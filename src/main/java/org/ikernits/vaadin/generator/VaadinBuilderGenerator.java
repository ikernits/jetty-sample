package org.ikernits.vaadin.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

class VaadinBuilderGenerator {
    @SuppressWarnings("unchecked")
    private final List<Class<? extends AbstractComponent>> sources = ImmutableList.of(
        VerticalLayout.class,
        HorizontalLayout.class,
        AbsoluteLayout.class,
        Button.class,
        TextField.class,
        TextArea.class,
        Label.class,
        Panel.class,
        MenuBar.class,
        TabSheet.class,
        Table.class,
        ComboBox.class,
        CheckBox.class,
        Link.class,
        Grid.class,
        Window.class,
        FormLayout.class,
        DateField.class
    );

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
            File file = new File(targetPath + "/" + clazz.getSimpleName() + "Builder.java");
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

            out.println("package " + targetPackage + ";");
            out.println("");
            imports.forEach(i -> out.println("import " + i + ";"));
            out.println("");

            out.println("@SuppressWarnings({\"deprecation\", \"unused\", \"unchecked\"})");
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

        out.println("package " + targetPackage + ";");
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

    List<Class<?>> copyClasses = ImmutableList.of(
        ComponentBuilder.class,
        VaadinComponentAttributes.class,
        VaadinComponentStyles.class
    );

    public void generate() throws IOException {
        componentMap.values().forEach((componentModel) -> {
            try {
                componentModel.generate();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        File file = new File(targetPath + "/VaadinBuilders.java");
        try (PrintStream printStream = new PrintStream(file)) {
            printBuilderClass(new IndentedPrinter(printStream));
        }

        for (Class<?> clazz : copyClasses) {
            String content = FileUtils.readFileToString(new File(generatorPath + "/" + clazz.getSimpleName() + ".java"));
            FileUtils.writeStringToFile(
                new File(targetPath + "/" + clazz.getSimpleName() + ".java"),
                StringUtils.replace(content, generatorPackage, targetPackage)
            );
        }
    }

    private static final String targetPackage = "org.ikernits.vaadin";
    private static final String sourcesPath = "./src/main/java";
    private static final String generatorPackage = VaadinBuilderGenerator.class.getPackage().getName();
    private static final String generatorPath = sourcesPath + "/" + generatorPackage.replace(".", "/");
    private static final String targetPath = sourcesPath + "/" + targetPackage.replace(".", "/");

    public static void main(String[] args) throws IOException {
        FileUtils.forceMkdir(new File(targetPath));
       new VaadinBuilderGenerator().build().generate();
    }
}
