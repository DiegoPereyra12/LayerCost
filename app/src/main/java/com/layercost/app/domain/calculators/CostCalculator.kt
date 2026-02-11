package com.layercost.app.domain.calculators

/**
 * Calculadora de costos reales de impresión 3D.
 *
 * Filosofía: "Si el cálculo no duele, es porque estás perdiendo dinero."
 * Incluye margen de error obligatorio del 10% por fallos (warping, nudos, cortes de luz).
 */
data class PrinterProfile(
    val name: String = "Generic Printer",
    val powerConsumptionWatts: Float = 300f, // Consumo promedio
    val lifespanHours: Float = 5000f, // Vida útil estimada antes de mantenimiento mayor
    val purchasePrice: Float = 6000f // Precio promedio en MXN (Ender 3 / similar)
)

data class MaterialProfile(
    val type: String = "PLA", // PLA, PETG, etc
    val name: String = "PLA Standard",
    val costPerKg: Float = 450f, // Precio promedio rollo 1kg en México
    val weightGrams: Float = 1000f
)

data class OverheadProfile(
    // Tarifa DAC (Doméstica de Alto Consumo) de CFE ronda los $3.00 - $6.00 dependiendo la zona.
    // En Villaflores, Chiapas, el calor afecta el rendimiento de fuentes de poder.
    // Usamos un promedio alto para no perder.
    val electricityCostKwh: Float = 4.5f, 
    val failureMargin: Float = 0.10f // 10% margen de error (5-10% solicitado)
)

data class JobSpecs(
    val gramsUsed: Float,
    val printTimeHours: Float
)

data class CostBreakdown(
    val materialCost: Float,
    val electricityCost: Float,
    val depreciationCost: Float,
    val subtotal: Float,
    val safetyMargin: Float,
    val totalCost: Float
)

object CostCalculator {
    fun calculate(
        printer: PrinterProfile,
        material: MaterialProfile,
        overhead: OverheadProfile,
        job: JobSpecs
    ): CostBreakdown {
        // 1. Costo Material
        // (Gramos usados / Peso total rollo) * Precio rollo
        val materialCost = (job.gramsUsed / material.weightGrams) * material.costPerKg

        // 2. Costo Electricidad
        // (Watts / 1000) * Horas * Costo KWh
        val kwhUsed = (printer.powerConsumptionWatts / 1000f) * job.printTimeHours
        val electricityCost = kwhUsed * overhead.electricityCostKwh

        // 3. Depreciación / Desgaste
        // (Horas uso / Vida útil) * Precio impresora
        val depreciationCost = (job.printTimeHours / printer.lifespanHours) * printer.purchasePrice

        // Subtotal directo
        val subtotal = materialCost + electricityCost + depreciationCost

        // 4. Margen de Seguridad (Failures)
        // El usuario pidió 5-10%. Usamos 10% porque en Chiapas la luz varía y el calor afecta el warping.
        val safetyMarginAmount = subtotal * overhead.failureMargin

        val total = subtotal + safetyMarginAmount

        return CostBreakdown(
            materialCost = materialCost,
            electricityCost = electricityCost,
            depreciationCost = depreciationCost,
            subtotal = subtotal,
            safetyMargin = safetyMarginAmount,
            totalCost = total
        )
    }
}
