package brainflow.misc.tracing;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EDTHogging extends JFrame {
	public EDTHogging() {
		super("EDT hogging");

		this.setLayout(new FlowLayout());
		JButton hogEDT = new JButton("Hog EDT");
		hogEDT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action started");
				// simulate load
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ie) {
				}
				System.out.println("Action ended");
			}
		});
		this.add(hogEDT);
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(
				new TracingEventQueue());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new EDTHogging().setVisible(true);
			}
		});
	}

}
