package net.nawaman.testsuite;

public class TestFailError extends RuntimeException {
	
	private static final long serialVersionUID = -5643081642590744197L;

	TestFailError(final String pMessage) {
		super(pMessage);
	}
	
}
