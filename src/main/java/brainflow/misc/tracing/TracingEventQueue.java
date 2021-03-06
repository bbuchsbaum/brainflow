package brainflow.misc.tracing;

import java.awt.AWTEvent;
import java.awt.EventQueue;

public class TracingEventQueue extends EventQueue {

	private TracingEventQueueThread tracingThread;

	public TracingEventQueue() {
		this.tracingThread = new TracingEventQueueThread(250);
		this.tracingThread.start();
	}

	@Override
	protected void dispatchEvent(AWTEvent event) {
		this.tracingThread.eventDispatched(event);
		super.dispatchEvent(event);
		this.tracingThread.eventProcessed(event);
	}
}
