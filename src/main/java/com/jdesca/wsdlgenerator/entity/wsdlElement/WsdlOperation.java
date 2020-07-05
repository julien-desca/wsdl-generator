package com.jdesca.wsdlgenerator.entity.wsdlElement;


public class WsdlOperation {
    String name;
    wsdlInput input;
    WsdlOutput output;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public wsdlInput getInput() {
        return input;
    }

    public void setInput(wsdlInput input) {
        this.input = input;
    }

    public WsdlOutput getOutput() {
        return output;
    }

    public void setOutput(WsdlOutput output) {
        this.output = output;
    }
    
    
    
}
