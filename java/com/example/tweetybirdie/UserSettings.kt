//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT



package com.example.tweetybirdie

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserSettings : Fragment() {

    private lateinit var btnMetric: Button
    private lateinit var btnImperial: Button
    private lateinit var btnDark: Button
    private lateinit var btnLight: Button
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvSliderText: TextView
    private lateinit var sliderDistance: RangeSlider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        tvUserName.text =
            "${ToolBox.users[0].Name} ${ToolBox.users[0].Surname}"

        tvUserEmail.text = "${currentUser?.email}"
        sliderDistance = view.findViewById(R.id.sliderDistance)
        tvSliderText = view.findViewById(R.id.tvMaxRadius)
        sliderDistance.setValues(ToolBox.users[0].MaxDistance.toFloat())
        sliderDistance.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))
        sliderDistance.tickTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        sliderDistance.trackActiveTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))
        sliderDistance.thumbTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tab_indicator))
        sliderDistance.addOnChangeListener { slider, value, fromUser ->
            var unit = "km"

            if (!ToolBox.users[0].isUnitKM) {
                unit = "mile"
            }

            val displayValue = "$value $unit"
            tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"
            ToolBox.users[0].MaxDistance = value.toDouble()

            updateUserSettings()
        }
        btnDark = view.findViewById(R.id.btnDark)
        btnLight = view.findViewById(R.id.btnLight)

        if (ToolBox.users[0].mapStyleIsDark) {
            toDark()
        } else {
            toLight()
        }

        btnDark.setOnClickListener {
            toDark()
        }

        btnLight.setOnClickListener {
            toLight()
        }

        btnMetric = view.findViewById(R.id.btnMetric)
        btnImperial = view.findViewById(R.id.btnImperial)

        if (ToolBox.users[0].isUnitKM == true) {
            ToMetric()
        } else {
            ToImperial()
        }

        btnImperial.setOnClickListener() {
            ToImperial()
        }

        btnMetric.setOnClickListener() {
            ToMetric()
        }

        return view
    }

    private fun ToMetric() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnMetric, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnImperial, unselectedColorStateList)

        ToolBox.users[0].isUnitKM = true

        var value = ToolBox.users[0].MaxDistance.toFloat()
        var unit = "km"
        val displayValue = "$value $unit"
        tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"

        updateUserSettings()
    }
    private fun ToImperial() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnImperial, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnMetric, unselectedColorStateList)

        ToolBox.users[0].isUnitKM = false

        var value = ToolBox.users[0].MaxDistance.toFloat()
        var unit = "mile"
        val displayValue = "$value $unit"
        tvSliderText.text = getString(R.string.MaxRadius) + "(" + displayValue + ")"

        updateUserSettings()

    }
    private fun toDark() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnDark, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnLight, unselectedColorStateList)

        ToolBox.users[0].mapStyleIsDark = true

        updateUserSettings()
    }

    private fun toLight() {
        val newSelectColor = ContextCompat.getColor(requireContext(), R.color.clickedMetric)
        val selectedColorStateList = ColorStateList.valueOf(newSelectColor)
        ViewCompat.setBackgroundTintList(btnLight, selectedColorStateList)

        val newUnselectedColor = ContextCompat.getColor(requireContext(), R.color.app_background)
        val unselectedColorStateList = ColorStateList.valueOf(newUnselectedColor)
        ViewCompat.setBackgroundTintList(btnDark, unselectedColorStateList)

        ToolBox.users[0].mapStyleIsDark = false

        updateUserSettings()
    }

    private fun updateUserSettings() {
        try {
            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")

            val user = ToolBox.users[0]

            val fbUser = Firebase.auth.currentUser

            if(fbUser != null) {
                usersCollection.document(fbUser.uid)
                    .update(
                        "isUnitKM", user.isUnitKM,
                        "mapStyleIsDark", user.mapStyleIsDark,
                        "maxDistance", user.MaxDistance
                    )
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { e ->

                    }
            }
        } catch (ex: Exception) {
            Log.e("log", "Error updating user settings in Firestore: ${ex.message}")
            ex.printStackTrace()
        }
    }
}