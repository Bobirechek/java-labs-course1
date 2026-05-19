package managers;

public class DatabaseConfig {

    // public static final String HOST = "localhost";
    // public static final String PORT = "5432";
    // public static final String NAME = "studs";
    // public static final String USER = "postgres";
    // public static final String PASSWORD = "postgres";

    public static final String HOST = "pg";
    public static final String PORT = "5432";
    public static final String NAME = "studs";
    public static final String USER = "s502846";
    public static final String PASSWORD = "IThG-4141";


    public static String getUrl() {
        return "jdbc:postgresql://" + HOST + ":" + PORT + "/" + NAME;
    }
}