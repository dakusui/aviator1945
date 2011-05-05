package oreactor.core;

public class Statistics {
	int numFramesNonDropped = 0;
	int numFramesDropped = 0;
	private long nanoSecondsTotal = 0;
	private int numFrames;
	
	void incFrames() {
		this.numFrames ++;
	}
	
	public void frameProcessedInTime(long nanosec) {
		this.numFramesNonDropped ++;
		this.incFrames();
		nanoSecondsTotal += nanosec;
	}
	
	public void frameProcessedNotInTime(long nanosec) {
		this.numFramesDropped ++;
		this.incFrames();
		nanoSecondsTotal += nanosec;
	}
	
	public String toString() {
		String ret = "";
		ret = ret + "Total frames:" + numFrames + "\n";
		ret = ret + "Frames dropped:" + numFramesDropped + "\n";
		ret = ret + "Frames not dropped:" + numFramesNonDropped + "\n";
		ret = ret + "Average time per frame [msec]:" + (this.nanoSecondsTotal / (double)this.numFrames / 1000000.0);
		return ret;
	}
}
