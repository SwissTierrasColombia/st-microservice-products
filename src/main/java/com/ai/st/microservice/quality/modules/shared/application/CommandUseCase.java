package com.ai.st.microservice.quality.modules.shared.application;

public interface CommandUseCase<C extends Command> {

    void handle(C command);

}
