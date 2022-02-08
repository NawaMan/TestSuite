package net.nawaman.testsuite;

import java.util.regex.*;

/** Individual Test */
public class TestEntry {

	/** The default title of a section */
	static public final String DefaultTitle_Section    = "Section";
	/** The default title of a sub-section */
	static public final String DefaultTitle_SubSection = "SubSection";

	TestEntry() {}

	/** Creates a new section. */
	static public TestEntry newSection(final String pTitle) {
		final TestEntry aSection = TestEntry.newSection(pTitle, false);
		return aSection;
	}

	/** Creates a new section. */
	static public TestEntry newSection(
			final String  pTitle,
			final boolean pIsToRecordTime
		) {
		final String aTitle = (pTitle != null)
				? pTitle 
				: TestEntry.DefaultTitle_Section;
		final AsSection aSection = new AsSection(aTitle, pIsToRecordTime); 
		return aSection;
	}
	/** Creates a new sub section. */
	static public TestEntry newSubSection(final String pTitle) {
		final String aTitle = (pTitle != null)
				? pTitle 
				: TestEntry.DefaultTitle_Section;
		final AsSubSection aSubSection = new AsSubSection(aTitle); 
		return aSubSection;
	}
	

	/** Creates a new sub section. */
	static public TestEntry newTestValue(
			final Object pValue,
			final Object pExpectedValue,
			final String pExpectedOutput) {
		final TestEntry aTestValue = new AsSimpleTest(
				pValue,
				pExpectedValue,
				pExpectedOutput
			);
		return aTestValue;
	}

	/** Creates a new sub section. */
	static public TestEntry newTest(
			final Test   pTest,
			final Object pExpectedValue,
			final String pExpectedOutput) {
		final TestEntry aTest = new AsComplexTest(pTest, pExpectedValue, pExpectedOutput);
		return aTest;
	}

	/** Creates a new sub section. */
	static public TestEntry newValueForError(
			final Object                     pValue,
			final Class<? extends Throwable> pExpectedProblemClass,
			final String                     pExpectedOutput) {
		final TestValue aTestValue = new TestValue(pValue);
		final TestEntry aTestEntry = new AsFullTest(aTestValue, pExpectedProblemClass, null, pExpectedOutput);
		return aTestEntry;
	}
	/** Creates a new sub section. */
	static public TestEntry newValueForError(
			final Object                     pValue,
			final Class<? extends Throwable> pExpectedProblemClass,
			final String                     pExpectedProblemPattern,
			final String                     pExpectedOutput) {
		final TestValue aTestValue = new TestValue(pValue);
		final TestEntry aTestEntry = new AsFullTest(
				aTestValue,
				pExpectedProblemClass,
				pExpectedProblemPattern,
				pExpectedOutput
			);
		return aTestEntry;
	}

	/** Creates a new sub section. */
	static public TestEntry newTestForError(
			final Test                       pTest,
			final Class<? extends Throwable> pExpectedProblemClass,
			final String                     pExpectedOutput) {
		final TestEntry aTestEntry = new AsFullTest(
				pTest,
				pExpectedProblemClass,
				null,
				pExpectedOutput
			);
		return aTestEntry;
	}

	/** Creates a new sub section. */
	static public TestEntry newTestForError(
			final Test                       pTest,
			final Class<? extends Throwable> pExpectedProblemClass,
			final String                     pExpectedProblemPattern,
			final String                     pExpectedOutput) {
		final TestEntry aTestEntry = new AsFullTest(
				pTest,
				pExpectedProblemClass,
				pExpectedProblemPattern,
				pExpectedOutput
			);
		return aTestEntry;
	}

	// Services --------------------------------------------------------------------------------------------------------

	// Not about testing -----------------------------------------------------------------
	boolean isSection()      { return false; }
	boolean isSubSection()   { return false; }
	String  getTitle()       { return  null; }
	boolean isToRecordTime() { return false; }

	// About testing ---------------------------------------------------------------------
	boolean isTest()               { return false; }
	Object  getTest()              { return  null; }
	Object  runTest(TestCase TC)   { return  null; }
	boolean shouldOutputCaptured() { return false; }
	Object  getExpectedValue()     { return  null; }
	String  getExpectedOutput()    { return  null; }

	Class<? extends Throwable> getExpectedProblemClass()   { return null; }
	Pattern                    getExpectedProblemPattern() { return null; }
	
	// Sub-classes -----------------------------------------------------------------------------------------------------

	static class AsNonTest extends TestEntry {
		final private String Title;
		
		AsNonTest(final String pTitle) {
			this.Title = pTitle;
		}
		@Override
		String getTitle() {
			return this.Title;
		}
	}
	
	static final class AsSection extends TestEntry.AsNonTest {
		final private boolean IsToRecordTime;
		
		AsSection(
				final String  pTitle,
				final boolean pIsToRecordTime) {
			super(pTitle);
			this.IsToRecordTime = pIsToRecordTime;
		}
		@Override
		boolean isSection() {
			return true;
		}
		@Override
		boolean isToRecordTime() {
			return this.IsToRecordTime;
		}
	}

	static final class AsSubSection extends TestEntry.AsNonTest {
		AsSubSection(final String pTitle) {
			super(pTitle);
		}
		@Override
		boolean isSubSection() {
			return true;
		}
	}

	static class AsTest extends TestEntry {
		final private Object ExpectedValue;
		final private String ExpectedOutput;
		
		AsTest(
				final Object pExpectedValue,
				final String pExpectedOutput) {
			this.ExpectedValue  = pExpectedValue;
			this.ExpectedOutput = pExpectedOutput;
		}
		@Override
		boolean isTest() {
			return true;
		}
		@Override
		boolean shouldOutputCaptured() {
			return (this.ExpectedOutput != null);
		}
		@Override
		Object getExpectedValue() {
			return this.ExpectedValue;
		}
		@Override
		String getExpectedOutput() {
			return  this.ExpectedOutput;
		}
	}
	
	static final class TestValue implements Test {
		final private Object Value;
		
		TestValue(final Object pValue) {
			this.Value = pValue;
		}
		@Override
		public Object run(TestCase TC) {
			return this.Value;
		}
		public Object getValue() {
			return this.Value;
		}
		@Override
		public String toString() {
			if (this.Value == null)
				return "null";
			return this.Value.toString();
		}
	}

	static final class AsSimpleTest extends TestEntry.AsTest {
		final private Object Value;
		
		AsSimpleTest(
				final Object pValue,
				final Object pExpectedValue,
				final String pExpectedOutput) {
			super(pExpectedValue, pExpectedOutput);
			this.Value = pValue;
		}
		@Override
		Object runTest(final TestCase TC) {
			return this.Value;
		}
	}
	static class AsComplexTest extends TestEntry.AsTest {
		final private Test Test;
		
		AsComplexTest(
				final Test   pTest,
				final Object pExpectedValue,
				final String pExpectedOutput) {
			super(pExpectedValue, pExpectedOutput);
			this.Test = pTest;
		}
		@Override
		boolean isTest() {
			return (this.Test != null);
		}
		@Override
		Object runTest(final TestCase TC) {
			return this.Test.run(TC);
		}
	}
	static final class AsFullTest extends TestEntry.AsComplexTest {
		final private Class<? extends Throwable> ExpectedProblemClass;
		final private Pattern                    ExpectedProblemPattern;
		
		AsFullTest(
				final Test                       pTest,
				final Class<? extends Throwable> pExpectedProblemClass,
				final String                     pExpectedProblemPattern,
				final String                     pExpectedOutput) {
			super(pTest, null, pExpectedOutput);
			this.ExpectedProblemClass   = pExpectedProblemClass;
			this.ExpectedProblemPattern = this.getExpectedProblemPattern(pExpectedProblemPattern);
		}
		
		private Pattern getExpectedProblemPattern(final String pExpectedProblemPattern) {
			if (pExpectedProblemPattern == null)
				return null;
			
			final Pattern aProblemPattern = Pattern.compile(pExpectedProblemPattern);
			return aProblemPattern;
		}
		
		@Override
		Class<? extends Throwable> getExpectedProblemClass() {
			return this.ExpectedProblemClass;
		}
		@Override
		Pattern getExpectedProblemPattern() {
			return this.ExpectedProblemPattern;
		}
	}
}