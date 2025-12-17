import java.util.Scanner;
interface Coffee {
    double cost();
    void description();
}

class SimpleCoffee implements Coffee {
    public double cost() {
        return 10.0;
    }

    public void description() {
        System.out.println("Simple Coffee");
    }
}

class MilkDecorator implements Coffee {
    private Coffee coffee;
    public MilkDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
    public double cost() {
        return coffee.cost() + 5.00;
    }

    public void description() {
        System.out.println("Milk Coffee");
    }
}

public class DecoratorPattern {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter coffee type (Simple/Milk coffee): ");
        String type = sc.nextLine();
        switch (type.toLowerCase()) {
            case "simplecoffee":
                SimpleCoffee simpleCoffee = new SimpleCoffee();
                System.out.println("Cost: " + simpleCoffee.cost());
                simpleCoffee.description();
                break;
        
            case "milk coffee":
                MilkDecorator milkCoffee = new MilkDecorator(new SimpleCoffee());
                System.out.println("Cost: " + milkCoffee.cost());
                milkCoffee.description();
                break;
            
            default:
                System.out.println("Invalid coffee type");
        }

    }    
}
