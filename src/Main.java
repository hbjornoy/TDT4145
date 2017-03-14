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

    public static void createInstance() {

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

    public static Connection createConnection() {

        String dbURL = "jdbc:mysql://localhost:3306/Datdat?useSSL=false";
        String username = "root";
        String password = "Eol1234";


        try {
            System.out.println("Database connected");
            return DriverManager.getConnection(dbURL, username, password);
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

        System.out.println("Tast inn dato og tid til økten (Format: 2017-03-15 18:00:00)");
        String time = reader.nextLine();

        System.out.println("Tast inn Økt-ID nr");
        int oktid = reader.nextInt();

        System.out.println("Tast inn varighet i timer (Format: 18:00:00)");
        String duration = reader.nextLine();

        String SQL = ("INSERT INTO `espenespen`.`treningsokt` (`dato`, `oktid`, `varighet`) values ('" + time + "', " + oktid + ", '" + duration + "')");
        //System.out.println(SQL)

        try {
            System.out.println("Opprettet trening");
            stmt.executeUpdate(SQL);
        } catch (SQLException ex) {
            System.out.println("Could not create workout");
            printException(ex);
        }

    }

    public static void createOvelse(Statement stmt, Scanner reader){

        //Bugfix
        reader.nextLine();

        System.out.println("Tast inn navn på øvelsen");
        String name = reader.nextLine();

        System.out.println("Hvilken type er øvelsen? (Styrke/Kondisjon)");
        int type = reader.nextInt();

        String SQL = ("INSERT INTO ovelse (ovelsenavn, treningstype) values ('" + name + "', " + type + ")");
        //System.out.println(SQL);

        try {
            System.out.println("Opprettet øvelse");
            stmt.executeUpdate(SQL);
        } catch (SQLException ex) {
            System.out.println("Kunne ikke opprette øvelse");
            printException(ex);
        }

    }

    public static void printOvelse(ResultSet ovelse){
        int count = 0;
        try {
            System.out.println("Printing workouts");
            while(ovelse.next())
            {
                ++count;
                System.out.println(ovelse.getString("ovelsenavn") + " - " + ovelse.getString("treningstype") + " - ");
            }
            if (count == 0) {
                System.out.println("Ingen øvelser funnet");
            }

        } catch (SQLException ex) {
            System.out.println("Could not print workouts");
            printException(ex);
        }

    }

    public static void printMenu() {
        System.out.println("-------------------");
        System.out.println("Velg et alternativ:");
        System.out.println("2) Skriv ut treninger");
        System.out.println("3) Skriv ut øvelser");
        System.out.println("4) Skriv ut resultater");
        System.out.println("5) Skriv ut kategorier");
        System.out.println("6) Skriv ut mål");
        System.out.println("8) Opprett ny trening");
        System.out.println("9) Opprett ny øvelse");
        System.out.println("10) Opprett nytt resultat");
        System.out.println("11) Opprett ny kategori");
        System.out.println("13) Generer rapport");
        System.out.println("14) Generer statistikk");
    }

    public static void main(String[] args) {

        //Variables
        Boolean active = true;
        Scanner reader = new Scanner(System.in);

        //Instance
        createInstance();
        Connection conn = createConnection();
        Statement stmt = createStatement(conn);
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Datdat.ovelse");
            printOvelse(rs);
            createOvelse(stmt, reader);
            System.out.println(rs.toString());
            System.out.println("works");
        } catch (SQLException e) {
            System.out.println("Rip");
        }
    }

    //Extra functions
    public static void printException(SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

}