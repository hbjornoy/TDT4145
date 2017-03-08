import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;

// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

public class Main {

    public static void createInstance {

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Driver loaded");
        }
        catch (Exception ex){
            System.out.println("Failed to load driver");
        }

    }

    public static Connection createConnection {

        try {
            System.out.println("Database connected");
            return DriverManager.getConnection("jdbc:mysql://localhost/espenespen?" + "user=root&password=" + System.getenv("DTD4145_SERVER_PASSWORD"));
        } catch (SQLException ex) {
            System.out.println("Failed to connect to DB");
            printException(ex);
        }

        return null;

    }

    public static Statement createStatement(Connection conn) {

        try {
            System.out.println("Statement created");
            return conn.createStatement();
        } catch (SQLException ex) {
            System.out.println("Failed to create statement");
            printException(ex);
        }

        return null;

    }

    public static void createWorkout(Statement stmt, Scanner reader){

        //Bugfix
        reader.nextLine();

        System.out.println("Tast inn navn på treningen ");
        String name = reader.nextLine();

        System.out.println("Tast inn tid (Format: 2017-03-15 18:00:00)");
        String time = reader.nextLine();

        System.out.println("Tast inn varighet i timer (Format: 2017-03-15 18:00:00)");
        String duration = reader.nextLine();

        System.out.println("Er treningen en mal? (Nei: 0, Ja:1)");
        int template = reader.nextInt();

        System.out.println("Er treningen inne eller ute? (Inne: " + WORKOUT_INDOOR + ", ute: " + WORKOUT_OUTDOOR + ")");
        int type = reader.nextInt();

        String SQL = ("INSERT INTO `espenespen`.`treningsokt` (`dato`, `oktid`, `varighet`, `prestasjon`) values ('" + dato + "', '" + oktid + "', " + varighet + ", " + prestasjon + ")");
        //System.out.println(SQL);

        c VALUES ('2017-03-15 20:00:00', '2', '01:00:00', '3');


        try {
            System.out.println("Opprettet trening");
            stmt.executeUpdate(SQL);
        } catch (SQLException ex) {
            System.out.println("Could not create workout");
            printException(ex);
        }

    }

    public static void main(String[] args) {

        //Variables
        Boolean active = true;
        Scanner reader = new Scanner(System.in);

        //Instance
        createInstance();
        Connection conn = createConnection();
        Statement stmt = createStatement(conn);

        SELECT varighet FROM espenespen.treningsokt where oktid = 1;

        String i = stmt.executeQuery("SELECT varighet FROM espenespen.treningsokt where oktid = 1");
        System.out.println(i);
    }

    //Extra functions
    public static void printException(SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

}