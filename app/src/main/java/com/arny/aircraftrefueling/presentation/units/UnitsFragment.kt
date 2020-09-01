package com.arny.aircraftrefueling.presentation.units


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arny.aircraftrefueling.R
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class UnitsFragment : MvpAppCompatFragment(), UnitsView {
    companion object {
        fun getInstance() = UnitsFragment()
    }

    private val presenter by moxyPresenter { UnitsPresener() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.menu_settings)
    }
}
