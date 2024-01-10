//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT

package com.example.tweetybirdie

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.tweetybirdie.Models.SightingModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHotspot : BottomSheetDialogFragment() {
    private var userSighting: Boolean = false
    private lateinit var totalSpeciesTextView: TextView
    private lateinit var informationText :TextView
    private var buttonClickListener: (() -> Unit)? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_hotspot, container, false)
        val startNavigationSmallButton = view.findViewById<ImageButton>(R.id.btnNavigationSmall)

        startNavigationSmallButton.setOnClickListener{

            buttonClickListener?.invoke()
        }

        val startNavigationButton = view.findViewById<Button>(R.id.btnStartNavigation)

        startNavigationButton.setOnClickListener {

            buttonClickListener?.invoke()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("View!!!!", "View Created")

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        val topViewToClick = view.findViewById<View>(R.id.dragLine)

        topViewToClick.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetBehavior.peekHeight = 250

        informationText = view.findViewById(R.id.tvHotspotInformation)
        totalSpeciesTextView = view.findViewById(R.id.tvNumOfSpecies)

        val bottomSheetLayout = view.findViewById<LinearLayout>(R.id.linearViewHotspotInformation)

        arguments?.getString(ARG_HEADING_TEXT)?.let {
            setBottomSheetHeadingText(it)
        }

        displaySightingsInBottomSheet(bottomSheetLayout, ToolBox.hotspotSightings)

    }

    companion object {
        const val TAG = "BottomSheetHotspot"

        private const val ARG_HEADING_TEXT = "arg_heading_text"
        fun newInstance(headingText: String, lat: Double, lng: Double): BottomSheetHotspot {
            val fragment = BottomSheetHotspot()
            val args = Bundle()
            args.putString(ARG_HEADING_TEXT, headingText)
            fragment.arguments = args
            return fragment
        }
    }

    fun setBottomSheetHeadingText(newText: String) {
        val textView = view?.findViewById<TextView>(R.id.tvBottomSheetHeading)
        textView?.text = newText
    }

    fun setButtonClickListener(listener: () -> Unit) {
        this.buttonClickListener = listener
    }

    @SuppressLint("InflateParams")
    fun displaySightingsInBottomSheet(
        bottomSheetView: LinearLayout,
        sightings: List<SightingModel>
    ) {

        Log.d("Display!!!!", "Display called")
        val inflater = LayoutInflater.from(bottomSheetView.context)
        var counter = 0

        if(sightings.isEmpty())
        {
            totalSpeciesTextView.text = "No Species were found here"
            informationText.isVisible = false
        }else
        {
            // Set number of species text
            totalSpeciesTextView.text = "${sightings.count()} species"
        }



        for (sighting in sightings) {
            counter++
            val hotspotSightingView = inflater.inflate(R.layout.hotspot_sighting, null)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 16, 0, 0)
            hotspotSightingView.layoutParams = layoutParams

            val commonNameTextView = hotspotSightingView.findViewById<TextView>(R.id.tvCommonName)
            val howManyTextView = hotspotSightingView.findViewById<TextView>(R.id.tvHowMany)
            val dateTextView = hotspotSightingView.findViewById<TextView>(R.id.tvDate)
            val commonNameText = "Common Name: "
            val italicCommonName = SpannableString(sighting.commonName)
            italicCommonName.setSpan(StyleSpan(Typeface.ITALIC), 0, italicCommonName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            val spannableCombined = SpannableStringBuilder().append(commonNameText).append(italicCommonName)
            val spanString = SpannableString(sighting.commonName)

            commonNameTextView.text =  spannableCombined
            howManyTextView.text = "How Many: ${sighting.howMany}"
            dateTextView.text = "Date: ${sighting.date}"

            bottomSheetView.addView(hotspotSightingView)

            val line = View(bottomSheetView.context)
            line.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                2
            )
            line.alpha = 0.5f
            line.setBackgroundColor(Color.BLACK)
            bottomSheetView.addView(line)
        }
    }

    fun updateHotspotSightings(sightings: List<SightingModel>) {
        val bottomSheetLayout = view?.findViewById<LinearLayout>(R.id.linearViewHotspotInformation)
        bottomSheetLayout?.let { displaySightingsInBottomSheet(it, sightings) }
    }
}
