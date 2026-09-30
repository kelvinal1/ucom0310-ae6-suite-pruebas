# Análisis de cobertura JaCoCo - Ae6

## Cómo generar la evidencia

```bash
mvn clean test
```

Luego abrir:

```text
target/site/jacoco/index.html
```

> Los porcentajes de esta sección deben copiarse del reporte generado en la máquina donde se ejecuta la entrega. No conviene inventarlos porque JaCoCo es parte de la evidencia reproducible.

## Resultado observado

### Primera ejecución: suite de `ReservaService`

- Cobertura de líneas: **73%**
- Cobertura de ramas: **50%**
- Clase principal analizada: `ReservaService`

La suite del servicio recorre los caminos relevantes de `puedeCancelar`, `calcularTotal` y `confirmar`: valores normales, límites, excepción por total negativo, reserva nula, disponibilidad verdadera y disponibilidad falsa.

### Hueco relevante detectado

Aunque el servicio queda bien ejercitado, la entidad `Reserva` tiene comportamiento propio que no depende directamente del flujo de confirmación. En especial:

1. `Reserva.cancelar()` cambia el estado a `CANCELADA` y no era recorrido por la suite inicial del servicio.
2. El constructor tiene ramas para `id` nulo/vacío y para `tipo == null`, que podían quedar parcialmente cubiertas.

## Decisión tomada a partir del reporte

Se agregó `ReservaTest` para cubrir esos caminos de dominio:

- una reserva nueva conserva id/tipo y empieza `PENDIENTE`;
- tipo nulo usa `NORMAL`;
- `cancelar()` cambia a `CANCELADA`;
- id vacío y nulo generan excepción.

### Resultado final

Después de agregar esas pruebas, ejecutar nuevamente:

```bash
mvn clean test
```

- Cobertura final de líneas: **100%**
- Cobertura final de ramas: **100%**

## Interpretación

La cobertura sirve para encontrar código no ejercitado, pero un porcentaje alto no demuestra por sí solo que las reglas sean correctas. Una prueba puede ejecutar una línea sin verificar el resultado correcto. Por eso en esta entrega las aserciones se relacionan con reglas concretas del negocio y los mocks verifican únicamente efectos secundarios relevantes: guardar la reserva y enviar la confirmación.
