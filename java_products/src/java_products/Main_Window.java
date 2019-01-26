package java_products;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main_Window extends JFrame {

	private JPanel contentPane;
	private JTextField text_id;
	private JTextField text_name;
	private JTextField text_price;
	JLabel lbl_image = new JLabel("");
	private JScrollPane scrollPane1;
	private JTable JTable_List;
	JDateChooser txt_add_date = new JDateChooser();
	
	private JTable table2; // Test-Button's table
	
	Connection connection = null;
	String ImagePath = null;
	
	int pos = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Window frame = new Main_Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Checks if NameField, PriceField or DateField are empty.
	 */
	public boolean checkInputs() {
		if(text_name.getText() == null || text_price.getText() == null || txt_add_date.getDate() == null ) {
			return false;
		} else {
			try {
				Float.parseFloat(text_price.getText());
				return true;
			} catch(Exception ex) {
				return false;
			}
		}
	}
		
	/**
	 * Image Resized.
	 */
	public ImageIcon ResizeImage(String imagePath, byte[] pic) {
		ImageIcon myImage =  null;
		
		if(imagePath != null) {
			myImage = new ImageIcon(imagePath);
		}else {
			myImage = new ImageIcon(pic);
		}
		
		Image img = myImage.getImage();
		Image img2 = img.getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(img2);
		return image;
	}
	
	
	/**
	 * Display Data In JTable
	 * 		1: Fill ArrayList With MySQL-Data
	 */
	
	public ArrayList<Product> getProductList(){
		
		ArrayList<Product> productList = new ArrayList<Product>();
		Connection connection = DBConnector.dbConnector();
		String query = "SELECT * FROM products";
		
		Statement st;
		ResultSet rs;
		
		try {
			
			st = connection.createStatement();
			rs = st.executeQuery(query);
			Product product;
			
			while(rs.next()) {
				product = new Product(rs.getInt("id"),rs.getString("name"),Float.parseFloat(rs.getString("price")),rs.getString("add_date"),rs.getBytes("image"));
				productList.add(product);
			}
		
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "List from Database not fetched");
		}
		
		return productList;
	}
	
	/**
	 * 		2: Populate The JTable
	 */
	
	public void Show_In_JTable() {
		ArrayList<Product> list =  getProductList();
		DefaultTableModel model = (DefaultTableModel) JTable_List.getModel();
		
		
		model.setRowCount(0); 		// Clears JTable's content
		
		Object[] row = new Object[4];
		for(int  i = 0; i < list.size(); i++) {
			row[0] = list.get(i).getId();
			row[1] = list.get(i).getName();
			row[2] = list.get(i).getPrice();
			row[3] = list.get(i).getAddDate();
			
			model.addRow(row);
		}
	}
	
	public void ShowItem(int index) {
		text_id.setText(Integer.toString(getProductList().get(index).getId()));
		text_name.setText(getProductList().get(index).getName());
		text_price.setText(Float.toString(getProductList().get(index).getPrice()));
		
		try {
			java.util.Date addDate = null;
			addDate = new SimpleDateFormat("dd-MM-yyyy").parse((String)getProductList().get(index).getAddDate());
			txt_add_date.setDate(addDate);
		}catch (ParseException e){
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		lbl_image.setIcon(ResizeImage(null,getProductList().get(index).getImage()));
		
	}
	
	/**
	 * Creates JTable within JScrollPane with Column Names.
	 */
	public JScrollPane table() {
		JScrollPane scrollPane1 = new JScrollPane();
		JTable_List = new JTable();
		JTable_List.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int index = JTable_List.getSelectedRow();
				ShowItem(index);
			}
		});
		
		JTable_List.setModel(new DefaultTableModel(
	            new Object [][] {

	            },
	            new String [] {
	                "id", "name", "price", "add_date"
	            }
	        ));
	    scrollPane1.setViewportView(JTable_List);
		return scrollPane1;
	}	

	/**
	 * Create the frame with Buttons and actions.
	 */
	public Main_Window() {
		
		connection = DBConnector.dbConnector(); 		// Calls DBConnector Class
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 936, 556);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JLabel lblAddDate = new JLabel("Add Date:");
		lblAddDate.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JLabel lblImage = new JLabel("Image:");
		lblImage.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		text_id = new JTextField();
		text_id.setEnabled(false);
		text_id.setFont(new Font("Tahoma", Font.BOLD, 14));
		text_id.setColumns(10);
		
		text_name = new JTextField();
		text_name.setFont(new Font("Tahoma", Font.BOLD, 14));
		text_name.setColumns(10);
		
		text_price = new JTextField();
		text_price.setFont(new Font("Tahoma", Font.BOLD, 14));
		text_price.setColumns(10);
		
		txt_add_date.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		lbl_image.setOpaque(true);
		lbl_image.setBackground(new Color(152, 251, 152));
		
		JScrollPane scrollPane1 = table();
	    Show_In_JTable();
		
		// Choose Image-Button & it's Action
		JButton btn_choose_image = new JButton("Choose Image");
		btn_choose_image.setFont(new Font("Tahoma", Font.BOLD, 14));
		btn_choose_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.image", "jpg","png");
				file.addChoosableFileFilter(filter);
				int result = file.showSaveDialog(null);
				
				if(result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = file.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					lbl_image.setIcon(ResizeImage(path, null));
					ImagePath = path;
				} else {
					System.out.println("No File Selected");
					JOptionPane.showMessageDialog(null, "No File Selected");
				}
			}
		});
		
		// Insert-Button & it's Action
		JButton btn_Insert = new JButton("Insert");
		btn_Insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(checkInputs() && ImagePath != null) {
			 
					try {
						Connection con = DBConnector.dbConnector();
						PreparedStatement ps = con.prepareStatement("INSERT INTO products(name,price,add_date,image)"
								+ "values(?,?,?,?)");
						ps.setString(1, text_name.getText());
						ps.setString(2, text_price.getText());
						SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
						String addDate = dateFormat.format(txt_add_date.getDate());
						ps.setString(3, addDate);
						
						InputStream img = new FileInputStream(new File(ImagePath));
						ps.setBlob(4, img);
						ps.executeUpdate();
						Show_In_JTable();
						JOptionPane.showMessageDialog(null, "Data Inserted To The Database");
						
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, e.getMessage());
						e.printStackTrace();
					} catch (FileNotFoundException e){
						JOptionPane.showMessageDialog(null, e.getMessage());
						
					}
				}else {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields");
				}
			}
		});
		btn_Insert.setIcon(new ImageIcon(Main_Window.class.getResource("/com/sun/java/swing/plaf/windows/icons/UpFolder.gif")));
				
		// Update-Button & it's Action
		JButton btn_Update = new JButton("Update");
		btn_Update.setIcon(new ImageIcon(Main_Window.class.getResource("/javax/swing/plaf/metal/icons/ocean/hardDrive.gif")));
		btn_Update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkInputs() && text_id.getText() != null)
				{
					String UpdateQuery = null;
					PreparedStatement ps = null;
					Connection con = DBConnector.dbConnector();
					
					// Update Without Image
					if(ImagePath == null) {
						try {
							UpdateQuery = "UPDATE products SET name = ?, price = ?, add_date = ? WHERE id = ?";					
							ps = con.prepareStatement(UpdateQuery);
							
							ps.setString(1, text_name.getText());
							ps.setString(2, text_price.getText());
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							String addDate = dateFormat.format(txt_add_date.getDate());
							ps.setString(3, addDate);
							
							ps.setInt(4, Integer.parseInt(text_id.getText()));
							
							ps.executeUpdate();
							Show_In_JTable();
							JOptionPane.showMessageDialog(null, "Data Updated");
							
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "Fill Empty Fields");
							e1.printStackTrace();
						}
					// Update With Image
					}else {
						try {
							InputStream img = new FileInputStream(new File(ImagePath));
							
							UpdateQuery = "UPDATE products SET name = ?, price = ?, add_date = ?, image= ? WHERE id = ?";					
							ps = con.prepareStatement(UpdateQuery);
							
							ps.setString(1, text_name.getText());
							ps.setString(2, text_price.getText());
							
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
							String addDate = dateFormat.format(txt_add_date.getDate());
							ps.setString(3, addDate);
							
							ps.setBlob(4, img);
							
							ps.setInt(5, Integer.parseInt(text_id.getText()));
							
							ps.executeUpdate();
							Show_In_JTable();
							JOptionPane.showMessageDialog(null, "Data Updated");
							
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "Fill Empty Fields");
							e1.printStackTrace();
						} catch (FileNotFoundException e1){
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						
					}
				}else {
					JOptionPane.showMessageDialog(null, "Fill Empty Fields Or Check Fields");
				}
			}
		});
		
		/* Delete-Button & it's Action */
		JButton btn_Delete = new JButton("Delete");
		btn_Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!text_id.getText().equals("")) {
					Connection con = DBConnector.dbConnector();
					
					try {
						PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id = ?");
						int id = Integer.parseInt(text_id.getText());
						ps.setInt(1, id);
						ps.executeUpdate();
						Show_In_JTable();
						JOptionPane.showMessageDialog(null, "Product Deleted");
						
					} catch (SQLException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "Product Not Deleted");
					}
					
				}else {
					JOptionPane.showMessageDialog(null, "Product Not Deleted | Choose Deletable Product With Product Id");
				}
			}
		});
		btn_Delete.setIcon(new ImageIcon(Main_Window.class.getResource("/com/sun/java/swing/plaf/windows/icons/image-failed.png")));
		
		/* First-Button & it's Action */
		JButton btn_First = new JButton("First");
		btn_First.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pos = 0;
				ShowItem(pos);
			}
		});
		
		/* Next-Button & it's Action */
		JButton btn_Next = new JButton("Next");
		btn_Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pos++;
				
				if(pos >= getProductList().size()) {
					pos = getProductList().size()-1;
				}
				
				ShowItem(pos);
			}
		});
		btn_Next.setIcon(new ImageIcon(Main_Window.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		
		
		/* Previous-Button & it's Action */
		JButton btn_Previous = new JButton("Previous");
		btn_Previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pos--;
				
				if(pos < 0) {
					pos = 0;
				}
				
				ShowItem(pos);
			}
		});
		btn_Previous.setIcon(new ImageIcon(Main_Window.class.getResource("/com/sun/javafx/scene/web/skin/Undo_16x16_JFX.png")));
		
		/* Last-Button & it's Action */
		JButton btn_Last = new JButton("Last");
		btn_Last.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pos = getProductList().size()-1;
				ShowItem(pos);
			}
		});
		
		/* Test-Button & it's Action */
		JButton btnNewButton_1 = new JButton("Test Button: Load data from DB");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					System.out.println("Button Pressed!");
					
					String query = "select * from products";
					PreparedStatement pst = connection.prepareStatement(query);
					ResultSet rs = pst.executeQuery();
					table2.setModel(DbUtils.resultSetToTableModel(rs));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		table2 = new JTable(); // Test-Button's table
		
		
		/* Components placement at the layout */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblId)
								.addComponent(lblPrice)
								.addComponent(lblImage)
								.addComponent(lblAddDate)
								.addComponent(lblName))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btn_choose_image, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
								.addComponent(lbl_image, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
								.addComponent(txt_add_date, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addComponent(text_id, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
									.addComponent(text_name, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
									.addComponent(text_price, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
							.addGap(121))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btn_Insert, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_Update, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_Delete)
							.addGap(68)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 429, GroupLayout.PREFERRED_SIZE)
							.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
								.addComponent(btn_First, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(btn_Next, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btn_Previous, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(btn_Last, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
						.addComponent(table2, GroupLayout.PREFERRED_SIZE, 428, GroupLayout.PREFERRED_SIZE))
					.addGap(98))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(415)
					.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
					.addGap(178))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblId)
									.addGap(18)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblName, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
										.addComponent(text_name, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
								.addComponent(text_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(17)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblPrice, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
								.addComponent(text_price, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txt_add_date, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblAddDate, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(18)
									.addComponent(lblImage, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lbl_image, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btn_choose_image)
						.addComponent(table2, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btn_Insert, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Update, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Delete, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Last, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Previous, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_First, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btn_Next, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addGap(51))
		);
		
		contentPane.setLayout(gl_contentPane);
		
	}
}

