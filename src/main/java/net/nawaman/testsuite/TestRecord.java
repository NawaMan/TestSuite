package net.nawaman.testsuite;

import java.util.Vector;

public class TestRecord {

	public TestRecord(
			final Class<?> pTestClass,
			final long     pTime) {
		this.TestClass = pTestClass;
		this.Time      = pTime;
	}
	
	final Class<?> TestClass;
	final long     Time;
	
	@Override
	public String toString() {
		final String aString = toString(this.TestClass, this.Time);
		return aString;
	}
	
	static public String toString(final TestRecord pRecord) {
		final String aString = toString(pRecord.TestClass, pRecord.Time);
		return aString;
	}
	static public String toString(
			final Class<?> pTestClass,
			final long     pTime) {
		final String aSimpleName = pTestClass.getSimpleName();
		final String aString     = String.format(TestSuite.TestRecord_String_Template, aSimpleName, pTime);
		return aString;
	}

	static public String toString(final Vector<TestRecord> pRecords) {
		if (pRecords == null)
			return null;
		
		final StringBuilder SB = new StringBuilder();
		for (int i = 0; i < pRecords.size(); i++) {
			TestRecord aTestRecord    = pRecords.get(i);
			String     aTestRecordStr = TestRecord.toString(aTestRecord);
			SB.append(aTestRecordStr);           
			SB.append("\n");
		}
		
		return SB.toString();
	}
}
