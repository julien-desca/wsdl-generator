package com.jdesca.wsdlgenerator.entity.xsd;


public class XsdImportedSchema {
    private String namespace;
    
    private String schemaLocation;

    public XsdImportedSchema(String namespace, String schemaLocation) {
        this.namespace = namespace;
        this.schemaLocation = schemaLocation;
    }

    public XsdImportedSchema() {
    }

    
    
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation) {
        this.schemaLocation = schemaLocation;
    }
    
    
}
