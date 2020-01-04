package com.cyberspace.cyberpaysdk.ui.dob

import android.app.DatePickerDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.model.Transaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dob.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

internal class DobFragment (var desc: String, var listener: OnSubmitListener): BottomSheetDialogFragment() {

    private lateinit var dob: EditText
    private lateinit var btContinue: TextView
    private lateinit var picker: DatePickerDialog
    private lateinit var description: TextView

    var date = ""

    fun toDate(s: String, f: String) : String? {
        return try {
            val sdf = SimpleDateFormat(f, Locale.getDefault())
            val netDate = Date(s.toLong())
            sdf.format(netDate)
        }
        catch (error : Exception){
            error.toString()
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        //Set the custom view
        val view = LayoutInflater.from(context).inflate(R.layout.dob, null)
        dialog.setContentView(view)

        dialog.setOnShowListener {
            // For AndroidX use: com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        dob = view.findViewById(R.id.dob)
        description = view.findViewById(R.id.desc)
        btContinue = view.findViewById(R.id.bt_continue)

        description.text = desc

        // dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        this.isCancelable = false

        btContinue.isEnabled = false
        btContinue.alpha = 0.5f

        btContinue.setOnClickListener {
            listener.onSubmit(dob.text.toString().replace("/",""))
            println(dob.text.toString().replace("/",""))
            dialog.dismiss()
        }

        dob.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            cldr.add(Calendar.YEAR, -1)

           date =  String.format("%02d%02d%02d",cldr.get(Calendar.DAY_OF_MONTH),
               cldr.get(Calendar.MONTH) + 1 ,cldr.get(Calendar.YEAR))

            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)


            picker = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                   // val calendar: Calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
                   // dob.setText(toDate(calendar.time.time.toString(),"E, MMM d"))
                    dob.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year))
                    btContinue.isEnabled = true
                    btContinue.alpha = 1f

                },
                year,
                month,
                day
            )

            picker.datePicker.maxDate = cldr.time.time
            picker.show()
        }

    }

}