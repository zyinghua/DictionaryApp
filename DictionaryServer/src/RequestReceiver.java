/*
    @Author: Yinghua Zhou
    Student ID: 1308266

    This thread is mainly responsible for receiving client requests and put them into the queue.
 */

import Utils.UtilsItems;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestReceiver extends Thread{
    private volatile boolean isRunning;
    private volatile ServerSocket serverSocket;
    private final WorkerPoolManager workerPoolManager;

    private volatile AtomicInteger verbose;

    public RequestReceiver(ServerSocket serverSocket, WorkerPoolManager workerPoolManager, AtomicInteger verbose)
    {
        this.serverSocket = serverSocket;
        this.workerPoolManager = workerPoolManager;
        this.verbose = verbose;
        this.isRunning = true;
    }

    public void run(){
        System.out.println("[Request Receiver] Running...");

        while(this.isRunning)
        {
            try {
                Socket clientConn = serverSocket.accept(); // wait and accept a connection

                if(verbose.get() == UtilsItems.VERBOSE_ON_HIGH)
                    System.out.println("\n[Request Receiver] Received a client request from: " + clientConn.getInetAddress());

                workerPoolManager.addClient(clientConn);
            } catch (SocketException e) {
                if (!e.getMessage().equals("Socket closed")) { // Otherwise it is a normal socket close called as part of the termination process
                    System.out.println("[Request Receiver] Socket Exception: " + e.getMessage());
                }
            }
            catch (IOException e) {
                System.out.println("[Request Receiver] IO Exception: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("[Request Receiver] Exception: " + e.getMessage());
            }
        }

        System.out.println("[Request Receiver] Finished.");
    }

    public synchronized void terminate()
    {
        this.isRunning = false;
        this.interrupt();
    }
}
