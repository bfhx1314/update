package com.limn.common;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class OwnTask extends Task {
    private String prop1;
    private String propertyName;

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public void setProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public void execute() throws BuildException {
        System.out.println("Execute Customized task " + prop1);
        setProp1(prop1 + " Changed");
        // prop1 = prop1 + " Changed";
        System.out.println("Execute Customized task " + prop1);

        getProject().setNewProperty(this.propertyName, "test change value in own task");
    }

}
