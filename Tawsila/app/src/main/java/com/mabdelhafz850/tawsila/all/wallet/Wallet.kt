package com.mabdelhafz850.tawsila.ui.activity.all.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.mabdelhafz850.tawsila.R
import com.mabdelhafz850.tawsila.databinding.FragmentWalletBinding
import com.mabdelhafz850.tawsila.ui.activity.all.chat.Chat
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.LineGraphSeries
import org.eazegraph.lib.models.ValueLinePoint
import org.eazegraph.lib.models.ValueLineSeries

class Wallet : Fragment() {

    lateinit var binding : FragmentWalletBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val model = ViewModelProviders.of(this).get(WalletViewModel::class.java)
        binding.vm = model

        model.getWallet(requireContext())



        val series = ValueLineSeries()
        series.color = -0xa9480f

        series.addPoint(ValueLinePoint("Jan", 2.4f))
        series.addPoint(ValueLinePoint("Feb", 3.4f))
        series.addPoint(ValueLinePoint("Mar", 0.4f))
        series.addPoint(ValueLinePoint("Apr", 1.2f))
        series.addPoint(ValueLinePoint("Mai", 2.6f))
        series.addPoint(ValueLinePoint("Jun", 1.0f))
//        series.addPoint(ValueLinePoint("Jul", 3.5f))
//        series.addPoint(ValueLinePoint("Aug", 2.4f))
//        series.addPoint(ValueLinePoint("Sep", 2.4f))
//        series.addPoint(ValueLinePoint("Oct", 3.4f))
//        series.addPoint(ValueLinePoint("Nov", .4f))
//        series.addPoint(ValueLinePoint("Dec", 1.3f))

        binding.cubiclinechart.addSeries(series)
        binding.cubiclinechart.startAnimation()


        return binding.root
    }
}


//
//Chat.currantFrag = R.layout.fragment_online_as_taxi6
//val graph: GraphView = view.findViewById(R.id.graph)
//val series = LineGraphSeries(arrayOf(
//        DataString("Sat", 0),
//        DataString("Sun", 2),
//        DataString("Mon", 1),
//        DataString("Tue", 3),
//        DataString("Wed", 2.7),
//        DataString("Thu", 5),
//        DataString("Fri", .6)
//))
//graph.addSeries(series)