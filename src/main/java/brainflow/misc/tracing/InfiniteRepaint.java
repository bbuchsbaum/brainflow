package brainflow.misc.tracing;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class InfiniteRepaint extends JFrame {
	public InfiniteRepaint() {
		JTextField tf = new JTextField("some text") {
			@Override
			public void paint(Graphics g) {
				this.setBorder(new LineBorder(Color.red));
				super.paint(g);
			}
		};
		tf.setEditable(false);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(tf);
		this.setSize(200, 100);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		RepaintManager.setCurrentManager(new TracingRepaintManager());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new InfiniteRepaint().setVisible(true);
			}
		});
	}
}
