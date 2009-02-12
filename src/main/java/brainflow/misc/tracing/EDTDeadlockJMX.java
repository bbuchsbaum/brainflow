package brainflow.misc.tracing;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EDTDeadlockJMX extends JFrame {
	private Object LOCK1 = new Object();
	private Object LOCK2 = new Object();

	public EDTDeadlockJMX() {
		super("EDT hogging");

		this.setLayout(new FlowLayout());
		JButton hogEDT = new JButton("Lock EDT");
		hogEDT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action started");
				// simulate deadlock
				synchronized (LOCK1) {
					System.out.println("Action listener acquired lock 1");
					new Thread() {
						@Override
						public void run() {
							synchronized (LOCK2) {
								System.out
										.println("New thread acquired lock 2");
								synchronized (LOCK1) {
									System.out
											.println("New thread acquired lock 1");
									System.out.println("Here i am 2/1");
								}
							}
						};
					}.start();
					try {
						Thread.sleep(200);
					} catch (InterruptedException ie) {
					}
					synchronized (LOCK2) {
						System.out.println("Action listener acquired lock 2");
						System.out.println("Here i am 1/2");
					}
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
				new TracingEventQueueJMX());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new EDTDeadlockJMX().setVisible(true);
			}
		});
	}

}
