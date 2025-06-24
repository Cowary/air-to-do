package org.cowary.airtodo.entity;

import lombok.Getter;

@Getter
public enum Priority {
    NO_PRIORITY(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    VERY_HIGH(4),
    IMMEDIATELY(5);

    private final Integer priorityNumber;

    Priority(Integer priorityNumber) {
        this.priorityNumber = priorityNumber;
    }

    public static Priority findByPriorityNumber(Integer priorityNumber) {
        return Priority.values()[priorityNumber];
    }

}
