public class Milk extends CoffeeDecorator {
    public Milk(Coffee coffee) {
        super(coffee);
    }

    @Override
    public void addTopping(Coffee coffee) {
        coffee.addTopping(this.coffee);
        this.coffee = this.coffee;
    }

    @Override
    public String printCoffee() {
        return this.coffee.printCoffee() + "-milk";
    }

    public Double Cost() {
        return this.coffee.Cost() + 0.15;
    }
}
