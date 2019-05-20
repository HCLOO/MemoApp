package com.example.memoapp.util;

public interface DBConstant {

    String DATABASE_NAME="memo";

    int DATABASE_VERSION=1;
    String TABLE_NAME_INFO="memo_information";
    String ID_INFO="id";
    String CONTENT_INFO="content";
    String ATTRIBUTE_INFO="attribute";
    String DATE_IFNO="date";
    String CREATE_INFO_TABLE="create table "+TABLE_NAME_INFO+"("+
            ID_INFO+" integer primary key autoincrement,"+
            CONTENT_INFO+" text," +
            ATTRIBUTE_INFO+" integer,"+
            DATE_IFNO+" text)";

    int DEFAULT=0;
    int DONE=1;
}
