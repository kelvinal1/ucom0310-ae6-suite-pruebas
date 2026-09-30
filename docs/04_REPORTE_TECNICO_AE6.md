# Reporte técnico breve - Ae6

## 1. Objetivo de Ae6

El objetivo de esta actividad fue transformar las reglas entregadas en el proyecto base en evidencia automatizada mediante JUnit 5. Además de comprobar resultados, se trabajaron escenarios normales, alternativos, límites, entradas inválidas e interacciones con dependencias. La entrega se completa con análisis de cobertura JaCoCo, trazabilidad Git y un Pull Request documentado.

## 2. Reglas y matriz de casos

Se analizaron tres comportamientos principales de `ReservaService`:

1. `puedeCancelar(int horasAnticipacion)`: permite cancelar cuando existen al menos dos horas de anticipación.
2. `calcularTotal(String tipo, double totalBase)`: aplica 15 % a VIP, 10 % a ESTUDIANTE, mantiene NORMAL y rechaza montos negativos.
3. `confirmar(Reserva reserva)`: valida la reserva, consulta disponibilidad y, si es válida, confirma, guarda y notifica.

La matriz completa se encuentra en `docs/01_MATRIZ_CASOS_PLANTILLA.md`. Se diseñaron casos normales, alternativos, de límite e inválidos para evitar que la suite se concentre únicamente en caminos exitosos.

## 3. Implementación JUnit y AAA

Las pruebas se organizaron para que cada método describa un comportamiento concreto. Cuando aporta claridad se sigue AAA:

- **Arrange:** preparar reserva, datos o comportamiento del Stub.
- **Act:** ejecutar un único comportamiento del servicio.
- **Assert:** comprobar resultado, estado, excepción o interacción relevante.

Un ejemplo importante es el límite de cancelación: dos horas exactas deben seguir devolviendo `true`. Este caso protege específicamente el riesgo de cambiar accidentalmente `>= 2` por `> 2`.

La evolución de la suite también quedó separada. Primero se probaron cancelación y descuentos, luego confirmación con Mockito y finalmente se agregaron pruebas directas del dominio a partir de la lectura de JaCoCo.

## 4. Stub y Mock utilizados

Para `confirmar()` no se crean implementaciones reales de disponibilidad, repositorio o notificación porque el objetivo es probar el servicio de forma aislada.

`DisponibilidadClient` se controla como **Stub** mediante Mockito, definiendo `true` o `false` según el escenario. `ReservaRepository` y `Notificador` se usan como **Mocks** para verificar dos efectos secundarios relevantes: guardar una reserva confirmada y enviar su confirmación.

También se comprueba el caso contrario: cuando el horario no está disponible, ni el repositorio ni el notificador deben ejecutarse. Para una reserva nula se verifica que ninguna dependencia sea consultada. No se mockea `Reserva`, ya que es una entidad simple que puede utilizarse directamente y hacerlo no aportaría valor a la prueba.

## 5. Resultado de `mvn clean test`

La ejecución final se realizó desde una compilación limpia con:

```bash
mvn clean test
```

El resultado obtenido fue:

- Pruebas ejecutadas: **18**.
- Fallos: **0**.
- Errores: **0**.
- Omitidas: **0**.
- Resultado: **BUILD SUCCESS**.

La distribución final fue de **13 pruebas en `ReservaServiceTest`** y **5 pruebas en `ReservaTest`**.

## 6. Cobertura JaCoCo e interpretación

JaCoCo se genera automáticamente durante `test` y el reporte queda en `target/site/jacoco/index.html`.

### Primera medición

Antes de agregar `ReservaTest`, la suite tenía 13 pruebas y `ReservaService` ya alcanzaba **100 % de instrucciones y 100 % de ramas**. Sin embargo, el resultado global todavía era menor debido al dominio:

- Total global de instrucciones: **87 %**.
- Líneas: **34 de 39 cubiertas (~87 %)**.
- Ramas: **83 %**, con `3 de 18` ramas sin cubrir.
- Paquete de dominio: **73 % de instrucciones** y **50 % de ramas**.
- Líneas del dominio sin cubrir: **5 de 18**.
- Métodos globales sin cubrir: **3 de 11**.

Esto mostró que la suite del servicio cubría correctamente sus reglas, pero no ejercitaba todo el comportamiento propio de `Reserva`.

### Mejora a partir del reporte

A partir de esa lectura se agregó `ReservaTest`, cubriendo:

- estado inicial `PENDIENTE` y conservación de datos;
- tipo `NORMAL` por defecto cuando se recibe `null`;
- transición de `cancelar()` a `CANCELADA`;
- excepción por id vacío;
- excepción por id nulo.

Después de esas cinco pruebas la suite pasó de 13 a 18 pruebas.

### Resultado final

La segunda ejecución de JaCoCo mostró:

- Instrucciones: **100 %** (`138 de 138`).
- Líneas: **100 %** (`39 de 39`).
- Ramas: **100 %** (`18 de 18`).
- Métodos: **100 %** (`11 de 11`).
- Clases: **100 %** (`3 de 3`).

Una cobertura alta no garantiza por sí sola la corrección. Ejecutar una línea solo demuestra que fue recorrida; por eso las pruebas contienen aserciones asociadas a reglas concretas y verificaciones de interacciones relevantes.

## 7. Flujo Git y commits

Ae6 se desarrolla en `ae6/suite-pruebas`, creada desde una línea base conocida en `main`. Los cambios se registran incrementalmente: matriz, pruebas de reglas, confirmación con Mockito, mejora derivada de cobertura y documentación.

El historial debe adjuntarse con:

```bash
git log --oneline --decorate
```

La secuencia buscada es:

```text
docs: completar evidencia y autorrevision ae6
docs: analizar cobertura jacoco
test: cubrir huecos detectados por jacoco
test: agregar confirmacion con stub y mocks
test: agregar casos de cancelacion y descuentos
docs: completar matriz de casos ae6
chore: registrar proyecto base de ae6
```

## 8. Pull Request y autorrevisión

La rama se publica con:

```bash
git push -u origin ae6/suite-pruebas
```

El Pull Request utiliza `main` como base y `ae6/suite-pruebas` como compare. Su descripción incluye objetivo, cambios, forma de verificación, cobertura, limitaciones y uso de IA.

Antes de entregar se revisan `Files changed`, archivos generados, nombres de pruebas, casos límite, mocks, commits y documentación.

## 9. Conclusiones, limitaciones y trabajo futuro

La suite protege las reglas principales del proyecto base y diferencia pruebas de resultado de pruebas de interacción. Los casos límite y de excepción permiten detectar regresiones que una suite enfocada solo en caminos normales podría ignorar.

El análisis de JaCoCo fue útil porque mostró que el servicio ya estaba completamente cubierto mientras todavía existían huecos en la entidad `Reserva`. En lugar de agregar pruebas únicamente para subir un porcentaje, se eligieron comportamientos observables del dominio y se validaron con aserciones concretas.

La principal limitación es que disponibilidad, persistencia y notificación se validan mediante dobles de prueba. Esto es adecuado para una prueba unitaria, pero no sustituye futuras pruebas de integración con implementaciones reales. Como trabajo futuro podrían agregarse integraciones concretas manteniendo esta suite rápida como primera red de seguridad.

## 10. Declaración de uso de IA

Se utilizó inteligencia artificial principalmente como apoyo para ordenar la matriz de casos, revisar alternativas de pruebas y mejorar la redacción del reporte y de la documentación. También se utilizó como apoyo puntual para contrastar el uso de Stub/Mock. La integración en el proyecto, ejecución de Maven, revisión de los resultados, interpretación de las capturas de JaCoCo, historial Git y validación final se realizaron directamente sobre el proyecto de la actividad.
