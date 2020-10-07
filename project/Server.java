package project;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements ServerInterface {

    private final static int PORT_NUMBER = 0;
	//place your server ip in the string below
    private final static String cpuIP = "3.131.133.188";
    private static final Server main = new Server();

    private static final HashMap<String, ServerDataValue> data = new HashMap<>();
    private static final ArrayList<String> dataKeys = new ArrayList<>(); // data key storage

    private static final HashMap<String, CallQueue> calls = new HashMap<>(); // call storage
    private static final ArrayList<String> callKeys = new ArrayList<>(); // call key storage

    private static final Lock dataLock = new ReentrantLock(true);
    private static final Lock callLock = new ReentrantLock(true);

    // public static final long updateInterval; // from ServerInterface;
    private static final int expirationDate = 20; // Number of update intervals before declared stale.
    private static final long timeDelta = updateInterval * expirationDate; // max difference from timestamp

    private static final Spinner spinner = new Spinner(new Character[]{' ', '-', '~', '-'}, 3); // prettified watchdog indicator

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public static void main(String[] args) {
        startServ();
    }

    private static void startServ() {
        System.setProperty("java.rmi.server.hostname", cpuIP);
        try {
            Registry registry;
            ServerInterface stub;

            stub = (ServerInterface) UnicastRemoteObject.exportObject(main, PORT_NUMBER);
            registry = LocateRegistry.getRegistry(PORT_NUMBER);
            registry.bind("ServerInterface", stub);

            System.out.println("Server Ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

        scheduler.scheduleAtFixedRate(Server::backgroundUpdater, 0, updateInterval, TimeUnit.MILLISECONDS);
    }

    private static void backgroundUpdater() {
        update();
    }

    private static void update() {
        cleanUp();
        printOut();
    }

    private static void printOut() {
        // Clear screen
        System.out.printf("\033[H\033[2JKnown Variables %s\n", spinner);

        System.out.flush();
        // Loop through every key in data and print it.
        // If it has a timestamp, print the timestamp.
        // Otherwise, it's a call list.
        dataLock.lock();
        for (String key : dataKeys) {
            ServerDataValue value = data.get(key);
            if (value != null) {
                System.out.println(value);
            }
        }
        dataLock.unlock();

        System.out.println("\nFunction Queues");
        callLock.lock();
        for (String key : callKeys) {
            CallQueue callQueue = calls.get(key);
            if (callQueue.size() > 0) {
                System.out.println(key);
                for (ServerCallArgs args : callQueue) {
                    System.out.println("    " + args);
                }
            }
        }
        callLock.unlock();
    }

    private static void cleanUp() {
        dataLock.lock();
        List<String> staleKeys = new ArrayList<>();
        for (String key : dataKeys) {
            ServerDataValue value = data.get(key);
            if (value.age() > timeDelta) {
                staleKeys.add(key);
            }
        }
        for (String key : staleKeys) {
            data.remove(key);
            dataKeys.remove(key);
        }
        dataLock.unlock();

        callLock.lock();
        for (String key : callKeys) {
            CallQueue callQueue = calls.get(key);
            while (!callQueue.isEmpty() && callQueue.peek().age() > timeDelta) {
                callQueue.dequeue();
            }
        }
        callLock.unlock();
    }

    public void serverSend(String key, Object value) throws RemoteException {
//        System.out.println("serverSend(" + key + ", " + value + ")");
        serverSend(key, value, false);
    }

    public void serverSend(String key, Object value, boolean burnAfterReading) throws RemoteException {
//        System.out.println("serverSend(" + key + ", " + value + ", " + burnAfterReading + ")");
        if (dataKeys.contains(key)) {
            dataLock.lock();
            data.get(key).setValue(value);
            dataLock.unlock();
        } else {
            dataLock.lock();
            data.put(key, new ServerDataValue(key, value, burnAfterReading));
            dataLock.unlock();
            dataKeys.add(key);
            Collections.sort(dataKeys);
        }
    }

    public Object serverReceive(String key) throws RemoteException {
//        System.out.println("serverReceive("+key+")");
        if (!dataKeys.contains(key)) {
            return null;
        }
        dataLock.lock();
        ServerDataValue value = data.get(key);
        dataLock.unlock();
        if (value.burnAfterReading) {
            dataLock.lock();
            data.remove(value.name);
            dataLock.unlock();
            dataKeys.remove(value.name);
        }
        return value.getValue();
    }

    public void serverCall(String key, Object... argv) throws RemoteException {
//        System.out.println("serverCall(" + key + ", " + Arrays.toString(argv) + ")");
        callLock.lock();
        if (!callKeys.contains(key)) {
            calls.put(key, new CallQueue());
            callKeys.add(key);
            Collections.sort(callKeys);
        }
        calls.get(key).enqueue(new ServerCallArgs(argv));
        callLock.unlock();
    }

    public Object[] serverGetCall(String key) {
//        System.out.println("serverGetCall(" + key + ")");
        if (!callKeys.contains(key)) {
            return null;
        }
        CallQueue queue = calls.get(key);
        if (queue.isEmpty()) {
            return null;
        }
        callLock.lock();
        Object[] args = queue.dequeue().getArgs();
        callLock.unlock();
        return args;
    }
}

class CallQueue extends LinkedList<ServerCallArgs> {
    void enqueue(ServerCallArgs value) {
        add(value);
    }

    ServerCallArgs dequeue() {
        return pop();
    }
}

class Spinner {
    private Object[] spinners;
    private int spinnerIndex;
    private int advancementCycles;
    private int currentCycle;
//    private static final int defaultCycles = 1;
//    private static final Character[] defaultSpinners = new Character[]{'|', '/', '-', '\\'};

//    Spinner() {
//        this(defaultSpinners, defaultCycles);
//    }

//    Spinner(int cycles) {
//        this(defaultSpinners, cycles);
//    }

//    <T> Spinner(T[] spinners) {
//        this(spinners, defaultCycles);
//    }

    <T> Spinner(T[] spinners, int cycles) {
        this.spinners = spinners;
        spinnerIndex = 0;
        advancementCycles = cycles;
        currentCycle = 1;
    }

    public String toString() {
        String ret = this.spinners[spinnerIndex].toString();
        currentCycle++;
        if (currentCycle % advancementCycles == 0) {
            spinnerIndex++;
        }
        spinnerIndex %= spinners.length;
        return ret;
    }
}

abstract class ServerDataObject {
    long timestamp;

    ServerDataObject() {
        timestamp = System.currentTimeMillis();
    }

    long age() {
        return System.currentTimeMillis() - timestamp;
    }

    abstract public String toString();
}

class ServerCallArgs extends ServerDataObject {
    private Object[] args;

    ServerCallArgs(Object... args) {
        super();

        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public String toString() {
        return String.format("[Age:%4d] %s", age(), Arrays.toString(args));
    }
}

class ServerDataValue extends ServerDataObject {
    String name;
    private Object value;
    boolean burnAfterReading;

    ServerDataValue(String name, Object value, boolean burnAfterReading) {
        super();
        this.name = name;
        setValue(value);
        this.burnAfterReading = burnAfterReading;
    }

    void setValue(Object value) {
        this.value = value;
        timestamp = System.currentTimeMillis();
    }

    private void recursiveLengthFinder(Object value, List<Integer> result) {
        if (value instanceof int[]) {
            int[] newValue = (int[]) value;

            result.add(newValue.length);
            if (newValue.length > 0) {
                recursiveLengthFinder(newValue[0], result);
            }
        } else if (value instanceof long[]) {
            long[] newValue = (long[]) value;

            result.add(newValue.length);
            if (newValue.length > 0) {
                recursiveLengthFinder(newValue[0], result);
            }
        } else if (value instanceof double[]) {
            double[] newValue = (double[]) value;

            result.add(newValue.length);
            if (newValue.length > 0) {
                recursiveLengthFinder(newValue[0], result);
            }
        } else if (value instanceof boolean[]) {
            boolean[] newValue = (boolean[]) value;

            result.add(newValue.length);
            if (newValue.length > 0) {
                recursiveLengthFinder(newValue[0], result);
            }
        } else if (value instanceof Object[]) {
            Object[] newValue = (Object[]) value;

            result.add(newValue.length);
            if (newValue.length > 0) {
                recursiveLengthFinder(newValue[0], result);
            }
        }
    }

    private String repr() {
        ArrayList<Integer> dimensions = new ArrayList<>();
        recursiveLengthFinder(value, dimensions);

        if (dimensions.isEmpty()) {
            return value.toString();
        }
        String typeName = value.getClass().getSimpleName();
        return String.format(
                "%s%s",
                typeName.substring(0, typeName.indexOf('[')),
                Arrays.toString(dimensions.toArray())
                        .replace(',', ']')
                        .replace(' ', '['));
    }

    public String toString() {
        return String.format("[Age:%4d] %s = %s", age(), name, repr());
    }

    Object getValue() {
        return value;
    }
}