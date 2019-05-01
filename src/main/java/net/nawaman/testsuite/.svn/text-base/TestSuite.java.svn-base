package net.nawaman.testsuite;

import java.io.*;
import java.util.*;

/** TestSuite is a group of test cases that will be run together */
public class TestSuite {
	
	static public final String ToQuiet = "Quiet";
	static public final String ToShout = "Shout";
	
	static final String[] ToQuiet_asArray            = new String[] { TestSuite.ToQuiet };
	static final String[] ToShout_asArray            = new String[] { TestSuite.ToShout };
	static final String   TestRecord_String_Template = "Test: %s is successful within %s ms.";
	
	static public boolean Check_isQuiet(String[] pArgs) {
		if (pArgs        == TestSuite.ToQuiet_asArray) return true;
		if (pArgs        == TestSuite.ToShout_asArray) return false;
		if (pArgs        ==                      null) return false;
		if (pArgs.length ==                         0) return false;
		if (pArgs[0]     ==                      null) return false;
		
		return TestSuite.ToQuiet.equals(pArgs[0]);
	}
	

	protected TestSuite() {
		this.setWidthToShow(DEFAULT_WIDTH_TOSHOW);
	}
	
	/** 
	 * eturns the pattern for filtering TestCase class names - The default is null (no-filtering)
	 **/
	protected String getTestCaseClassNameFilterPattern() {
		return null;
	}

	/** Returns the list of classes of the test cases */
	public Vector<Class<? extends TestCase>> getTestClasses() {
		String Pattern = this.getTestCaseClassNameFilterPattern();
		
		// Prepare Test
		Vector<Class<? extends net.nawaman.testsuite.TestCase>> TestClasses;
		TestClasses = new Vector<Class<? extends net.nawaman.testsuite.TestCase>>();
		TestClasses.addAll(Arrays.asList(GetTestCaseClasses(Pattern)));
		return TestClasses;
	}

	/** Automatically discover all tests and Run them all */
	final public void runTests() {
		// Configuration
		final boolean            IsQuiet = true;
		final Vector<TestRecord> Records = new Vector<TestRecord>();

		// Prepare the list of the Tests
		final Vector<Class<? extends TestCase>> aTCs = this.getTestClasses();
		final Vector<Class<? extends TestCase>> TCs = (aTCs != null)
				? aTCs
				: new Vector<Class<? extends TestCase>>();
		
		final Object[] TestClassObjects = TCs.toArray(new Class<?>[TCs.size()]);
		
		@SuppressWarnings("unchecked")
		final Class<? extends TestCase>[] TestClasses = (Class<? extends TestCase>[])TestClassObjects; 

		// Do the test
		DoAllTestMain(this, TestClasses, IsQuiet, Records);

		// Show the result
		TestRecord.toString(Records);

		final StringBuilder Ending = new StringBuilder("DONE!!! ");
		while (Ending.length() < (this.WidthTitle)) Ending.append("-"); // -1 is adjustment (No idea why it is)
		this.println(Ending.toString());
	}

	/** Do the TestMain from the TestClass */
	static public void DoTestMain(
			final TestSuite          pTS,
			final Class<?>           pTestClass,
			final boolean            pIsQuiet,
			final Vector<TestRecord> pTestRecord) {

		// Ensure no null
		if (pTS == null)
			throw new NullPointerException();

		// To shout or not to shout
		final String[] ToVerbal = pIsQuiet
				? TestSuite.ToQuiet_asArray
				: TestSuite.ToShout_asArray;

		// Preparation
		long      StartTime = 0;
		long      EndTime   = 0;
		Throwable Problem   = null;
		try {
			// Do the test
			StartTime = System.currentTimeMillis();

			final TestCase aTC = (TestCase) pTestClass.newInstance();
			aTC.setTestSuite(pTS);

			// Do the test
			aTC.test(ToVerbal);
			EndTime = System.currentTimeMillis();
			
		} catch (Throwable T) {
			Problem = T;
		}

		// Throw if there is a problem
		if (Problem != null)
			throw new RuntimeException(
					String.format(
						"Test: %s fail with error: %s",
						pTestClass.getSimpleName(), Problem),
					Problem
			);

		// Record the problem if exist
		final long aTestTime = EndTime - StartTime;
		if (pTestRecord != null) {
			final TestRecord aTestRecord = new TestRecord(pTestClass, EndTime - StartTime);
			pTestRecord.add(aTestRecord);
		}
		// Or shout out if needed
		else if (!pIsQuiet)
			TestRecord.toString(pTestClass, aTestTime);
	}

	/** Do test main from all test case of the test cases' classes from the array */
	static public void DoAllTestMain(
			final TestSuite          pTS,
			final Class<?>[]         pTestClasses,
			final boolean            pIsQuiet,
			final Vector<TestRecord> pTestRecord) {
		
		if(pTestClasses == null)
			return;
		
		for(int i = 0; i < pTestClasses.length; i++)
			DoTestMain(pTS, pTestClasses[i], pIsQuiet, pTestRecord);
	}

	/** Do test main from all test case of the test cases' classes from the vector */
	static public void DoAllTestMain(TestSuite TS, Vector<Class<?>> pTestClasses, boolean pIsQuiet,
			Vector<TestRecord> pTestRecord) {
		
		if(pTestClasses == null) return;
		for(int i = 0; i < pTestClasses.size(); i++) DoTestMain(TS, pTestClasses.get(i), pIsQuiet, pTestRecord);
	}

	// Default implementation for TestCase -----------------------------------------------------------------------------
	
	/** Process before the the test case is performed */
	public boolean preTest(TestCase TC, final String ... Args) {
		return true;
	}
	/** Process after the the test case is performed */
	public boolean postTest(TestCase TC, final String ... Args) {
		return true;
	}

	/** This method will be called just before exist whether success or fail. */
	protected void doFinally(TestCase TC, boolean IsAllSuccess, final String ... Args) {
		return;
	}
	
	/** Actually do this test case by running all the test values */
	protected void doTest(TestCase TC, final String ... Args) {
		TC.doTestEntries(Args);
	}

	/** Process the first object */
	public Object processTestValue(TestCase TC, Object pTestValue) {
		return pTestValue;
	}
	/** Process the second object */
	public Object processExpectedValue(TestCase TC, Object pExpectedValue) {
		return pExpectedValue;
	}
	
	/** Process the printed output. */
	public String processCapturedOutput(TestCase TC, String pCapturedOutput) {
		return TC.toString(pCapturedOutput);
	}

	/** Compares two objects for the assertion */
	public boolean compare(TestCase TC, Object pTestValue, Object pExpectedValue) {
		if(pTestValue == pExpectedValue)                     return true;
		if((pTestValue == null) || (pExpectedValue == null)) return false;
		return pTestValue.equals(pExpectedValue);
	}

	/** Returns the string representation of the object */
	public String toString(TestCase TC, Object O) {
		return Util.GetToString(O);
	}
	
	/** Do an individual assertion without processing - Customize this if you want a different method of comparing */
	protected void doAssertRAW(TestCase TC, Object pTestValue, Object pExpectedValue) {
		TC.doAssertRAWCompare(pTestValue, pExpectedValue);
	}
	
	// Printing and dosplaying -----------------------------------------------------------------------------------------

	static final PrintStream SYSTEM_OUT           = System.out;
	static final PrintStream SYSTEM_ERR           = System.err;
	static final int         DEFAULT_WIDTH_TOSHOW = 40;

	int WidthToShow = DEFAULT_WIDTH_TOSHOW;
	int WidthToCut;
	int WidthQoute;
	int WidthTitle;

	/** Print a text without appearing in the output capture */
	final protected void print(String pText) {
		SYSTEM_OUT.print(pText);
	}

	/** Print a text without appearing in the output capture */
	final protected void println() {
		SYSTEM_OUT.println();
	}

	/** Print a text without appearing in the output capture */
	final protected void println(Object pText) {
		SYSTEM_OUT.println(pText);
	}

	/** Print a text without appearing in the output capture */
	final protected void show(Object pObj) {
		SYSTEM_OUT.println(pObj + ":" + ((pObj == null) ? "Void" : pObj.getClass().getSimpleName()));
	}

	/** Print a text without appearing in the output capture */
	final protected void printf(String pFormat, Object... Args) {
		SYSTEM_OUT.printf(pFormat, (Object[]) Args);
	}

	/** The with of the comparison value */
	final protected int getWidthToShow() {
		return this.WidthToShow;
	}

	/** Changes the Width of the comparison value. The maximum value is 5. */
	final protected void setWidthToShow(int Width) {
		this.WidthToShow = (Width < 5)?5:Width;

		this.WidthToCut = this.WidthToShow - 3;
		this.WidthQoute = this.WidthToShow + 3;
		this.WidthTitle = this.WidthToShow * 2 + 30;
	}
	
	final public String prepareValueStringToShow(String pStr) {
		String aStr = pStr;
		
		if (aStr.length() > this.WidthToShow)
			aStr = pStr.substring(0, this.WidthToCut) + "...";
		
		StringBuilder SB = new StringBuilder(String.format(" `%s`", aStr));
		Util.AddTailChars(SB, this.WidthQoute, ' ');

		String aDisplayValue = SB.toString();
		return aDisplayValue;
	}

	/** Execute the test suite (should be called from the sub class Main function) */
	static public void runTests(final String... Args) {
		try {
			
			Class<? extends TestSuite> aTestSuite_CallerClass;
				
			aTestSuite_CallerClass = Util.GetTestSuite_CallerClass();
			TestSuite aTestSuite   = aTestSuite_CallerClass.newInstance();
			aTestSuite.runTests();
			
		} catch (Exception CCE) {
			throw new RuntimeException(CCE);
		}
	}

	static protected Class<? extends TestCase>[] GetTestCaseClasses() {
		final Class<? extends TestCase>[] aTestCaseClasses = Util.DiscoverClasses(null, TestCase.class);
		return aTestCaseClasses;
	}
	
	static protected Class<? extends TestCase>[] GetTestCaseClasses(final String FilterName) {
		final Class<? extends TestCase>[] aTestCaseClasses = Util.DiscoverClasses(FilterName, TestCase.class); 
		return aTestCaseClasses;
	}
	
	// SelfTest --------------------------------------------------------------------------------------------------------
	
	static public final void __SelfTest() {
		__SelfTest_IsQuite();
	}
	
	static private void __SelfTest_IsQuite() {
		Asserter.Assert( TestSuite.Check_isQuiet(TestSuite.ToQuiet_asArray));
		Asserter.Assert(!TestSuite.Check_isQuiet(TestSuite.ToShout_asArray));
	}

}
