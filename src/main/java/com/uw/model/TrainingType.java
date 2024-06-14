package com.uw.model;

import jakarta.persistence.*;

@Entity
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_NAME")
    private TrainingTypeEnum typeName;

    public TrainingType() {
        // no-args constructor
    }

    public TrainingType(TrainingTypeEnum typeName) {
        this.typeName = typeName;
    }

    public TrainingTypeEnum getTypeName() {
        return typeName;
    }

    public void setTypeName(TrainingTypeEnum typeName) {
        this.typeName = typeName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTypeName(String typeName){
        switch (typeName.toUpperCase()) {
            case "YOGA":
                this.typeName = TrainingTypeEnum.YOGA;
                break;
            case "STRENGTH":
                this.typeName = TrainingTypeEnum.STRENGTH;
                break;
            case "FUNCTIONAL":
                this.typeName = TrainingTypeEnum.FUNCTIONAL;
                break;
            case "CARDIO":
                this.typeName = TrainingTypeEnum.CARDIO;
                break;
            default:
                this.typeName = TrainingTypeEnum.OTHER;
                break;
        }
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", typeName=" + typeName +
                '}';
    }
}
