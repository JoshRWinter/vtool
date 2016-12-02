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
import java.awt.event.ActionListener;

public class GraphicalMain extends JFrame implements ActionListener{

	private JButton button;
	private JButton encryptbutton;
	private JTextArea text;
	private JLabel vstatus;
	private int ioc; // suggested key period
	private double iocvalue; // raw ioc

	private Vtool vtool;

	public GraphicalMain(){
		this.vtool = null;

		// setup the button
		this.button = new JButton("Decrypt");
		this.button.addActionListener(this);

		// setup the encrypt button
		this.encryptbutton = new JButton("Encrypt");
		this.encryptbutton.addActionListener(this);

		// set up the text area
		this.text = new JTextArea();
		this.text.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(this.text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// other swing nonsense
		JPanel bottom = new JPanel(new BorderLayout());
		add(scroller,BorderLayout.CENTER);
		bottom.add(this.button,BorderLayout.CENTER);
		bottom.add(this.encryptbutton,BorderLayout.NORTH);
		this.vstatus = new JLabel("Vtool by Josh Winter");
		bottom.add(this.vstatus,BorderLayout.SOUTH);
		add(bottom,BorderLayout.SOUTH);

		this.setTitle("vigenere solver by josh winter");
		this.setSize(1200,800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		if(source == this.encryptbutton){
			String text = this.text.getText();
			if(text.length() == 0)
				return;

			String input = JOptionPane.showInputDialog(null, "Enter the key:");
			if(input == null || input.length() == 0)
				return;

			String ctext = Vigenere.encrypt(text,input);
			this.text.setText(ctext);
		}
		else{
			String ctext = this.text.getText();
			if(ctext.length() == 0)
				return;

			if(this.vtool == null){
				// user provided key
				String key = JOptionPane.showInputDialog("Input decryption key (leave blank to guess):");
				if(key.length() != 0){
					StringBuilder keysb = Vigenere.convertToUpper(key);
					StringBuilder sb = Vigenere.convertToUpper(ctext);
					this.text.setText(Vigenere.vigenere(sb.toString(), keysb.toString()));
					return;
				}

				// user provided no key
				this.vtool = new Vtool(ctext,1,12);
				this.ioc = Vigenere.indexOfCoincedence(this.vtool.getCText());
				this.iocvalue = Vigenere.ioc(this.vtool.getCText());
				StatusUpdater su = new StatusUpdater(this);
				su.start();
			}
		}
	}

	public static void main(String[] args){
		boolean noGraphics = false;
		boolean noColors = false;
		boolean quiet = false;
		String file = null;
		if(args.length>0){
			if(args[0].equals("--help") || args[0].equals("-h")){
				System.out.println("Vtool by Josh Winter.\n" + 
					"Usage: java -jar vtool.jar [--no-graphical] [--no-colors] [--quiet] [file]\n" +
					"When run with no options, vtool will launch a graphical interface.\nWith option \"--no-graphical\", vtool will interact with you on the cmd line\n" + 
					"When \"--no-colors\" is set, no ANSI escape color-codes will be used (Windows terminal probably won't like them, so use this option on windows...)\n" +
					"When \"--quiet\" is used, only the decrypted plain text will be printed to stdout\n" +
					"When a file is provided (in --no-graphical mode), vtool will use <file> instead of asking interactively."
				);
				System.exit(0);
			}
			if(GraphicalMain.argumentExists(args, "--no-graphical"))
				noGraphics = true;
			if(GraphicalMain.argumentExists(args, "--no-colors"))
				noColors = true;
			if(GraphicalMain.argumentExists(args, "--quiet"))
				quiet = true;
			file = args[args.length - 1];
		}
		if(noGraphics){
			CMDMain.main(noColors,quiet,file);
			noGraphics = false;
		}
		else{
			try{
				new GraphicalMain();
			}catch(Exception e){
				noGraphics = true;
			}
		}
		if(noGraphics)
			CMDMain.main(noColors,quiet,file);
	}

	private static boolean argumentExists(String[] args,String argument){
		for(int i = 0; i < args.length; ++i)
			if(args[i].equals(argument))
				return true;
		return false;
	}

	public void setStatus(String text){
		this.vstatus.setText(text);
	}

	public void setText(String text){
		this.text.setText(text);
		String iocstring = String.format("%.3f", this.iocvalue);
		String msg = this.vstatus.getText() + "\nLogical Processors: " + WordFinder.processorCount() + "\nIOC suggested period " + this.ioc + " (" + iocstring + ")\nKey: \"" + this.vtool.getKey() + "\"\n\nThese words supported the plaintext:\n";

		if(!text.equals("!")){ // "!" means vtool couldn't decrypt it
			String[] foundWords = vtool.getFoundWords();
			for(int i = 0; i < WordFinder.WORD_COUNT; ++i)
				msg = msg + (i + 1) + ": " + foundWords[i] + "\n";
		}
		else
			msg = "Vtool could not decrypt this message";

		JOptionPane.showMessageDialog(this, msg);
		this.vtool = null;
	}

	public Vtool vtool(){
		return this.vtool;
	}
}
