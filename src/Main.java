import java.sql.*;
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

        String dbURL = "jdbc:mysql://localhost:3306/espenespen?useSSL=false";
        String username = "root";
        String password = "passord";


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

        //Bugfix pga. nextInt i main
        reader.nextLine();

        System.out.println("Tast inn dato til økten (Format: 2017-03-15)");
        String dato = reader.nextLine().trim();

        System.out.println("Tast inn starttid på økten (Format: 18:00:00)");
        String tid = reader.nextLine().trim();

        System.out.println("Tast inn Økt-ID nr");
        String oktid = reader.nextLine().trim();

        System.out.println("Tast inn varighet i timer (Format: 00:45:00)");
        String varighet = reader.nextLine().trim();

        String SQL = ("INSERT INTO `espenespen`.`treningsokt` (`dato`, `starttid`, `oktid`, `varighet`) values ('" + dato + "', '" + tid + "', '" + oktid + "', '" + varighet + "');");
        System.out.println(SQL);

        try {
            stmt.executeUpdate(SQL);
            System.out.println("Opprettet trening");
        } catch (SQLException ex) {
            System.out.println("Could not create workout");
            printException(ex);
        }

    }

    public static ResultSet getWorkouts(Statement stmt){
        try {
            // System.out.println("Fetching workouts");
            return stmt.executeQuery("SELECT * FROM treningsokt ");
        } catch (SQLException ex) {
            System.out.println("Could not fetch workouts");
            printException(ex);
        }
        return null;
    }

    public static void printWorkouts(ResultSet workouts) {
        int count = 0;
        try {
            while(workouts.next())
            {
                ++count;
                System.out.println("Workout_ID: " + workouts.getInt("oktid") +
                        " ----- Start: " + workouts.getDate("dato").toString() + "  " + readableTimestamp(workouts.getTimestamp("starttid")) +
                        " ----- Length: " + readableTimestamp(workouts.getTimestamp("varighet"))
                );
            }
            if (count==0)
            {
                System.out.println("Det finnes ingen treninger");
            }
        }
        catch (SQLException ex)
        {
            System.out.println("Was not able to print workouts");
            printException(ex);

        }
    }

    public static void createData(Statement stmt, Scanner reader){

        //Bugfix pga. nextInt i main
        reader.nextLine();

        System.out.println("Tast inn tidspunkt til dataen (Format: 2017-03-15 18:00:00)");
        String tid = reader.nextLine().trim();

        System.out.println("Tast inn puls (int)");
        String puls = reader.nextLine().trim();

        System.out.println("Tast inn lengdegrad");
        String lengdegrad = reader.nextLine().trim();

        System.out.println("Tast inn breddegrad");
        String breddegrad = reader.nextLine().trim();

        System.out.println("Tast inn meter over havet");
        String masl = reader.nextLine().trim();

        System.out.println("Tast inn Økt_ID");
        String oktid = reader.nextLine().trim();

        String SQL = ("INSERT INTO `espenespen`.`data` (`tid`, `puls`, `lengdegrad`, `breddegrad`, `hoydeOverHavet`, `oktid`) values ('" + tid + "', '" + puls + "', '" + lengdegrad + "', '" + breddegrad + "', '" + masl + "', '" + oktid + "');");
        System.out.println(SQL);

        try {
            stmt.executeUpdate(SQL);
            System.out.println("Opprettet trening");
        } catch (SQLException ex) {
            System.out.println("Could not create workout");
            printException(ex);
        }

    }

    public static ResultSet getData(Statement stmt){
        try {
            return stmt.executeQuery("SELECT * FROM data");
        } catch (SQLException ex) {
            System.out.println("Could not fetch data");
            printException(ex);
        }
        return null;
    }

    public static void printData(ResultSet data) {
        int count = 0;
        try {
            while(data.next())
            {
                ++count;
                System.out.println(" ----- WorkOut_ID: " + data.getInt("oktid") +
                        " ----- Time: " + data.getTimestamp("tid") +
                        " ----- Puls: " + data.getInt("puls") +
                        " ----- Longitude, latitude and MASL: " + data.getInt("lengdegrad") + ",  " +
                        data.getInt("breddegrad") + ",  " + data.getInt("hoydeOverHavet")
                );
            }
            if (count == 0) {
                System.out.println("there is no data in the database");
            }
        }
        catch (SQLException ex) {
            System.out.println("Could not print out data");
            printException(ex);
        }
    }

    public static void createOvelse(Statement stmt, Scanner reader){

        //Bugfix
        reader.nextLine();
        boolean correct = false;

        System.out.println("Tast inn navn på øvelsen");
        String name = reader.nextLine().trim();

        System.out.println("Hvilken type er øvelsen? (styrke/kondisjon)");
        String type = "";
        while (!correct) {
            type = reader.nextLine().trim();
            if (type.equals("styrke") || type.equals("kondisjon")) {
                correct = true;
            }
            else {
                System.out.println("Hvilken type er øvelsen? (øvelsen må være styrke eller kondisjon..)");
            }
        }

        String SQL = ("INSERT INTO ovelse (ovelsenavn, treningstype) values ('" + name + "', '" + type + "')");
        //System.out.println(SQL);

        try {
            System.out.println("Opprettet øvelse");
            stmt.executeUpdate(SQL);
        } catch (SQLException ex) {
            System.out.println("Kunne ikke opprette øvelse");
            printException(ex);
        }

    }

    public static ResultSet getOvelse(Statement stmt){
        try {
            return stmt.executeQuery("SELECT * FROM ovelse");
        } catch (SQLException ex) {
            System.out.println("Could not fetch ovelse");
            printException(ex);
        }
        return null;
    }

    public static void printOvelse(ResultSet ovelse) {
        int count = 0;
        try {
            System.out.println("Printing workouts");
            while (ovelse.next()) {
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
        System.out.println("1) Skriv ut treninger");
        System.out.println("2) Skriv ut data(puls osv)"); // kan få til å spørre om oktid osv
        System.out.println("3) Skriv ut øvelser");
        //System.out.println("3) Skriv ut resultater");
        //System.out.println("4) Skriv ut kategorier");
        //System.out.println("5) Skriv ut mål");
        System.out.println("6) Opprett ny trening");
        System.out.println("7) Opprett ny data(pusl osv)");
        System.out.println("8) Opprett ny øvelse");
        //System.out.println("8) Opprett nytt resultat");
        //System.out.println("9) Opprett ny kategori");
        // System.out.println("10) Generer rapport");
        // System.out.println("11) Generer statistikk");
    }

    public static String readableTimestamp(Timestamp timestamp){
        return new SimpleDateFormat("HH:mm:ss").format(timestamp).toString();
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
            while (active) {
                printMenu();
                int alternative = reader.nextInt();
                System.out.println("----------------------------");
                switch(alternative){
                    case 1:
                        printWorkouts(getWorkouts(stmt));
                        break;
                    case 2:
                        printData(getData(stmt));
                        break;
                    case 3:
                        printOvelse(getOvelse(stmt));
                        break;
                    case 6:
                        createWorkout(stmt, reader);
                        break;
                    case 7:
                        createData(stmt, reader);
                        break;
                    case 8:
                        createOvelse(stmt, reader);
                        break;
                    default:
                        System.out.println("Ugyldig handling");

                }
            }

        } catch (Exception e) {
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