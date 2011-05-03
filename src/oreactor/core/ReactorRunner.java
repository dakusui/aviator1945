package oreactor.core;

import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorWindowClosedException;

public class ReactorRunner {
	ReactorRunner() {
	}

	int runReactor(String[] args) throws OpenReactorException {
		int ret = 255;
		try {
			ArgParser argParser = ArgParser.createArgParser(args);
			Reactor reactor = argParser.chooseReactor();
			reactor.argParser(argParser);
			System.out.println("reactor=<" + reactor + ">");
			Settings settings = reactor.loadSettings(); 
			System.out.println("settings are loaded.");
			reactor.execute(settings);
		} catch (OpenReactorWindowClosedException e) {
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
