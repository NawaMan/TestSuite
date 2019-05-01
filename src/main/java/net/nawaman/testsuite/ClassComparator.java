package net.nawaman.testsuite;

import java.io.*;
import java.util.*;

/**
 * A comparator for Java Classes.
 **/
public class ClassComparator
		implements
			Comparator<Class<?>>,
			Serializable
		{
	
	private static final long serialVersionUID = -7120534646583185358L;
	
	static public final ClassComparator Default = new ClassComparator();
		
	/**
	 * Compares two classes.
	 **/
	public int compare(
			final Class<?> pClass1,
			final Class<?> pClass2) {
		
		if (pClass1 == pClass2)
			return 0;
		
		if (pClass1 == null)
			return Integer.MIN_VALUE;
		
		if (pClass2 == null)
			return Integer.MAX_VALUE;
		
		final String aC1_Name      = pClass1.getCanonicalName();
		final String aC2_Name      = pClass2.getCanonicalName();
		final int    aNameCompared = StringComparator.Default.compare(aC1_Name, aC2_Name);
		return aNameCompared;
	}

}