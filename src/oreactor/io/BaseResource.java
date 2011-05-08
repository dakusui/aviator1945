package oreactor.io;

import oreactor.exceptions.OpenReactorException;


public abstract class BaseResource implements Resource {
	public void release() throws OpenReactorException {}
}
