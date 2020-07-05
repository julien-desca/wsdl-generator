package com.jdesca.wsdlgenerator.entity.wsdlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Logical representation of <wsdl:message> tag
 */
public class WsdlMessage {
    
    private String name;
    
    private List<WsdlPart> parts;

    public WsdlMessage(String name, List<WsdlPart> parts) {
        this.name = name;
        this.parts = parts;
    }

    public WsdlMessage() {
        this.parts = new ArrayList<WsdlPart>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WsdlPart> getParts() {
        return parts;
    }

    public void setParts(List<WsdlPart> parts) {
        this.parts = parts;
    }
    
    
}
