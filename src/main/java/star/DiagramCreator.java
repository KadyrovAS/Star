package star;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Формирует puml файл для построения диаграммы
 */
public class DiagramCreator {
    private final String pathString;

    public DiagramCreator(String path) {
        this.pathString = path;
    }

    private List<Path> getListFiles(String filename) throws IOException, ClassNotFoundException {
        Path path = Paths.get(pathString);
        List<Path> pathList = new LinkedList<>();
        Stream<Path> paths = Files.walk(path);
        toWrite("@startuml", filename, false);
        for (Path localPath : paths.toList()) {
            if (localPath.toString().endsWith(".java")) {
                pathList.add(localPath);
                List<String> lines = getClassString(localPath);
                toWrite(lines, filename, true);
            }
        }
        toWrite("@enduml", filename, true);
        return pathList;
    }

    public List<String> getClassString(Path path) throws ClassNotFoundException {
        String fileName = path.toString();
        String linePrepare;
        int ind = fileName.indexOf(".java");
        fileName = fileName.substring(0, ind).replace("\\", ".");
        ind = fileName.indexOf("star");
        fileName = fileName.substring(ind);
        List<String> result = new LinkedList<>();

        Class clazz;
        try {
            Class clazzz = Class.forName(fileName);
        }catch (ClassNotFoundException e){
            System.out.println(fileName);
            return result;
        }

        clazz = Class.forName(fileName);

        if (clazz.getName().equals(this.getClass().getName())){
            return result;
        }
        result.add("class " + clazz.getSimpleName() + "{");

        for (Field field : clazz.getDeclaredFields()) {
            linePrepare = getModify(field.getModifiers()) + field.getName() + ": " +
                    getReturnType(field.getGenericType());
            result.add(linePrepare);
        }

        linePrepare = "";
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            linePrepare = getModify(constructor.getModifiers()) +
                    "{constructor} " + constructor.getDeclaringClass().getSimpleName() + "(";
            for (Parameter parameter : constructor.getParameters()) {
                linePrepare += parameter.getType().getSimpleName() + " " + parameter.getName() + ", ";
            }
            if (constructor.getParameterCount() > 0) {
                linePrepare = linePrepare.substring(0, linePrepare.length() - 2);
            }
            linePrepare += ")";
            result.add(linePrepare);
        }

        linePrepare = "";
        for (Method method : clazz.getDeclaredMethods()) {
            linePrepare = getModify(method.getModifiers()) +
                    method.getName() + "(";
            for (Parameter parameter : method.getParameters()) {
                linePrepare += parameter.getType().getSimpleName() + " " + parameter.getName() + ", ";
            }
            if (method.getParameterCount() > 0) {
                linePrepare = linePrepare.substring(0, linePrepare.length() - 2);
            }
            linePrepare += "): ";
            linePrepare += getReturnType(method.getGenericReturnType());
            result.add(linePrepare);
        }

        result.add("}");
        result.add("");
        return result;
    }

    private String getModify(int modify) {
        if (Modifier.isPrivate(modify)) {
            return "-";
        } else if (Modifier.isPublic(modify)) {
            return "+";
        } else {
            return "#";
        }
    }

    private void toWrite(String line, String fileName, boolean update) {
        fileName += ".puml";
        try (FileWriter fileWriter = new FileWriter(fileName, update);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }

    private void toWrite(List<String> lines, String fileName, boolean update) {
        for (String line : lines) {
            toWrite(line, fileName, update);
        }
    }

    private String getReturnType(Type returnType) {

        if (returnType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) returnType;
            StringBuilder sb = new StringBuilder();

            sb.append(pt.getRawType().getTypeName()
                    .substring(pt.getRawType().getTypeName().lastIndexOf('.') + 1));

            sb.append("<");
            Type[] typeArgs = pt.getActualTypeArguments();
            for (int i = 0; i < typeArgs.length; i++) {
                String argName = typeArgs[i].getTypeName()
                        .substring(typeArgs[i].getTypeName().lastIndexOf('.') + 1);
                sb.append(argName);
                if (i < typeArgs.length - 1) sb.append(", ");
            }
            sb.append(">");

            return sb.toString();
        }

        return returnType.getTypeName()
                .substring(returnType.getTypeName().lastIndexOf('.') + 1);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DiagramCreator diagramCreator = new DiagramCreator("");
        diagramCreator.getListFiles("diagram");
    }
}
