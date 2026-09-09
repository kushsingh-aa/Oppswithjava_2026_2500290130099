import java.util.*;

class Vehicle{
    private String name;
    private int speed;
    public void display(){
        System.out.println("Vehicle "+name+" is moving at speed "+speed);
    }
    public void move(){
        System.out.println("Vehicle can move");
    }
    public Vehicle(String x,int s){
        name=x;
        speed=s;
    }
}

class Bike extends Vehicle{
    public void wheels(){
        System.out.println("Bike has 2 wheels");
    }
    public Bike(String name,int speed){
        super(name,speed);
    }
}

class Car extends Vehicle{
    public void wheels(){
        System.out.println("Car has 4 wheels");
    }
    public Car(String name,int speed){
        super(name,speed);
    }
}

public class Inheritance {
    Vehicle obj1=new Bike("Splendor",120);
    Vehicle obj2=new Car("Nano",520);

    public static void main(String[] args) {
        Vehicle []obj={new Bike("Splendor",120),new Car("Nano",520)};
        for(Vehicle i:obj){
            i.move();
            i.display();
        }
    }
}
