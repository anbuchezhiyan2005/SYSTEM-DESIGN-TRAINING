// CREATING CONCRETE INTERFACES

interface button {
    void render();
}

interface Checkbox {
    void render();
}

// IMPLEMENTING CONCRETE CLASSES

class MyButton implements button {
    public void render() {
        System.out.println("Rendering MyButton");
    }
}

class MyCheckbox implements Checkbox {
    public void render() {
        System.out.println("Rendering MyCheckbox");
    }
}

// CRETING ABSTRACT FACTORY
interface UIFactory {
    button createButton();
    Checkbox createCheckbox();
}

//IMPLEMENTING ABSTRACT FACTORY CLASSES

class MyFactory1 implements UIFactory {
    public button createButton() {
        return new MyButton();
    }

    public Checkbox createCheckbox() {
        return new MyCheckbox();
    }
}

class MyFactory2 implements UIFactory {
    public button createButton() {
        return new MyButton();
    }

    public Checkbox createCheckbox() {
        return new MyCheckbox();
    }
}

// MAIN FUNCTION

public class AbstractFactoryPattern {
    public static void main(String args[]) {
        UIFactory factory1 = new MyFactory1();
        button btn1 = factory1.createButton();
        Checkbox cb1 = factory1.createCheckbox();
        btn1.render();
        cb1.render();

        UIFactory factory2 = new MyFactory2();
        button btn2 = factory2.createButton();
        Checkbox cb2 = factory2.createCheckbox();
        btn2.render();
        cb2.render();
    }
}