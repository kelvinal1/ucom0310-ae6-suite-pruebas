# Análisis de cobertura JaCoCo - Ae6

## Cómo se generó la evidencia

```bash
mvn clean test
```

Luego se revisó el reporte generado en:

```text
target/site/jacoco/index.html
```

La primera medición se realizó después de completar las pruebas de `ReservaService`, incluyendo los escenarios de confirmación con Mockito, pero antes de agregar `ReservaTest`.

## Resultado de la primera medición

En ese momento la suite tenía **13 pruebas**, todas en verde.

### Resultado global

- Instrucciones cubiertas: **87 %** (`17 de 138` instrucciones sin cubrir).
- Líneas cubiertas: **34 de 39**, equivalente aproximadamente a **87 %**.
- Ramas cubiertas: **83 %** (`3 de 18` ramas sin cubrir).
- Métodos sin cubrir: **3 de 11**.
- Clases sin cubrir: **0 de 3**.

### `ReservaService`

`ReservaService` ya quedó completamente ejercitado por la suite del servicio:

- Instrucciones: **100 %**.
- Ramas: **100 %**.
- Líneas: **21 de 21**.
- Métodos: **4 de 4**.

Esto confirmó que los casos de cancelación, descuentos y confirmación estaban cubriendo tanto caminos normales como límites, excepciones y disponibilidad verdadera/falsa.

### Paquete de dominio

El paquete `edu.uees.testing.domain` todavía mostraba huecos:

- Instrucciones: **73 %**.
- Ramas: **50 %**.
- Líneas sin cubrir: **5 de 18**.
- Métodos sin cubrir: **3 de 7**.

## Hueco relevante detectado

El reporte mostró que el problema ya no estaba en `ReservaService`, sino en comportamiento propio de `Reserva` que la suite del servicio no recorría completamente.

Los caminos relevantes eran:

1. `Reserva.cancelar()`, que cambia el estado a `CANCELADA`.
2. La rama del constructor donde `tipo == null` y se utiliza `NORMAL` por defecto.
3. Las validaciones de identificador `null` o vacío.
4. Los getters y estado inicial de una reserva construida directamente.

La matriz ya contemplaba estos riesgos como casos complementarios (CP-13 a CP-15), pero la primera ejecución de JaCoCo confirmó que esos caminos efectivamente seguían sin ser ejercitados. Por esa razón se implementaron después de revisar la cobertura.

## Prueba añadida a partir del análisis

Se agregó `ReservaTest` con cinco pruebas pequeñas de dominio:

- una reserva nueva empieza en `PENDIENTE` y conserva sus datos;
- un tipo nulo utiliza `NORMAL`;
- `cancelar()` cambia el estado a `CANCELADA`;
- un id vacío genera `IllegalArgumentException`;
- un id nulo genera `IllegalArgumentException`.

Después de agregar estas pruebas la suite pasó de **13 a 18 pruebas**.

## Resultado final

La segunda ejecución de:

```bash
mvn clean test
```

terminó con:

```text
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

El reporte final de JaCoCo mostró:

- Instrucciones: **100 %** (`0 de 138` sin cubrir).
- Líneas: **100 %** (`0 de 39` sin cubrir).
- Ramas: **100 %** (`0 de 18` sin cubrir).
- Métodos: **100 %** (`0 de 11` sin cubrir).
- Clases: **100 %** (`0 de 3` sin cubrir).

## Interpretación

La mejora de cobertura fue útil porque permitió detectar comportamiento de dominio que no estaba siendo ejercitado por la suite del servicio. Sin embargo, el objetivo no fue perseguir el 100 % por sí mismo. Una línea puede ejecutarse sin que una prueba valide correctamente la regla asociada.

Por eso las pruebas agregadas tienen aserciones concretas sobre estados, excepciones y valores por defecto. De igual forma, los mocks del servicio verifican solamente efectos secundarios que sí son parte del comportamiento esperado: guardar una reserva confirmada y enviar su confirmación.
