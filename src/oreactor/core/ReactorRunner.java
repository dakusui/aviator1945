package oreactor.core;

import oreactor.core.Settings.WindowMode;
import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorQuitException;
import oreactor.exceptions.OpenReactorWindowClosedException;

public class ReactorRunner {
	private Logger logger = Logger.getLogger();
	ReactorRunner() {
	}

	int runReactor(String[] args) throws OpenReactorException {
		int ret = 255;
		try {
			ArgParser argParser = ArgParser.createArgParser(args);
			String reactorClassName = argParser.chooseReactorClassName();
			Reactor reactor = Reactor.loadReactor(reactorClassName);
			
			logger.debug("reactor=<" + reactor + ">");
			Settings settings = argParser.loadSettings(); 
			settings.windowMode(WindowMode.Frame);
			logger.debug("settings are loaded.");
			reactor.execute(settings);
		} catch (OpenReactorWindowClosedException e) {
			System.out.println(e.getMessage());
			ret = 0;
		} catch (OpenReactorQuitException e) {
			System.out.println(e.getMessage());
			ret = 0;
		} catch (OpenReactorException e) {
			e.printStackTrace(System.out);
			ret = 1;
		}
		return ret;
	}
	

	public static void main(String[] args) throws OpenReactorException {
		ReactorRunner reactorRunner = new ReactorRunner();
		int ret = 255;
		try {
			ret = reactorRunner.runReactor(args);
		} catch (Throwable t){
			t.printStackTrace();
		} finally {
			System.exit(ret);
		}
	}
}
