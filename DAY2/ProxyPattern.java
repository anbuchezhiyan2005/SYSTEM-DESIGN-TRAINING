interface Service {
    void perform();
}

class RealService implements Service {
    public void perform() {
        System.out.println("Service is being performed");
    }
}

class ServiceProxy implements Service {
    private RealService realService;
    private String userRole;
    public ServiceProxy(String userRole) {
        this.userRole = userRole;
        this.realService = new RealService();
    }

    public void perform() {
        if(!"ADMIN".equals(userRole)) {
            System.out.println("Access Denied!");
            return;
        }
        realService.perform();
    }
}


public class ProxyPattern {
    public static void main(String args[]) {
        Service s1 = new ServiceProxy("USER");
        s1.perform();
        Service s2 = new ServiceProxy("ADMIN");
        s2.perform();
    }
}
