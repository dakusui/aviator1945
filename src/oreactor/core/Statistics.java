package oreactor.core;

public class Statistics {
	int numFramesNonDropped = 0;
	int numFramesDropped = 0;
	private long nanoSecondsTotal = 0;
	private int numFrames;
	private long nanoSecondsForAction;
	private long nanoSecondsForDroppedFrame;
	private long nanoSecondsForDroppedFrameAction;
	private long nanoSecondsForNonDroppedFrame;
	private long nanoSecondsForNonDroppedFrameAction;
	
	void incFrames(boolean dropped) {
		this.numFrames ++;
		if (dropped) {
			this.numFramesDropped ++;
		} else {
			this.numFramesNonDropped ++;
		}
	}
	
	public void frameProcessedInTime(long nanosec, long timeSpentForAction) {
		this.incFrames(false);
		nanoSecondsForNonDroppedFrame += nanosec;
		nanoSecondsForNonDroppedFrameAction += timeSpentForAction;
		nanoSecondsTotal += nanosec;
		nanoSecondsForAction += timeSpentForAction;
	}
	
	public void frameProcessedNotInTime(long nanosec, long timeSpentForAction) {
		this.incFrames(true);
		nanoSecondsForDroppedFrame += nanosec;
		nanoSecondsForDroppedFrameAction += timeSpentForAction;
		nanoSecondsTotal += nanosec;
		nanoSecondsForAction += timeSpentForAction;
	}
	
	public String toString() {
		String ret = "";
		ret = ret + "Total frames:" + numFrames + "\n";
		ret = ret + "Frames dropped:" + numFramesDropped + "\n";
		ret = ret + "Frames not dropped:" + numFramesNonDropped + "\n";
		ret = ret + "====\n";
		ret = ret + "Average time per dropped frame [msec]:" + (this.nanoSecondsForDroppedFrame / (double)this.numFramesDropped / 1000000.0) + "\n";
		ret = ret + "Average time for action per dropped frame [msec]:" + (this.nanoSecondsForDroppedFrameAction / (double)this.numFramesDropped / 1000000.0) + "\n";
		ret = ret + "----\n";
		ret = ret + "Average time per non-dropped frame [msec]:" + (this.nanoSecondsForNonDroppedFrame / (double)this.numFramesNonDropped / 1000000.0) + "\n";
		ret = ret + "Average time for action per non-dropped frame [msec]:" + (this.nanoSecondsForNonDroppedFrameAction / (double)this.numFramesNonDropped / 1000000.0) + "\n";
		ret = ret + "----\n";
		ret = ret + "Average time per frame [msec]:" + (this.nanoSecondsTotal / (double)this.numFrames / 1000000.0) + "\n";
		ret = ret + "Average time for action per frame [msec]:" + (this.nanoSecondsForAction / (double)this.numFrames / 1000000.0) + "\n";
		return ret;
	}
}
