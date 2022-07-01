public class BlackCoffee extends CoffeeDecorator {
    public BlackCoffee(Coffee coffee) {
        super(coffee);
    }

    @Override
    public void addTopping(Coffee coffee) {
        coffee.addTopping(this.coffee);
        //instructions();
    }

    @Override
    public String printCoffee() {
        return this.coffee.printCoffee();
    }

    public void instructions() {
        System.out.println("Pour coffee from pot into cup");
    }

    public Double Cost() {
        return this.coffee.Cost();
    }
}
