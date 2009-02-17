package avis.sound;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import avis.base.AException;
import avis.base.Avis;


public class AMultiThreadedSoundManager extends ASoundManager {
	private List<Command> queue = new LinkedList<Command>();
	private ASoundManager target;
	private boolean finished = false;
	private Thread thread;
	
	public AMultiThreadedSoundManager(final ASoundManager target) throws SAudioException {
		super();
		this.target = target;
		thread = new Thread(new Runnable() {
			public void run() {
				Avis.logger().info("Sound: Sound Manager Thread is started.");
				while (!finished) {
					Command cmd = null;
					synchronized (queue) {
						if (queue.size() > 0) {
							cmd = queue.remove(0);
						}
					}
					if (cmd != null) {
						cmd.run(target);
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace(System.out);
					}
				}
				Avis.logger().info("Sound: Sound Manager Thread is finished.");
			}
		}) {
		};	
		thread.start();
	}

	@Override
	public String load(String resourceName) throws AException {
		return target.load(resourceName);
	}

	@Override
	public void play(String resourceName) throws AException {
		synchronized (queue) {
			queue.add(new Command(Command.Name.Play, resourceName));
		}
	}

	@Override
	public void prepare(int maxVoices) throws SAudioException {
		target.prepare(maxVoices);
	}

	@Override
	public void render() {
		synchronized (queue) {
			queue.add(new Command(Command.Name.Render, null));
		}
	}
	private static class Command {
		private static enum Name {
			Play, Render
		}
		Command(Name name, String resource) {
			this.name = name;
			this.resource = resource;
		}
		public void run(ASoundManager target) {
			if (this.name == Name.Play) {
				try {
					target.play(this.resource);
				} catch (AException e) {
					e.printStackTrace(System.out);
				}
			} else if (this.name == Name.Render) {
				target.render();
			}
			
		}
		Name name;
		String resource;
	}

	@Override
	public void terminate() {
		synchronized (queue) {
			finished = true;
			target.terminate();
			queue.clear();
		}
	}

	@Override
	protected Set<String> resources() {
		return target.resources();
	}

	@Override
	public void stop(String resourceName) {
		target.stop(resourceName);
	}
}
