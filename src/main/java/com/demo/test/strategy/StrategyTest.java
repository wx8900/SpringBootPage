package com.demo.test.strategy;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class StrategyTest {

    private Operate operate;

    @org.junit.Test
    public void test() {
        setOperate(OperateImpl.ADD);
        Optional.ofNullable(operate.operate(1, 2))
                .ifPresent(System.out :: println);

        setOperate(OperateImpl.SUB);
        Optional.ofNullable(operate.operate(1, 2))
                .ifPresent(System.out :: println);
    }

    public interface Operate{
        float operate(float x, float y);
    }

    public enum OperateImpl implements Operate{
        ADD((x,y)->{return x+y;}),
        SUB((x,y)->{return x-y;});

        private BinaryOperator<Float> operator;

        OperateImpl(BinaryOperator<Float>  operator) {
            this.operator = operator;
        }

        @Override
        public float operate(float x, float y) {
            return this.operator.apply(x, y);
        }
    }

    public Operate getOperate() {
        return operate;
    }

    public void setOperate(Operate operate) {
        this.operate = operate;
    }

}
