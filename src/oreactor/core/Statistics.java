package oreactor.core;

public class Statistics {
	int numFramesNonDropped = 0;
	int numFramesDropped = 0;
	private long nanoSecondsTotal = 0;
	private int numFrames;
	private long nanoSecondsForAction;
	
	void incFrames() {
		this.numFrames ++;
	}
	
	public void frameProcessedInTime(long nanosec, long timeSpentForAction) {
		this.numFramesNonDropped ++;
		this.incFrames();
		nanoSecondsTotal += nanosec;
		nanoSecondsForAction += timeSpentForAction;
	}
	
	public void frameProcessedNotInTime(long nanosec, long timeSpentForAction) {
		this.numFramesDropped ++;
		this.incFrames();
		nanoSecondsTotal += nanosec;
		nanoSecondsForAction += timeSpentForAction;
	}
	
	public String toString() {
		String ret = "";
		ret = ret + "Total frames:" + numFrames + "\n";
		ret = ret + "Frames dropped:" + numFramesDropped + "\n";
		ret = ret + "Frames not dropped:" + numFramesNonDropped + "\n";
		ret = ret + "Average time per frame [msec]:" + (this.nanoSecondsTotal / (double)this.numFrames / 1000000.0) + "\n";
		ret = ret + "Average time for action per frame [msec]:" + (this.nanoSecondsForAction / (double)this.numFrames / 1000000.0);
		return ret;
	}
}
