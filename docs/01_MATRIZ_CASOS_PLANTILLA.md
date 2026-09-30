# Matriz de casos de prueba - Ae6

La matriz parte de las tres reglas principales solicitadas en la actividad: cancelación, descuentos y confirmación. Se agregan casos de borde y errores para no quedarnos solo con el camino feliz.

| ID | Regla / método | Escenario | Entrada | Resultado esperado | Tipo | Riesgo que cubre |
|---|---|---|---|---|---|---|
| CP-01 | `puedeCancelar` | Anticipación normal | `5` horas | `true` | Normal | Rechazar una cancelación que sí cumple la regla. |
| CP-02 | `puedeCancelar` | Límite exacto | `2` horas | `true` | Límite | Implementar por error `> 2` en vez de `>= 2`. |
| CP-03 | `puedeCancelar` | Debajo del límite | `1` hora | `false` | Límite | Permitir cancelar fuera del tiempo mínimo. |
| CP-04 | `calcularTotal` | Cliente NORMAL | `NORMAL, 100` | `100.00` | Normal | Aplicar descuento donde no corresponde. |
| CP-05 | `calcularTotal` | Cliente VIP | `VIP, 100` | `85.00` | Alternativo | Calcular mal el 15 % de descuento. |
| CP-06 | `calcularTotal` | Cliente ESTUDIANTE | `ESTUDIANTE, 100` | `90.00` | Alternativo | Calcular mal el 10 % de descuento. |
| CP-07 | `calcularTotal` | Total negativo | `NORMAL, -1` | `IllegalArgumentException` | Inválido | Aceptar un monto imposible para el cálculo. |
| CP-08 | `calcularTotal` | Total cero | `VIP, 0` | `0.00` | Límite | Tratar el cero como inválido aunque la regla solo rechaza negativos. |
| CP-09 | `calcularTotal` | Tipo sin respetar mayúsculas | `vip, 100` | `85.00` | Alternativo | Perder el comportamiento `equalsIgnoreCase`. |
| CP-10 | `confirmar` | Horario disponible | Reserva `R-001` + disponible `true` | Estado `CONFIRMADA`, guardar y notificar | Normal / interacción | Cambiar estado pero olvidar persistencia o notificación. |
| CP-11 | `confirmar` | Horario no disponible | Reserva `R-002` + disponible `false` | `IllegalStateException`, sigue `PENDIENTE`, no guarda ni notifica | Alternativo / excepción | Ejecutar efectos secundarios aun cuando no hay disponibilidad. |
| CP-12 | `confirmar` | Reserva nula | `null` | `IllegalArgumentException`, sin interacciones externas | Inválido / excepción | Consultar dependencias antes de validar la entrada. |
| CP-13 | `Reserva` | Tipo nulo | `R-101, null` | Tipo `NORMAL` | Alternativo | Perder el valor por defecto del dominio. |
| CP-14 | `Reserva.cancelar` | Cancelación directa del dominio | Reserva pendiente | Estado `CANCELADA` | Cobertura / estado | Dejar sin verificar una transición presente en la entidad. |
| CP-15 | `Reserva` | Id vacío o nulo | `"   "` / `null` | `IllegalArgumentException` | Inválido | Crear reservas sin identificador válido. |

## Prioridad de los casos

- **Alta:** CP-02, CP-07, CP-10, CP-11 y CP-12 porque validan límites, excepciones y efectos secundarios.
- **Media:** CP-01, CP-03, CP-04, CP-05 y CP-06 porque cubren las reglas principales.
- **Complementaria:** CP-08, CP-09 y CP-13 a CP-15 porque fortalecen bordes y caminos detectables mediante cobertura.

## Nota sobre los casos derivados de cobertura

Los casos CP-13 a CP-15 se mantuvieron en la matriz como riesgos complementarios del dominio. La primera ejecución de JaCoCo, realizada con las 13 pruebas de `ReservaServiceTest`, confirmó que esos caminos seguían parcialmente sin cobertura: el paquete de dominio mostraba 73 % de instrucciones y 50 % de ramas. Por eso esos casos se implementaron después del análisis mediante `ReservaTest` y no como parte de la primera ronda de pruebas del servicio.
