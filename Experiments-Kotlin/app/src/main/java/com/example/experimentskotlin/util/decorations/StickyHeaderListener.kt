package com.example.experimentskotlin.util.decorations

import android.view.View
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedAdapter

/**
 * Имплементация StickyHeaderInterface, совместимая с SingleLayoutSectionedAdapter
 */
class StickyHeaderListener<T>(
    private val adapter: SingleLayoutSectionedAdapter<*, T>,
    private val headerId: Int,
    private val sectionViewHolderFactory: (View) -> SingleLayoutSectionedAdapter.BaseItemViewHolder<T>
) : StickHeaderItemDecoration.StickyHeaderInterface {
    private val itemSectionIdMap = mutableMapOf<Int, Int>()
    private val itemSectionDataMap = mutableMapOf<Int, T>()
    fun reset() {
        itemSectionIdMap.clear()
        itemSectionDataMap.clear()
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        var sectionId = itemSectionIdMap[itemPosition]
        if (sectionId == null) {
            var i = 0
            for (section in adapter.data) {
                val headerPositionTmp = i
                i += section.itemsData.count() + 1
                if (itemPosition < i) {
                    sectionId = headerPositionTmp
                    itemSectionIdMap[itemPosition] = sectionId
                    break
                }
            }
        }
        return sectionId!!
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return headerId
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        val sectionViewHolder = sectionViewHolderFactory(header!!)
        var sectionData = itemSectionDataMap[headerPosition]
        if (sectionData == null) {
            var i = 0
            for (section in adapter.data) {
                i += section.itemsData.count() + 1
                if (headerPosition < i) {
                    sectionData = section.data
                    itemSectionDataMap[headerPosition] = sectionData
                    break
                }
            }
        }
        sectionViewHolder.setup(sectionData!!)
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return try {
            getHeaderPositionForItem(itemPosition) == itemPosition
        } catch (e: Exception) {
            false
        }
    }
}
