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

    public void setTypeName(String typeName){
        if (typeName.equalsIgnoreCase("YOGA")){
            this.typeName = TrainingTypeEnum.YOGA;
        }else if (typeName.equalsIgnoreCase("STRENGTH")){
            this.typeName = TrainingTypeEnum.STRENGTH;
        }else if (typeName.equalsIgnoreCase("FUNCTIONAL")){
            this.typeName = TrainingTypeEnum.FUNCTIONAL;
        }else if (typeName.equalsIgnoreCase("CARDIO")){
        }else{
            this.typeName = TrainingTypeEnum.OTHER;
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
