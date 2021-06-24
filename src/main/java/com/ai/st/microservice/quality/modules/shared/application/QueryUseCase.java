package com.ai.st.microservice.quality.modules.shared.application;

public interface QueryUseCase<Q extends Query, R extends Response> {

    R handle(Q query);

}
