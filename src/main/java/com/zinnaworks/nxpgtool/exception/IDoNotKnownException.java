package com.zinnaworks.nxpgtool.exception;

public class IDoNotKnownException  extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public IDoNotKnownException() {
        super("난 몰라요...");
    }

}
