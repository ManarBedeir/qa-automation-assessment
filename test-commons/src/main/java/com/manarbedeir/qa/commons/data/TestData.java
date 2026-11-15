package com.manarbedeir.qa.commons.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class TestData {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestData() {
    }

    public static <T> List<T> readList(String resource, String section, Class<T> type) {
        try (InputStream in = TestData.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Test data file not found: " + resource);
            }
            JsonNode node = MAPPER.readTree(in).get(section);
            if (node == null || !node.isArray()) {
                throw new IllegalStateException("No array '" + section + "' in " + resource);
            }
            CollectionType listType = MAPPER.getTypeFactory().constructCollectionType(List.class, type);
            return MAPPER.readerFor(listType).readValue(node);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read " + resource, e);
        }
    }

    public static Object[][] asRows(List<?> items) {
        Object[][] rows = new Object[items.size()][];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new Object[]{items.get(i)};
        }
        return rows;
    }
}
