package io.github.zenhelix.validation.constraints

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.annotation.AnnotationTarget.TYPE_PARAMETER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.reflect.KClass

/**
 * The annotated element can be ```null```, but must contain at least one if not ```null```
 */
@MustBeDocumented
@Constraint(validatedBy = [NullableNotBlankValidator::class])
@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, FIELD, ANNOTATION_CLASS, CONSTRUCTOR, VALUE_PARAMETER, CLASS, TYPE, TYPE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@JvmRepeatable(NullableNotBlank.List::class)
public annotation class NullableNotBlank(
    val message: String = "{io.github.zenhelix.validation.constraints.NullableNotBlank.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = []
) {
    @Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, FIELD, ANNOTATION_CLASS, CONSTRUCTOR, VALUE_PARAMETER, CLASS, TYPE, TYPE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    public annotation class List(vararg val value: NullableNotBlank)
}


public class NullableNotBlankValidator : ConstraintValidator<NullableNotBlank, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }

        return value.isNotBlank()
    }

}