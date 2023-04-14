package com.surveybaba.model.PolylineViewLayerModel;

public class Feature {

    private String type;
    private Geometry geometry;
    private Properties properties;
    private String id;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public String getType() {
        return type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getId() {
        return id;
    }


//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setType(String type) {
        this.type = type;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setId(String id) {
        this.id = id;
    }
}
