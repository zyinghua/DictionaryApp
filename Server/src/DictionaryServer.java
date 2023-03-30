import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.*;

public class DictionaryServer {
    private static final String defaultFileName = "dictionary_data.json"; // Used when no default file is provided
    private static final int corePoolSize = 20;
    private static final int maximumPoolSize = 40;
    private static final long keepAliveTime = 60L;
    private static final int queueCapacity = 100;


    public static void main(String[] args){
        if (args.length < 1 || args.length > 2) {
            // Handle invalid number of arguments
            System.out.println("Usage: java -jar DictionaryServer.jar <port> <dictionary-file>[optional]");
            System.exit(1);
        }

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("Server started. Listening on port " + args[0] + " for incoming connections...");

            String fileName = args.length == 2 ? args[1] : defaultFileName;

            Dictionary dict = new Dictionary(args.length == 2, fileName);
            AutoFileSaver autoFileSaver = new AutoFileSaver(fileName, dict);
            autoFileSaver.start();

            RequestReceiver requestReceiver = new RequestReceiver(serverSocket, dict,
                    corePoolSize, maximumPoolSize, keepAliveTime, queueCapacity);
            requestReceiver.start();

            String command = "";
            Scanner scanner = new Scanner(System.in);

            while (!command.equals("quit")) {
                System.out.println("[Admin panel] Type 'quit' to stop the server: ");
                command = scanner.nextLine().toLowerCase();

                switch (command) {
                    case "quit" -> System.out.println("Server stopping...");
                    case "save" -> {
                        dict.writeDictDataToFile();
                        System.out.println("Dictionary saved to file.");
                    }
                    case "check dictionary" -> System.out.println(dict.getDict().toString());
                    default -> System.out.println("Invalid command.");
                }
            }

            requestReceiver.terminate();
            autoFileSaver.terminate();

        } catch (NumberFormatException e) {
            System.out.println("Port must be an integer.");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("IO Exception encountered on starting up the server.");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println("Port must be between 0 and 65535");
            System.exit(1);
        }
    }
}