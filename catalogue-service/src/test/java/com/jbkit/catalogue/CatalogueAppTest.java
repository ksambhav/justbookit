package com.jbkit.catalogue;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class CatalogueAppTest {
    @Test
    void verifyApplicationModules() {
        ApplicationModules modules = ApplicationModules.of(CatalogueApp.class);
        modules.verify();
        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeModuleCanvases()
                .writeIndividualModulesAsPlantUml();
    }
}