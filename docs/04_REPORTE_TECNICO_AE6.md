# Reporte técnico breve - Ae6

## 1. Objetivo de Ae6

El objetivo de esta actividad fue transformar las reglas entregadas en el proyecto base en evidencia automatizada mediante JUnit 5. Además de comprobar resultados, se trabajaron escenarios normales, alternativos, límites, entradas inválidas e interacciones con dependencias. La entrega se completa con análisis de cobertura JaCoCo, trazabilidad Git y un Pull Request documentado.

## 2. Reglas y matriz de casos

Se analizaron tres comportamientos principales de `ReservaService`:

1. `puedeCancelar(int horasAnticipacion)`: permite cancelar cuando existen al menos dos horas de anticipación.
2. `calcularTotal(String tipo, double totalBase)`: aplica 15 % a VIP, 10 % a ESTUDIANTE, mantiene NORMAL y rechaza montos negativos.
3. `confirmar(Reserva reserva)`: valida la reserva, consulta disponibilidad y, si es válida, confirma, guarda y notifica.

La matriz completa se encuentra en `docs/01_MATRIZ_CASOS_PLANTILLA.md`. Se diseñaron casos normales, de límite e inválidos para evitar que la suite se concentre únicamente en caminos exitosos.

## 3. Implementación JUnit y AAA

Las pruebas se organizaron de forma que cada método describa un comportamiento concreto. Cuando aporta claridad se sigue AAA:

- **Arrange:** preparar reserva, datos o comportamiento del Stub.
- **Act:** ejecutar un único comportamiento del servicio.
- **Assert:** comprobar resultado, estado, excepción o interacción relevante.

Ejemplo del límite de cancelación: dos horas exactas deben seguir devolviendo `true`. Este caso protege específicamente el riesgo de cambiar accidentalmente `>= 2` por `> 2`.

## 4. Stub y Mock utilizados

Para `confirmar()` no se crean implementaciones reales de disponibilidad, repositorio o notificación porque el objetivo es probar el servicio de forma aislada.

`DisponibilidadClient` se controla como **Stub** mediante Mockito, definiendo `true` o `false` según el escenario. `ReservaRepository` y `Notificador` se usan como **Mocks** para verificar dos efectos secundarios importantes: guardar una reserva confirmada y enviar su confirmación.

También se comprueba el caso contrario: cuando el horario no está disponible, ni el repositorio ni el notificador deben ejecutarse. Para reserva nula se verifica que ninguna dependencia sea consultada. No se mockea `Reserva`, ya que es una entidad simple que puede utilizarse directamente y hacerlo no aportaría valor a la prueba.

## 5. Resultado de `mvn clean test`

Ejecutar desde una copia limpia del proyecto:

```bash
mvn clean test
```

Registrar aquí la evidencia final obtenida localmente:

- Pruebas ejecutadas: **COMPLETAR CON SALIDA MAVEN**
- Fallos: **0 esperado**
- Errores: **0 esperado**
- Resultado: **BUILD SUCCESS esperado**

No se debe completar esta sección con valores supuestos; la evidencia debe corresponder a la ejecución final.

## 6. Cobertura JaCoCo e interpretación

JaCoCo se genera automáticamente durante `test` y el reporte queda en `target/site/jacoco/index.html`.

Primero se analiza `ReservaService`, verificando que sus caminos normales y de excepción estén ejercitados. Luego se revisa el resto del código y se identifica comportamiento propio de `Reserva` que no necesariamente es recorrido por las pruebas del servicio, principalmente `cancelar()` y ramas del constructor.

Como resultado del análisis se incorpora `ReservaTest`, cubriendo estado inicial, tipo por defecto, transición a `CANCELADA` e identificadores inválidos.

Completar con el reporte real:

- Cobertura de líneas final: **COMPLETAR**
- Cobertura de ramas final: **COMPLETAR**

Una cobertura alta no garantiza corrección. Ejecutar una línea solo demuestra que fue recorrida; por eso las pruebas de esta entrega contienen aserciones asociadas a reglas concretas y verificaciones de interacciones relevantes.

## 7. Flujo Git y commits

Ae6 se desarrolla en `ae6/suite-pruebas`, creada desde una línea base conocida en `main`. Los cambios se registran incrementalmente: matriz, pruebas de reglas, confirmación con Mockito, mejora derivada de cobertura y documentación.

El historial debe adjuntarse con:

```bash
git log --oneline --decorate
```

## 8. Pull Request y autorrevisión

La rama se publica con:

```bash
git push -u origin ae6/suite-pruebas
```

El Pull Request usa `main` como base y `ae6/suite-pruebas` como compare. La descripción incluye objetivo, cambios, forma de verificación, cobertura, limitaciones y uso de IA.

Antes de entregar se revisan `Files changed`, archivos generados, nombres de pruebas, casos límite, mocks, commits y documentación.

## 9. Conclusiones, limitaciones y trabajo futuro

La suite protege las reglas principales del proyecto base y diferencia pruebas de resultado de pruebas de interacción. Los casos límite y de excepción permiten detectar regresiones que una suite enfocada solo en caminos normales podría ignorar.

La principal limitación es que disponibilidad, persistencia y notificación se validan mediante dobles de prueba. Esto es adecuado para una prueba unitaria, pero no sustituye futuras pruebas de integración con implementaciones reales. Como trabajo futuro podrían agregarse integraciones concretas manteniendo esta suite rápida como primera red de seguridad.

## 10. Declaración de uso de IA

Se utilizó inteligencia artificial como apoyo para ordenar la matriz de casos, revisar alternativas de pruebas y mejorar la redacción del reporte y de la documentación. También se utilizó como apoyo puntual para contrastar el uso de Stub/Mock. La integración en el proyecto, ejecución de Maven, revisión de los resultados, interpretación del reporte JaCoCo, historial Git y validación final deben realizarse y revisarse directamente sobre el repositorio de la actividad.
