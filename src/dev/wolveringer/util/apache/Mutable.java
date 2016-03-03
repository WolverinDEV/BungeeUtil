package dev.wolveringer.util.apache;


/**
 * Provides mutable access to a value.
 * <p>
 * <code>Mutable</code> is used as a generic interface to the implementations in this package.
 * <p>
 * A typical use case would be to enable a primitive or string to be passed to a method and allow that method to
 * effectively change the value of the primitive/string. Another use case is to store a frequently changing primitive in
 * a collection (for example a total in a map) without needing to create new Integer/Long wrapper objects.
 * 
 * @author Matthew Hawthorne
 * @since 2.1
 * @version $Id: Mutable.java 618693 2008-02-05 16:33:29Z sebb $
 */
public interface Mutable {

    /**
     * Gets the value of this mutable.
     * 
     * @return the stored value
     */
    Object getValue();

    /**
     * Sets the value of this mutable.
     * 
     * @param value
     *            the value to store
     * @throws NullPointerException
     *             if the object is null and null is invalid
     * @throws ClassCastException
     *             if the type is invalid
     */
    void setValue(Object value);

}