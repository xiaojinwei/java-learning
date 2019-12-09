package com.cj.learn.patterns.strategy;

/**
 * 策略者模式
 * 意图：定义一系列的算法,把它们一个个封装起来, 并且使它们可相互替换。
 * 主要解决：在有多种算法相似的情况下，使用 if...else 所带来的复杂和难以维护。
 * 何时使用：一个系统有许多许多类，而区分它们的只是他们直接的行为。
 * 优点： 1、算法可以自由切换。 2、避免使用多重条件判断。 3、扩展性良好。
 * 缺点： 1、策略类会增多。 2、所有策略类都需要对外暴露。
 */
public class Strategy {
    public static void main(String[] args) {
        DragonSlayer slayer = new DragonSlayer(new BusStrategy());
        System.out.println(slayer.calculate(10));
        slayer.setStrategy(new TaxiStrategy());
        System.out.println(slayer.calculate(30));
    }
}

 class DragonSlayer{
     CalculationStrategy strategy;

     public DragonSlayer(CalculationStrategy strategy) {
         this.strategy = strategy;
     }

     public void setStrategy(CalculationStrategy strategy) {
         this.strategy = strategy;
     }

     public int calculate(int km){
         return strategy.calculate(km);
     }
 }

 interface CalculationStrategy {
    int calculate(int km);
}

 class BusStrategy implements CalculationStrategy {
    @Override
    public int calculate(int km) {
        return 2;
    }
}

 class TaxiStrategy implements CalculationStrategy {
    @Override
    public int calculate(int km) {
        if(km <= 3){
            return 14;
        }
        return (km - 3) * 1 + 14;
    }
}
