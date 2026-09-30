# UEES UCOM0310 — Semana 7 — Ae6

Proyecto individual para la actividad **Ae6 - Suite de pruebas, cobertura y Pull Request documentado**.

## Requisitos

- Java 21
- Maven 3.9+
- Git

## Objetivo

Proteger mediante pruebas automatizadas las reglas entregadas en el proyecto base:

- cancelación según horas de anticipación;
- cálculo de total para NORMAL, VIP y ESTUDIANTE;
- confirmación de reservas según disponibilidad;
- efectos relevantes de guardar y notificar.

## Estructura de pruebas

La suite usa JUnit 5 y sigue la estructura AAA cuando ayuda a leer el escenario:

- **Arrange:** preparar datos y dobles;
- **Act:** ejecutar el comportamiento;
- **Assert:** validar estado, resultado, excepción o interacción.

Para `confirmar()` se usa Mockito porque existen dependencias externas al comportamiento:

- `DisponibilidadClient`: Stub para controlar si el horario está disponible.
- `ReservaRepository`: Mock para comprobar el guardado.
- `Notificador`: Mock para comprobar el envío de confirmación.

No se mockean objetos simples como `Reserva` porque se puede trabajar directamente con la entidad real.

## Ejecutar la suite

```bash
mvn clean test
```

## Reporte JaCoCo

JaCoCo está configurado en `pom.xml` y se genera durante la fase `test`.

Después de ejecutar:

```bash
mvn clean test
```

abrir:

```text
target/site/jacoco/index.html
```

El análisis se documenta en:

```text
docs/02_ANALISIS_COBERTURA_PLANTILLA.md
```

## Documentación Ae6

- `docs/01_MATRIZ_CASOS_PLANTILLA.md`: matriz de casos y riesgos.
- `docs/02_ANALISIS_COBERTURA_PLANTILLA.md`: interpretación de JaCoCo.
- `docs/03_PULL_REQUEST_PLANTILLA.md`: texto base para el Pull Request.
- `.github/pull_request_template.md`: plantilla automática del PR.

## Flujo Git esperado

El trabajo de Ae6 se realiza en una rama separada:

```bash
git switch main
git pull
git switch -c ae6/suite-pruebas
```

No se debe trabajar directamente en `main` porque el Pull Request forma parte de la evidencia de la actividad.

## Uso de inteligencia artificial

Se utilizó IA como apoyo para ordenar casos de prueba, revisar alternativas de uso de dobles y mejorar la redacción de la documentación. La ejecución, interpretación de resultados y revisión final deben realizarse sobre el proyecto local antes de entregar.

## Resultado validado de Ae6

La ejecución final realizada con Java 21 produjo:

```text
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

La suite final contiene:

- `ReservaServiceTest`: 13 pruebas.
- `ReservaTest`: 5 pruebas.

La primera medición de JaCoCo, antes de `ReservaTest`, mostró **87 % global de instrucciones**, **83 % de ramas**, mientras `ReservaService` ya tenía **100 %**. Después de agregar las pruebas de dominio derivadas del análisis, el resultado final fue:

- líneas: **100 %** (`39/39`);
- ramas: **100 %** (`18/18`);
- instrucciones: **100 %** (`138/138`);
- métodos: **100 %** (`11/11`).
