package com.blueeaglecreditunion.script;

public class myPassword {
    private static String pass = "Black@rose";
    private static String username = "wsmith";
    private static String databaseName = "jdbc:db2://208.69.139.109:50000";

    public myPassword() {
    }

    public myPassword(String password, String user, String database) {
        pass = password;
        username = user;
        databaseName = database;
    }

    public static String getPassword() {
        return pass;
    }

    public static String getUsername() {
        return username;
    }

    public static String getDatabase() {
        return databaseName;
    }
}

