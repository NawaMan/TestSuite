package net.nawaman.testsuite.test;

import java.io.InputStream;

import net.nawaman.testsuite.*;

public class Tests {
	
	static public void main(final String ... pArgs) {
		Test_Asserter();
		
		TestSuite.__SelfTest();
		
		Test_ClassComparator();
		Test_StringComparator();
		
		Test_IsQuite();
		System.out.println("DONE!!!");
	}
	
	// Asserter --------------------------------------------------------------------------------------------------------

	static private void AssertSuccess(Runnable aTest) {
		aTest.run();
	}
	static private void AssertFailure(Runnable aTest) {
		boolean aIsExpected = false;
		try {
			aTest.run();
		} catch (AssertionError e) {
			aIsExpected = true;
		}
		if (!aIsExpected)
			throw new AssertionError();
	}
	
	static private void Test_Asserter() {
		
		final Integer aTestInteger1 = new Integer(2000);
		final Integer aTestInteger2 = new Integer(2000);
		final Integer aTestInteger3 = new Integer(3000);
		
		
		AssertFailure(new Runnable() { public void run() {
			Asserter.ThrowAssertError();
		}});
		

		AssertSuccess(new Runnable() { public void run() {
			Asserter .Assert(true);
		}});
		AssertFailure(new Runnable() { public void run() {
			Asserter.Assert(false);
		}});
		
		
		AssertSuccess(new Runnable() { public void run() {
			Asserter.AssertExact(aTestInteger1, aTestInteger1);
		}});
		AssertFailure(new Runnable() { public void run() {
			Asserter.AssertExact(aTestInteger1, aTestInteger2);
		}});
		AssertFailure(new Runnable() { public void run() {
			Asserter.AssertExact(aTestInteger1, aTestInteger3);
		}});
		
		
		AssertSuccess(new Runnable() { public void run() {
			Asserter.AssertEquals(aTestInteger1, aTestInteger1);
		}});
		AssertSuccess(new Runnable() { public void run() {
			Asserter.AssertEquals(aTestInteger1, aTestInteger2);
		}});
		AssertFailure(new Runnable() { public void run() {
			Asserter.AssertEquals(aTestInteger1, aTestInteger3);
		}});
		
		
		AssertFailure(new Runnable() { public void run() {
			Asserter.AssertInequals(aTestInteger1, aTestInteger1);
		}});
		AssertFailure(new Runnable() { public void run() {
			Asserter.AssertInequals(aTestInteger1, aTestInteger2);
		}});
		AssertSuccess(new Runnable() { public void run() {
			Asserter.AssertInequals(aTestInteger1, aTestInteger3);
		}});
		
		
		
		final Asserter aAsserter = Asserter.Default;
		
		AssertSuccess(new Runnable() { public void run() {
			aAsserter.assertTrue(true);
		}});
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertTrue(false);
		}});
		
		AssertSuccess(new Runnable() { public void run() {
			aAsserter.assertFalse(false);
		}});
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertFalse(true);
		}});
		
		
		AssertSuccess(new Runnable() { public void run() {
			aAsserter.assertExact(aTestInteger1, aTestInteger1);
		}});
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertExact(aTestInteger1, aTestInteger2);
		}});
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertExact(aTestInteger1, aTestInteger3);
		}});
		
		
		AssertSuccess(new Runnable() { public void run() {
			aAsserter.assertEquals(aTestInteger1, aTestInteger1);
		}});
		AssertSuccess(new Runnable() { public void run() {
			aAsserter.assertEquals(aTestInteger1, aTestInteger2);
		}});
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertEquals(aTestInteger1, aTestInteger3);
		}});
		
		
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertInequals(aTestInteger1, aTestInteger1);
		}});
		AssertFailure(new Runnable() { public void run() {
			aAsserter.assertInequals(aTestInteger1, aTestInteger2);
		}});
		AssertSuccess(new Runnable() { public void run() {
			aAsserter.assertInequals(aTestInteger1, aTestInteger3);
		}});
	}
	
	// Class Comparator ------------------------------------------------------------------------------------------------
	
	static private class File {}
	
	static private void Test_ClassComparator() {
		final ClassComparator aClassComparator = ClassComparator.Default;
		
		final int aCompare_TwoClearlyDifferentClasses = aClassComparator.compare(Integer.class, InputStream.class);
		Asserter.AssertInequals(aCompare_TwoClearlyDifferentClasses, 0);
		
		final int aCompare_ClassesInSamePackage = aClassComparator.compare(Integer.class, String.class);
		Asserter.AssertInequals(aCompare_ClassesInSamePackage, 0);
		
		final int aCompare_ClassesInSameName = aClassComparator.compare(File.class, java.io.File.class);
		Asserter.AssertInequals(aCompare_ClassesInSameName, 0);
		
		final int aCompare_SameClasses = aClassComparator.compare(java.io.File.class, java.io.File.class);
		Asserter.AssertEquals(aCompare_SameClasses, 0);
		

		Asserter.Assert(aClassComparator.compare(java.io  .File   .class, java.io  .File   .class) == 0);
		Asserter.Assert(aClassComparator.compare(java.lang.Integer.class, java.lang.Byte   .class) >  0);
		Asserter.Assert(aClassComparator.compare(java.lang.Byte   .class, java.lang.Integer.class) <  0);

		final StringComparator aStringComparator = StringComparator.Default;
		Asserter.AssertExact(
			aClassComparator .compare( java.io.File.class,  java.io.File.class),
			aStringComparator.compare("java.io.File"     , "java.io.File")
		);
		Asserter.AssertExact(
			aClassComparator .compare( java.lang.Integer.class,  java.lang.Byte.class),
			aStringComparator.compare("java.lang.Integer"     , "java.lang.Byte")
		);
		Asserter.AssertExact(
			aClassComparator .compare( java.lang.Byte.class,  java.lang.Integer.class),
			aStringComparator.compare("java.lang.Byte"     , "java.lang.Integer")
		);
	}
	
	static private void Test_StringComparator() {
		final StringComparator aStringComparator = StringComparator.Default;
		
		Asserter.Assert(aStringComparator.compare("String", "String") == 0);
		
		final String aStr = "Str";
		Asserter.Assert(aStringComparator.compare("String",                   aStr + "ing") == 0);
		Asserter.Assert(aStringComparator.compare(aStr + "ING".toLowerCase(), aStr + "ing") == 0);

		Asserter.Assert(aStringComparator.compare("a", "b") == -1);
		Asserter.Assert(aStringComparator.compare("b", "a") ==  1);

		Asserter.Assert(aStringComparator.compare("aa",  "a")   ==  1);
		Asserter.Assert(aStringComparator.compare("aaa", "a")   ==  2);
		Asserter.Assert(aStringComparator.compare("a",   "aaa") == -2);
		Asserter.Assert(aStringComparator.compare("b",   "aaa") ==  1);
	}
	
	// Test Suite ------------------------------------------------------------------------------------------------------
	
	static private void Test_IsQuite() {
		
		// See in 
		// TestSuite.SelfTest();

		if ( TestSuite.Check_isQuiet(null))
			throw new AssertionError();

		if ( TestSuite.Check_isQuiet(new String[0]))
			throw new AssertionError();

		if ( TestSuite.Check_isQuiet(new String[] {}))
			throw new AssertionError();

		if (!TestSuite.Check_isQuiet(new String[] { TestSuite.ToQuiet }))
			throw new AssertionError();

		if (!TestSuite.Check_isQuiet(new String[] { "Quiet" }))
			throw new AssertionError();
	}

}
