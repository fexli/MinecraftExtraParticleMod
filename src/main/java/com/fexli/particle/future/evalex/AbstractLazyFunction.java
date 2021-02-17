package com.fexli.particle.future.evalex;


import java.util.Locale;

/**
 * Abstract implementation of a lazy function which implements all necessary
 * methods with the exception of the main logic.
 */
public abstract class AbstractLazyFunction implements LazyFunction {
    /**
     * Name of this function.
     */
    protected String name;
    /**
     * Number of parameters expected for this function. <code>-1</code>
     * denotes a variable number of parameters.
     */
    protected int numParams;

    /**
     * Whether this function is a boolean function.
     */
    protected boolean booleanFunction;

    /**
     * Creates a new function with given name and parameter count.
     *
     * @param name
     *            The name of the function.
     * @param numParams
     *            The number of parameters for this function.
     *            <code>-1</code> denotes a variable number of parameters.
     * @param booleanFunction
     *            Whether this function is a boolean function.
     */
    protected AbstractLazyFunction(String name, int numParams, boolean booleanFunction) {
        this.name = name.toUpperCase(Locale.ROOT);
        this.numParams = numParams;
        this.booleanFunction = booleanFunction;
    }

    /**
     * Creates a new function with given name and parameter count.
     *
     * @param name
     *            The name of the function.
     * @param numParams
     *            The number of parameters for this function.
     *            <code>-1</code> denotes a variable number of parameters.
     */
    protected AbstractLazyFunction(String name, int numParams) {
        this(name, numParams, false);
    }

    public String getName() {
        return name;
    }

    public int getNumParams() {
        return numParams;
    }

    public boolean numParamsVaries() {
        return numParams < 0;
    }

    public boolean isBooleanFunction() {
        return booleanFunction;
    }
}