package org.tpspencer.tal.mvc.commons.util;

/**
 * This class provides basic cloning services between two
 * objects of roughly the same type.
 * 
 * @author Tom Spencer
 */
public interface ObjectCloner {

	public void clone(Object src, Object dest, String... exclusions);
}
