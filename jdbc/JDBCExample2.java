import java.sql.*;

public class JDBCExample2 {
    public static void main(String[] args) throws Exception {
        /*
        ** Step 1: Load the driver class
        ** 
        ** Note: Class.forName() may not resolve the class and load the 
        **       bytecode into memory.  If it does not, use the command
        **       Class.forName(driverName).newInstance() instead.
        **
        */
        //String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
        String driverName = "com.imaginary.sql.msql.MsqlDriver";
        Class.forName(driverName);

        /*
        ** Step 2: Create a URL String to the database
        ** 
        ** Format: protocol:subprotocol://dbhostname:port/database
        ** 
        */ 
        //String databaseLocation = "jdbc:odbc:Coffees.mdb";
        String databaseLocation = "jdbc:msql://localhost:4333/le-shop";

        /*
        ** Step 3: Get a Connection from the DriverManager class.
        ** 
        */ 
        Connection dbConnection;
        dbConnection = DriverManager.getConnection(databaseLocation);

        /*
        ** Step 4: Create a Statement for use with the database.
        ** 
        ** The type of Statement object created depends on the need.
        ** The Statement interface defines reusable objects for issuing
        ** dynamic SQL to the database.
        ** The PreparedStatement interface is a subinterface of Statement
        ** that creates precompiled SQL.  The precompiled statements may be 
        ** parameterized.
        ** The CallableStatement interface is a subinterface of 
        ** PreparedStatement that involves stored procedures.  The procedure 
        ** may or may not accept a parameterized result.  If it does, the
        ** result parameter must be registered as an out parameter.
        ** 
        */ 
        Statement sqlStatement;
        sqlStatement = dbConnection.createStatement();

        /*
        ** Step 5: Issue the command to the database and retrieve the result.
        ** 
        ** The result from a command can be a parameterized result (Callable)
        ** or a directly issued result.  Direct results are either an integer
        ** indicating the number of rows modified in response to an update or
        ** a ResultSet object in response to a query.
        ** 
        */ 
        ResultSet result;
        String queryString = "SELECT * FROM COFFEES";
        result = sqlStatement.executeQuery(queryString);

        /*
        ** Step 6: Process the result received from the command.
        ** 
        ** ResultSet objects are processed using a combination of row
        ** iteration, using the next() method, and field extraction, using the
        ** various getXXX() methods.  The next() method will return a 
        ** boolean value that is true when the cursor points to a valid row
        ** of data.  The getXXX() methods return a value that is of the same
        ** type as the XXX substring of the method name.  Note that the 
        ** getString() method is a universal field extractor, and will work on
        ** any field type.
        ** 
        ** Note also that the getXXX() methods are overloaded to retrieve
        ** fields by field name.  This approach can give unpredictable 
        ** results, however.  If a ResultSet object has two fields with the 
        ** same name, the method will return the value on the field with 
        ** the first occurence of that field name.
        **
        */ 
        while(result.next()) {
            System.out.println("Coffee: "+result.getString(1));
            System.out.println("Supplier: "+result.getString(2));
            System.out.println("Price: "+result.getString(3));
            System.out.println("Sales: "+result.getString(4));
        }

        /*
        ** Step 7: Close the connection and release all resources.
        ** 
        */ 
        result.close();
        sqlStatement.close();
        dbConnection.close();
    }
}
