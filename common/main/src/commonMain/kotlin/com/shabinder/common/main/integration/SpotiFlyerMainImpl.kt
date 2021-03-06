package com.shabinder.common.main.integration

import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.shabinder.common.di.Picture
import com.shabinder.common.di.isInternetAvailable
import com.shabinder.common.di.showPopUpMessage
import com.shabinder.common.main.SpotiFlyerMain
import com.shabinder.common.main.SpotiFlyerMain.*
import com.shabinder.common.main.store.SpotiFlyerMainStore.Intent
import com.shabinder.common.main.store.SpotiFlyerMainStoreProvider
import com.shabinder.common.main.store.getStore
import kotlinx.coroutines.flow.Flow

internal class SpotiFlyerMainImpl(
    componentContext: ComponentContext,
    dependencies: Dependencies
): SpotiFlyerMain,ComponentContext by componentContext, Dependencies by dependencies {

    private val store =
        instanceKeeper.getStore {
            SpotiFlyerMainStoreProvider(
                storeFactory = storeFactory,
                database = database
            ).provide()
        }

    override val models: Flow<State> = store.states

    override fun onLinkSearch(link: String) {
        if(isInternetAvailable) mainOutput.callback(Output.Search(link = link))
        else showPopUpMessage("Check Network Connection Please")
    }

    override fun onInputLinkChanged(link: String) {
        store.accept(Intent.SetLink(link))
    }

    override fun selectCategory(category: HomeCategory) {
        store.accept(Intent.SelectCategory(category))
    }

    override suspend fun loadImage(url: String): Picture = dir.loadImage(url)
}