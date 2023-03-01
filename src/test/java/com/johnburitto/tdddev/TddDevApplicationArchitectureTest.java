package com.johnburitto.tdddev;

import com.johnburitto.tdddev.service.interfaces.IService;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class TddDevApplicationArchitectureTest {
    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_PACKAGE_INFOS)
                .importPackages("com.johnburitto.tdddev");
    }

    @Test
    void shouldFollowLayersArchitecture() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                //
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                //
                .check(importedClasses);
    }

    @Test
    void servicesShouldNotDependOnControllerLevel() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should()
                .dependOnClassesThat().resideInAPackage("..controller..")
                .because("DENIED!!! Out of rules")
                .check(importedClasses);
    }

    @Test
    void repositoryShouldNotDependOnControllerLevel() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat().resideInAPackage("..controller..")
                .because("DENIED!!! Out of rules")
                .check(importedClasses);
    }

    @Test
    void repositoryShouldNotDependOnServiceLevel() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat().resideInAPackage("..service..")
                .because("DENIED!!! Out of rules")
                .check(importedClasses);
    }

    @Test
    void controllerShouldNotDependOnRepositoryLevel() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat().resideInAPackage("..repository..")
                .because("DENIED!!! Out of rules")
                .check(importedClasses);
    }

    @Test
    void controllerShouldDependOnServiceLevel() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat().resideInAPackage("..service..")
                .check(importedClasses);
    }

    @Test
    void serviceShouldDependOnRepositoryLevel() {
        classes()
                .that().resideInAPackage("..service")
                .should()
                .dependOnClassesThat().resideInAPackage("..repository..")
                .check(importedClasses);
    }

    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(importedClasses);
    }

    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
                .that().resideInAPackage("..service..")
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }

    @Test
    void repositoryClassesShouldBeNamedXRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .haveSimpleNameEndingWith("Repository")
                .check(importedClasses);
    }

    @Test
    void controllerClassesShouldBeAnnotatedByRestControllerAndRequestMapping() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .beAnnotatedWith(RequestMapping.class)
                .check(importedClasses);
    }

    @Test
    void shouldUseFieldsAutowired() {
        noFields()
                .should().beAnnotatedWith(Autowired.class)
                .check(importedClasses);
    }

    @Test
    void shouldModelClassesBeAnnotatedAsDocument() {
        classes()
                .that().resideInAPackage("..model..")
                .should()
                .beAnnotatedWith(Document.class)
                .check(importedClasses);
    }

    @Test
    void controllerShouldHaveOnlyFinalFields() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveOnlyFinalFields()
                .check(importedClasses);
    }

    @Test
    void serviceShouldHaveOnlyFinalFields() {
        classes()
                .that().resideInAPackage("..service")
                .should()
                .haveOnlyFinalFields()
                .check(importedClasses);
    }

    @Test
    void controllerShouldBeOnlyInControllerPackage() {
        classes()
                .that()
                .haveSimpleNameEndingWith("Controller")
                .should()
                .resideInAPackage("..controller..")
                .check(importedClasses);
    }

    @Test
    void serviceShouldBeOnlyInServicePackage() {
        classes()
                .that()
                .haveSimpleNameEndingWith("Service")
                .should()
                .resideInAPackage("..service..")
                .check(importedClasses);
    }

    @Test
    void repositoryShouldBeOnlyInRepositoryPackage() {
        classes()
                .that()
                .haveSimpleNameEndingWith("Repository")
                .should()
                .resideInAPackage("..repository..")
                .check(importedClasses);
    }

    @Test
    void servicesShouldImplementInterfaceIService() {
        classes()
                .that().resideInAPackage("..service")
                .should()
                .implement(IService.class)
                .check(importedClasses);
    }

    @Test
    void controllerMethodsShouldBeAnnotatedRequestMapping() {
        methods()
                .that()
                .arePublic()
                .and()
                .areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().beAnnotatedWith(RequestMapping.class)
                .orShould().beAnnotatedWith(GetMapping.class)
                .orShould().beAnnotatedWith(PostMapping.class)
                .orShould().beAnnotatedWith(PutMapping.class)
                .orShould().beAnnotatedWith(DeleteMapping.class)
                .check(importedClasses);

    }

/*  @Test
    void modelShouldHaveLombokAnnotations() {
        classes()
                .that().resideInAPackage("..model..")
                .should()
                .beAnnotatedWith(AllArgsConstructor.class)
                .andShould()
                .beAnnotatedWith(NoArgsConstructor.class)
                .andShould()
                .beAnnotatedWith(Data.class)
                .check(importedClasses);
    }*/
}