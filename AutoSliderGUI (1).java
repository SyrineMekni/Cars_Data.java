import javax.swing.*; 
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.sql.*; 

public class AutoSliderGUI extends JFrame {

    private JSlider mpgSlider; // MPG slider component 
    private JSlider horsepowerSlider; //horsepower slider component 
    private JTextArea resultArea; // This is for displaying results

    public AutoSliderGUI() {
        setTitle("Auto MPG and Horsepower Filter"); // GUI window title
        setSize(400, 300); // GUI window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // setting the close operation

        mpgSlider = new JSlider(0, 50, 0); // MPG slider with 0-50 rane and initialized by value 0
        horsepowerSlider = new JSlider(0, 300, 0); // horsepower slider with 0-300 range and initialized by value 0
        resultArea = new JTextArea(); // This is to display results
        
        //This set the ajor tick space = 10 and the minor ticks to 1 and make them both visible
        mpgSlider.setMajorTickSpacing(10); 
        mpgSlider.setMinorTickSpacing(1); 
        mpgSlider.setPaintTicks(true); 
        mpgSlider.setPaintLabels(true); 
        
      //This set the ajor tick space = 10 and the minor ticks to 1 and make them both visible
        horsepowerSlider.setMajorTickSpacing(50);
        horsepowerSlider.setMinorTickSpacing(10); 
        horsepowerSlider.setPaintTicks(true); 
        horsepowerSlider.setPaintLabels(true); 
        
        
        //This is to make the user able to slide though MPG values and horsepower values and display data accordinely
        mpgSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedMpg = mpgSlider.getValue();
                int selectedHorsepower = horsepowerSlider.getValue(); 
                filterData(selectedMpg, selectedHorsepower); 
            }
        });
        
        
      //This is also to make th euser able to slide though MPG values and horsepower values and display data accordinely
        horsepowerSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedMpg = mpgSlider.getValue();
                int selectedHorsepower = horsepowerSlider.getValue(); 
                filterData(selectedMpg, selectedHorsepower);
            }
        });
        
        //This is to set the BorderLayout for the MPG  and horspower slider and adding the result area
        setLayout(new BorderLayout()); 
        add(mpgSlider, BorderLayout.NORTH); 
        add(horsepowerSlider, BorderLayout.CENTER); 
        add(new JScrollPane(resultArea), BorderLayout.SOUTH); 
    }

    private void filterData(int selectedMpg, int selectedHorsepower) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Auto", "testuser", "Pa$$word");
             Statement stmt = conn.createStatement()) {
            // This is to connect with the database "Auto" 
            String sql = "SELECT * FROM mpg WHERE mpg >= " + selectedMpg + " AND horsepower >= " + selectedHorsepower;
            // Constructing the SQL query to filter data based on selected MPG and horsepower values

            ResultSet rs = stmt.executeQuery(sql); // Execute the SQL query and obtain the result set
            displayResults(rs); // display the query results
        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace if an exception occurs during database operations
        }
    }

    private void displayResults(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder(); // Creating a StringBuilder to store the result text

        ResultSetMetaData rsmd = rs.getMetaData(); // Getting the metadata of the result set
        int columnCount = rsmd.getColumnCount(); // Getting the number of columns in the result set

        // Appending column names
        for (int i = 1; i <= columnCount; i++) {
            sb.append(rsmd.getColumnName(i)).append("\t"); 
        }
        sb.append("\n"); // Add new line after the every column names

        // Appending rows
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                sb.append(rs.getString(i)).append("\t"); // Appending each cell value to the StringBuilder
            }
            sb.append("\n"); // Add new line after the every row
        }

        resultArea.setText(sb.toString()); // Set the text of the result area to the content of the StringBuilder
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                AutoSliderGUI gui = new AutoSliderGUI(); // Create an instance of the AutoSliderGUI class
                gui.setVisible(true); // Make the GUI window visible
            }
        });
    }
}