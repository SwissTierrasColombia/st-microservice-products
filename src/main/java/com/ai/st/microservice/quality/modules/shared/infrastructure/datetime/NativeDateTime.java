package com.ai.st.microservice.quality.modules.shared.infrastructure.datetime;

import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public final class NativeDateTime implements DateTime {

    @Override
    public Date now() {
        return new Date();
    }
}
