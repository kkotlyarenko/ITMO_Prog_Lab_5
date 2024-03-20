package main.format;

import main.collection.UserCollection;
import main.models.Route;

import java.nio.file.Path;


public class FileManager {
    private static final FormatWorker<Route> formatWorker = new CSVFormatWorker();

    public static UserCollection loadCollection(Path path) {
        UserCollection collection = new UserCollection();
        formatWorker.readFile(path).forEach(collection::add);
        return collection;
    }

    public static void saveCollection(Path path, UserCollection collection) {
        formatWorker.writeFile(collection, path);
    }
}
