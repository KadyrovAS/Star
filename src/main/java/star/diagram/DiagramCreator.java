package star.diagram;

import org.h2.util.json.JSONTarget;

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

public class DiagramCreator{
private final String pathString;

    public DiagramCreator(String path) {
        this.pathString = path;
    }

    private List<Path> getListFiles() throws IOException, ClassNotFoundException {
        Path path = Paths.get(pathString);
        List<Path>pathList = new LinkedList<>();
        Stream<Path> paths = Files.walk(path);
        for (Path localPath: paths.toList()){
            if (localPath.toString().endsWith(".java")) {
                pathList.add(localPath);
                List<String> lines = getClassString(localPath);
                toWrite(lines, "diagram");
            }
        }
        return pathList;
    }

    public List<String> getClassString(Path path) throws ClassNotFoundException {
        String fileName = path.toString();
        String linePrepare;
        int ind = fileName.indexOf(".java");
        fileName = fileName.substring(0, ind).replace("\\", ".");
        ind = fileName.indexOf("star");
        fileName = fileName.substring(ind);
        Class clazz = Class.forName(fileName);
        List<String>result = new LinkedList<>();
        result.add("class " + clazz.getSimpleName() + "{");

        for (Field field: clazz.getDeclaredFields()){
            linePrepare = getModify(field.getModifiers()) + field.getName() + ": " + field.getType().getSimpleName();
            result.add(linePrepare);
        }

        linePrepare = "";
        for (Constructor constructor: clazz.getDeclaredConstructors()){
            linePrepare = getModify(constructor.getModifiers()) + constructor.getDeclaringClass().getSimpleName() + "(";
            for (Parameter parameter: constructor.getParameters()){
                linePrepare += parameter.getType().getSimpleName() + " " + parameter.getName() + ", ";
            }
            if (constructor.getParameterCount() > 0) {
                linePrepare = linePrepare.substring(0, linePrepare.length() - 2);
            }
            linePrepare += ")";
            result.add(linePrepare);
        }

        linePrepare = "";
        for (Method method: clazz.getDeclaredMethods()){
            linePrepare = method.getReturnType().getSimpleName() + " " +
                    getModify(method.getModifiers()) + method.getName() + "(";
            for (Parameter parameter: method.getParameters()){
                linePrepare += parameter.getType().getSimpleName() + " " + parameter.getName() + ", ";
            }
            if (method.getParameterCount() > 0) {
                linePrepare = linePrepare.substring(0, linePrepare.length() - 2);
            }
            linePrepare += ")";
            result.add(linePrepare);
        }

        result.add("}");
        result.add("");
        return result;
    }

    private String getModify(int modify){
        if (Modifier.isPrivate(modify)){
            return "-";
        } else if (Modifier.isPublic(modify)) {
            return "+";
        }else {
            return "#";
        }
    }

    private void toWrite(List<String>lines, String fileName)  {
        fileName += ".puml";
        try(FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ){
            for (String line: lines){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DiagramCreator diagramCreator = new DiagramCreator("");
        diagramCreator.getListFiles();
    }
}
