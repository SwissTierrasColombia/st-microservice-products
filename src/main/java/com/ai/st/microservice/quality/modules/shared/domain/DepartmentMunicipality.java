package com.ai.st.microservice.quality.modules.shared.domain;

public final class DepartmentMunicipality {

    private final DepartmentName department;
    private final MunicipalityName municipality;

    public DepartmentMunicipality(DepartmentName department, MunicipalityName municipality) {
        this.department = department;
        this.municipality = municipality;
    }

    public DepartmentName department() {
        return department;
    }

    public MunicipalityName municipality() {
        return municipality;
    }

}
