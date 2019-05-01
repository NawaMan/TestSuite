package net.nawaman.testsuite;

/**
 * Utility class for asserion.
 **/
public class Asserter {
	
	static public final Asserter Default = new Asserter();
	
	protected Asserter() {}
	
	// Static functions ----------------------------------------------------------------------------
	
	static public void ThrowAssertError() {
		throw new AssertionError();
	}
	
	static public void Assert(final boolean aIsAsserted) {
		if (!aIsAsserted)
			ThrowAssertError();
	}
	
	static public void AssertExact(
			final Object aValue1,
			final Object aValue2) {
		
		if (aValue1 == aValue2)
			return;
		
		ThrowAssertError();
	}
	
	static public void AssertEquals(
			final Object aValue1,
			final Object aValue2) {
		
		final boolean aIsEqual = UCompare.CheckEquals(aValue1, aValue2);
		if (aIsEqual)
			return;
		
		throw new AssertionError();
	}
	
	static public void AssertInequals(
			final Object aValue1,
			final Object aValue2) {
		
		final boolean aIsEqual = UCompare.CheckEquals(aValue1, aValue2);
		if (!aIsEqual)
			return;		
		
		throw new AssertionError();
	}

	// Non-static functions --------------------------------------------------------------------------------------------
	
	public void assertTrue(final boolean aIsAsserted) {
		Assert(aIsAsserted);
	}

	public void assertFalse(final boolean aIsNotAsserted) {
		Assert(!aIsNotAsserted);
	}
	
	public void assertExact(
			final Object aValue1,
			final Object aValue2) {
		AssertExact(aValue1, aValue2);
	}
	
	public void assertEquals(
			final Object aValue1,
			final Object aValue2) {
		AssertEquals(aValue1, aValue2);
	}
	
	public void assertInequals(
			final Object aValue1,
			final Object aValue2) {
		AssertInequals(aValue1, aValue2);
	}
	
}