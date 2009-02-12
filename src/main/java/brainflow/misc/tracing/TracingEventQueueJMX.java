package brainflow.misc.tracing;

import java.awt.AWTEvent;
import java.awt.EventQueue;

public class TracingEventQueueJMX extends EventQueue {

	private TracingEventQueueThreadJMX tracingThread;

	public TracingEventQueueJMX() {
		this.tracingThread = new TracingEventQueueThreadJMX(500);
		this.tracingThread.start();
	}

	@Override
	protected void dispatchEvent(AWTEvent event) {
		this.tracingThread.eventDispatched(event);
		super.dispatchEvent(event);
		this.tracingThread.eventProcessed(event);
	}
}
