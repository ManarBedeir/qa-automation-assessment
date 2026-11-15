package com.manarbedeir.qa.api.services;

import com.manarbedeir.qa.commons.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class BaseService {

    private final RequestSpecification specification;

    protected BaseService() {
        int timeout = (int) Config.getMillis("api.timeout.millis").toMillis();
        RestAssuredConfig config = RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL))
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeout)
                        .setParam("http.socket.timeout", timeout));
        specification = new RequestSpecBuilder()
                .setBaseUri(Config.get("api.base.url"))
                .setBasePath(Config.get("api.base.path"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(config)
                .build();
    }

    protected RequestSpecification request() {
        return RestAssured.given().spec(specification);
    }
}
