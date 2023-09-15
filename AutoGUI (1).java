import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AutoGUI extends JFrame {

    private JTextField inputField; // This is to receive the user input
    private JTextArea resultArea; // This is to display query results

    public AutoGUI() {
        setTitle("Auto MPG Data"); // This is forthe window title
        setSize(400, 300); // set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the default close operation

        inputField = new JTextField(); // This is to create a new text field
        JButton executeButton = new JButton("Execute"); // This is to create a new button
        resultArea = new JTextArea(); // This is to create a new text area

        executeButton.addActionListener(new ActionListener() { // This method is for action listener
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = inputField.getText(); // this is to get the user's input from the text field
                executeSQL(userInput); // and use the executeSQL method with the user input
            }
        });

        setLayout(new BorderLayout()); // create the layout manager for the frame
        add(inputField, BorderLayout.NORTH); // Add the input field to the top of the frame
        add(executeButton, BorderLayout.CENTER); // Add the execute button to the center of the frame
        add(new JScrollPane(resultArea), BorderLayout.SOUTH); // Add the result area with a scroll pane to the bottom of the frame
    }

    private void executeSQL(String userInput) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Auto", "testuser", "Pa$$word");
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM mpg"; // set the SQL statement to select all records from AutoTable

            if (userInput.equalsIgnoreCase("ALL")) {// "All" will display the whole data
                
                 ResultSet rs = stmt.executeQuery(sql);
                displayResults(rs); // Display the query results
            } else {
                // otherwise display what the user input indicate
                sql = userInput;
                ResultSet rs = stmt.executeQuery(sql);
                displayResults(rs); // Display the query results
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print the SQL exception stack trace if an error occurs
        }
    }

    private void displayResults(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();

        ResultSetMetaData rsmd = rs.getMetaData(); //metadata of the result set
        int columnCount = rsmd.getColumnCount(); // this will return the number of columns

        
        for (int i = 1; i <= columnCount; i++) {
            sb.append(rsmd.getColumnName(i)).append("\t"); // Append column name followed by a tab
        }
        sb.append("\n"); // Append a new line after the column names

        
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                sb.append(rs.getString(i)).append("\t"); // Append each column value followed by a tab
            }
            sb.append("\n"); // seperate content with the new line
        }
        resultArea.setText(sb.toString()); // Set the text area content with the generated string
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                AutoGUI gui = new AutoGUI(); // Create an instance of AutoGUI
                gui.setVisible(true); // Make the GUI visible
            }
        });
    }
}
