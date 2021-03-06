package projetoES.simple;

import javax.swing.JFrame;
import java.awt.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.awt.Font;

public class GUI {
	private App app;	
	private JFrame frame;
	private JButton btnFile;
	private JButton btnInsert;
	private JButton btnSearch;
	private JButton btnQuality;
	private JTextField textField1;
	private JTextField textField2;
	private JComboBox<Object> comboBox;
	private JComboBox<Object> comboBox1;
	private String selected;
	private String selected1;
	private String selected2;
	private JTextPane textPane ;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane1;
	private JTable excel;
	private JTable def;
	private Defeitos defeitos;


	/**
	 * Main - runs the project's interface
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GUI() throws InvalidFormatException, IOException {
		initialize();
	}

	private void initialize() throws InvalidFormatException, IOException {
		initComponents();
		createEvents();
	}

	/**
	 * Updates the table with changed values
	 */
	private void update() {
		frame.remove(scrollPane);
		app.newData();
		excel = new JTable (app.getRowData(), app.getColumnNames());
		excel.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(excel);
		scrollPane.setBounds(40, 250, 500, 200 );
		frame.add(scrollPane);
	}

	/**
	 * Creates all interface's components from default
	 */
	private void initComponents() {
		app = new App();
		frame = new JFrame();
		frame.setTitle("Projeto");
		frame.setBounds(100, 100, 570, 523);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnFile = new JButton("Choose File");
		btnFile.setBounds(412, 29, 85, 21);
		frame.getContentPane().add(btnFile);

		textField1 = new JTextField();
		textField1.setBounds(52, 30, 350, 19);
		frame.getContentPane().add(textField1);
		textField1.setColumns(10);

		String[] metricas = { "LOC", "CYCLO", "ATFD", "LAA"};
		comboBox = new JComboBox<Object>(metricas);
		comboBox.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		comboBox.setBounds(52, 70, 59, 21);
		frame.getContentPane().add(comboBox);

		String[] metricas2 = {">", "<", "="};
		comboBox1 = new JComboBox<Object>(metricas2);
		comboBox1.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		comboBox1.setBounds(118, 70, 59, 21);
		frame.getContentPane().add(comboBox1);

		textField2 = new JTextField();
		textField2.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		textField2.setBounds(187, 70, 96, 21);
		frame.getContentPane().add(textField2);
		textField2.setColumns(10);

		btnInsert = new JButton("Insert");
		btnInsert.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		btnInsert.setBounds(412, 60, 85, 32);
		frame.getContentPane().add(btnInsert);

		btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		btnSearch.setBounds(412, 95, 85, 32);
		frame.getContentPane().add(btnSearch);

		btnQuality= new JButton("Indicadores de Qualidade");
		btnQuality.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		btnQuality.setBounds(412, 130, 85, 32);
		frame.getContentPane().add(btnQuality);

		textPane = new JTextPane();
		textPane.setFont(new Font("Century Gothic", Font.PLAIN, 10));
		textPane.setBounds(52, 101, 304, 26);
		frame.getContentPane().add(textPane);

	}

	/**
	 * Creates events for all of the buttons when pressed
	 */
	private void createEvents() throws InvalidFormatException, IOException {
		btnFile.setFont(new Font("Tahoma", Font.PLAIN, 9));
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads")); //Downloads Directory as default
				chooser.setDialogTitle("Select Location");
				chooser.setAcceptAllFileFilterUsed(false);
				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = chooser.getSelectedFile();
					textField1.setText(selectedFile.getAbsolutePath ());
					try {
						app.readFile(textField1.getText());
						app.newData();
						excel = new JTable (app.getRowData(), app.getColumnNames());
						excel.setFillsViewportHeight(true);
						scrollPane = new JScrollPane(excel);
						scrollPane.setBounds(40, 250, 500, 200 );
						frame.add(scrollPane);

					} catch (InvalidFormatException | IOException e1) {
						System.out.println("Something went wrong...");
					}
				}}
		});

		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textPane.setText("");
				selected = comboBox.getSelectedItem().toString();
				selected1 = comboBox1.getSelectedItem().toString();
				selected2 = textField2.getText();
				if (selected2.isEmpty()) {
					System.out.println("Espaço não preenchido");
				} else {
					textPane.setText(textPane.getText()+" "+selected);
					textPane.setText(textPane.getText()+" "+selected1);
					textPane.setText(textPane.getText()+" "+selected2);
				}

			}	
		});


		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String metrics= textPane.getText();
				if (metrics.isEmpty()) {
					System.out.println("Nada a procurar!");
				} else {
					String[] v= metrics.split(" ");
					String c = v[1];
					try {
						System.out.println("Changing: " + c);
						for (int i=0; i<app.getColumnNames().length; i++){
							if(c.equals(app.get(0, i))) {
								for (int j=1 ; j<app.getRowData().length + 1 ; j++) {
									Metric m = new Metric(v[1], v[2], Integer.parseInt(v[3]));
									app.escreveVF(m, j);
								}
							}
							update();
						}
					} catch(Exception a) {
						a.printStackTrace();
					}
				}}});


		btnQuality.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				app.newData();
				defeitos = new Defeitos(app.getRowData());
				def = new JTable (defeitos.detecaoDefeitos(), defeitos.getHeader());
				def.setFillsViewportHeight(true);
				scrollPane1 = new JScrollPane(def);
				scrollPane1.setBounds(40, 150, 300, 85 );
				frame.add(scrollPane1);
			}
		});
	}
}