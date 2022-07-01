public class hotwater extends CoffeeDecorator {

    public hotwater(Coffee coffee) {
        super(coffee);
    }

    @Override
    public void addTopping(Coffee coffee) {
        coffee.addTopping(this.coffee);
        this.coffee = coffee;
    }

    @Override
    public String printCoffee() {
        return this.coffee.printCoffee() + "-Hot Water";
    }

    public Double Cost() {
        return this.coffee.Cost() + 0.0;
    }

}
