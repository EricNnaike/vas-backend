package com.oasis.cac.vas.pojo.count_builder;

import lombok.Data;

import java.util.List;

/*
* 60 kobo make one naira
* */

@Data
public class CountBuilder {

    private String label;
    private String title;
    private Long price;
    private int count;
    private boolean value;
    private boolean disabled;
    private String type;
    private List<CountBuilder> innerFields;

    public CountBuilder() {
        this.price = 30000L;
        this.value = false;
        this.type = "checkbox";
        this.disabled = true;
    }


    @Override
    public String toString() {
        return "CountBuilder{" +
                "label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                '}';
    }
}
