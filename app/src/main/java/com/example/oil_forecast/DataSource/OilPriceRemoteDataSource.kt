package com.example.oil_forecast.DataSource

import com.example.oil_forecast.Entity.OilPrice
import com.example.oil_forecast.NetWork.OilPriceService
import com.example.oil_forecast.Utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

class OilPriceRemoteDataSource(
    private val oilPriceService: OilPriceService,
    private val ioDispatcher: CoroutineDispatcher,
) : IOilPriceRemoteDataSource {
    override suspend fun getOilPrice(): Result<List<OilPrice>> =
        withContext(ioDispatcher) {
            try {
                val response = oilPriceService.getOilPrice()
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    if (body != null) {
                        val doc = Jsoup.parse(body)
                        val oilPrices = mutableListOf<OilPrice>()
                        val cpcElements = doc.select("div#cpc")

                        // CPC Prices
                        val cpcPriceElement = cpcElements.first()
                        if (cpcPriceElement != null) {
                            val cpcTitle = cpcPriceElement.select("h2").text()
                            val cpcPriceItems = cpcPriceElement.select("ul li")
                            val cpc92 = cpcPriceItems[0].text().replace("92:", "").trim()
                            val cpc95 = cpcPriceItems[1].text().replace("95油價:", "").trim()
                            val cpc98 = cpcPriceItems[2].text().replace("98:", "").trim()
                            val cpcDiesel = cpcPriceItems[3].text().replace("柴油:", "").trim()
                            oilPrices.add(OilPrice(cpcTitle, cpc92, cpc95, cpc98, cpcDiesel))
                        }

                        // Formosa Prices
                        if (cpcElements.size > 1) {
                            val formosaPriceElement = cpcElements[1]
                            val formosaTitle = formosaPriceElement.select("h2").text()
                            val formosaPriceItems = formosaPriceElement.select("ul li")
                            val formosa92 = formosaPriceItems[0].text().replace("92:", "").trim()
                            val formosa95 = formosaPriceItems[1].text().replace("95油價:", "").trim()
                            val formosa98 = formosaPriceItems[2].text().replace("98:", "").trim()
                            val formosaDiesel = formosaPriceItems[3].text().replace("柴油:", "").trim()
                            oilPrices.add(OilPrice(formosaTitle, formosa92, formosa95, formosa98, formosaDiesel))
                        }
                        Result.Success(oilPrices)
                    } else {
                        Result.Error(Exception("Response body is null"))
                    }
                } else {
                    Result.Error(Exception("Network request failed with code: ${response.code()}"))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
}
