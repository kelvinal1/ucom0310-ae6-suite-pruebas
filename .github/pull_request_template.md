## Objetivo

Proteger las reglas principales del módulo de reservas mediante una suite reproducible de JUnit 5.

## Cambios

- [x] Casos normales y de límite
- [x] Excepciones
- [x] Stub/Mock justificados
- [x] Cobertura JaCoCo analizada
- [x] Documentación actualizada

## Cómo verificar

```bash
mvn clean test
```

Resultado obtenido:

```text
Tests run: 18, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Cobertura

Primera medición:

- Líneas globales: 34 de 39 (~87 %)
- Ramas globales: 83 %
- `ReservaService`: 100 % instrucciones / 100 % ramas
- Dominio: 73 % instrucciones / 50 % ramas

Resultado final:

- Líneas: **100 %**
- Ramas: **100 %**
- Instrucciones: **100 %**
- Métodos: **100 %**

## Limitaciones

Las dependencias de disponibilidad, persistencia y notificación se prueban con dobles. La suite es unitaria y no reemplaza pruebas de integración con implementaciones reales. Además, un 100 % de cobertura no implica ausencia total de defectos fuera de las reglas evaluadas.

## Autorrevisión

- [ ] Revisé Files changed línea por línea
- [ ] No hay `target/` ni archivos generados
- [ ] No hay credenciales o datos sensibles
- [ ] Los casos límite están presentes
- [ ] Los mocks verifican interacciones relevantes
- [ ] Los commits son pequeños y descriptivos

## Uso de IA

IA utilizada como apoyo para organización de casos, contraste puntual del uso de dobles y redacción. La ejecución, revisión de Maven, capturas de JaCoCo y validación final se realizaron directamente sobre el proyecto.
