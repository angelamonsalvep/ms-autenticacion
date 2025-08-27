package com.angelapmonsalve.microservices.autenticacion.config;

import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperConfigTest {

    @Test
    void testObjectMapperBean() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(ObjectMapperConfig.class)) {

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

            assertNotNull(objectMapper, "El bean ObjectMapper no debe ser nulo");
            assertInstanceOf(ObjectMapperImp.class, objectMapper, "Debe ser una instancia de ObjectMapperImp");
        }
    }
}
