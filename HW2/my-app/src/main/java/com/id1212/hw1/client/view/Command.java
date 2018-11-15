package com.id1212.hw1.client.view;


// command that is received from a user
public class Command {
    private static final String PARAM_DELIMETER = " ";
    private Cmd type;
    private String[] params;

    public Command(String line) {
        parseline(line);
    }

    // parse a line to see what kind of mesage it is
    private void parseline(String line) {
        try {
            params = line.split(PARAM_DELIMETER);
            type = Cmd.valueOf(params[0].toUpperCase());
        } catch (Exception e) {
            // Illegal command
            type = Cmd.ILLEGAL;
        }
    }

    public String getParameter(int index) {
        if(params != null && index < params.length) {
            return params[index];
        }
        return null;
    }

    /**
     * @return the type
     */
    public Cmd getType() {
        return type;
    }
}