package fyp;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DiseaseGenesIdentifierGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DiseaseGenesIdentifierGUI frame = new DiseaseGenesIdentifierGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DiseaseGenesIdentifierGUI() throws Exception {
		int seed = 37;
		int i = 1;
		DiseaseGenesIdentifier example = new DiseaseGenesIdentifier(seed);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 606, 579);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Fitness value");
		lblNewLabel.setBounds(99, 57, 120, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblFitnessValue = new JLabel("New label");
		lblFitnessValue.setBounds(344, 57, 69, 14);
		contentPane.add(lblFitnessValue);
		
		JLabel lblNewLabel_2 = new JLabel("The total number of solution needed is");
		lblNewLabel_2.setBounds(99, 107, 214, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblSize = new JLabel("New label");
		lblSize.setBounds(344, 107, 46, 14);
		contentPane.add(lblSize);
		
		lblFitnessValue.setText(example.getFitnessValueToPrint());
		lblSize.setText(example.getDiseaseScoreToPrint());
		
		TextArea textAreaResults = new TextArea();
		textAreaResults.setBounds(45, 145, 505, 373);
		contentPane.add(textAreaResults);
		
		JLabel lblNewLabel_1 = new JLabel("Disease Genes Identifier");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 27));
		lblNewLabel_1.setBounds(93, 11, 380, 35);
		contentPane.add(lblNewLabel_1);
		for(Entry<String, List<String>> e : example.getScore().entrySet()){
			textAreaResults.append(e.getKey() + "\n");
			for(String item : e.getValue()){
				textAreaResults.append(item + "\n");
			}
		}
		textAreaResults.append("--------------------------------------------------------------------\n");
		textAreaResults.append("Fitness values are in after each evaluation \n");
		for (Double fitnessValue : example.getFitnessValues()) {
			textAreaResults.append(i + " evaluation " + " fitness value is " + fitnessValue + "\n");
			i++;
		}

	}
}
