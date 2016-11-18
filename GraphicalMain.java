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
	private int ioc;

	private Vtool vtool;

	public GraphicalMain(){
		this.vtool = null;

		// setup the button
		this.button = new JButton("Decrypt");
		this.button.addActionListener((ActionEvent e) -> {
			String ctext = this.text.getText();
			if(ctext.length() == 0)
				return;

			if(this.vtool == null){
				this.vtool = new Vtool(ctext,1,12);
				this.ioc = Vigenere.indexOfCoincedence(this.vtool.getCText());
				StatusUpdater su = new StatusUpdater(this);
				su.start();
			}
		});

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
		this.vstatus = new JLabel("Vtool by Josh Winter");
		bottom.add(this.vstatus,BorderLayout.SOUTH);
		add(bottom,BorderLayout.SOUTH);

		this.setTitle("vigenere solver by josh winter");
		this.setSize(1200,800);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
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
		String msg = this.vstatus.getText() + "\nLogical Processors: " + WordFinder.processorCount() + "\nIOC suggested period " + this.ioc + "\nKey: \"" + this.vtool.getKey() + "\"\n\nThese words supported the plaintext:\n";

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
