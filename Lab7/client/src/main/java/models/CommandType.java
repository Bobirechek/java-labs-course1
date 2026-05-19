package models;

import java.io.Serializable;

public enum CommandType implements Serializable {
    ADD,
    CLEAR,
    COUNT_BY_SOUNDTRACK_NAME,
    EXECUTE_SCRIPT,
    EXIT,
    FILTER_CONTAINS_NAME,
    HELP,
    INFO,
    LOGIN,
    LOGOUT,
    PRINT_FIELD_DESCENDING_IMPACT_SPEED,
    REGISTER,
    REMOVE_BY_ID,
    REMOVE_GREATER,
    REORDER,
    SHOW,
    SORT,
    UPDATE
}