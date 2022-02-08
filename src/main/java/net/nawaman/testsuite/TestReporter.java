package net.nawaman.testsuite;

/** Report testing progress */
public interface TestReporter {
    
    /** Reports that a TestSuite starts its testing session */
    public boolean reportTestSuiteStart(final TestSuite pTestSuite);
    /** Reports that a TestSuite ends its testing session */
    public void    reportTestSuiteEnd  (final TestSuite pTestSuite);
    
    /** Reports that a TestCase starts its testing session */
    public boolean reportTestCaseStart(final TestCase pTestCase);
    /** Reports that a TestCase ends its testing session */
    public boolean reportTestCaseEnd  (final TestCase pTestCase);
    
    /** Reports that a new section starts its testing session */
    public boolean reportSectionStart(final TestCase pTestCase, final String pSection);
    /** Reports that a TestCase ends its testing session */
    public boolean reportSectionEnd  (final TestCase pTestCase, final String pSection);
    
    /** Reports that a new subsection starts its testing session */
    public boolean reportSubSectionStart(final TestCase pTestCase, final String pSubSection);
    /** Reports that a TestCase ends its testing session */
    public boolean reportSubSectionEnd  (final TestCase pTestCase, final String pSubSection);
    
}
