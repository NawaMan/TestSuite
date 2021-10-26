package net.nawaman.testsuite;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.*;
import java.util.regex.*;

import net.nawaman.testsuite.TestEntry.TestValue;

public class Util {

	static final Class<?>[] EmptyClassArray       = new Class<?>[0];
	static final String[]   DefaultTestSuiteNames = new String[] {
		"TestSuite",
		"AllTests"
	};
	
	// String-related functions ----------------------------------------------------------------------------------------
	
	/** Converts the CharSequence to a escape text. For example Tab => '\t', Newline => '\n' e.g. */
	static public CharSequence GetEscapeText(final CharSequence pCS) {
		
		final StringBuffer SB = new StringBuffer();
		for (int i = 0; i < pCS.length(); i++) {
			char c = ' ';
			try {
				c = pCS.charAt(i);
			} catch(StringIndexOutOfBoundsException E) {
				System.out.println("escapeText: " + E.toString());
			}
			
			switch (c) {
				case '\t': SB.append("\\t");  continue;
				case '\n': SB.append("\\n");  continue;
				case '\r': SB.append("\\r");  continue;
				case '\f': SB.append("\\f");  continue;
				case '\b': SB.append("\\b");  continue;
				case '\'': SB.append("\\\'"); continue;
				case '\"': SB.append("\\\""); continue;
				case '\\': SB.append("\\\\"); continue;
				default  : SB.append(c);
			}
		}
		return SB;
	}
	

	static public void AddTailChars(
			final StringBuilder pSB,
			final int           pLength,
			final char          pTailChar) {
		while (pSB.length() < pLength)
			pSB.append(pTailChar);
	}
	
	static public int GetDigitWidth(final int pNumber) {
		int aWidth = 2;
		if(pNumber >= 100) {
			aWidth++;
			if(pNumber >= 1000) {
				aWidth++;
				if(pNumber >= 10000)
					aWidth++;
			}
		}
		return aWidth;
	}
	
	
	/** Returns the string representation of the object */
	static public String GetToString(final Object pObj) {
		final String aString = Util.Get_toString_Object(pObj, null);
		return aString;
	}
	
	/** Returns the string representation of the object */
	static private String Get_toString_Object(
			final Object          pObj,
			final HashSet<Object> pObjs) {
		
		if (pObj == null)
			return "null";
		
		final boolean isObjContained = ((pObjs != null) && pObjs.contains(pObj));
		if (isObjContained) {
			final String aString = "" + pObj.toString();
			return aString;
		}
		
		final HashSet<Object> aObjs = (pObjs != null)
			? pObjs
			: new HashSet<Object>();

		aObjs.add(pObj);
		

		if (pObj instanceof TestValue) {
			final String aString = Get_toString_TestValue((TestValue)pObj);
			return aString;
		}
		
		if (pObj instanceof Collection) {
			final Collection<?> aCollection = (Collection<?>)pObj;
			final String        aString     = Get_toString_Collection(aCollection, aObjs);
			return aString;
		}
		
		if (pObj instanceof Map) {
			final Map<?,?> aMap    = (Map<?, ?>)pObj;
			final String   aString = Get_toString_Map(aMap, aObjs);
			return aString;
		}
		
		final boolean aIsArray = Check_ifArray(pObj);
		if (aIsArray) {
			final Object aArray  = pObj;
			final String aString = Get_toString_Array(aArray, aObjs);
			return aString;
		}
		
		final String aString = pObj.toString();
		return aString;
	}
	static private String Get_toString_TestValue(final TestValue pTestValue) {
		final String aString = Util.GetToString(pTestValue.getValue());
		return aString;
	}
	static private String Get_toString_Collection(
			final Collection<?>   pCollection,
			final HashSet<Object> pObjs) {
		
		final StringBuilder SB = new StringBuilder();
		SB.append("[");
		int i = 0;
		for(final Object C : pCollection) {
			if (i != 0)
				SB.append(",");
			
			final String aStr = Util.Get_toString_Object(C, pObjs);
			SB.append(aStr);
		}
		SB.append("]");
		
		final String aString = SB.toString();
		return aString;
	}
	static private String Get_toString_Map(
			final Map<?,?>        pMap,
			final HashSet<Object> pObjs) {
		
		final StringBuilder SB = new StringBuilder();
		SB.append("[");
		
		int i = 0;
		final Set<?> aKeys = pMap.keySet();
		for (final Object aKey : aKeys) {
			if (i != 0)
				SB.append(",");
			
			final Object aValue    = pMap.get(aKey);
			final String aKeyStr   = Util.Get_toString_Object(aKey,   pObjs);
			final String aValueStr = Util.Get_toString_Object(aValue, pObjs);
			SB.append(aKeyStr).append(" => ").append(aValueStr);
		}
		SB.append("]");
		
		final String aString = SB.toString();
		return aString;
	}
	static private String Get_toString_Array(
			final Object          pArray,
			final HashSet<Object> pObjs) {
		final StringBuilder SB = new StringBuilder();
		SB.append("[");
		
		final int aLength = Array.getLength(SB);
		for (int i = 0; i < aLength; i++) {
			if (i != 0)
				SB.append(",");
			
			final Object aValue = Array.get(pArray, i);
			final String aStr   = Util.Get_toString_Object(aValue, pObjs);
			SB.append(aStr);
		}
		SB.append("]");
		
		final String aString = SB.toString();
		return aString;
	}

	// Class-related functions -----------------------------------------------------------------------------------------

	static public Class<?> GetCallerClass() {
		final Class<?> aCallerClass = DoGetCallerClass();
		return aCallerClass;
	}
	static private Class<?> DoGetCallerClass() {
		try { Math.abs(5/0); } catch (final Exception E) {
			try {
				final StackTraceElement[] aStackTraces     = E.getStackTrace();
				final StackTraceElement   aStackTrace      = aStackTraces[3];
				final String              aCallerClassName = aStackTrace.getClassName();
				final Class<?>            aCallerClass     = Class.forName(aCallerClassName);
				return aCallerClass;
			} catch(final Exception CCE) {
				if (CCE instanceof RuntimeException)
					throw (RuntimeException)CCE;
				
				throw new RuntimeException(CCE);
			}
		}
		
		throw new RuntimeException("Fail to obtain the caller class.");
	}
	
	static public <T> Class<? extends T> GetCallerClass(final Class<T> aSuperClass) {		
		if (aSuperClass == null)
			return null;
		
		final Class<? extends T> aCallerClass = DoGetCallerClass(aSuperClass);
		return aCallerClass;
	}
	static private <T> Class<? extends T> DoGetCallerClass(final Class<T> aSuperClass) {
		if (aSuperClass != null) {
			try { Math.abs(5/0); } catch (final Exception E) {

				try {
					final StackTraceElement[] aStackTraces = E.getStackTrace();
				
					final int StartIndex = 3;
					for (int i = StartIndex; i < aStackTraces.length; i++) {
						final StackTraceElement   aStackTrace      = aStackTraces[i];
						final String              aCallerClassName = aStackTrace.getClassName();
						final Class<?>            aCallerClass     = Class.forName(aCallerClassName);
						if (aSuperClass.isAssignableFrom(aCallerClass)) {
							final Class<? extends T> aCallerClassAsSuper = aCallerClass.asSubclass(aSuperClass);
							return aCallerClassAsSuper;
						}
					}
				} catch(final Exception CCE) {
					if (CCE instanceof RuntimeException)
						throw (RuntimeException)CCE;
						
					throw new RuntimeException(CCE);
				}
			}
		}
		
		throw new RuntimeException(String.format("Fail to obtain the caller class as a '%s' class.", aSuperClass));
	}
	static Class<? extends TestCase> GetTestCase_CallerClass() {
		final Class<? extends TestCase> aTestCaseCaller = DoGetCallerClass(TestCase.class);
		return aTestCaseCaller;
	}
	static Class<? extends TestSuite> GetTestSuite_CallerClass() {
		final Class<? extends TestSuite> aTestSuiteCaller = DoGetCallerClass(TestSuite.class);
		return aTestSuiteCaller;
	}
	
	
	static private boolean Check_ifArray(final Object pObj) {
		final boolean aIsArray = pObj.getClass().isArray();
		return aIsArray;
	}


	@SuppressWarnings("unchecked")
	static private TestSuite DoLookForDefaultTestSuite(
			final Class<? extends TestCase> pTestCase,
			final String                    pTestSuiteClassName) {
		try {
			final String                     aClassName = GetDefaultTestSuiteClassName(pTestCase, pTestSuiteClassName);
			final Class<? extends TestSuite> aClass     = (Class<? extends TestSuite>)Class.forName(aClassName);
			final TestSuite                  aTestSuite = aClass.getConstructor().newInstance();
			return aTestSuite;
		}
		catch (final ClassNotFoundException CCE) {}
		catch (final IllegalAccessException IAE) {}
		catch (final InstantiationException  IE) {}
		catch (IllegalArgumentException  e) {}
		catch (InvocationTargetException e) {}
		catch (NoSuchMethodException     e) {}
		catch (SecurityException         e) {}
		return null;
	}
	static private String GetDefaultTestSuiteClassName(
			final Class<? extends TestCase> pTestCase,
			final String                    pTestSuiteClassName) {
		final String aClassName = pTestCase.getCanonicalName();
		final int    aIndex     = aClassName.lastIndexOf('.');
		
		if (aIndex == -1) 
			return null;
		
		final String aPackageName        = aClassName.substring(0, aIndex);
		final String aTestSuiteClassName = aPackageName + '.' + pTestSuiteClassName;
		return aTestSuiteClassName;
	}
	
	static private TestSuite PrepareTestSuite(final Class<? extends TestCase> aTestCaseClass) {
		TestSuite aTestSuite = null;
		for (int i = 0; i < DefaultTestSuiteNames.length; i++) {
			aTestSuite = DoLookForDefaultTestSuite(aTestCaseClass, DefaultTestSuiteNames[i]);
			if (aTestSuite != null)
				break;
		}
		
		if (aTestSuite == null)
			aTestSuite = new TestSuite();
		
		return aTestSuite;
	}
	static private TestCase InstantiateTestCase(final Class<? extends TestCase> aTestCaseClass) {
		Exception aException = null;
		try {
			final TestCase aTestCase = aTestCaseClass.getConstructor().newInstance();
			return aTestCase;
		}
		catch (final IllegalAccessException IAE) { aException = IAE; }
		catch (final InstantiationException IE)  { aException = IE;  }
        catch (IllegalArgumentException  e) { aException = e; }
        catch (InvocationTargetException e) { aException = e; }
        catch (NoSuchMethodException     e) { aException = e; }
        catch (SecurityException         e) { aException = e; }
	
		final String TestCaseInstantiateErrorMessage = "TestSuite: Fail to create an instance of a TestCase.";
		throw new RuntimeException(TestCaseInstantiateErrorMessage, aException);
	}	
	static public TestCase PrepareTestCase(final Class<? extends TestCase> aTestCaseClass) {
		final TestSuite aTestSuite = Util.PrepareTestSuite(aTestCaseClass);
		final TestCase  aTestCase  = Util.InstantiateTestCase(aTestCaseClass);
		aTestCase.setTestSuite(aTestSuite);
		return aTestCase;
	}

	// TestCase Discovery ----------------------------------------------------------------------------------------------
	
	static private Class<?> GetNonUtilNorTestSuiteCaller() {
		try { Math.abs(5/0); } catch (final Exception E) {
			try {
				final StackTraceElement[] STEs = E.getStackTrace();

				// Find the first class up the stack that is not this Util or TestSuite
				for (int i = 1; i < STEs.length; i++) {
					final Class<?> aCaller = Class.forName(STEs[i].getClassName());
					
					final boolean IsUtilOrTestSuite = (aCaller == Util.class) || (aCaller == TestSuite.class);
					if (IsUtilOrTestSuite)
						continue;
					
					return aCaller;
				}
				
			} catch (Exception CCE) {}
		}
		
		return null;
	}
	
	static private String GetClassName_afterPackageAsPath(
			final String pFileName,
			final String pPkgAsPath) {
		final String  CName                    = pFileName.substring(0, pFileName.length() - 6) .replace('/', '.');
		final String  CName_AfterPackageAsPath = CName.substring(pPkgAsPath.length());
		return CName_AfterPackageAsPath;
	}
	static private String GetClassName_IfImmediateClass_ofPackageAsPath(
			final String pFileName,
			final String pPkgAsPath) {
		
		final boolean aIsClass = pFileName.endsWith(".class");
		if (!aIsClass)
			return null;

		final boolean aIsBelongToPackage = pFileName.startsWith(pPkgAsPath);
		if (!aIsBelongToPackage)
			return null;

		final String  aClassName = Util.GetClassName_afterPackageAsPath(pFileName, pPkgAsPath);
		final boolean aIsImmedientMemberOfPaclage = aClassName.contains(".");
		
		if (aIsImmedientMemberOfPaclage)
			 return aClassName;
		else return null;
	}

	static private File GetPackageFile(
			final String pPkgName,
			final File   pPkgPath) {
		
		final String aPkgFileName = pPkgPath.getAbsoluteFile().toString() + '/' + pPkgName.replace('.', '/');
		final File   aPkgFile     = new File(aPkgFileName);
		
		final boolean aIsExist             = aPkgFile.exists();
		final boolean aIsDirectory         = aPkgFile.isDirectory();
		final boolean aIsExist_asDirectory = aIsExist && aIsDirectory;
		if(!aIsExist_asDirectory)
			return null;
		
		return aPkgFile;
	}
	static private boolean Check_isJarFile(final File pFile) {
		final boolean aIsJarFile = pFile.toString().endsWith(".jar");
		return aIsJarFile;
	}
	
	static private ArrayList<String> DiscoverClassNames_fromJarFile(final PkgInfo pPkgInfo) {
		
		final ArrayList<String> aClassNames = new ArrayList<String>();
		try {
			final JarFile               JF  = new JarFile(pPkgInfo.PkgPath);
			final Enumeration<JarEntry> JEs = JF.entries();
			
			while (JEs.hasMoreElements()) {
				final JarEntry aJE     = JEs.nextElement();
				final String   aJEName = aJE.getName();
				
				final String aClassName = GetClassName_IfImmediateClass_ofPackageAsPath(aJEName, pPkgInfo.PkgAsPath);
				if (aClassName == null)
					continue;
				
				aClassNames.add(aClassName);
			}
			
			JF.close();
		} catch (IOException e) {}
		
		return aClassNames;
	}
	static private ArrayList<String> DiscoverClassNames_fromDirectory(PkgInfo pPkgInfo) {

		final ArrayList<String> aClassNames = new ArrayList<String>();

		final File aPkgFile = Util.GetPackageFile(pPkgInfo.PkgName, pPkgInfo.PkgPath);
		if (aPkgFile == null)
			return aClassNames;
		
		final String[] aFileNames = aPkgFile.list();
		for(String aFileName : aFileNames) {
			final boolean aIsClassFile = aFileName.endsWith(".class");
			if (!aIsClassFile)
				continue;

			final String aSimpleName = aFileName.substring(0, aFileName.length() - 6);
			final String aClassName  = pPkgInfo.PkgName + '.' + aSimpleName;
			aClassNames.add(aClassName);
		}
		
		return aClassNames;
	}
	
	static public class PkgInfo {
		PkgInfo(
				final File   pPkgPath,
				final String pPkgName,
				final String pPkgAsPath) {
			
			this.PkgPath   = pPkgPath;
			this.PkgName   = pPkgName;
			this.PkgAsPath = pPkgAsPath;
		}
		final File   PkgPath;
		final String PkgName;
		final String PkgAsPath;
	}
	static public PkgInfo GetPackageInfoOf(Class<?> pClass) {
		File   aPkgPath   = null;
		String aPkgName   = null;
		String aPkgAsPath = null;

		try {
			aPkgPath   = new File(pClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			aPkgName   = pClass.getPackage().getName();
			aPkgAsPath = aPkgName.replace('.', '/') + '/';
		}
		catch (Throwable e) {}

		if (aPkgPath == null)
			return null;
		
		final PkgInfo aPkgInfo = new PkgInfo(aPkgPath, aPkgName, aPkgAsPath);
		return aPkgInfo;
	}
	static public ArrayList<String> DiscoverClassNames_inPackage(final PkgInfo pPkgInfo) {

		if (pPkgInfo == null)
			return null;

		ArrayList<String> aClassNames = new ArrayList<String>();
		
		if (pPkgInfo.PkgPath.isDirectory()) {
			
			aClassNames = Util.DiscoverClassNames_fromDirectory(pPkgInfo);
			
		} else if(pPkgInfo.PkgPath.isFile()) {
			boolean aIsJarFile = Util.Check_isJarFile(pPkgInfo.PkgPath);
			if (!aIsJarFile)
				return null;
			
			aClassNames = Util.DiscoverClassNames_fromJarFile(pPkgInfo);
		}
		
		return aClassNames;
	} 
	
	/**
	 * Returns an array of class in the same package of the caller that is not this class not TestSuite.
	 * 
	 * @param pFilterName     - Regular expression to match the desired classes' name (nullif no filtering needed)
	 * @param pFilterClass   - The super class of the desired classes (null if no filtering needed)
	 * 
	 * @return                - The array of matched classes, null if there is a problem.
	 * 
	 * @author The rest       - Nawaman  http://nawaman.net
	 * @author Package as Dir - Jon Peck http://jonpeck.com
	 *                              (adapted from http://www.javaworld.com/javaworld/javatips/jw-javatip113.html)
	 */
	@SuppressWarnings("unchecked")
	static <T> Class<? extends T>[] DiscoverClasses(
			final String   pFilterName,
			final Class<T> pFilterClass) {

		final Pattern aClsNamePattern = (pFilterName == null) ? null : Pattern.compile(pFilterName);
		
		PkgInfo aPkgInfo = null;

		try {
			final Class<?> aCaller = Util.GetNonUtilNorTestSuiteCaller();
			aPkgInfo = GetPackageInfoOf(aCaller);
		}
		catch (Throwable e) {}

		if (aPkgInfo == null)
			return null;

		final ArrayList<String> aClassNames = DiscoverClassNames_inPackage(aPkgInfo);
		
		if (aClassNames == null)
			return null;
		
		if (aClassNames.size() == 0)
			return null;
		
		final ArrayList<Class<?>> aClasses = new ArrayList<Class<?>>();
		for(final String aClassName : aClassNames) {
			
			if ((aClsNamePattern != null) && !aClsNamePattern.matcher(aClassName).matches())
				continue;

			// Get the class and filter it
			Class<?> aClass = null;
			try {
				aClass = Class.forName(aClassName);
			} catch (ClassNotFoundException e) {
				continue;
			}
			
			if((pFilterClass != null) && !pFilterClass.isAssignableFrom(aClass))
				continue;

			// Only for this
			if(Modifier.isAbstract(aClass.getModifiers())) 
				continue;
			
			// Class is belong to an object
			if(aClassName.contains("$") && !aClass.isMemberClass())
				continue;

			aClasses.add(aClass.asSubclass(pFilterClass));
		}

		Collections.sort(aClasses, ClassComparator.Default);
		Class<? extends T>[] aClassesArray = aClasses.toArray((Class<? extends T>[]) (new Class[aClasses.size()]));

		return aClassesArray;
	}
}