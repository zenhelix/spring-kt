package io.github.zenhelix.spring.testing.assertion.spring.processor

import org.assertj.core.api.Condition
import org.hamcrest.collection.IsMapContaining
import org.springframework.boot.configurationprocessor.metadata.ConfigurationMetadata
import org.springframework.boot.configurationprocessor.metadata.ItemDeprecation
import org.springframework.boot.configurationprocessor.metadata.ItemHint
import org.springframework.boot.configurationprocessor.metadata.ItemMetadata
import org.springframework.util.ObjectUtils
import java.util.Collections
import kotlin.collections.iterator

public object Metadata {

    public fun withGroup(name: String): MetadataItemCondition = MetadataItemCondition(ItemMetadata.ItemType.GROUP, name)

    public fun withGroup(name: String, type: Class<*>): MetadataItemCondition = MetadataItemCondition(ItemMetadata.ItemType.GROUP, name).ofType(type)

    public fun withGroup(name: String, type: String): MetadataItemCondition = MetadataItemCondition(ItemMetadata.ItemType.GROUP, name).ofType(type)

    public fun withProperty(name: String): MetadataItemCondition = MetadataItemCondition(ItemMetadata.ItemType.PROPERTY, name)

    public fun withProperty(name: String, type: Class<*>): MetadataItemCondition = MetadataItemCondition(ItemMetadata.ItemType.PROPERTY, name).ofType(type)

    public fun withProperty(name: String, type: String): MetadataItemCondition = MetadataItemCondition(ItemMetadata.ItemType.PROPERTY, name).ofType(type)

    public fun withEnabledFlag(key: String): MetadataItemCondition = withProperty(key).ofType(Boolean::class.java)

    public fun withHint(name: String): MetadataHintCondition = MetadataHintCondition(name)

    public class MetadataItemCondition(
        private val itemType: ItemMetadata.ItemType,
        private val name: String,
        private val type: String? = null,
        private val sourceType: Class<*>? = null,
        private val sourceMethod: String? = null,
        private val description: String? = null,
        private val defaultValue: Any? = null,
        private val deprecation: ItemDeprecation? = null
    ) : Condition<ConfigurationMetadata>() {

        init {
            describedAs(createDescription())
        }

        private fun createDescription(): String {
            val description = StringBuilder()
            description.append("an item named '").append(this.name).append("'")
            if (this.type != null) {
                description.append(" with dataType:").append(this.type)
            }
            if (this.sourceType != null) {
                description.append(" with sourceType:").append(this.sourceType)
            }
            if (this.sourceMethod != null) {
                description.append(" with sourceMethod:").append(this.sourceMethod)
            }
            if (this.defaultValue != null) {
                description.append(" with defaultValue:").append(this.defaultValue)
            }
            if (this.description != null) {
                description.append(" with description:").append(this.description)
            }
            if (this.deprecation != null) {
                description.append(" with deprecation:").append(this.deprecation)
            }
            return description.toString()
        }

        override fun matches(value: ConfigurationMetadata): Boolean {
            val itemMetadata = findItem(value, this.name) ?: return false
            if (this.type != null && this.type != itemMetadata.type) {
                return false
            }
            if (this.sourceType != null && sourceType.name != itemMetadata.sourceType) {
                return false
            }
            if (this.sourceMethod != null && this.sourceMethod != itemMetadata.sourceMethod) {
                return false
            }
            if (this.defaultValue != null
                && !ObjectUtils.nullSafeEquals(this.defaultValue, itemMetadata.defaultValue)
            ) {
                return false
            }
            if (this.defaultValue == null && itemMetadata.defaultValue != null) {
                return false
            }
            if (this.description != null && this.description != itemMetadata.description) {
                return false
            }
            if (this.deprecation == null && itemMetadata.deprecation != null) {
                return false
            }
            return this.deprecation == null || this.deprecation == itemMetadata.deprecation
        }

        public fun ofType(dataType: Class<*>): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, dataType.name, this.sourceType,
            this.sourceMethod, this.description, this.defaultValue, this.deprecation
        )

        public fun ofType(dataType: String): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, dataType, this.sourceType, this.sourceMethod,
            this.description, this.defaultValue, this.deprecation
        )

        public fun fromSource(sourceType: Class<*>): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, this.type, sourceType, this.sourceMethod,
            this.description, this.defaultValue, this.deprecation
        )

        public fun fromSourceMethod(sourceMethod: String): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, this.type, this.sourceType, sourceMethod,
            this.description, this.defaultValue, this.deprecation
        )

        public fun withDescription(description: String): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, this.type, this.sourceType, this.sourceMethod,
            description, this.defaultValue, this.deprecation
        )

        public fun withDefaultValue(defaultValue: Any?): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, this.type, this.sourceType, this.sourceMethod,
            this.description, defaultValue, this.deprecation
        )

        public fun withDeprecation(): MetadataItemCondition = withDeprecation(null, null, null, null)

        public fun withDeprecation(reason: String?, replacement: String?, since: String?): MetadataItemCondition =
            withDeprecation(reason, replacement, since, null)

        public fun withDeprecation(reason: String?, replacement: String?, since: String?, level: String?): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, this.type, this.sourceType, this.sourceMethod,
            this.description, this.defaultValue, ItemDeprecation(reason, replacement, since, level)
        )

        public fun withNoDeprecation(): MetadataItemCondition = MetadataItemCondition(
            this.itemType, this.name, this.type, this.sourceType, this.sourceMethod,
            this.description, this.defaultValue, null
        )

        private fun findItem(metadata: ConfigurationMetadata, name: String): ItemMetadata? {
            val candidates = metadata.items.filter { item -> item.isOfItemType(this.itemType) && name == item.name }
            check(candidates.size <= 1) { "More than one metadata item with name '$name': $candidates" }
            return if (candidates.size == 1) candidates[0] else null
        }
    }

    public class MetadataHintCondition : Condition<ConfigurationMetadata> {
        private val name: String

        private val valueConditions: List<ItemHintValueCondition>
        private val providerConditions: List<ItemHintProviderCondition>

        public constructor(name: String) {
            this.name = name
            this.valueConditions = emptyList()
            this.providerConditions = emptyList()
        }

        public constructor(
            name: String, valueConditions: List<ItemHintValueCondition>,
            providerConditions: List<ItemHintProviderCondition>
        ) {
            this.name = name
            this.valueConditions = valueConditions
            this.providerConditions = providerConditions
            describedAs(createDescription())
        }

        private fun createDescription(): String {
            val description = StringBuilder()
            description.append("a hints name '").append(this.name).append("'")
            if (valueConditions.isNotEmpty()) {
                description.append(" with values:").append(this.valueConditions)
            }
            if (providerConditions.isNotEmpty()) {
                description.append(" with providers:").append(this.providerConditions)
            }
            return description.toString()
        }

        override fun matches(metadata: ConfigurationMetadata): Boolean {
            val itemHint = getFirstHintWithName(metadata, this.name) ?: return false
            return matches(itemHint, this.valueConditions) && matches(itemHint, this.providerConditions)
        }

        private fun matches(itemHint: ItemHint, conditions: List<Condition<ItemHint>>): Boolean {
            for (condition in conditions) {
                if (!condition.matches(itemHint)) {
                    return false
                }
            }
            return true
        }

        private fun getFirstHintWithName(metadata: ConfigurationMetadata, name: String): ItemHint? {
            for (hint in metadata.hints) {
                if (name == hint.name) {
                    return hint
                }
            }
            return null
        }

        public fun withValue(index: Int, value: Any?, description: String?): MetadataHintCondition = MetadataHintCondition(
            this.name,
            add(this.valueConditions, ItemHintValueCondition(index, value, description)),
            this.providerConditions
        )

        public fun withProvider(provider: String): MetadataHintCondition = withProvider(providerConditions.size, provider, null)

        public fun withProvider(provider: String?, key: String, value: Any): MetadataHintCondition =
            withProvider(providerConditions.size, provider, Collections.singletonMap(key, value))

        public fun withProvider(index: Int, provider: String?, parameters: Map<String, Any>?): MetadataHintCondition = MetadataHintCondition(
            this.name, this.valueConditions,
            add(this.providerConditions, ItemHintProviderCondition(index, provider, parameters))
        )

        private fun <T> add(items: List<T>, item: T): List<T> = ArrayList(items).also { it.add(item) }

    }

    public class ItemHintValueCondition(private val index: Int, private val value: Any?, private val description: String?) : Condition<ItemHint>() {
        init {
            describedAs(createDescription())
        }

        private fun createDescription(): String {
            val description = StringBuilder()
            description.append("value hint at index '").append(this.index).append("'")
            if (this.value != null) {
                description.append(" with value:").append(this.value)
            }
            if (this.description != null) {
                description.append(" with description:").append(this.description)
            }
            return description.toString()
        }

        override fun matches(value: ItemHint): Boolean {
            if (this.index + 1 > value.values.size) {
                return false
            }
            val valueHint = value.values[index]
            if (this.value != null && this.value != valueHint.value) {
                return false
            }
            return this.description == null || this.description == valueHint.description
        }
    }

    public class ItemHintProviderCondition(private val index: Int, private val name: String?, private val parameters: Map<String, Any>?) :
        Condition<ItemHint>() {

        init {
            describedAs(createDescription())
        }

        public fun createDescription(): String {
            val description = StringBuilder().append("value provider")
            if (this.name != null) {
                description.append(" with name:").append(this.name)
            }
            if (this.parameters != null) {
                description.append(" with parameters:").append(this.parameters)
            }
            return description.toString()
        }

        override fun matches(hint: ItemHint): Boolean {
            if (this.index + 1 > hint.providers.size) {
                return false
            }
            val valueProvider = hint.providers[index]
            if (this.name != null && this.name != valueProvider.name) {
                return false
            }
            if (this.parameters != null) {
                for ((key, value) in this.parameters) {
                    if (!IsMapContaining.hasEntry(key, value).matches(valueProvider.parameters)) {
                        return false
                    }
                }
            }
            return true
        }
    }
}
