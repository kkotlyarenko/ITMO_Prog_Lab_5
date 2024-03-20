package main;

import main.command.list.*;
import main.command.handler.CommandHandler;
import main.console.ConsoleWorker;
import main.console.BaseConsoleWorker;
import main.console.ConsoleRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static Path ROOT_FILE;

    public static void main(String[] args) {
        String file_name = System.getenv("FILE_NAME");

        if (file_name == null || file_name.isEmpty()) {
            System.out.println("Enter the name of the loaded file as an environment variable like FILE_NAME=<filename>");
            System.exit(1);
        }

        ROOT_FILE = Paths.get(file_name);

        if (Files.notExists(ROOT_FILE)) {
            try {
                Files.createFile(ROOT_FILE);
                System.out.printf("File %s was successfully created%n", ROOT_FILE.getFileName());
            } catch (IOException e) {
                System.out.printf("Unable to create file! %s%n", e.getMessage());
                System.exit(1);
            }
        }

        ConsoleRunner consoleRunner = getConsoleRunner();

        new Thread(consoleRunner).start();
    }

    private static ConsoleRunner getConsoleRunner() {
        CommandHandler commandHandler = new CommandHandler(List.of(
                new Info(),
                new Show(),
                new Add(),
                new UpdateById(),
                new RemoveById(),
                new Clear(),
                new Save(),
                new ExecuteScript(),
                new Exit(),
                new Head(),
                new AddIfMax(),
                new AddIfMin(),
                new RemoveAnyByDistance(),
                new CountLessThanDistance(),
                new PrintFieldAscendingDistance()
        ));
        ConsoleWorker consoleWorker = new BaseConsoleWorker();

        return new ConsoleRunner(commandHandler, consoleWorker);
    }
}

//+help : вывести справку по доступным командам
//+info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
//+show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
//+add {element} : добавить новый элемент в коллекцию
//+update id {element} : обновить значение элемента коллекции, id которого равен заданному
//+remove_by_id id : удалить элемент из коллекции по его id
//+clear : очистить коллекцию
//+save : сохранить коллекцию в файл
//+execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
//+exit : завершить программу (без сохранения в файл)
//+head : вывести первый элемент коллекции
//+add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции
//+add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
//+remove_any_by_distance distance : удалить из коллекции один элемент, значение поля distance которого эквивалентно заданному
//+count_less_than_distance distance : вывести количество элементов, значение поля distance которых меньше заданного
//print_field_ascending_distance : вывести значения поля distance всех элементов в порядке возрастания
