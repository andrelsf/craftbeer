package com.beerhouse.core.validation.enums.constraints;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.beerhouse.core.validation.enums.ValueOfEnumValidator;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = ValueOfEnumValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
public @interface ValueOfEnum {

	/**
	 * @return class containing enum values to which this String should match
	 */
	Class<? extends Enum<?>> enumClass();
	
	/**
	 * @return the error message template
	 */
	String message() default "must be any of enum {enumClass}";
	
	/**
	 * @return the groups the constraint belongs to
	 */
	Class<?>[] groups() default {};
	
	/**
	 * @return the payload associated to the constraint
	 */
	Class<? extends Payload>[] payload() default {}; 
}
