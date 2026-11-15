package com.manarbedeir.qa.api.data;

public record InvalidBookIdCase(String id, int expectedStatus, String expectedTitle) {
}
