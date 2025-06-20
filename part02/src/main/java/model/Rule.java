package model;

public class Rule{
    private String query;

    private String[] arguments;

    private boolean negative;


    public Rule(String query, String[] arguments, boolean negative) {
        this.query = query;
        this.arguments = arguments;
        this.negative = negative;
    }

    public String getQuery() {
        return query;
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean isNegative() {
        return negative;
    }
}
