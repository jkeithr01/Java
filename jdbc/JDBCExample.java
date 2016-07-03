// My JDBCExample
// requires a Pointbase database and client driver JAR file: pbserver40re.jar
//
// can be modified to use *any* database by changing driver and the 
// arguments to DriverManager.getConnection(...)
//

package ratliffs.net;

import java.sql.*;

public class JDBCExample implements Runnable
{
    // The assignment of values to private variables should be replaced with
    // a static initializer once the operation of the initializer has been
    // verified.
    // Also, the values should check for the existence of a properties file
    // and load those values in favor of the internal defaults, should the 
    // file exist.
    // In addition, if command line arguments are present, they should be
    // loaded and validated for correct format.  Then, if the data is valid,
    // the variables should be initialized using command line values over a
    // properties file or the internal values.

    // default values for connecting
    // these can be overridden by calling the parameterized constructor
    private static final String driverName = 
        "com.pointbase.jdbc.jdbcUniversalDriver";
    private static final String dbURL = "jdbc:pointbase://brie:9092/league";
    private static final String username = "public";
    private static final String password = "public";
    
    // default query
    // this value can be overridden by passing a query from the command line
    private String query = "SELECT * FROM League";
    
    private Connection connection;
    
    public JDBCExample() throws SQLException
    {
        // Create the object with the default values
        this( JDBCExample.driverName, JDBCExample.dbURL, 
              JDBCExample.username, JDBCExample.password);
    }
    
    public JDBCExample(String driverName, String dbURL, String username,
                       String password) throws SQLException
    {
        try {
            // Load JDBC Driver
            Class.forName(driverName);
            // Initialize the database connection instance variable
            connection = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ERROR: The database driver class is not in " +
                "the current CLASSPATH.");
            cnfe.printStackTrace();
        }
    }
    
    public void run()
    {
        Statement statement = null;
        ResultSet rset = null;
        try {
            // Create a generic SQL statement object for arbitrary queries
            statement = connection.createStatement();
            
            // execute the query and retrieve the result set
            rset = statement.executeQuery(query);
            
            // get the number of fields from the resutl set schema
            int numfields = rset.getMetaData().getColumnCount();
            
            // process each row of the result set
            while (rset.next()) {
                for (int i = 1; i <= numfields; i++) {
                    System.out.print(rset.getString(i)+"   ");
                }
                System.out.println();
            }
        } catch (SQLException sqle) {
            System.err.println("ERROR: Database execution error.");
            sqle.printStackTrace();
        } finally {
            // clean-up all JDBC resources
            try {
                rset.close();
                statement.close();
            } catch (Exception e) {
                System.err.println("ERROR: Unexpected error; terminating " +
                    "program.");
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }
    
    public static void main(String[] args) 
    {
        // to execute *this* version of the program:
        // java -cp .:${PB_HOME}/client/lib/pbclient40re.jar \ (cont next line)
        //      sl314.example.JDBCExample
        try {
            JDBCExample example = new JDBCExample();
            if (args.length > 0) {
                if (args.length > 1) {
                    System.err.println("Usage: java <classname> <sql_query>");
                    System.exit(3);
                } else {
                    example.query = args[0];
                }
            }
            example.run();
        } catch (SQLException sqle) {
            System.err.println("ERROR: Unable to execute JDBCExample due to " +
                "a catastrophic database error.");
            sqle.printStackTrace();
            System.exit(2);
        }
    }
}

