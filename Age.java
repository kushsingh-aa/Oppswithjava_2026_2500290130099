import java.util.Scanner;

public class Age {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        try {
            checkAge(age);
        } catch (AgeIsInvalidException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void checkAge(int age) throws AgeIsInvalidException {
        if (age < 0) {
            throw new AgeIsInvalidException("Age cannot be negative");
        }

        if (age >= 18) {
            System.out.println("Person is eligible to vote");
        } else {
            System.out.println("Person is not eligible to vote");
        }
    }
}

class AgeIsInvalidException extends Exception {
    public AgeIsInvalidException(String message) {
        super(message);
    }
}