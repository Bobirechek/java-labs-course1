package models;

import java.io.Serializable;

public enum CommandType implements Serializable {
    ADD,
    CLEAR,
    COUNT_BY_SOUNDTRACK_NAME,
    EXIT,
    FILTER_CONTAINS_NAME,
    HELP,
    INFO,
    PRINT_FIELD_DESCENDING_IMPACT_SPEED,
    REMOVE_BY_ID,
    REMOVE_GREATER,
    REORDER,
    SAVE,
    SHOW,
    SORT,
    UPDATE
}