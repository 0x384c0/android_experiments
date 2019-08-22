package com.example.experimentskotlin.util.decorations

import android.view.View
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedAdapter
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedDiffUtilAdapter

/**
 * Имплементация StickyHeaderInterface, совместимая с SingleLayoutSectionedAdapter и SingleLayoutSectionedDiffUtilAdapter
 */
class StickyHeaderListener<T>(
        private val adapter: SingleLayoutSectionedAdapter<*, T>? = null,
        private val adapterDiffUtil: SingleLayoutSectionedDiffUtilAdapter<*, T>? = null,
        private val headerId: Int,
        private val sectionViewHolderFactory: (View) -> SingleLayoutSectionedAdapter.BaseItemViewHolder<T>
) : StickHeaderItemDecoration.StickyHeaderInterface {
    private val itemSectionIdMap = mutableMapOf<Int, Int>()
    private val itemSectionDataMap = mutableMapOf<Int, T>()
    fun reset() {
        itemSectionIdMap.clear()
        itemSectionDataMap.clear()
    }

    override fun getHeaderLayout(headerPosition: Int): Int {
        return headerId
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return try {
            getHeaderPositionForItem(itemPosition) == itemPosition
        } catch (e: Exception) {
            false
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return when {
            adapter != null -> getHeaderPositionForItemFromAdapter(itemPosition, adapter)
            adapterDiffUtil != null -> getHeaderPositionForItemFromDiffUtilAdapter(itemPosition, adapterDiffUtil)
            else -> throw Exception("adapters are null")
        }
    }

    override fun bindHeaderData(header: View?, headerPosition: Int) {
        return when {
            adapter != null -> bindHeaderDataFromAdapter(header, headerPosition, adapter)
            adapterDiffUtil != null -> bindHeaderDataFromDiffUtilAdapter(header, headerPosition, adapterDiffUtil)
            else -> throw Exception("adapters are null")
        }
    }


    //region SingleLayoutSectionedAdapter
    private fun getHeaderPositionForItemFromAdapter(
            itemPosition: Int,
            adapter: SingleLayoutSectionedAdapter<*, T>
    ): Int {
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

    private fun bindHeaderDataFromAdapter(
            header: View?,
            headerPosition: Int,
            adapter: SingleLayoutSectionedAdapter<*, T>
    ) {
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
    //endregion

    //region SingleLayoutSectionedDiffUtilAdapter}
    private fun getHeaderPositionForItemFromDiffUtilAdapter(
            itemPosition: Int,
            adapter: SingleLayoutSectionedDiffUtilAdapter<*, T>
    ): Int {
        var sectionId = itemSectionIdMap[itemPosition]
        if (sectionId == null) {
            for (i in itemPosition downTo 0){
                if (adapter.data[i].sectionData != null) {
                    sectionId = i
                    itemSectionIdMap[itemPosition] = sectionId
                    break
                }
            }
        }
        return sectionId!!
    }

    private fun bindHeaderDataFromDiffUtilAdapter(
            header: View?,
            headerPosition: Int,
            adapter: SingleLayoutSectionedDiffUtilAdapter<*, T>
    ) {
        val sectionViewHolder = sectionViewHolderFactory(header!!)
        var sectionData = itemSectionDataMap[headerPosition]
        if (sectionData == null) {
            sectionData = adapter.data[headerPosition].sectionData!!
            itemSectionDataMap[headerPosition] = sectionData
        }
        sectionViewHolder.setup(sectionData)
    }
    //endregion

}
