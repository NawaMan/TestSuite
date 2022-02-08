package net.nawaman.testsuite;

import java.io.Serializable;
import java.util.Comparator;

public class StringComparator
		implements
			Comparator<String>,
			Serializable
		{
	
	private static final long serialVersionUID = 5603928894805771653L;
	
	static public final StringComparator Default = new StringComparator();

	/**
	 * Compares two strings.
	 **/
	public int compare(
			final String pString1,
			final String pString2) {
		
		if (pString1 == pString2)
			return 0;
		
		if (pString1 == null)
			return Integer.MIN_VALUE;
		
		if (pString2 == null)
			return Integer.MAX_VALUE;
		
		if (pString1.equals(pString2))
			return 0;
		
		final int aS1_Length = pString1.length();
		final int aS2_Length = pString2.length();
		final int aMinLength = Math.min(aS1_Length, aS2_Length);
		for(int i = 0; i < aMinLength; i++) {
			final char aC1 = pString1.charAt(i);
			final char aC2 = pString2.charAt(i);
			
			if (aC1 != aC2)
				return aC1 - aC2;
		}
		
		final int aLenghtDiffer = aS1_Length - aS2_Length;
		return aLenghtDiffer;
	}

}
