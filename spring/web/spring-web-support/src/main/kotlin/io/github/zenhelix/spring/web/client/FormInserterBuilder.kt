package io.github.zenhelix.spring.web.client

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

public class FormInserterBuilder {
    private val multiMap = LinkedMultiValueMap<String, String>()

    public fun add(name: String, value: String?): FormInserterBuilder = apply {
        if (value != null) {
            multiMap.add(name, value)
        }
    }

    public fun build(): MultiValueMap<String, String> = multiMap

}