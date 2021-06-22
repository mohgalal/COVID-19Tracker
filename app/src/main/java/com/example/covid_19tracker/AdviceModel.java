package com.example.covid_19tracker;

import java.io.Serializable;

public class AdviceModel implements Serializable {
    private  int adviceImage;
    private String adviceDescription;

    public AdviceModel(int adviceImage, String adviceDescription) {
        this.adviceImage = adviceImage;
        this.adviceDescription = adviceDescription;
    }

    public int getAdviceImage() {
        return adviceImage;
    }

    public void setAdviceImage(int adviceImage) {
        this.adviceImage = adviceImage;
    }

    public String getAdviceDescription() {
        return adviceDescription;
    }

    public void setAdviceDescription(String adviceDescription) {
        this.adviceDescription = adviceDescription;
    }
}
