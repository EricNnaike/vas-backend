package com.oasis.cac.vas.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountBuilderDto {

        private String label;
        private String title;
        private Long price;
        private int count;
        private boolean value;
        private boolean disabled;
        private String type;
        private List<CountBuilderDto> innerFields;

        public CountBuilderDto() {
            this.price = 30000L;
            this.type = "checkbox";
            this.disabled = true;
        }

    @Override
    public String toString() {
        return "CountBuilderDto{" +
                "label='" + label + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", value=" + value +
                ", disabled=" + disabled +
                ", type='" + type + '\'' +
                '}';
    }
}
