package com.uw.dto;

public class TrainingTypeDTO {
    private long id;
    private String typeName;

    public TrainingTypeDTO(long id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
