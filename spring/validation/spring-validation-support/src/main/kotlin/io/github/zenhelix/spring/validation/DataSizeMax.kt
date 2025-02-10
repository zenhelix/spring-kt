package io.github.zenhelix.spring.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.util.unit.DataSize
import org.springframework.util.unit.DataUnit
import kotlin.properties.Delegates
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FIELD,
    AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [DataSizeMaxValidator::class])
public annotation class DataSizeMax(
    val message: String = "{io.github.zenhelix.spring.validation.DataSizeMax.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
    /**
     * @return value the element must be lower or equal to
     */
    val value: Long,
    val dataUnit: DataUnit = DataUnit.BYTES
)

public class DataSizeMaxValidator : ConstraintValidator<DataSizeMax, DataSize> {

    private var maxValue by Delegates.notNull<Long>()
    private lateinit var dataUnit: DataUnit

    override fun initialize(annotaion: DataSizeMax) {
        this.maxValue = annotaion.value
        this.dataUnit = annotaion.dataUnit
    }

    override fun isValid(value: DataSize?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }

        val maxSize = DataSize.of(maxValue, dataUnit)

        return value <= maxSize
    }

}