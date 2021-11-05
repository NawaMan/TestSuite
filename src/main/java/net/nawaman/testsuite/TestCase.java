package net.nawaman.testsuite;

import java.io.*;
import java.util.regex.*;

import net.nawaman.testsuite.TestEntry.*;

/** A test case */
abstract public class TestCase {
	
	// General elements ------------------------------------------------------------------------------------------------
	
	private TestSuite TheTestSuite = null;
	private boolean   IsQuiet      = true;

	/** Change the TestQuite */
	final protected void setTestSuite(TestSuite pTestSuite) {
		if (pTestSuite == null)
			return;
		
		this.TheTestSuite = pTestSuite;
	}
	/** Returns the current test suite */
	final public TestSuite getTestSuite() {
		if (this.TheTestSuite == null)
			this.TheTestSuite = new TestSuite();	// default TestSuite
		
		return this.TheTestSuite;
	}
	
	/** Set the assertion to be quiet */
	final protected void setQuiet(boolean pIsQuiet) {
		this.IsQuiet = pIsQuiet;
	}
	/** Set the assertion to be quiet */
	final public boolean isQuiet() {
		return this.IsQuiet;
	}
	
	// Test ------------------------------------------------------------------------------------------------------------
	
	private void doBeforeTest() {
		this.printTestName();
		
		if (!this.preTest())
			throw new RuntimeException("Fail pre-test activity.");
	}
	private void printTestName() {
		String        aSimpleName = this.getClass().getSimpleName();
		StringBuilder aTestName   = new StringBuilder("Testing: " + aSimpleName + " ...");
		
		if(this.isQuiet())
			this.addTestName_TailDots(aTestName);
		
		this.TheTestSuite.print(aTestName.toString());
		
		if(!this.isQuiet())
			this.TheTestSuite.println();
	}
	private void addTestName_TailDots(StringBuilder pTestName) {
		int WidthTitleWithoutDone = TheTestSuite.WidthTitle - 5;	// 5 for " Done"
		Util.AddTailChars(pTestName, WidthTitleWithoutDone, '.');
	}
	
	private void doTheTest(final String ... Args) {
		try     { this.doTest(Args);           }
		catch (Exception e) {
		    e.printStackTrace();
        }
		finally { this.releaseOutputCapture(); }
	}
	private void releaseOutputCapture() {
		this.disableOutputCapture();
	}
	
	private void doAfterTest(final String ... pArgs) {
		if (!this.postTest())
			throw new RuntimeException("Fail post-test activity.");

		this.doFinally(true, pArgs);
		
		if(this.isQuiet())
			 this.printShortDone();
		else this.printLongDONE();
	}
	private void printShortDone() {
		this.TheTestSuite.println(" done");
	}
	private void printLongDONE() {
		StringBuilder aEnding = new StringBuilder("DONE ");
		this.addDONE_TailDashs(aEnding);
		this.TheTestSuite.println(aEnding.toString());
	}
	private void addDONE_TailDashs(StringBuilder pTestName) {
		// -1 is adjustment (No idea why it is)
		int WidthTitleWithoutDONE = TheTestSuite.WidthTitle - 1;
		Util.AddTailChars(pTestName, WidthTitleWithoutDONE, '-');
	}
	
	/** Do this test case (with default configuration) */
	final public void test(final String ... pArgs) {
		this.setQuiet(TestSuite.Check_isQuiet(pArgs));
		this.doBeforeTest();
		this.restartTestNumber();
		this.doTheTest(pArgs);
		this.doAfterTest();
	}
		
	static private void DoTest(TestCase pTestCase, String ... pArgs) {
		pTestCase.test(pArgs);
	}
	
	/**
	 * Run test from the main method. NOTE: Only this test (should be called from the sub class Main function)!!!
	 * 
	 * This method will try to create a TestSuite instance by looking for a TestSuite class in the same package as the
	 *    sub class of the TestCase that is named as `TestSuite` or `AllTests`
	 **/
	static public void runTest(String ... pArgs) {
		Class<? extends TestCase> aTestCase_CallerClass;
		
		aTestCase_CallerClass = Util.GetTestCase_CallerClass();
		TestCase  aTestCase   = Util.PrepareTestCase(aTestCase_CallerClass);
		DoTest(aTestCase, pArgs);
	}
	
	// Testing ---------------------------------------------------------------------------------------------------------
	
	private int     TestNumber =  0;
	private String  Section    = "Section";
	private String  SubSection = "SubSection";
	private boolean NoExit     = false;
	
	/** Checks if no exit when test fail. */
	protected boolean isNoExit() {
		return this.NoExit;
	}
	
	protected void toNoExit() {
		this.NoExit = true;
	}
	protected void clearNoExit() {
		this.NoExit = false;
	}
	
	static private final String AssertionErrorMessageTemplate = "Test `%s` - `%s` #%d FAIL%s.\n\n"; 
	/** Throws assertion without error message */
	protected void throwAssertion() {
		this.throwAssertion(null);
	}
	/** Throws assertion with the message */
	protected void throwAssertion(String pMsg) {
		if (!this.isNoExit())
			throw new TestFailError(pMsg);
		
		String aErrorMessage = this.getAssertionErrorMessage(pMsg);
		TestSuite.SYSTEM_ERR.println(aErrorMessage);
	}
	private String getAssertionErrorMessage(String pMsg) {
		String aSectionTitle = this.getSectionTitle();
		String aSubSectTitle = this.getSubSectionTitle();
		int    aTestNumber   = this.TestNumber;
		String aColonMessage = (pMsg == null) ? "" : ": " + pMsg;
		
		String aErrorMessage = String.format(TestCase.AssertionErrorMessageTemplate,
				aSectionTitle,
				aSubSectTitle,
				aTestNumber,
				aColonMessage
			);
		return aErrorMessage;
	}
	
	/** Returns the current test number */
	final public int getTestNumber() {
		return this.TestNumber;
	}
	private void restartTestNumber() {
		this.TestNumber = 0;
	}
	private int increaseTestNumber() {
		int aNewTestNumber = this.TestNumber++;
		return aNewTestNumber;
	}
	
	/** Returns the current Section */
	final public String getSectionTitle() {
		if (this.Section == null)
			this.Section = "Section";
		
		return this.Section;
	}
	/** Returns the current SubSection */
	final public String getSubSectionTitle() {
		if (this.SubSection == null)
			this.SubSection = "SubSection";
		
		return this.SubSection;
	}

	/** This method will be called just before exist whether success or fail. */
	protected void doFinally(boolean IsAllSuccess, final String ... Args) {
		TestSuite aTestSuite = this.getTestSuite();
		aTestSuite.doFinally(this, IsAllSuccess, Args);
	}
	
	/** Actually do this test case by running all the test values */
	protected void doTest(final String ... Args) {
		TestSuite aTestSuite = this.getTestSuite(); 
		aTestSuite.doTest(this, Args);
	}
	
	/** Process before the the test case is performed */
	public boolean preTest(final String ... pArgs) {
		TestSuite aTestSuite    = this.getTestSuite();
		boolean   aIsToContinue = aTestSuite.preTest(this, pArgs);
		return aIsToContinue;
	}
	/** Process after the the test case is performed */
	public boolean postTest(final String ... Args) {
		TestSuite aTestSuite    = this.getTestSuite();
		boolean   aIsToContinue = aTestSuite.postTest(this, Args);
		return aIsToContinue;
	}

	/** Process the first object */
	public Object processTestValue(Object pTestValue) {
		TestSuite aTestSuite   = this.getTestSuite();
		Object    aResultValue = aTestSuite.processTestValue(this, pTestValue); 
		return aResultValue;
	}
	/** Process the second object */
	public Object processExpectedValue(Object pExpectedValue) {
		TestSuite aTestSuite   = this.getTestSuite();
		Object    aResultValue = aTestSuite.processExpectedValue(this, pExpectedValue); 
		return aResultValue;
	}

	/** Compares two objects for the assertion */
	public boolean compare(Object pTestValue, Object pExpectedValue) {
		TestSuite aTestSuite     = this.getTestSuite();
		boolean   aCompareResult = aTestSuite.compare(this, pTestValue, pExpectedValue); 
		return aCompareResult;
	}

	/** Returns the string representation of the object */
	public String toString(Object O) {
		TestSuite aTestSuite = this.getTestSuite();
		String    aString    = aTestSuite.toString(this, O); 
		return aString;
	}
	
	/** Do an individual assertion without processing - Customize this if you want a different method of comparing */
	protected void doAssertRAW(Object pTestValue, Object pExpectedValue) {
		TestSuite aTestSuite = this.getTestSuite();
		aTestSuite.doAssertRAW(this, pTestValue, pExpectedValue);
	}
	
	// Displaying ------------------------------------------------------------------------------------------------------
	
	/** Print the prefix for each test (with the test number) */
	private void printTestNumber() {
		this.printTestNumber(false);
	}
	
	/** Print the prefix for each test (with the test number) */
	private void printTestNumber(boolean pIsBlank) {
		if(this.isQuiet()) return;
		
		this.increaseTestNumber();
		int aWidth = Util.GetDigitWidth(this.TestNumber);
		
		if(pIsBlank)
			 this.printf("%"+aWidth+"s      :", " ");
		else this.printf("Test #%"+aWidth+"s:", this.TestNumber);
	}
	
	private String getDisplayValue(Object pValue) {
		String aStr = (pValue == null) ? "null" : this.toString(pValue);
		
		boolean isNeedEscape = aStr.contains("\n") || aStr.contains("\t");
		if (isNeedEscape)
			aStr = Util.GetEscapeText(aStr).toString();
		
		String aDisplayValue = this.TheTestSuite.prepareValueStringToShow(aStr);
		return aDisplayValue;
	}

	/** Print the boot value for each test */
	final protected void printAssert(Object pTestValue, Object ... pExpectedValues) {
		this.doPrintAssert(true, pTestValue, pExpectedValues);
	}

	/** Print the boot value for each test */
	final void doPrintAssert(boolean pIsPrintTestNumber, Object pTestValue, Object ... pExpectedValues) {
		if(this.isQuiet()) return;
		
		String aDisplayValue = this.getDisplayValue(pTestValue);
		if((pExpectedValues == null) || (pExpectedValues.length == 0)) {
			String S2 = this.getDisplayValue("");
			
			// The test number
			this.printTestNumber(!pIsPrintTestNumber);
			this.printf("%s -and-%s\n", aDisplayValue, S2);
			return;
		}
		
		for(int i = 0; i < pExpectedValues.length; i++) {
			String S2 = this.getDisplayValue(pExpectedValues[i]);

			// The test number
			this.printTestNumber(!pIsPrintTestNumber);
			this.TestNumber--;	// Keep the same test number
			this.printf("%s -and-%s\n", aDisplayValue, S2);
			
			if(i == 0) {
				// Tail spaces
				StringBuilder SB = new StringBuilder("");
				while(SB.length() < this.TheTestSuite.WidthQoute) SB.append(" ");
				aDisplayValue = SB.toString();
			}
		}
		this.TestNumber++;
	}
	
	// RAW Assertion (no more processing) ------------------------------------------------------------------------------
	
	// Regular Comparing ------------------------------------------------------

	/** Do assertive compare using this.compare(...) */
	final protected void doAssertRAWCompare(Object pTestValue, Object pExpectedValue) {
		if((pTestValue == pExpectedValue) || this.compare(pTestValue, pExpectedValue)) return;

		TestSuite.SYSTEM_OUT.printf("It's `%s`:%s but it should be `%s`:%s.\n",
			pTestValue,     (pTestValue     == null)?"void":pTestValue    .getClass().getSimpleName(),
			pExpectedValue, (pExpectedValue == null)?"void":pExpectedValue.getClass().getSimpleName()
		);
		
		this.doFinally(false);
		this.throwAssertion();
	}
	
	// Compare like a string --------------------------------------------------
	
	private String ShortLineFormat = "`%s`";
	private String LongLineFormat  = null;
	
	/** Get the display format for the long line */
	private String getLongLineformat() {
		// A new line + A line of '+++' + '%s' + A line of '---' and another new line (1 + W + 2 + W + 1)
		if((this.LongLineFormat == null) || (this.LongLineFormat.length() != ((this.TheTestSuite.WidthTitle * 2) + 4))) {
			StringBuilder SBO = new StringBuilder();
			while(SBO.length() < this.TheTestSuite.WidthTitle) SBO.append("+");

			StringBuilder SBC = new StringBuilder();
			while(SBC.length() < this.TheTestSuite.WidthTitle) SBC.append("-");
			
			this.LongLineFormat = String.format("\n%s%s%s\n", SBO, "\n%s\n", SBC);
		}
		return this.LongLineFormat;
	}

	private String ErrorOpenCloseLine = null;
	
	/** Get the display format for the long line */
	protected String getErrorOpenCloseLine() {
		if((this.ErrorOpenCloseLine == null) || (this.ErrorOpenCloseLine.length() != this.TheTestSuite.WidthTitle)) {
			StringBuilder SB = new StringBuilder();
			while(SB.length() < (this.TheTestSuite.WidthTitle - 4)) SB.append("-");
			this.ErrorOpenCloseLine = String.format("<!%s!>", SB);
		}
		return this.ErrorOpenCloseLine;
	}

	/** Do an individual assertion */
	final protected void doAssertRAWToStringCompare(Object pTestValue, Object pExpectedValue) {
		if(pTestValue == pExpectedValue) return;

		String S1 = this.toString(pTestValue);
		String S2 = this.toString(pExpectedValue);
		
		// Do the compare
		if((S1 == S2) || ((S1 != null) && S1.equals(S2)))
			return;

		TestSuite.SYSTEM_OUT.println();
		TestSuite.SYSTEM_OUT.println(this.getErrorOpenCloseLine());
		
		String ShowS1 = (S1 == null) ? "null" : (String.format((S1.indexOf('\n') == -1) ? this.ShortLineFormat : this.getLongLineformat(), S1));
		String ShowS2 = (S2 == null) ? "null" : (String.format((S2.indexOf('\n') == -1) ? this.ShortLineFormat : this.getLongLineformat(), S2));
			
		TestSuite.SYSTEM_OUT.printf("It's %s but it should be %s.\n", ShowS1, ShowS2);

		int S1_Length = ((S1 == null) ? 0 : S1.length());
		int S2_Length = ((S2 == null) ? 0 : S2.length());
		TestSuite.SYSTEM_OUT.println("Lengths: " + S1_Length + " & " + S2_Length);

		int c = 0;
		boolean Unmatch    = false;
		boolean PreUnmatch = false;
		for(int i = 0; ((i < S1_Length) && (i < S2_Length)); i++) {
				
			Unmatch = (S1.charAt(i) != S2.charAt(i));
			if (Unmatch != PreUnmatch) TestSuite.SYSTEM_OUT.println();
			if (Unmatch) {
				if(++c > 20) {
					TestSuite.SYSTEM_OUT.println(" ... more unmatch");
					break;
				}
				if(PreUnmatch) TestSuite.SYSTEM_OUT.println();
					
				TestSuite.SYSTEM_OUT.print("[" + S1.charAt(i) + "|");
			}

			TestSuite.SYSTEM_OUT.print(S2.charAt(i));
			
			if(Unmatch) TestSuite.SYSTEM_OUT.print("]");
			PreUnmatch = Unmatch;
		}
		
		TestSuite.SYSTEM_OUT.println();
		TestSuite.SYSTEM_OUT.println(this.getErrorOpenCloseLine());
		
		this.doFinally(false);
		this.throwAssertion();
	}
	
	// Assertion methods -----------------------------------------------------------------------------------------------
	
	/** Check if the TestValue (processed) against the expected value (un-processed) */
	final protected void assertSimpleValue(Object pTestValue, Object pExpectedValue) {
		this.printAssert(pTestValue, pExpectedValue);
		this.doAssertRAWCompare(this.processTestValue(pTestValue), pExpectedValue);
	}

	/** Check if the TestValue (processed) is null */
	final protected void assertNull(Object pTestValue) {
		this.assertSimpleValue(pTestValue, null);
	}
	/** Check if the TestValue (processed) is true */
	final protected void assertTrue(Object pTestValue) {
		this.assertSimpleValue(pTestValue, Boolean.TRUE);
	}
	/** Check if the TestValue (processed) is false */
	final protected void assertFalse(Object pTestValue) {
		this.assertSimpleValue(pTestValue, Boolean.FALSE);
	}

	/** Check if the TestValue (processed) equals the ExpectedValue (processed) */
	final protected void assertValue(Object pTestValue, Object pExpectedValue) {
		this.assertValue(pTestValue, pExpectedValue, null);
	}

	/** Check if the TestValue (processed) equals the ExpectedValue (processed) */
	final protected void assertValue(Object pTestValue, Object pExpectedValue, String pExpectedOutput) {
		this.printAssert(pTestValue, pExpectedValue);
		
		if(pExpectedOutput != null) this.startCapture();
		
		pTestValue     = this.processTestValue(pTestValue);
		pExpectedValue = this.processExpectedValue(pExpectedValue);
		
		this.doAssertRAW(pTestValue, pExpectedValue);
		if(pExpectedOutput != null) this.assertCaptured(false, pExpectedOutput);
	}

	/** Check if the toString value of the TestValue (processed) equals with the toString value of the ExpectedValue */
	final protected void assertStringValue(Object pTestValue, Object pExpectedValue) {
		this.assertStringValue(pTestValue, pExpectedValue, null);
	}

	/** Check if the toString value of the TestValue (processed) equals with the toString value of the ExpectedValue */
	final protected void assertStringValue(Object pTestValue, Object pExpectedValue, String pExpectedOutput) {
		this.printAssert(pTestValue, pExpectedValue);
		
		if(pExpectedOutput != null) this.startCapture();
		
		pTestValue     = this.processTestValue(pTestValue);
		pExpectedValue = this.processExpectedValue(pExpectedValue);

		this.doAssertRAWToStringCompare(this.processTestValue(pTestValue), pExpectedValue);
		if(pExpectedOutput != null) this.assertCaptured(false, pExpectedOutput);
	}

	/**
	 * Individually Check if toString value of each ExpectedValue (non-process) is part of the toString value of
	 *     TestValue (non-processed)
	 **/
	final protected void assertRAWStringContains(Object pTestValue, Object ... pExpectedValues) {
		if((pExpectedValues == null) || (pExpectedValues.length == 0)) {
			if(!this.isQuiet()) {
				this.printTestNumber();
				this.println("Skiped");
			}
			return;
		}
		String Str = this.toString(pTestValue);

		for(int i = 0; i < pExpectedValues.length; i++) {
			String S = this.toString(pExpectedValues[i]);
			
			// Print the info
			if(!this.isQuiet()) {
				this.doPrintAssert((i == 0), pTestValue, pExpectedValues[i]);
				this.TestNumber--;	// Keep the same test number
			}
			
			// Check
			if((Str != null) && Str.contains(S))
				continue;

			// Show the differ
			String ShowS1 = (Str == null) ? null : (String.format((Str.indexOf('\n') == -1) ? this.ShortLineFormat : this.getLongLineformat(), Str));
			String ShowS2 = (S   == null) ? null : (String.format((S  .indexOf('\n') == -1) ? this.ShortLineFormat : this.getLongLineformat(), S  ));
			TestSuite.SYSTEM_ERR.printf("The value %s does not contain %s.\n", ShowS1, ShowS2);
			this.throwAssertion();
		}
		this.TestNumber++;
	}

	/**
	 * Individually Check if toString value of each ExpectedValue (non-process) is part of the toString value of
	 *     TestValue (processed)
	 **/
	final protected void assertStringContains(Object pTestValue, Object ... pExpectedValues) {
		this.assertRAWStringContains(this.processTestValue(pTestValue), (Object[])pExpectedValues);
	}
	
	// Output/Inout Capturing ------------------------------------------------------------------------------------------
	
	private ByteArrayOutputStream OutputBuffer = null;
	private PrintStream           Output       = null;
	
	/**
	 * Process the printed output.
	 * 
	 * This result of this function will be asserted agaist whatever printed out to Standard output while
	 *     processTestValue(...) and processExpectedValue(...) is running.
	 * If this returns null, there will be no assertion check of the output.
	 **/
	public String processCapturedOutput(String CapturedOutput) {
		// By default, Ask the TestSuite for an assistent 
		return this.getTestSuite().processCapturedOutput(this, CapturedOutput);
	}
	
	/** Enable/Reset Output Capture */
	final protected void enableOutputCapture() {
		this.OutputBuffer = new ByteArrayOutputStream();
		this.Output       = new PrintStream(this.OutputBuffer);
		System.setOut(this.Output);
	}
	
	/** Reset Output Capture */
	final protected void startCapture() {
		this.OutputBuffer = new ByteArrayOutputStream();
		this.Output       = new PrintStream(this.OutputBuffer);
		System.setOut(this.Output);
	}
	/** Reset Output Capture */
	final protected String endCapture() {
		String OutputValue = this.OutputBuffer.toString();
		this.startCapture();
		return OutputValue;
	}
	
	/** Enable/Reset Output Capture */
	final protected void disableOutputCapture() {
		this.OutputBuffer = null;
		this.Output       = null;
		System.setOut(TestSuite.SYSTEM_OUT);
	}
	
	/** Checks the captured output against the Expected Capture */
	final protected void assertCaptured(Object pExpectedOutput) {
		this.assertCaptured(true, pExpectedOutput);
	}
	
	/** Checks the captured output against the Expected Capture */
	final protected void assertCaptured(boolean IsPrintTestNumber, Object pExpectedOutput) {
		String OutputValue = this.endCapture();
		this.doPrintAssert(IsPrintTestNumber, OutputValue, pExpectedOutput);
		// Process
		OutputValue = this.processCapturedOutput(OutputValue);
		// Do Assert
		this.doAssertRAWToStringCompare(OutputValue, pExpectedOutput);
	}
	
	/** Checks the captured output (processte) against the Expected Capture (non-processed) */
	final protected void assertCapturedContains(Object ... pExpectedOutputs) {
		String OutputValue = this.endCapture();
		this.printAssert(OutputValue, pExpectedOutputs);
		// Process
		OutputValue = this.processCapturedOutput(OutputValue);
		// Do Assert
		this.assertRAWStringContains(OutputValue, pExpectedOutputs);
	}
	
	/** Test for problem */
	final protected void assertProblem(Object pValue, Class<? extends Throwable> ProblemCls, String ExpectedProblemPattern) {
		Pattern P = (ExpectedProblemPattern != null) ? Pattern.compile(ExpectedProblemPattern) : null;
		this.assertFull(pValue, null, null, ProblemCls, P, (String[])null);
	}
	
	/** Test for problem */
	final void assertProblem(Object pValue, Class<? extends Throwable> ProblemCls, Pattern ExpectedProblemPattern, String ... Args) {
		this.assertFull(pValue, null, null, ProblemCls, ExpectedProblemPattern, Args);
	}
	
	/** Test in full */
	final void assertFull(Object pTestOrValue, Object pExpectedValue, String pExpectedOutput,
			Class<? extends Throwable> pProblemCls, Pattern pExpectedProblemPattern, String ... Args) {
		
		boolean IsOutputCaptured   = (pExpectedOutput != null);
		boolean HasExpectedProblem = false; 
		
		try {
			if(IsOutputCaptured) this.startCapture();
			
			Object Value = pTestOrValue;
			if(Value instanceof Test) {
				if(Value instanceof TestValue)
					 Value = ((TestValue)Value).getValue();
				else Value = ((Test)     Value).run(this);
			}
			
			if(pProblemCls != null) {
				// Print before test
				this.printAssert(
						Value,
						pProblemCls.getSimpleName() +
							((pExpectedProblemPattern == null)?"":" :: `" + pExpectedProblemPattern + "`"));
				
				// Do the assertion
				this.doAssertRAW(this.processTestValue(Value), pExpectedValue);
				
				if(pProblemCls != null) {
					TestSuite.SYSTEM_ERR.println(
							"The test #" + (this.TestNumber + 1) + " has success but it is extected to fail from the " +
							"problem of `"+pProblemCls+"`");
					this.throwAssertion();
				}
				
				if(pExpectedProblemPattern != null) {
					TestSuite.SYSTEM_ERR.println(
							"The test #" + (this.TestNumber + 1) + " has success but it is extected to fail from the " +
							"problem with the message that match `"+pExpectedProblemPattern+"`");
				}
			} else { 
				// Do the assert
				this.assertValue(Value, pExpectedValue);
			}
			
		} catch (final TestFailError T) {
			
			if(pProblemCls != null) {
				TestSuite.SYSTEM_ERR.println(
						"The test #" + (this.TestNumber + 1) + " has success but it is extected to fail from the " +
						"problem of `"+pProblemCls+"`");
				this.throwAssertion();
			}
			
			if(pExpectedProblemPattern != null) {
				TestSuite.SYSTEM_ERR.println(
						"The test #" + (this.TestNumber + 1) + " has success but it is extected to fail from the " +
						"problem with the message that match `"+pExpectedProblemPattern+"`");
				this.throwAssertion();
			}
			throw T;
			
		} catch (Throwable T) {
			// Ensure that the problem class is matched
			if((pProblemCls == null) || !pProblemCls.isInstance(T)) {
				TestSuite.SYSTEM_ERR.println("The test #" + (this.TestNumber + 1) + " has failed with a throwable: ");
				T.printStackTrace(System.err);
				this.throwAssertion();
			}
			
			// Ensure that the problem message is matched
			HasExpectedProblem = true;
			Pattern P = pExpectedProblemPattern;
			if(P != null) {
				// Print the problem into a buffer
				ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
				PrintStream           PS   = new PrintStream(BAOS);
				PS.println(T.getMessage());
				T.printStackTrace(PS);
				
				String Msg = BAOS.toString();
				PS.close();
				
				// Check against the buffer
				Matcher Matcher = P.matcher(Msg);
				if(!Matcher.find()) {
					TestSuite.SYSTEM_ERR.println(
							"The test #" + (this.TestNumber + 1) + " problem's message does not match the regular " +
							"expression \""+P.pattern()+".\n");
					TestSuite.SYSTEM_ERR.println("Problem: " + T.getMessage());
					T.printStackTrace(TestSuite.SYSTEM_ERR);
					this.throwAssertion();
				}
			}
			
		} finally {
			// Check the output
			if(IsOutputCaptured) {
				if(pExpectedOutput == null) pExpectedOutput = "";
				this.assertCaptured(false, pExpectedOutput);
				this.TestNumber--;
			}
		}

		// Report that the expected problem does not arose.
		if(!HasExpectedProblem && (pProblemCls != null)) {
			TestSuite.SYSTEM_ERR.println("The test #" + (this.TestNumber + 1) + " expected to have a problem of "+
					pProblemCls+" but it did not.");
			this.throwAssertion();
		}
	}
	
	// On going tests --------------------------------------------------------------------------------------------------

	/** Print a new section title */
	final protected void printSection(String pTitle) {
		if(pTitle == null) pTitle = "";
		this.Section = pTitle;
		
		if(this.isQuiet()) return;
		
		// Show the title
		StringBuilder Title  = new StringBuilder("\n");
		Title.append("== ").append(pTitle).append(" ==");
		while(Title.length() < this.TheTestSuite.WidthTitle) Title.append("=");
		TestSuite.SYSTEM_OUT.println(Title);

		// Reset the number
		this.TestNumber = 0;
	}

	/** Print a new section title */
	final protected void printSubSection(String pTitle) {
		if(pTitle == null) pTitle = "";
		this.SubSection = pTitle;
		
		if(this.isQuiet()) return;
		
		// Show the title
		StringBuilder Title  = new StringBuilder("\n");
		Title.append("-- ").append(pTitle).append(" --");
		while(Title.length() < this.TheTestSuite.WidthTitle) Title.append("-");
		TestSuite.SYSTEM_OUT.println(Title);
	}
	
	/** Print a text without appearing in the output capture */
	final protected void print(String pText) {
		if(this.isQuiet()) return;
		
		this.TheTestSuite.print(pText);
	}
	/** Print a text without appearing in the output capture */
	final protected void println() {
		if(this.isQuiet()) return;
		
		this.TheTestSuite.println();
	}
	/** Print a text without appearing in the output capture */
	final protected void println(Object pObj) {
		if(this.isQuiet()) return;
		
		this.TheTestSuite.println(pObj);
	}
	/** Print a text without appearing in the output capture */
	final protected void show(Object pObj) {
		if(this.isQuiet()) return;
		
		this.TheTestSuite.show(pObj);
		
	}
	/** Print a text without appearing in the output capture */
	final protected void printf(String pFormat, Object ... Args) {
		if(this.isQuiet()) return;
		
		this.TheTestSuite.printf(pFormat, (Object[])Args);
	}

	// Test by TestEntry -----------------------------------------------------------------------------------------------
	// This is the default test method

	private TestEntry[] TEntries = null;
	private int         TEIndex  = 0;
	
	/** Returns the test entry */
	protected TestEntry[] getTestEntries() {
		return null;
	}
	
	/**
	 * Returns the next test entry or null if no more.
	 * 
	 * The default implementation is to ask this.getTestEntries() for an array of TestEntry and iterate them. 
	 **/
	protected TestEntry getTestEntry() {
		if(this.TEntries == null) {
			this.TEntries = this.getTestEntries();
			if(this.TEntries == null) return null;
		}
		
		if(this.TEIndex++ >= this.TEntries.length) return null;
		return this.TEntries[this.TEIndex - 1];
	}
	
	/** The with of the comparison value */
	final protected int getWidthToShow() {
		return this.TheTestSuite.WidthToShow;
	}
	
	/**
	 * Changes the Width of the comparison value.
	 * The maximum value is 5.
	 **/
	final protected void setWidthToShow(int Width) {
		this.TheTestSuite.setWidthToShow(Width);
	}

	/** Perform TestEntry test */
	protected void doTestEntries(final String ... Args) {
		int       I  =   -1;
		TestEntry TE = null;
		while((TE = this.getTestEntry()) != null) {

			// Section
			if(TE.isSection()) {
				if(this.isQuiet()) continue;
				// Show the title
				this.printSection(TE.getTitle());
				continue;
			}
			
			// SubSection
			if(TE.isSubSection()) {
				if(this.isQuiet()) continue;
				// Show the title
				this.printSubSection(TE.getTitle());
				continue;
			}
			
			// Don't know  why it is here
			if (!TE.isTest())
				continue;

			if (!this.isQuiet() && (I == 0))
				this.println();
			
			Object TestOrValue;
			if (TE instanceof AsComplexTest)
				 TestOrValue = ((AsComplexTest)TE).getTest();
			else TestOrValue = TE.runTest(this);
			
			this.assertFull(
					TestOrValue,
					TE.getExpectedValue(),
					TE.getExpectedOutput(),
					TE.getExpectedProblemClass(),
					TE.getExpectedProblemPattern(),
					Args
				);
		}
	}
	
	// Utilities -----------------------------------------------------------------------------------
	
	/** Creates an array of TestEntries */
	final public TestEntry[] newTests(TestEntry ... TEs) {
		return TEs;
	}

	/** Creates a new section. */
	final public TestEntry newSection(String pTitle) {
		TestEntry aSection = TestEntry.newSection(pTitle);
		return aSection;
	}
	/** Creates a new section. */
	final public TestEntry newSection(String pTitle, boolean pIsToRecordTime) {
		TestEntry aSection = TestEntry.newSection(pTitle, pIsToRecordTime);
		return aSection;
	}

	/** Creates a new sub section. */
	final public TestEntry newSubSection(String pTitle) {
		TestEntry aSubSection = TestEntry.newSubSection(pTitle);
		return aSubSection;
	}

	/** Creates a new sub section. */
	final public TestEntry newValue(
			Object pValue,
			Object pExpectedValue) {
		TestEntry aValue = TestEntry.newTestValue(
				pValue,
				pExpectedValue,
				null
			);
		return aValue;
	}
	/** Creates a new sub section. */
	final public TestEntry newValue(
			Object pValue,
			Object pExpectedValue,
			String pExpectedOutput) {
		TestEntry aValue = TestEntry.newTestValue(
				pValue,
				pExpectedValue,
				pExpectedOutput
			);
		return aValue;
	}

	/** Creates a new sub section. */
	final public TestEntry newTest(
			Test   pTest,
			Object pExpectedValue,
			String pExpectedOutput) {
		TestEntry aTest = TestEntry.newTest(
				pTest,
				pExpectedValue,
				pExpectedOutput
			);
		return aTest;
	}

	/** Creates a new sub section. */
	final public TestEntry newValueForError(
			Object                     pValue,
			Class<? extends Throwable> pExpectedProblemClass) {
		TestEntry aValueForError = TestEntry.newValueForError(
				pValue,
				pExpectedProblemClass,
				null,
				null);
		return aValueForError;
	}

	/** Creates a new sub section. */
	final public TestEntry newValueForError(
			Object                     pValue,
			Class<? extends Throwable> pExpectedProblemClass,
			String                     pExpectedProblemPattern) {
		TestEntry aValueForError = TestEntry.newValueForError(
				pValue,
				pExpectedProblemClass,
				pExpectedProblemPattern,
				null
			);
		return aValueForError;
	}
	/** Creates a new sub section. */
	final public TestEntry newValueForError(
			Object                     pValue,
			Class<? extends Throwable> pExpectedProblemClass,
		    String                     pExpectedProblemPattern,
		    String                     pExpectedOutput) {
		TestEntry aValueForError = TestEntry.newValueForError(
				pValue,
				pExpectedProblemClass,
				pExpectedProblemPattern,
				pExpectedOutput
			);
		return aValueForError;
	}

	/** Creates a new sub section. */
	final public TestEntry newTestForError(
			Test                       pTest,
			Class<? extends Throwable> pExpectedProblemClass) {
		TestEntry aTestForError = TestEntry.newTestForError(
				pTest,
				pExpectedProblemClass,
				null
			);
		return aTestForError;
	}
	/** Creates a new sub section. */
	final public TestEntry newTestForError(
			Test                       pTest,
			Class<? extends Throwable> pExpectedProblemClass,
			String                     pExpectedProblemPattern) {
		TestEntry aTestForError = TestEntry.newTestForError(
				pTest,
				pExpectedProblemClass,
				pExpectedProblemPattern,
				null
			);
		return aTestForError;
	}
	/** Creates a new sub section. */
	final public TestEntry newTestForError(
			Test                       pTest,
			Class<? extends Throwable> pExpectedProblemClass,
		    String                     pExpectedProblemPattern,
		    String                     pExpectedOutput) {
		TestEntry aTestForError = TestEntry.newTestForError(
				pTest,
				pExpectedProblemClass,
				pExpectedProblemPattern,
				pExpectedOutput
			);
		return aTestForError;
	}
}
