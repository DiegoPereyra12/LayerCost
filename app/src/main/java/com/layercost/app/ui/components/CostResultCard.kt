package com.layercost.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.layercost.app.R
import com.layercost.app.domain.calculators.CostBreakdown

@Composable
fun CostResultCard(breakdown: CostBreakdown, salePrice: Float) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.production_cost_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            ResultRow(stringResource(R.string.material_cost_label), breakdown.materialCost)
            ResultRow(stringResource(R.string.energy_cost_result_label), breakdown.electricityCost)
            ResultRow(stringResource(R.string.wear_cost_result_label), breakdown.depreciationCost)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${stringResource(R.string.total_cost_label)} ${stringResource(R.string.currency_symbol)}${"%.2f".format(breakdown.subtotal)} ${stringResource(R.string.mxn_currency)}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            if (salePrice > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                val profit = salePrice - breakdown.subtotal
                val profitColor = if (profit >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                
                Text(
                    text = stringResource(R.string.estimated_profit_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${stringResource(R.string.currency_symbol)}${"%.2f".format(profit)} ${stringResource(R.string.mxn_currency)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = profitColor
                )
            }
        }
    }
}

@Composable
fun ResultRow(label: String, value: Float) {
    Text(
        text = "$label ${stringResource(R.string.currency_symbol)}${"%.2f".format(value)}",
        style = MaterialTheme.typography.bodyMedium
    )
}
