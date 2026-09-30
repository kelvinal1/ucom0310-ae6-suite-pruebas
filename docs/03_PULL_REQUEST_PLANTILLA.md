# Pull Request Ae6

## Objetivo

Completar la suite de pruebas del módulo de reservas, proteger reglas normales, límites, excepciones e interacciones, y dejar evidencia reproducible mediante JUnit 5, Mockito, JaCoCo y Git.

## Cambios realizados

- Se completó la matriz con casos normales, alternativos, límite e inválidos.
- Se agregaron pruebas JUnit 5 siguiendo estructura AAA.
- Se usó `DisponibilidadClient` como Stub controlado con Mockito.
- Se verificaron `ReservaRepository` y `Notificador` como Mocks en el flujo de confirmación.
- Se ejecutó una primera medición de cobertura con 13 pruebas.
- A partir de los huecos observados en el dominio se agregó `ReservaTest`.
- La suite final quedó en 18 pruebas verdes.
- Se documentó el análisis antes/después de JaCoCo.

## Casos de prueba principales

- Cancelación: 5 horas, límite de 2 horas y 1 hora.
- Descuentos: NORMAL, VIP, ESTUDIANTE, cero, minúsculas y total negativo.
- Confirmación: disponible, no disponible y reserva nula.
- Dominio: estado inicial, tipo por defecto, cancelación e id inválido.

## Cómo verificar

```bash
mvn clean test
```

Resultado obtenido:

```text
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Cobertura:

```text
target/site/jacoco/index.html
```

## Cobertura

Primera medición, antes de `ReservaTest`:

- Líneas globales: **34 de 39 (~87 %)**.
- Ramas globales: **83 %** (`3 de 18` sin cubrir).
- `ReservaService`: **100 %** de instrucciones y ramas.
- Dominio: **73 %** de instrucciones y **50 %** de ramas.

Resultado final:

- Líneas: **100 %** (`39 de 39`).
- Ramas: **100 %** (`18 de 18`).
- Instrucciones: **100 %** (`138 de 138`).
- Métodos: **100 %** (`11 de 11`).

El análisis detallado está en `docs/02_ANALISIS_COBERTURA_PLANTILLA.md`.

## Limitaciones

- La disponibilidad, persistencia y notificación son interfaces y en esta actividad se prueban mediante dobles; no se realiza integración con implementaciones reales.
- La cobertura del 100 % no implica que no puedan existir errores fuera de las reglas incluidas en el proyecto base.
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

Se utilizó inteligencia artificial como apoyo para organizar la matriz de casos, revisar alternativas de pruebas y mejorar la redacción de la documentación. También se utilizó de forma puntual para contrastar el uso de Stub/Mock. La integración de los cambios, ejecución de Maven, revisión de resultados, capturas de JaCoCo y validación final se realizaron directamente sobre el proyecto de la actividad.
