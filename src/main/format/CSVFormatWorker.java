package main.format;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import main.collection.UserCollection;
import main.models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CSVFormatWorker implements FormatWorker<Route>{
    @Override
    public List<Route> readFile(Path filePath) {
        if (Files.notExists(filePath)) {
            System.out.println("file " + filePath.getFileName() + " doesn't exist");
            return Collections.emptyList();
        }

        if (!Files.isReadable(filePath)) {
            System.out.println("file " + filePath.getFileName() + " impossible to read");
            return Collections.emptyList();
        }

        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(filePath, StandardOpenOption.READ))) {
            return new CsvToBeanBuilder<>(new InputStreamReader(inputStream))
                    .withType(Route.class)
                    .withFilter(strings -> {
                        for (String one : strings)
                            if (one != null && !one.isEmpty())
                                return true;

                        return false;
                    })
                    .withExceptionHandler(e -> {
                        System.out.println("! error adding element: #" + e.getLineNumber() + ": " + Arrays.toString(e.getLine()) + " (" + e.getMessage() + ")");
                        return null;
                    })
                    .build().parse().stream().map(q -> {
                        try {
                            Route unverified = (Route) q;

                            Route result = new Route();

                            result.setName(unverified.getName());
                            result.setId(unverified.getId());

                            Coordinates coordinates = new Coordinates();

                            coordinates.setX(unverified.getCoordinates().getX());
                            coordinates.setY(unverified.getCoordinates().getY());

                            result.setCoordinates(unverified.getCoordinates());

                            LocationFrom locationfrom = new LocationFrom();

                            locationfrom.setX(unverified.getFrom().getX());
                            locationfrom.setY(unverified.getFrom().getY());
                            locationfrom.setName(unverified.getFrom().getName());

                            result.setFrom(unverified.getFrom());

                            LocationTo locationto = new LocationTo();

                            locationto.setX(unverified.getTo().getX());
                            locationto.setY(unverified.getTo().getY());
                            locationto.setZ(unverified.getTo().getZ());

                            result.setTo(unverified.getTo());

                            result.setDistance(unverified.getDistance());

                            return result;
                        } catch (Throwable ex) {
                            System.out.println("Can't add element: " + ex.getMessage());
                            return null;
                        }

                    }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            if (e.getMessage().contains("CSV"))
                System.out.println("file is empty! initializing an empty collection.");
            else System.out.println("error reading file: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    @Override
    public List<Route> readString(String csvContent) {
        try {
            return new CsvToBeanBuilder<>(new StringReader(csvContent))
                    .withType(Route.class)
                    .build().parse().stream().map(q -> (Route) q).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void writeFile(Collection<Route> values, Path filePath) {
        if (Files.notExists(filePath)) {
            System.out.println("file " + filePath.getFileName() + " doesn't exist");
            return;
        }

        if (!Files.isWritable(filePath)) {
            System.out.println("file " + filePath.getFileName() + " impossible to write");
            return;
        }

        try (Writer fileWriter = new FileWriter(filePath.toFile(), false)) {
            StatefulBeanToCsv<Route> beanToCsv = new StatefulBeanToCsvBuilder<Route>(fileWriter)
                    .withSeparator(',')
                    .build();

            beanToCsv.write(new ArrayList<>(values));

            System.out.println("Collection saved to " + filePath.getFileName());
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String writeString(List<Route> values) {
        try (Writer writer = new StringWriter()) {
            StatefulBeanToCsv<Route> beanToCsv = new StatefulBeanToCsvBuilder<Route>(writer)
                    .withSeparator(',')
                    .build();

            beanToCsv.write(values.stream().toList());
            return writer.toString();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    @Override
    public void removeById(Long id, Path filePath) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath.toFile()));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            while (fileReader.ready()) {
                final String line = fileReader.readLine();
                if (line.startsWith("\"" + id + "\"")) continue;
                fileWriter.write(line);
                fileWriter.write("\n");
            }
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
