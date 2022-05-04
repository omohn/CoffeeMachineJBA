package machine;

import java.util.Scanner;

public class CoffeeMachine {

    boolean isRunning;
    int water;
    int milk;
    int coffee;
    int cups;
    int money;
    Status status;
    Scanner scanner;
    String[] statusMessages = {"Write action (buy, fill, take, remaining, exit):",
            "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:", "Exit",
            "ml of water you want to add:", "ml of milk you want to add:", "grams of coffee you want to add:",
            "disposable cups of coffee you want to add:"};

    public CoffeeMachine() {
        this.water = 400;
        this.milk = 540;
        this.coffee = 120;
        this.cups = 9;
        this.money = 550;
        this.isRunning = true;
        this.status = Status.CHOOSE_ACTION;
        this.scanner = new Scanner(System.in);
        run();

    }

    public static void main(String[] args) {

        new CoffeeMachine();

    }

    private void take() {

        System.out.println("I gave you $" + money);
        System.out.println();
        money = 0;
    }

    public boolean enoughSupply(Beverage beverage) {
        return !(cups < 1 || water < beverage.waterMl || milk < beverage.milkMl
                || coffee < beverage.coffeeGr);

    }

    private void run() {
        do {
            displayStatusMessage(status);
            processInput(scanner.nextLine());
        } while (isRunning);
    }

    private void processInput(String nextLine) {
        switch (status) {
            case CHOOSE_ACTION:
                chooseAction(nextLine);
                break;
            case CHOOSE_COFFEE:
                chooseCoffee(nextLine);
                break;
            case FILL_WATER:
                fillWater(nextLine);
                break;
            case FILL_MILK:
                fillMilk(nextLine);
                break;
            case FILL_CUPS:
                fillCups(nextLine);
                break;
            case FILL_BEANS:
                fillBeans(nextLine);
                break;
        }
    }

    private void chooseAction(String inputAction) {

        switch (inputAction) {
            case "buy":
                status = Status.CHOOSE_COFFEE;
                break;
            case "fill":
                status = Status.FILL_WATER;
                break;
            case "take":
                take();
                break;
            case "remaining":
                reportStatus();
                break;
            case "exit":
                status = Status.EXIT;
                isRunning = false;
                break;
            default:
                break;
        }


    }

    private void chooseCoffee(String input) {

        Beverage beverage = null;

        switch (input) {
            case "back":
                status = Status.CHOOSE_ACTION;
                return;
            case "1":
                beverage = new Espresso();
                break;
            case "2":
                beverage = new Latte();
                break;
            case "3":
                beverage = new Cappuccino();
                break;
            default:
                break;
        }
        prepareBeverage(beverage);
    }

    private void prepareBeverage(Beverage beverage) {
        if (enoughSupply(beverage)) {
            System.out.println("I have enough resources, making you a coffee!\n");
            this.water -= beverage.waterMl;
            this.milk -= beverage.milkMl;
            this.coffee -= beverage.coffeeGr;
            this.cups--;
            this.money += beverage.price;
        } else {
            if (water < beverage.waterMl) {
                System.out.println("Sorry, not enough water!");
                System.out.println();
            }
            if (milk < beverage.milkMl) {
                System.out.println("Sorry, not enough milk!");
            }
            if (coffee < beverage.coffeeGr) {
                System.out.println("Sorry, not enough coffee beans!");
            }
            if (cups < 1) {
                System.out.println("Sorry, not enough cups!");
            }
        }
        status = Status.CHOOSE_ACTION;

    }

    private void reportStatus() {
        System.out.printf("The coffee machine has:\n%d ml of water\n%d ml of milk\n" +
                        "%d g of coffee beans\n%d disposable cups\n$%d of money\n\n",
                water, milk, coffee, cups, money);
    }

    private void fillWater(String input) {
        water += Integer.parseInt(input);
        status = Status.FILL_MILK;
    }

    private void fillMilk(String input) {
        milk += Integer.parseInt(input);
        status = Status.FILL_BEANS;
    }

    private void fillBeans(String input) {
        coffee += Integer.parseInt(input);
        status = Status.FILL_CUPS;
    }

    private void fillCups(String input) {
        cups += Integer.parseInt(input);
        status = Status.CHOOSE_ACTION;
    }

    private void displayStatusMessage(Status status) {
        if (status.ordinal() >= 3) {
            System.out.print("Write how many ");
        }
        System.out.println(statusMessages[status.ordinal()]);
    }

    enum Status {
        CHOOSE_ACTION, CHOOSE_COFFEE, EXIT, FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS
    }
}