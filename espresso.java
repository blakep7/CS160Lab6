public class espresso extends CoffeeDecorator {
    public espresso(Coffee coffee) {
        super(coffee);
    }

    @Override
    public void addTopping(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public String printCoffee() {
        return this.coffee.printCoffee() + " -Espresso Shot";
    }

    public Double Cost() {
        return this.coffee.Cost() + 0.35;
    }
}
