package nr.king.carserv.fragment

import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.scopes.FragmentScoped
import nr.king.carserv.R
import nr.king.carserv.databinding.HomeFragmentBinding
import java.text.DecimalFormat

@FragmentScoped
class HomeFragment : Fragment() {

    lateinit var homeFragment: HomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragment = DataBindingUtil.inflate(inflater, R.layout.home_fragment, null, false)
        return homeFragment.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragment.apply {
            setPieData()
            setChartBData()
        }
    }

    private fun setPieData() {
        homeFragment.apply {
            var time = floatArrayOf(
                (0..100).random()?.toFloat()!!,
                (0..60).random()?.toFloat()!!,
                (0..70).random()?.toFloat()!!
            )
            val activity = arrayOf("Completed", "Booked", "Pending")
            val pieEntires: MutableList<PieEntry> = java.util.ArrayList()
            for (i in time.indices) {
                pieEntires.add(PieEntry(time.get(i), activity.get(i)))
            }
            val dataSet = PieDataSet(pieEntires, "")
            dataSet.setColors(
                resources.getColor(R.color.red),
                resources.getColor(R.color.dark_blue),
                resources.getColor(R.color.sky_blue)
            );
            val data = PieData(dataSet)
            pieChart.data = data
            data.setValueTextSize(14f);
            data.setValueTextColor(ContextCompat.getColor(context!!, R.color.white));
            data.setValueFormatter(MyValueFormatter())
            pieChart.invalidate()
            pieChart.description.isEnabled = false;
            pieChart.legend.isEnabled = true;
            // Hide the legend
            pieChart.legend.textColor = Color.BLACK
            pieChart.setDrawSliceText(false); // To remove slice text
        }
    }


    private fun setChartBData() {
        homeFragment.apply {
            chart.invalidate()

            var qcAnswer: Int? = 0
            var qWAnswer: Int? = 0
            var qUnread: Int? = 0
            val values1: ArrayList<BarEntry> = ArrayList()
            val values2: ArrayList<BarEntry> = ArrayList()
            val values3: ArrayList<BarEntry> = ArrayList()

            Log.v("@@values1", "" + values3.size)

            for (i in 400..1000)
            {
                if (i%2==0 || (i%3 ==0 && i%6==0))
                {
                    values1.add(BarEntry(i.toFloat(),i.toFloat()))
                }
                else if(i%3==0)
                {
                    values2.add(BarEntry(i.toFloat(),i.toFloat()))
                }
                else{
                    values3.add(BarEntry(i.toFloat(),i.toFloat()))
                }
            }



            val barDataSet1: BarDataSet = BarDataSet(values1, "")
            barDataSet1.color = this@HomeFragment.resources.getColor(R.color.purple_200)
            val barDataSet2: BarDataSet = BarDataSet(values2, "")
            barDataSet2.color = Color.RED
            val barDataSet3: BarDataSet = BarDataSet(values3, "")
            barDataSet3.color = resources.getColor(R.color.buttoncolor)

            val months =
                arrayOf(
                    "Mon",
                    "Tues",
                    "Wed",
                    "Thu",
                    "Fri"
                )
            val data = BarData(barDataSet1, barDataSet1, barDataSet2, barDataSet3)
            chart.data = data
            val xAxis: XAxis = chart.xAxis
            data.setValueTextColor(ContextCompat.getColor(this@HomeFragment.context!!, R.color.white));
            data.setValueFormatter(MyValueFormatter())

            xAxis.valueFormatter = IndexAxisValueFormatter(months)
            chart.axisLeft.axisMinimum = 0F
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setCenterAxisLabels(true)
            xAxis.isGranularityEnabled = true
            val barSpace = 0.10f
            val groupSpace = 0.10f
            val groupCount = 5
            data.barWidth = 0.25f
            chart.xAxis.axisMinimum = 0F
            chart.xAxis.axisMaximum = 0 + chart.barData.getGroupWidth(groupSpace, barSpace) * groupCount
            chart.groupBars(0F, groupSpace, barSpace)
            chart.description.isEnabled = false;
            chart.axisLeft.isEnabled = false;
            chart.axisRight.isEnabled = false;
            chart.xAxis.setDrawAxisLine(false);
            chart.xAxis.setDrawGridLines(false);
            chart.axisLeft.textColor = ContextCompat.getColor(this@HomeFragment.context!!, R.color.white); // left y-axis
            chart.xAxis.textColor = ContextCompat.getColor(this@HomeFragment.context!!, R.color.white);
            chart.legend.isEnabled = true
            val lEntryChargeDischarge = LegendEntry(
                "Completed",
                Legend.LegendForm.SQUARE,
                10f,
                2f,
                null,
                R.color.purple_200
            )
            val lEntryChargeDischarge1 = LegendEntry(
                "InProgress",
                Legend.LegendForm.SQUARE,
                10f,
                2f,
                null,
                R.color.red
            )
            val lEntryChargeDischarge2 = LegendEntry(
                "Pending",
                Legend.LegendForm.SQUARE,
                10f,
                2f,
                null,
                R.color.buttoncolor
            )
            chart.legend.setCustom(
                arrayOf(
                    lEntryChargeDischarge,
                    lEntryChargeDischarge1,
                    lEntryChargeDischarge2
                )
            )
            chart.legend.textSize = 10f;
            chart.legend.textColor = Color.WHITE;
            chart.legend.xEntrySpace = 40f; // set the space between the legend entries on the x-axis
            chart.legend.yEntrySpace = 40f;
            chart.legend.isWordWrapEnabled = false;
        }

    }


}


class MyValueFormatter : ValueFormatter() {
    private val mFormat: DecimalFormat = DecimalFormat("#")
    override fun getFormattedValue(value: Float): String {
        return mFormat.format(value)
    }

}
