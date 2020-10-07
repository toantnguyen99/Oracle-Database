import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.*;

public class MainFrame extends JFrame{

	private MainPanel mainPanel;
	private JFrame frame;
	public MainFrame()
	{
		super("412 Honor Contract");
		
		frame = new JFrame();
		setLayout(new BorderLayout());
		mainPanel = new MainPanel();
		add(mainPanel);
		
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
