package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class StoreFileError extends DomainError {

    public StoreFileError() {
        super("store_file_error", "Ha ocurrido un error guardando el archivo.");
    }

}
