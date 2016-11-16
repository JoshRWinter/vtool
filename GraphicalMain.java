import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

public class GraphicalMain extends JFrame{

	private JButton button;
	private JTextArea text;
	private JLabel vstatus;

	private Vtool vtool;

	public GraphicalMain(){

		// setup the button
		this.button = new JButton("this is a button");
		this.button.addActionListener((ActionEvent e) -> {
			String ctext = this.text.getText();
			if(ctext.length() == 0)
				return;

			if(this.vtool == null){
				this.vtool = new Vtool(ctext,1,12);
				vtool.decrypt();
				VtoolStatus status;
				do{
					status = this.vtool.status(null);
					this.vstatus.setText(status.status);
					repaint();
				}while(status.decrypted == null);
				this.text.setText(status.decrypted);
				JOptionPane.showMessageDialog(null,status.status + "\nkey: " + this.vtool.getKey());
				this.vtool = null;
			}
		});

		// set up the text area
		this.text = new JTextArea();
		this.text.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(this.text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// other swing nonsense
		JPanel bottom = new JPanel();
		add(scroller,BorderLayout.CENTER);
		bottom.add(this.button,BorderLayout.CENTER);
		this.vstatus = new JLabel("heyy");
		bottom.add(this.vstatus,BorderLayout.SOUTH);
		add(bottom,BorderLayout.SOUTH);

		this.setTitle("vigenere solver by josh winter");
		this.setSize(1200,800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args){
		GraphicalMain gm = new GraphicalMain();
	}
}
