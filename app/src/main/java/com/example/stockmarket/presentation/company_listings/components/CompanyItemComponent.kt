package com.example.stockmarket.presentation.company_listings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.ui.theme.StockMarketAppTheme

@Composable
fun CompanyItemComponent(
    company: CompanyListing,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
) {
    Column(modifier = Modifier.weight(1f)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = company.name,
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = company.exchange,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colors.onBackground
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "(${company.symbol})",
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
@Preview
fun CompanyItemComponentPreview() {
    StockMarketAppTheme {
        CompanyItemComponent(
            company = CompanyListing(
                name = "Agilent Technologies Inc",
                symbol = "A",
                exchange = "NYSE"
            )
        )
    }
}