package collection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.sql.*;
class Inventory1
{
	int qty;
	float price;
	String nm;
	public Inventory1(int qty, float price, String nm) {
		
		this.qty = qty;
		this.price = price;
		this.nm = nm;
	}
	@Override
	public String toString() {
		return "  Quantity=" + qty + ", Price=" + price + ", Name=" + nm + " ";
	}
	public Object getName() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setQuantity(int qty2) {
		// TODO Auto-generated method stub
		
	}
	public void setPrice(float price2) {
		// TODO Auto-generated method stub
		
	}
	
	
}
public class InventoryMgtDBConn extends JFrame implements ActionListener
{
    JLabel nm,qty,price,titleLabel;
    JTextField t1,t2,t3;
    JButton add,update,delete;
    ArrayList<Inventory1>list=new ArrayList<Inventory1>();
    Hashtable<String, Inventory1> hashtable = new Hashtable<String, Inventory1>();
    DefaultTableModel tableModel;
    JTable jTable;
    JLabel searchLabel;
    JTextField searchField;
    JButton searchButton;
    JButton reportButton;
    Connection con;
    PreparedStatement stat;

    public InventoryMgtDBConn()
    {
        super("Inventory Management System");
    	setLayout(null);
    	
    	titleLabel = new JLabel("<html><font color='blue'><b>Inventory Management System</b></font></html>");
        titleLabel.setBounds(60, 20, 300, 25);
        add(titleLabel);
    	
    	nm=new JLabel("Name");
    	qty=new JLabel("Quantity");
    	price=new JLabel("Price");
    	
    	t1=new JTextField(30);
    	t2=new JTextField(30);
    	t3=new JTextField(30);
    	
    	add=new JButton("Add");
    	update=new JButton("Update");
    	delete=new JButton("Delete");
    	
    	add.addActionListener(this);
    	update.addActionListener(this);
    	delete.addActionListener(this);
    	
    	nm.setBounds(60, 60, 80, 25);
        qty.setBounds(60, 90, 80, 25);
        price.setBounds(60, 120, 80, 25);

        t1.setBounds(140, 60, 200, 25);
        t2.setBounds(140, 90, 200, 25);
        t3.setBounds(140, 120, 200, 25);

        add.setBounds(60, 160, 80, 25);
        update.setBounds(160, 160, 80, 25);
        delete.setBounds(260, 160, 80, 25);
        
     
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");
        jTable = new JTable(tableModel);
        jTable.getTableHeader().setBackground(Color.gray);
        jTable.getTableHeader().setForeground(Color.white);
        jTable.setSelectionBackground(Color.BLUE);
        jTable.setSelectionForeground(Color.white);
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setBounds(400, 60, 400, 300);
        
        try
        {
        	Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "Root@123");

            // Fetch all data from the database
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM inventrymgt");

            // Populate the table model with the retrieved data
            while (rs.next()) {
                String nm = rs.getString("Name");
                int qty = rs.getInt("Quantity");
                float price = rs.getFloat("Price");
                tableModel.addRow(new Object[]{nm, qty, price});
            }

            rs.close();
            stmt.close();
        }
        catch(Exception e)
        {
        	System.out.println(e);
        }
        
        searchLabel = new JLabel("Search by Name:");
        searchLabel.setBounds(400, 20, 150, 25);
        add(searchLabel);

        searchField = new JTextField(30);
        searchField.setBounds(550, 20, 150, 25);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(710, 20, 80, 25);
        searchButton.addActionListener(this);
        searchButton.setBackground(Color.BLUE); 
        searchButton.setForeground(Color.white);
        add(searchButton);
        
        reportButton = new JButton("Generate Report");
        reportButton.setBounds(400, 370, 150, 25); // Adjust the position as needed
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        add(reportButton);
        
    	add(nm);
    	add(t1);
    	add(qty);
    	add(t2);
    	add(price);
    	add(t3);
    	add(add);
    	add(update);
    	add(delete);
    	 add(scrollPane);
    	 
       	setSize(400,400);
    	setVisible(true);
    	    	
    }
    @Override
	public void actionPerformed(ActionEvent e) 
    {
    	 if (e.getSource() == add)
    	 {
    		 String nm = t1.getText();
    	        int qty = Integer.parseInt(t2.getText());
    	        float price = Float.parseFloat(t3.getText());
    	        Inventory1 item = new Inventory1(qty,price ,nm );
    	        
    	        
    	        try
    	        {
    	        	stat=con.prepareStatement("insert into inventrymgt values(?,?,?)");
    	        	stat.setString(1,nm);
    	        	stat.setInt(2, qty);
    	        	stat.setFloat(3, price);
    	        	stat.executeUpdate();
    	        	tableModel.addRow(new Object[]{item.nm, item.qty, item.price});
        	        list.add(item);
        	        System.out.println("Added item: " + item);
        	        t1.setText("");
        	        t2.setText("");
        	        t3.setText("");
        	        
    	        }
    	        catch(Exception e1)
    	        {
    	        	System.out.println(e1);
    	        }
         }
    	 else if (e.getSource() == update) {
    		    int selectedRow = jTable.getSelectedRow();
    		    if (selectedRow != -1) {
    		        String nm = (String) jTable.getValueAt(selectedRow, 0);
    		        int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter the new quantity:"));
    		        float price = Float.parseFloat(JOptionPane.showInputDialog("Enter the new price:"));
    		        Inventory1 newItem = new Inventory1(qty, price, nm);
    		        
    		        try
    		        {
    		        	stat=con.prepareStatement("update inventrymgt set Quantity=?, Price=? where Name=?");
    		        	stat.setInt(1, qty);
    		        	stat.setFloat(2, price);
    		        	stat.setString(3, nm);
    		        	stat.executeUpdate();
    		        	System.out.println("Updated item: " + newItem);
        		        
    		        }
    		        catch(Exception e2)
    		        {
    		        	System.out.println(e2);
    		        }
    		        tableModel.setValueAt(newItem.nm, selectedRow, 0);
    		        tableModel.setValueAt(newItem.qty, selectedRow, 1);
    		        tableModel.setValueAt(newItem.price, selectedRow, 2);
    		        list.set(selectedRow, newItem);
    	            JOptionPane.showMessageDialog(this, "Item updated successfully.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);

    		    } 
    		    else {
    		        JOptionPane.showMessageDialog(this, "Please select a row to update", "Update Error", JOptionPane.ERROR_MESSAGE);
    		    }
    		}    	
    	 else if (e.getSource() == delete) {
    		    int selectedRow = jTable.getSelectedRow();
    		    if (selectedRow != -1) {
    		        String nm = (String) jTable.getValueAt(selectedRow, 0);
    		        try
    		        {
    		        	stat=con.prepareStatement("delete from inventrymgt where Name=?");
    		        	stat.setString(1, nm);
    		        	stat.executeUpdate();
    		        	tableModel.removeRow(selectedRow);
    		        	 for (Iterator<Inventory1> iterator = list.iterator(); iterator.hasNext();) {
    		                    Inventory1 item = iterator.next();
    		                    if (item.nm.equals(nm)) {
    		                        iterator.remove();
    		                        System.out.println("Deleted item: " + item);
    		                        break;
    		                    }
    		                }
    		                JOptionPane.showMessageDialog(this, "Item deleted successfully.", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);
    		          
    		        }
    		        catch(Exception e3)
    		        {
    		        	System.out.println(e3);
    		        }
    		        
    		        
    		    } 
    		    else {
    		        JOptionPane.showMessageDialog(this, "Please select a row to delete", "Delete Error", JOptionPane.ERROR_MESSAGE);
    		    }
    		}
    	 
    	 else if (e.getSource() == searchButton) {
    	        String searchTerm = searchField.getText();
    	        if (!searchTerm.isEmpty()) {
    	        	try
    	        	{
    	        		 tableModel.setRowCount(0);
    	         		 stat=con.prepareStatement("select * from inventrymgt where Name like ?");
    	        		 stat.setString(1, searchTerm);
    	        		 ResultSet rs=stat.executeQuery();
    	        		 while(rs.next())
    	        		 {
    	        			 String nm = rs.getString("Name");
    	                     int qty = rs.getInt("Quantity");
    	                     float price = rs.getFloat("Price");
    	                     tableModel.addRow(new Object[]{nm, qty, price}); 
    	        		 }
    	        		 rs.close();
    	        		 stat.close();
    	        	}
    	        	catch(Exception e4)
    	        	{
    	        		System.out.println(e4);
    	        	}
    	        	
    	        } else {
    	            JOptionPane.showMessageDialog(this, "Please enter a search term", "Search Result", JOptionPane.INFORMATION_MESSAGE);
    	        }
    	    }
	}
    private void generateReport() {
        try {
            // Open a file chooser dialog to choose the location to save the report
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Get the selected file
                File fileToSave = fileChooser.getSelectedFile();

                // Create a FileWriter and BufferedWriter to write to the file
                FileWriter writer = new FileWriter(fileToSave);
                BufferedWriter bw = new BufferedWriter(writer);

                // Write header
                bw.write("Name\tQuantity\tPrice\n");

                // Write inventory data from the table
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        bw.write(tableModel.getValueAt(row, col).toString());
                        bw.write("\t");
                    }
                    bw.newLine();
                }

                // Close the writer
                bw.close();

                // Show success message
                JOptionPane.showMessageDialog(this, "Report generated successfully.", "Report Generated", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
	public static void main(String[] args) {
		InventoryMgtDBConn fr=new InventoryMgtDBConn();
		
	}
	

}