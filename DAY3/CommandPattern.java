interface Command {
    void execute();
}
class Light {
    public void turnOn() { System.out.println("Light is on!");}
    public void turnOff() { System.out.println("Light is off!");}
}


class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOn();
    }
}


class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOff();
    }
}

class Remote {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}

public class CommandPattern {
    public static void main(String args[]) {
        Light light = new Light();
        Command turnOn = new LightOnCommand(light);
        Remote remote = new Remote();
        remote.setCommand(turnOn);
        remote.executeCommand();
        Command turnOff = new LightOffCommand(light);
        Remote remote2 = new Remote();
        remote2.setCommand(turnOff);
        remote.executeCommand();
    }
}