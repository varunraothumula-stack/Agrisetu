package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AppRepository
import com.example.model.AppLanguage
import com.example.model.ListingStatus
import com.example.model.RequestStatus
import com.example.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies AgriSetu app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AgriSetu", appName)
    }

    @Test
    fun `repository state updates role and language`() {
        val repo = AppRepository.instance
        repo.setRole(UserRole.BUYER)
        assertEquals(UserRole.BUYER, repo.currentRole.value)

        repo.setLanguage(AppLanguage.HINDI)
        assertEquals(AppLanguage.HINDI, repo.currentLanguage.value)
    }

    @Test
    fun `accepting buy request records five percent marketplace commission`() {
        val repo = AppRepository.instance
        val initialCommission = repo.totalCommissionEarned.value
        val initialOrders = repo.ordersCount.value

        // Send a buy request for 50 quintals at ₹2000 = ₹1,00,000 deal value
        repo.sendBuyRequest(
            listingId = "lst_01",
            cropName = "Wheat",
            buyerName = "Test Buyer",
            buyerContact = "+91 99999 88888",
            quantity = 50.0,
            offeredPrice = 2000.0,
            pickupDate = "Tomorrow",
            message = "Test order"
        )

        val latestReq = repo.buyRequests.value.first()
        repo.updateRequestStatus(latestReq.id, RequestStatus.ACCEPTED)

        // 5% of 1,00,000 is 5,000
        assertEquals(initialCommission + 5000.0, repo.totalCommissionEarned.value, 0.01)
        assertEquals(initialOrders + 1, repo.ordersCount.value)
    }
}
