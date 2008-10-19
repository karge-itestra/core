package org.jboss.webbeans.introspector;

import java.lang.reflect.Field;

/**
 * AnnotatedField provides a uniform access to the annotations on an annotated
 * field 
 * 
 * @author Pete Muir
 *
 */
public interface AnnotatedField<T> extends AnnotatedItem<T, Field>
{
   
   public Field getAnnotatedField();

}
