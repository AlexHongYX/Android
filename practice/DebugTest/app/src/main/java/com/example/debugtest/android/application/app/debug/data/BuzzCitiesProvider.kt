package com.ss.android.application.app.debug.data

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.ss.android.buzz.BuzzSPModel
import com.ss.android.utils.GsonProvider
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by zhangchong on 2019/4/11
 *
 */
object BuzzCitiesProvider {
    const val LANGUAGE = "语言"
    const val TIER = "级别"
    const val STATE = "州邦"
    const val CITY = "城市"

    private val cityMap = HashMap<String, CityInfo>()
    private var locationList: List<CityInfo>? = listOf()
    private val languageList = mutableSetOf<String>()   //语言列表

    init {
        locationList = GsonProvider.getDefaultGson().
            fromJson<List<CityInfo>>(BuzzSPModel.buzzDebugCities.value, object : TypeToken<List<CityInfo>>() {}.type)
        locationList?.let {
            for (city in it) {
                //默认显示
                languageList.add(LANGUAGE)
                languageList.add(city.language)
                cityMap[city.city] = city
            }
        }
    }

    fun getCity(city: String): CityInfo? {
        return cityMap[city]
    }

    fun getCityList (language: String? = null, tier: String? = null, state: String? = null): List<String>? {
        locationList?.asSequence()?.filter {
            language.isNullOrEmpty() || language == LANGUAGE || !language.isNullOrEmpty() && it.language == language
        }?.filter {
            tier.isNullOrEmpty() || tier == TIER || !tier.isNullOrEmpty() && it.getTierStr() == tier
        }?.filter {
            state.isNullOrEmpty() || state == STATE || !state.isNullOrEmpty() && it.state == state
        }?.filter {
            it.city.isNotEmpty()
        }?.map {
            it.city
        }?.toList()?.let {
            val cities = it.sorted()
            return ArrayList<String>().apply {
                add(0, CITY)
                addAll(cities)
            }
        }

        return ArrayList<String>().apply {
            add(0, CITY)
        }
    }

    fun getLanguageList(): List<String>? {
        return languageList.toList()
    }

    fun getTierList(language: String? = null): List<String>? {
        locationList?.asSequence()?.filter {
            language.isNullOrEmpty() || language == LANGUAGE || !language.isNullOrEmpty() && it.language == language
        }?.filter {
            it.tier > 0
        }?.map {
            it.getTierStr()
        }?.toList()?.let {
            val tiers = ArrayList<String>()
            tiers.add(0, TIER)
            tiers.addAll(it)
            return tiers.distinct()
        }
        return ArrayList<String>().apply {
            add(0, TIER)
        }
    }

    fun getStateList(language: String? = null, tier: String? = null): List<String>? {
        locationList?.asSequence()?.filter {
            language.isNullOrEmpty() || language == LANGUAGE || !language.isNullOrEmpty() && it.language == language
        }?.filter {
            tier.isNullOrEmpty() || tier == TIER || !tier.isNullOrEmpty() && it.getTierStr() == tier
        }?.filter {
            !it.state.isEmpty()
        }?.map {
            it.state
        }?.toList()?.let {
            val states = ArrayList<String>()
            states.add(0, STATE)
            states.addAll(it)
            return states.distinct()
        }
        return ArrayList<String>().apply {
            add(0, STATE)
        }
    }
}

class CityInfo(@SerializedName("language") var language: String,
               @SerializedName("tier") var tier: Int,
               @SerializedName("state") var state: String,
               @SerializedName("city") var city: String,
               @SerializedName("longitude") var longitude: Double,
               @SerializedName("latitude") var latitude: Double){

    fun getTierStr(): String{
        return "${tier}线"
    }
}