package com.arny.aircraftrefueling.presentation.units

import android.content.Context
import com.arny.aircraftrefueling.data.models.MeasureUnit
import com.arny.aircraftrefueling.utils.AbstractArrayAdapter

class MeasureUnitsAdapter(context: Context) : AbstractArrayAdapter<MeasureUnit>(context, android.R.layout.simple_list_item_1) {
    override fun getItemTitle(item: MeasureUnit?): String = item?.title ?: ""
}
