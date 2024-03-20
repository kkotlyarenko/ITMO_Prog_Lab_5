package main.console;

import main.Main;
import main.command.handler.CommandHandler;
import main.console.ConsoleWorker;
import main.console.Request;
import main.console.Response;
import main.collection.CollectionManager;
import main.models.Coordinates;
import main.models.Route;
import main.models.LocationFrom;
import main.models.LocationTo;

import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConsoleRunner implements Runnable{
    public static final int MAX_RECURSION_DEPTH = 15;

    private final CommandHandler handler;
    private final ConsoleWorker consoleWorker;

    private int currentRecursionDepth = 0;

    private final Deque<String> inboundQueries = new ArrayDeque<>();

    public ConsoleRunner(CommandHandler handler, ConsoleWorker consoleWorker) {
        this.handler = handler;
        this.consoleWorker = consoleWorker;
    }

    @Override
    public void run() {
        CollectionManager.getInstance().getCollection();

        if (!Files.isWritable(Main.ROOT_FILE))
            System.out.println("warning! file is not available for writing");
        if (!Files.isReadable(Main.ROOT_FILE))
            System.out.println("warning! file is not available for reading");


        String input;

        while ((input = consoleWorker.get("[%tl:%tM:%tS] ~ ")) != null) {
            currentRecursionDepth = 0;
            process(input);
        }

        consoleWorker.print("Goodbye!");
    }

    private void process(String input) {
        if (currentRecursionDepth > MAX_RECURSION_DEPTH) {
            consoleWorker.print("Recursion depth can't be > " + MAX_RECURSION_DEPTH);
            inboundQueries.clear();
            return;
        }

        Request request = new Request();

        while (input.contains("element")) {
            input = input.replaceFirst("element", "");
            Route inputRoute = inputRoute();

            request.getCollection().add(inputRoute);
        }

        String[] parts = input.split(" ", 2);

        request.setCommand(parts[0].trim());

        if (parts.length == 2) {
            request.setText(parts[1].trim());
        }

        Response response = handler.handle(request);

        if (!inboundQueries.isEmpty()) currentRecursionDepth++;
        inboundQueries.addAll(response.getInboundRequests());
        while (!inboundQueries.isEmpty()) process(inboundQueries.pollLast());

        if (response.getText() != null) consoleWorker.print(response.getText());

        if (response.getRoutes() != null)
            response.getRoutes().stream().map(Route::toString).forEach(consoleWorker::print);

        consoleWorker.skip();
    }

    private Route inputRoute() {
        consoleWorker.print("New Route:");
        consoleWorker.skip();
        consoleWorker.print("Enter main information:");

        Route route = new Route();

        while(!input("name", route::setName, str->str));

        consoleWorker.skip();
        consoleWorker.print("Enter coordinates:");
        Coordinates coordinates = new Coordinates();
        while(!input("x", coordinates::setX, Long::parseLong));
        while(!input("y", coordinates::setY, Double::parseDouble));
        route.setCoordinates(coordinates);

        consoleWorker.skip();
        consoleWorker.print("Enter location from:");
        LocationFrom locationFrom = new LocationFrom();
        while(!input("x", locationFrom::setX, Long::parseLong));
        while(!input("y", locationFrom::setY, Integer::parseInt));
        while(!input("name", locationFrom::setName, str->str));
        route.setFrom(locationFrom);

        consoleWorker.skip();
        consoleWorker.print("Enter location to:");
        LocationTo locationTo = new LocationTo();
        while(!input("x", locationTo::setX, Long::parseLong));
        while(!input("y", locationTo::setY, Long::parseLong));
        while(!input("z", locationTo::setZ, Double::parseDouble));
        route.setTo(locationTo);

        while(!input("distance", route::setDistance, Long::parseLong));

        route.setId(CollectionManager.getInstance().getFreeId());

        return route;
    }

    private <K> boolean input(String fieldName, Consumer<K> setter, Function<String, K> parser) {
        try {
            setter.accept(parser.apply(consoleWorker.get(" - " + fieldName)));
            return true;
        } catch (Exception ex) {
            consoleWorker.print(ex.getMessage());
            return false;
        }
    }
}
