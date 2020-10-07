import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.sql.*; //api specification
import java.util.Vector;


public class MainPanel extends JPanel {
	private JLabel title;
	private TextArea textArea;
	private JScrollPane scrollPane;
	private JButton addButton;
	private JButton modifyButton;
	private JButton deleteButton;
	private JButton displayButton;
	private JButton queryButton;

	
	//create the GUI
	public MainPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEtchedBorder());
		
		//top title section
		title = new JLabel ("SQL Functionality");
		title.setFont(new Font("Time New Roman", Font.BOLD, 20));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		panel1.add(title);
		add(panel1, BorderLayout.NORTH);
		
		//middle text area section
		textArea = new TextArea();
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		add(textArea, BorderLayout.CENTER);
		
		
		//bottom section
		addButton = new JButton("ADD");
		modifyButton = new JButton("MODIFY");
		deleteButton = new JButton("DELETE");
		displayButton = new JButton("DISPLAY");
		queryButton = new JButton("RUN QUERY");
		addButton.addActionListener(new EventListener());
		deleteButton.addActionListener(new EventListener());
		displayButton.addActionListener(new EventListener());
		modifyButton.addActionListener(new EventListener());
		queryButton.addActionListener(new EventListener());
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout());
		panel3.add(addButton);
		panel3.add(modifyButton);
		panel3.add(deleteButton);
		panel3.add(displayButton);
		panel3.add(queryButton);
		add(panel3, BorderLayout.SOUTH);
	}
	
	
	private class EventListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			JButton clicked = (JButton)e.getSource();
			
			//when add is clicked
			if (clicked == addButton)
			{
				//labels
				JLabel addTitle = new JLabel("Add to Club Table");
				addTitle.setFont(new Font("Time New Roman", Font.BOLD, 20));
				JLabel clubName = new JLabel("c_name");
				JLabel clubID = new JLabel("clubID");
				JLabel president = new JLabel("President");
				JLabel vicePresident = new JLabel("vicePresident");
				JLabel clubBalance = new JLabel("clubBalance");
				
				//text fields with 20 characters in each each
				JTextField clubNameField = new JTextField(20);
				JTextField clubIDField = new JTextField(20);
				JTextField presidentField = new JTextField(20);
				JTextField vicePresidentField = new JTextField(20);
				JTextField clubBalanceField = new JTextField(20);
				
				//left panel
				JPanel leftPanel = new JPanel();
				leftPanel.setLayout(new GridLayout(5,1));
				leftPanel.add(clubName);
				leftPanel.add(clubID);
				leftPanel.add(president);
				leftPanel.add(vicePresident);
				leftPanel.add(clubBalance);
				
				//right panel
				JPanel rightPanel = new JPanel();
				rightPanel.setLayout(new GridLayout(5,1));
				rightPanel.add(clubNameField);
				rightPanel.add(clubIDField);
				rightPanel.add(presidentField);
				rightPanel.add(vicePresidentField);
				rightPanel.add(clubBalanceField);
				
				//top panel
				JPanel topPanel = new JPanel();
				topPanel.setLayout(new FlowLayout());
				topPanel.add(addTitle);
				
				//overall panel
				JPanel allPanel = new JPanel();
				allPanel.setLayout(new BorderLayout());
				allPanel.add(topPanel, BorderLayout.NORTH);
				allPanel.add(leftPanel, BorderLayout.WEST);
				allPanel.add(rightPanel, BorderLayout.CENTER);
				
				//pop up
				int result = JOptionPane.showConfirmDialog(null, allPanel, 
			               "ADD", JOptionPane.OK_OPTION);
				
				//add to the database
				if (result == JOptionPane.OK_OPTION) {
					boolean canQuery = true;
					try {
						//load driver
						Class.forName("oracle.jdbc.driver.OracleDriver");

						//create connection
						Connection conn = DriverManager.getConnection(  
								"jdbc:oracle:thin:@acaddb2.asu.edu:1521:orcl","tnguy107","tnguy107");  
						//create statement
						Statement stmt = conn.createStatement();

						float balance = Float.parseFloat(clubBalanceField.getText());
						String name = clubNameField.getText();
						int id = Integer.parseInt(clubIDField.getText());
						String pres = presidentField.getText();
						String vicePres = vicePresidentField.getText();
						
						String sql = "INSERT INTO club " + " VALUES ('" + name + "','" + id + "' ,'" + pres + "','" + balance + "','" + vicePres + "')";  
						System.out.println(sql);
						canQuery = false;
						stmt.executeUpdate(sql);					
						
						stmt.close();
						conn.close();
						
					} catch (NumberFormatException exception) {
						JOptionPane pane = new JOptionPane();
						pane.showMessageDialog(null, "Error: club balance/ club ID is not a number");
					} catch(Exception ex){		
						if (!canQuery)
						{
							JOptionPane pane = new JOptionPane();
							pane.showMessageDialog(null, "Error: check your inputs");
						}
						else
						{
							JOptionPane pane = new JOptionPane();
							pane.showMessageDialog(null, "Error");
						}
					}		
				}
			}
			
			
			
			
			//when modify is clicked
			if (clicked == modifyButton)
			{

				JComboBox jcombo = new JComboBox();
				DefaultComboBoxModel jcom = new DefaultComboBoxModel();
				try {
					//load driver
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//create connection
					Connection conn = DriverManager.getConnection(  
							"jdbc:oracle:thin:@acaddb2.asu.edu:1521:orcl","tnguy107","tnguy107");  
					//create statement
					Statement stmt = conn.createStatement();
					
					
					//create pop up
					//labels
					JLabel addTitle = new JLabel("Modify From Club Table");
					addTitle.setFont(new Font("Time New Roman", Font.BOLD, 20));
					JLabel clubID = new JLabel("clubId");
					JLabel clubName = new JLabel("c_name");
					JLabel president = new JLabel("President");
					JLabel vicePresident = new JLabel("vicePresident");
					JLabel clubBalance = new JLabel("clubBalance");
					
					//text fields with 20 characters in each each
					JTextField clubNameField = new JTextField(20);
					JTextField presidentField = new JTextField(20);
					JTextField vicePresidentField = new JTextField(20);
					JTextField clubBalanceField = new JTextField(20);
					
					//jcomboBox
					//getting the data
					ResultSet rs = stmt.executeQuery("SELECT * FROM club");
					while (rs.next())
					{
						String description = "club id: " + rs.getString("clubID");
						jcom.addElement(description);
					}
					jcombo.setModel(jcom);
					
					//top panel
					JPanel topPanel = new JPanel();
					topPanel.setLayout(new FlowLayout());
					topPanel.add(addTitle);
					
					//left panel
					JPanel leftPanel = new JPanel();
					leftPanel.setLayout(new GridLayout(5,1));
					leftPanel.add(clubID);
					leftPanel.add(clubName);
					leftPanel.add(president);
					leftPanel.add(vicePresident);
					leftPanel.add(clubBalance);
					
					//right panel
					JPanel rightPanel = new JPanel();
					rightPanel.setLayout(new GridLayout(5,1));
					rightPanel.add(jcombo);
					rightPanel.add(clubNameField);
					rightPanel.add(presidentField);
					rightPanel.add(vicePresidentField);
					rightPanel.add(clubBalanceField);
					
					
					//overall panel
					JPanel allPanel = new JPanel();
					allPanel.setLayout(new BorderLayout());
					allPanel.add(topPanel, BorderLayout.NORTH);
					allPanel.add(leftPanel, BorderLayout.WEST);
					allPanel.add(rightPanel, BorderLayout.CENTER);
					
					//pop up
					
					int result = JOptionPane.showConfirmDialog(null, allPanel, 
				               "MODIFY", JOptionPane.OK_OPTION);
					
					
					
					//get selected item and delete
					if (result == JOptionPane.OK_OPTION) {
						String res = (String) jcombo.getSelectedItem();
						String substring = res.substring(9);
					
						float balance = Float.parseFloat(clubBalanceField.getText());
						String name = clubNameField.getText();
						String pres = presidentField.getText();
						String vicePres = vicePresidentField.getText();
						
						String sql = String.format("UPDATE club SET c_name = '%s', clubBalance = '%f', president = '%s' , vicePresident = '%s' WHERE clubID = '%s' ", name, balance, pres, vicePres, substring);
						System.out.println(sql);
						
						stmt.executeUpdate(sql);
						
					}
					stmt.close();
					conn.close();


				}
				catch(Exception ex)
				{		
					JOptionPane pane = new JOptionPane();
					pane.showMessageDialog(null, "Error");
				}
				
				
				
			}
			
			
			
			
			
			//when delete is clicked
			if (clicked == deleteButton)
			{
				JComboBox jcombo = new JComboBox();
				DefaultComboBoxModel jcom = new DefaultComboBoxModel();
				try {
					//load driver
					Class.forName("oracle.jdbc.driver.OracleDriver");
					//create connection
					Connection conn = DriverManager.getConnection(  
							"jdbc:oracle:thin:@acaddb2.asu.edu:1521:orcl","tnguy107","tnguy107");  
					//create statement
					Statement stmt = conn.createStatement();
					
					
					//create popup
					//labels
					JLabel addTitle = new JLabel("Delete From Club Table");
					addTitle.setFont(new Font("Time New Roman", Font.BOLD, 20));
					JLabel clubName = new JLabel("club Id");
					
					//jcomboBox
					//getting the data
					ResultSet rs = stmt.executeQuery("SELECT * FROM club");
					while (rs.next())
					{
						String description = "club id: " + rs.getString("clubID");
						jcom.addElement(description);
					}
					jcombo.setModel(jcom);
					
					//top panel
					JPanel topPanel = new JPanel();
					topPanel.setLayout(new FlowLayout());
					topPanel.add(addTitle);
					
					//left panel
					JPanel leftPanel = new JPanel();
					leftPanel.setLayout(new FlowLayout());
					leftPanel.add(clubName);
					
					//right panel
					JPanel rightPanel = new JPanel();
					rightPanel.setLayout(new FlowLayout());
					rightPanel.add(jcombo);
					
					//overall panel
					JPanel allPanel = new JPanel();
					allPanel.setLayout(new BorderLayout());
					allPanel.add(topPanel, BorderLayout.NORTH);
					allPanel.add(leftPanel, BorderLayout.WEST);
					allPanel.add(rightPanel, BorderLayout.CENTER);
					
					//pop up
					int result = JOptionPane.showConfirmDialog(null, allPanel, 
				               "DELETE", JOptionPane.OK_OPTION);
					
					
					
					//get selected item and delete
					if (result == JOptionPane.OK_OPTION) {
						String res = (String) jcombo.getSelectedItem();
						String substring = res.substring(9);
						System.out.println(substring);
						String sql = "DELETE FROM club WHERE clubID = '" + substring + "'";
						System.out.println(sql);
						
						stmt.executeUpdate(sql);
						
					}
					stmt.close();
					conn.close();


				}
				catch(Exception ex)
				{		
					JOptionPane pane = new JOptionPane();
					pane.showMessageDialog(null, "Error");
				}
			}
			
			
			
			
			
			//when display is clicked
			if (clicked == displayButton)
			{
				try {
					//load driver
					Class.forName("oracle.jdbc.driver.OracleDriver");				
					//create connection
					Connection conn = DriverManager.getConnection(  
							"jdbc:oracle:thin:@acaddb2.asu.edu:1521:orcl","tnguy107","tnguy107");  				
					//create statement
					Statement stmt = conn.createStatement();
					
					
					
					//getting the data		
					ResultSet rs = stmt.executeQuery("SELECT * FROM club");
					while (rs.next())
					{
						String description = "c_name: " + rs.getString("c_name") + "\n"
											+ "clubID: " + rs.getInt("clubID") + "\n"
											+ "Club Balance: " + rs.getFloat("clubBalance") + "\n"
											+ "President: " + rs.getString("president") + "\n"
											+ "Vice President: " + rs.getString("vicePresident") + "\n"
											+ "\n";
						textArea.setForeground(Color.WHITE);
						textArea.append(description);
					}
					String endLine = "--------------------------------------------------------\n";
					textArea.append(endLine);
					
					stmt.close();
					conn.close();
				}
				catch(Exception ex)
				{		
					System.out.print("catch error");
				}

			}
			//when run query is clicked
			if (clicked == queryButton)
			{
				try {
					//load driver
					Class.forName("oracle.jdbc.driver.OracleDriver");				
					//create connection
					Connection conn = DriverManager.getConnection(  
							"jdbc:oracle:thin:@acaddb2.asu.edu:1521:orcl","tnguy107","tnguy107");  				
					//create statement
					Statement stmt = conn.createStatement();
					
					
					//labels
					JLabel addTitle = new JLabel("Run Query");
					addTitle.setFont(new Font("Time New Roman", Font.BOLD, 20));
					
					//text fields with 20 characters in each each
					JTextField clubNameField = new JTextField(20);
					
					

					//right panel
					JPanel rightPanel = new JPanel();
					rightPanel.setLayout(new FlowLayout());
					rightPanel.add(clubNameField);
			
					
					//top panel
					JPanel topPanel = new JPanel();
					topPanel.setLayout(new FlowLayout());
					topPanel.add(addTitle);
					
					//overall panel
					JPanel allPanel = new JPanel();
					allPanel.setLayout(new BorderLayout());
					allPanel.add(topPanel, BorderLayout.NORTH);
					allPanel.add(rightPanel, BorderLayout.CENTER);
					
					//pop up
					int result = JOptionPane.showConfirmDialog(null, allPanel, 
				               "Query", JOptionPane.OK_OPTION);
					
					if (result == JOptionPane.OK_OPTION) {
			
						String sql = clubNameField.getText();
						System.out.println(sql);
						
						ResultSet rs = stmt.executeQuery(sql);
						
						
						int colSize = rs.getMetaData().getColumnCount();
						textArea.append("Runing Query \n\n");
						
						String title = "";
						for (int i = 1; i <= colSize; i++)
						{
							title += rs.getMetaData().getColumnName(i) + "\t";
						}
						
						title += "\n";
						textArea.append(title);
						
						while (rs.next())
						{
							
							String description = "";
							for (int i = 1; i <= colSize; i++)
							{
								description += rs.getString(i) + "\t";
							}
							
							description += "\n";
							textArea.setForeground(Color.WHITE);
							textArea.append(description);
							
						}
							String endLine = "--------------------------------------------------------\n";
							textArea.append(endLine);
						
					}
				 
					
					stmt.close();
					conn.close();
				}
				catch(Exception ex)
				{		
					System.out.print("catch error");
				}

			}
		}
	}
}





























