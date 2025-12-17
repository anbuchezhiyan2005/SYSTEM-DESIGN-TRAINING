class LazySync {
    private static LazySync instance;
    private LazySync() {
        System.out.println("LazySyncSingleton created");
    }

    public static synchronized LazySync getInstance() {
        if(instance == null) {
            instance = new LazySync();
        }
        return instance;
    }

    public void log(String msg) {
        System.out.println("Log: " + msg);
    }
}

public class LazySyncSingleton {
    public static void main(String args[]) {
        LazySync s1 = LazySync.getInstance();
        LazySync s2 = LazySync.getInstance();
        System.out.println(s1 == s2);
    }
}