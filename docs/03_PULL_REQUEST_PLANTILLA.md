# Pull Request Ae6

## Objetivo

Completar la suite de pruebas del módulo de reservas, proteger reglas normales, límites, excepciones e interacciones, y dejar evidencia reproducible mediante JUnit 5, Mockito, JaCoCo y Git.

## Cambios realizados

- Se completó la matriz con casos normales, alternativos, límite e inválidos.
- Se agregaron pruebas JUnit 5 siguiendo estructura AAA.
- Se usó `DisponibilidadClient` como Stub controlado con Mockito.
- Se verificaron `ReservaRepository` y `Notificador` como Mocks en el flujo de confirmación.
- Se añadió una segunda ronda de pruebas para comportamientos de `Reserva` detectables mediante el análisis de cobertura.
- Se documentó cómo ejecutar la suite y cómo interpretar JaCoCo.

## Casos de prueba principales

- Cancelación: 5 horas, límite de 2 horas y 1 hora.
- Descuentos: NORMAL, VIP, ESTUDIANTE, cero, minúsculas y total negativo.
- Confirmación: disponible, no disponible y reserva nula.
- Dominio: estado inicial, tipo por defecto, cancelación e id inválido.

## Cómo verificar

```bash
mvn clean test
```

Cobertura:

```text
target/site/jacoco/index.html
```

## Cobertura

Completar antes de abrir el PR con los valores reales obtenidos de JaCoCo:

- Líneas: **COMPLETAR**
- Ramas: **COMPLETAR**

El análisis detallado está en `docs/02_ANALISIS_COBERTURA_PLANTILLA.md`.

## Limitaciones

- La disponibilidad, persistencia y notificación son interfaces y en esta actividad se prueban mediante dobles; no se realiza integración con implementaciones reales.
- La suite valida las reglas proporcionadas por el proyecto base y no agrega reglas de negocio nuevas.

## Autorrevisión

- [ ] `mvn clean test` termina en `BUILD SUCCESS`.
- [ ] Revisé `Files changed` línea por línea.
- [ ] No se subió `target/`.
- [ ] No existen credenciales ni datos sensibles.
- [ ] Los nombres de las pruebas describen el comportamiento.
- [ ] Las aserciones validan resultados y estados, no detalles irrelevantes.
- [ ] Los mocks verifican solo interacciones importantes.
- [ ] El reporte JaCoCo fue generado e interpretado.
- [ ] Los commits muestran una evolución incremental.

## Uso de IA

Se utilizó inteligencia artificial como apoyo para organizar la matriz de casos, revisar alternativas de pruebas y mejorar la redacción de la documentación. La implementación, ejecución de la suite, revisión de resultados, análisis de cobertura y validación final se realizaron sobre el proyecto de la actividad.
