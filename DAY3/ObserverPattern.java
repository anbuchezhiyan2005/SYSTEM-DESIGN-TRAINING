import java.util.ArrayList;
import java.util.List;

interface NewsSubject {
    void register(NewsObserver observer);
    void unregister(NewsObserver observer);
    void notifyObservers(String news);
}

interface NewsObserver {
    void update(String news);
    int getID();
}

class Observer implements NewsObserver {
    private int ID;

    public Observer(int ID) {
        this.ID = ID;
    }

    public void update(String news) {
        System.out.println("News: " + news);
    }

    public int getID() {
        return ID;
    }
}

class NewsService implements NewsSubject {
    private List<NewsObserver> observers = new ArrayList<>();

    public void register(NewsObserver observer) {
        observers.add(observer);
        System.out.println("Observer with ID: " + observer.getID() + " has been added!");
    }

    public void unregister(NewsObserver observer) {
        int ID = observer.getID();
        for(NewsObserver ob : observers) {
            if(ob.getID() == ID) {
                observers.remove(ob);
                System.out.println("Observer with ID: " + ID + " has been removed!");
            }
        }
    }

    public void notifyObservers(String news) {
        for(NewsObserver ob : observers) {
            ob.update(news);
            System.out.println("Observer with ID: " + ob.getID() + " has been notified!");
        }
    }
}

public class ObserverPattern {
    public static void main(String args[]) {
        Observer ob1 = new Observer(1);
        Observer ob2 = new Observer(2);
        Observer ob3 = new Observer(3);
        NewsService newsService = new NewsService();
        newsService.register(ob1);
        newsService.register(ob2);
        newsService.register(ob3);
        newsService.notifyObservers("Recent News");
        newsService.unregister(ob3);
    }
}

