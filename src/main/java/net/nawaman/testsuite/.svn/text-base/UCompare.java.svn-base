package net.nawaman.testsuite;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;

/**
 * Public services for comparing objects.
 * 
 * The static methods are implement in the general way that is likely to cover most comparion needed.
 **/
final public class UCompare {
	
	// Exact comparison ------------------------------------------------------------------------------------------------
	// This part is simple to the level that there is no need to make it as readable as expected.
	// These primitive comparing method are done to avoid autoboxing problem of caching when use the more generic one.
	
	static public boolean CheckExacts(final byte   aV1, final byte   aV2) { return (aV1 == aV2); }
	static public boolean CheckExacts(final short  aV1, final short  aV2) { return (aV1 == aV2); }
	static public boolean CheckExacts(final int    aV1, final int    aV2) { return (aV1 == aV2); }
	static public boolean CheckExacts(final long   aV1, final long   aV2) { return (aV1 == aV2); }
	static public boolean CheckExacts(final float  aV1, final float  aV2) { return (aV1 == aV2); }
	static public boolean CheckExacts(final double aV1, final double aV2) { return (aV1 == aV2); }
	static public boolean CheckExacts(final char   aV1, final char   aV2) { return (aV1 == aV2); }
	
	static public boolean CheckExacts(
			final Object aValue1,
			final Object aValue2) {
		
		final boolean aIsExact = (aValue1 == aValue2);
		return aIsExact;
	}
	
	// Equal comparison ------------------------------------------------------------------------------------------------
	
	static public boolean CheckEquals(
			final Object aValue1,
			final Object aValue2) {
		
		final boolean aIsExact = CheckExacts(aValue1, aValue2);
		if (aIsExact)
			return true;
		
		if (aValue1 == null)
			return false;
		
		if (aValue2 == null)
			return false;
		
		final boolean aIsEqual = aValue1.equals(aValue2);
		return aIsEqual;
	}
	
	// Compatible comparison -------------------------------------------------------------------------------------------
	
	static public boolean IsNumberZero(final Number aValue) {
		if (aValue == null)
			return true;
		
		if (aValue instanceof Integer) {
			final boolean aIsZero = (aValue.intValue() == 0);
			return aIsZero;
		}
		
		if (aValue instanceof Double) {
			final boolean aIsZero = (aValue.doubleValue() == 0.0);
			return aIsZero;
		}
		
		if (aValue instanceof Long) {
			final boolean aIsZero = (aValue.longValue() == 0L);
			return aIsZero;
		}
		
		if (aValue instanceof Byte) {
			final boolean aIsZero = (aValue.byteValue() == ((byte)0));
			return aIsZero;
		}

		if (aValue instanceof BigInteger) {
			final boolean aIsZero = BigInteger.ZERO.equals(aValue);
			return aIsZero;
		}

		if (aValue instanceof BigDecimal) {
			final boolean aIsZero = BigDecimal.ZERO.equals(aValue);
			return aIsZero;
		}
		
		if (aValue instanceof Float) {
			final boolean aIsZero = (aValue.floatValue() == ((float)0));
			return aIsZero;
		}
		
		if (aValue instanceof Short) {
			final boolean aIsZero = (aValue.shortValue() == ((short)0));
			return aIsZero;
		}
		
		// None of the above, should find the better way
		if (aValue.doubleValue() == 0.0)
			return true;
		
		return false;
	}

	
	static Boolean PreCondition(
			final Object aValue1,
			final Object aValue2) {
		
		if (aValue1 == aValue2)
			return Boolean.TRUE;
		
		if (aValue1 == null) {
			final boolean aIsNull = DoCheckCompatibleToNull(aValue2);
			return Boolean.valueOf(aIsNull);
		}
		if (aValue2 == null) {
			final boolean aIsNull = DoCheckCompatibleToNull(aValue1);
			return Boolean.valueOf(aIsNull);
		}
		
		if (aValue1.equals(aValue2))
			return Boolean.TRUE;
		
		return null;
	}
	
	
	static public boolean CheckCompatibleToNull(final Object aValue) {
		if (aValue == null)
			return true;
		
		final boolean IsNull = DoCheckCompatibleToNull(aValue);
		return IsNull;
	}
	static boolean DoCheckCompatibleToNull(final Object aValue) {
		if (aValue instanceof Number) {
			final boolean aIsZero = IsNumberZero((Number)aValue);
			return aIsZero;
		}
		
		if (aValue instanceof Boolean) {
			final boolean aIsFalse = Boolean.FALSE.equals(aValue);
			return aIsFalse;
		}
		
		if (aValue instanceof Character) {
			final boolean aIsZero = ((Character)aValue).charValue() == '\0';
			return aIsZero;
		}
		
		if (aValue instanceof Collection<?>) {
			final boolean aIsEmpty = ((Collection<?>)aValue).isEmpty();
			return aIsEmpty;
		}
		
		if (aValue instanceof Iterable<?>) {
			final boolean aIsEmpty = !((Iterable<?>)aValue).iterator().hasNext();
			return aIsEmpty;
		}
		
		if (aValue instanceof Map<?,?>) {
			final boolean aIsEmpty = ((Map<?,?>)aValue).isEmpty();
			return aIsEmpty;
		}
		
		if (aValue instanceof Iterator<?>) {
			final boolean aIsEmpty = !((Iterator<?>)aValue).hasNext();
			return aIsEmpty;
		}
		
		if (aValue.getClass().isArray()) {
			final boolean aIsEmpty = (Array.getLength(aValue) == 0);
			return aIsEmpty;
		}
		
		// In some case, object simulate null
		try {
			final boolean aIsNull = aValue.equals(null);
			return aIsNull;
		} catch (NullPointerException NPE) {}
		
		return false;
	}

	
	static public boolean CheckArrayCompatible(
			final Object aArray1,
			final Object aArray2) {
		
		Boolean aIsPreCondition = PreCondition(aArray1, aArray2);
		if (aIsPreCondition != null) {
			if (aIsPreCondition.booleanValue()) {
				if (!aArray1.getClass().isArray())
					return false;
				
				if (!aArray2.getClass().isArray())
					return false;		
			}
			return false;
		}

		final int a1_Length = Array.getLength(aArray1);
		final int a2_Length = Array.getLength(aArray2);
		if (a1_Length != a2_Length)
			return false;
		
		final boolean aIsEqual = aArray1.equals(aArray2);
		if (aIsEqual) 
			return true;
		
		final boolean IsCompatible = DoCheckArrayCompatible(aArray1, aArray2);
		return IsCompatible;
	}
	static boolean DoCheckArrayCompatible(
			final Object aArray1,
			final Object aArray2) {

		final int a1_Length = Array.getLength(aArray1);
		final int a2_Length = Array.getLength(aArray2);
		if (a1_Length != a2_Length)
			return false;
		
		for (int i = a1_Length; --i >= 0;) {
			Object aElement1 = Array.get(aArray1, i);
			Object aElement2 = Array.get(aArray2, i);
			final boolean aIsElementCompatible = CheckCompatible(aElement1, aElement2);
			if (!aIsElementCompatible)
				return false;
		}
		return true;
	}


	static public boolean CheckCollectionCompatible(
			final Collection<?> aCollection1,
			final Collection<?> aCollection2) {

		Boolean aIsPreCondition = PreCondition(aCollection1, aCollection2);
		if (aIsPreCondition != null)
			return aIsPreCondition.booleanValue();
		

		final int a1_Size = aCollection1.size();
		final int a2_Size = aCollection2.size();
		if (a1_Size != a2_Size)
			return false;
		
		final boolean aIsEqual = aCollection1.equals(aCollection2);
		if (aIsEqual) 
			return true;
		
		final boolean IsCompatible = DoCheckCollectionCompatible(aCollection1, aCollection2);
		return IsCompatible;
	}
	static boolean DoCheckCollectionCompatible(
			final Collection<?> aCollection1,
			final Collection<?> aCollection2) {
		
		final int a1_Size = aCollection1.size();
		final int a2_Size = aCollection2.size();
		if (a1_Size != a2_Size)
			return false;
		
		final boolean IsCompatible = DoCheckIterableCompatible(aCollection1, aCollection2);
		return IsCompatible;
	}
	
	
	static public boolean CheckIteratorCompatible(
			final Iterator<?> aIterator1,
			final Iterator<?> aIterator2) {

		Boolean aIsPreCondition = PreCondition(aIterator1, aIterator2);
		if (aIsPreCondition != null)
			return aIsPreCondition.booleanValue();
		
		
		final boolean aIsEqual = aIterator1.equals(aIterator2);
		if (aIsEqual) 
			return true;
		
		final boolean IsCompatible = DoCheckIteratorCompatible(aIterator1, aIterator2);
		return IsCompatible;
	}
	static boolean DoCheckIteratorCompatible(
			final Iterator<?> aIterator1,
			final Iterator<?> aIterator2) {
		
		for (int i = 0; aIterator1.hasNext(); i++) {
			if (aIterator2.hasNext())
				return false;

			Object aElement1 = aIterator1.next();
			Object aElement2 = aIterator2.next();
			final boolean aIsElementCompatible = CheckCompatible(aElement1, aElement2);
			if (!aIsElementCompatible)
				return false;
		}
		
		if (aIterator2.hasNext())
			return false;
		
		return true;
	}
	
	
	static public boolean CheckIterableCompatible(
			final Iterable<?> aIterable1,
			final Iterable<?> aIterable2) {

		Boolean aIsPreCondition = PreCondition(aIterable1, aIterable2);
		if (aIsPreCondition != null)
			return aIsPreCondition.booleanValue();
		
		
		final boolean aIsEqual = aIterable1.equals(aIterable2);
		if (aIsEqual) 
			return true;
		
		final boolean IsCompatible = DoCheckIterableCompatible(aIterable1, aIterable2);
		return IsCompatible;
	}
	static boolean DoCheckIterableCompatible(
			final Iterable<?> aIterable1,
			final Iterable<?> aIterable2) {
		
		final boolean IsCompatible = CheckIteratorCompatible(aIterable1.iterator(), aIterable2.iterator());
		return IsCompatible;
	}
	
	
	static public boolean CheckMapCompatible(
			final Map<?,?> aMap1,
			final Map<?,?> aMap2) {

		Boolean aIsPreCondition = PreCondition(aMap1, aMap2);
		if (aIsPreCondition != null)
			return aIsPreCondition.booleanValue();
		

		final int a1_Size = aMap1.size();
		final int a2_Size = aMap2.size();
		if (a1_Size != a2_Size)
			return false;
		
		final boolean aIsEqual = aMap1.equals(aMap2);
		if (aIsEqual) 
			return true;
		
		final boolean IsCompatible = DoCheckMapCompatible(aMap1, aMap2);
		return IsCompatible;
	}
	static boolean DoCheckMapCompatible(
			final Map<?,?> aMap1,
			final Map<?,?> aMap2) {

		final int a1_Size = aMap1.size();
		final int a2_Size = aMap2.size();
		if (a1_Size != a2_Size)
			return false;
		
		final Set<?> aKeys = aMap1.keySet();
		for (final Object aKey : aKeys) {
			final boolean aIsContain = aMap2.containsKey(aKey);
			if (!aIsContain)
				return false;
			
			final Object  aValue1            = aMap1.get(aKey);
			final Object  aValue2            = aMap2.get(aKey);
			final boolean aIsValueCompatible = CheckCompatible(aValue1, aValue2);
			if (!aIsValueCompatible)
				return false;
		}
		
		return false;
	}
	
	
	static public boolean CheckCompatible(
			final Object aValue1,
			final Object aValue2) {

		Boolean aIsPreCondition = PreCondition(aValue1, aValue2);
		if (aIsPreCondition != null)
			return aIsPreCondition.booleanValue();
		
		
		final boolean aIsEqual = aValue1.equals(aValue2);
		if (aIsEqual)
			return true;

		
		final boolean aIs1_Array = aValue1.getClass().isArray();
		final boolean aIs2_Array = aValue2.getClass().isArray();
		if (aIs1_Array && aIs2_Array) {
			final boolean aIsCompatible = DoCheckArrayCompatible(aValue1, aValue2);
			return aIsCompatible;
		}
		

		final boolean aIs1_Iterable = (aValue1 instanceof Iterable<?>);
		final boolean aIs2_Iterable = (aValue2 instanceof Iterable<?>);
		if (aIs1_Iterable && aIs2_Iterable) {
			final boolean aIsCompatible = DoCheckIterableCompatible((Iterable<?>)aValue1, (Iterable<?>)aValue2);
			return aIsCompatible;
		}
		

		final boolean aIs1_Iterator = (aValue1 instanceof Iterator<?>);
		final boolean aIs2_Iterator = (aValue2 instanceof Iterator<?>);
		if (aIs1_Iterator && aIs2_Iterator) {
			final boolean aIsCompatible = DoCheckIteratorCompatible((Iterator<?>)aValue1, (Iterator<?>)aValue2);
			return aIsCompatible;
		}
		

		final boolean aIs1_Map = (aValue1 instanceof Map<?,?>);
		final boolean aIs2_Map = (aValue2 instanceof Map<?,?>);
		if (aIs1_Map && aIs2_Map) {
			final boolean aIsCompatible = DoCheckMapCompatible((Map<?,?>)aValue1, (Map<?,?>)aValue2);
			return aIsCompatible;
		}
		
		return false;
	}

}
