public class WhippedCream extends CoffeeDecorator {

    public WhippedCream(Coffee coffee) {
        super(coffee);
    }

    @Override
    public void addTopping(Coffee coffee) {
        coffee.addTopping(this.coffee);
        this.coffee = this.coffee;
    }

    @Override
    public String printCoffee() {
        if (this.coffee instanceof WhippedCream) {
            return "1";
        } else {
            return this.coffee.printCoffee() + "-whipped cream";
        }
    }

    public Double Cost() {
        if (this.coffee instanceof WhippedCream) {
            return 1.0;
        } else {
            return this.coffee.Cost() + 0.10;
        }
    }

}
