package com.fexli.particle.dumped.fexpression;

import java.util.Map;
import java.util.Random;

public interface IExpression<Output, Input extends Number> {
    Output invoke(Map<String, Input> paramMap);

    public enum Operator {
        ADD("+"),
        SUB("-"),
        NEG("-"),
        MUL("*"),
        DIV("/"),
        POW("^"),
        ASS("="),
//        APPEND("+="),
        EQU("=="),
        NEQ("!="),
        GRE(">"),
        LES("<"),
        GREEQU(">="),
        LESEQU("<="),
        AND("&"),
        OR("|"),
        NOT("!"),
        SPL(","),
        SPLLIN(",,");

        private String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return this.operator;
        }
    }

    public enum Function {
        SIN("sin", Math::sin),
        COS("cos", Math::cos),
        TAN("tan", Math::tan),
        ASIN("asin", Math::asin),
        ACOS("acos", Math::acos),
        ATAN("atan", Math::atan),
        TORADIANS("toRadians", Math::toRadians),
        TODEGREES("toDegrees", Math::toDegrees),
        EXP("exp", Math::exp),
        LN("ln", Math::log),
        LOG("log", Math::log10),
        SQRT("sqrt", Math::sqrt),
        CBRT("cbrt", Math::cbrt),
        CEIL("ceil", Math::ceil),
        FLOOR("floor", Math::floor),
        RINT("rint", Math::rint),
        ROUND("round", Function::round),
        ABS("abs", Function::abs),
        ULP("ulp", Function::ulp),
        SIGNUM("signum", Function::signum),

        SINH("sinh", Math::sinh),
        COSH("cosh", Math::cosh),
        TANH("tanh", Math::tanh),
        MAX("max", Function::max),
        MIN("min", Function::min),
        HYPOT("hypot", Math::hypot),
        ATAN2("atan2", Math::atan2),
        POW("pow", Math::pow),
        IEEEREMAINDER("mod", Math::IEEEremainder),
        IF("if",Function::ifcondition),
        RAND("rand", Function::rand);

        private static Random random = new Random();

        private String name;

        private Fun1P fun1p;

        private Fun2P fun2p;

        private Fun3P fun3p;

        private int paramCount;

        static {

        }

        private static double rand(double max) {
            return random.nextDouble() * max;
        }

        private static double round(double a) {
            return Math.round(a);
        }
        private static double abs(double a) {
            return Math.abs(a);
        }
        private static double ulp(double d) {
            return Math.ulp(d);
        }
        private static double signum(double x) {
            return Math.signum(x);
        }
        private static double min(double a,double b) {
            return Math.min(a,b);
        }
        private static double max(double a,double b) {
            return Math.max(a,b);
        }
        private static double ifcondition(double bl, double iftrue,double iffalse) {
            if (bl > 0) {return iftrue;} return iffalse;
        }

        Function(String name, Fun1P fun) {
            this.name = name;
            this.fun1p = fun;
            this.paramCount = 1;
        }

        Function(String name, Fun2P fun) {
            this.name = name;
            this.fun2p = fun;
            this.paramCount = 2;
        }
        Function(String name, Fun3P fun){
            this.name = name;
            this.fun3p = fun;
            this.paramCount = 3;
        }

        public String getName() {
            return this.name;
        }

        public int getParamCount() {
            return this.paramCount;
        }

        public double invoke(double... in) {
            double result = 0.0D;
            switch (this.paramCount) {
                case 1:
                    result = this.fun1p.invok(in[0]);
                    break;
                case 2:
                    result = this.fun2p.invok(in[0], in[1]);
                    break;
                case 3:
                    result = this.fun3p.invoke(in[0],in[1],in[2]);
            }
            return result;
        }

        @FunctionalInterface
        static interface Fun2P extends Fun {
            double invok(double param2Double1, double param2Double2);
        }

        @FunctionalInterface
        static interface Fun3P extends Fun{
            double invoke(double param2Double1,double param2Double2,double param2Double3);
        }

        @FunctionalInterface
        static interface Fun1P extends Fun {
            double invok(double param2Double);
        }

        static interface Fun {}
    }
}
