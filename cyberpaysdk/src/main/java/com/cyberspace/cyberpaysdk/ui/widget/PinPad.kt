package com.cyberspace.cyberpaysdk.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.utils.fonts.ExtraBold
import java.lang.Exception


internal class PinPad constructor(context: Context, attributeSet: AttributeSet?) : LinearLayout (context, attributeSet)
{
    /*
    customizable properties
     */
    var randomKeys : Boolean? = false
    var title : String? = null
    var desc : String? = null
    var pinLength : Int? = 0
    var obscured : Boolean? = false

    var validate : Boolean? = false
    var minLength : Int? = 0
    var maxLength : Int? = 0

    /*
    pin-pad keys
     */
    private  var one : AppCompatTextView
    private  var two : AppCompatTextView
    private  var three : AppCompatTextView
    private  var four : AppCompatTextView
    private  var five : AppCompatTextView
    private  var six : AppCompatTextView
    private  var seven : AppCompatTextView
    private  var eight : AppCompatTextView
    private  var nine : AppCompatTextView
    private  var zero : AppCompatTextView
    private  var delete : ImageView
    private  var pin : ExtraBold

    private  var mTitle : AppCompatTextView
    private  var  mDesc: AppCompatTextView

    private var keyArray = mutableListOf<Int>()
    private var btContinue : TextView

    private var attributes: TypedArray? = null

    interface ClickListener{
        fun onClick(key: Int)
    }

    interface Submitted {
        fun onSubmit(pin : String)
    }

    var onItemSelected : ClickListener? = null
    var onSubmitted : Submitted ? = null

    private fun isKeyAdded (k : Int) : Boolean {
       try {
           for (a in 0..9){
               if(keyArray[a] == k) return true
           }

           return false
       }catch (error : Exception){
           return false
       }
    }

    private fun disableSubmit(){
        btContinue.setBackgroundResource(R.drawable.disable_background)
        btContinue.isEnabled = false
    }

    private fun enableSubmit(){
        btContinue.setBackgroundResource(R.drawable.primary_button_background)
        btContinue.isEnabled = true
    }

    private fun generateKeys(){

        val random =  java.util.Random()
        var k = random.nextInt(10)

        for (i in 0..9){
            while (isKeyAdded(k))  k = random.nextInt(10)
            keyArray.add(i, k)
        }

    }

    init {

        //get the attributes specified in attrs.xml using the name we included
        attributes = context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.PinPad, 0, 0
        )

        View.inflate(context, R.layout.pinpad, this)

        one = findViewById(R.id.one)
        two = findViewById(R.id.two)
        three = findViewById(R.id.three)
        four = findViewById(R.id.four)
        five = findViewById(R.id.five)
        six = findViewById(R.id.six)
        seven = findViewById(R.id.seven)
        eight = findViewById(R.id.eight)
        nine = findViewById(R.id.nine)
        zero = findViewById(R.id.zero)
        delete = findViewById(R.id.delete)
        btContinue = findViewById(R.id.bt_continue)
        mTitle = findViewById(R.id.title)
        mDesc = findViewById(R.id.desc)
        pin = findViewById(R.id.pin)

        pin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if(minLength!! > 0) {
                    if (minLength!! <= s.toString().length ) enableSubmit()
                    else disableSubmit()
                }
                else enableSubmit()

                if(validate!!){
                    if (s.toString().length == pinLength!!) enableSubmit()
                    else disableSubmit()
                }
            }
        })

        btContinue.setOnClickListener {
            if(onSubmitted != null) onSubmitted?.onSubmit(pin.text.toString())
        }

        /*
        set click listeners
         */
        one.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[0].toString())
           if(onItemSelected != null) onItemSelected?.onClick(one.text.toString().toInt())
        }

        two.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[1].toString())
            if(onItemSelected != null) onItemSelected?.onClick(two.text.toString().toInt())
        }

        three.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[2].toString())
            if(onItemSelected != null) onItemSelected?.onClick(three.text.toString().toInt())
        }

        four.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[3].toString())
            if(onItemSelected != null) onItemSelected?.onClick(four.text.toString().toInt())
        }

        five.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[4].toString())
            if(onItemSelected != null) onItemSelected?.onClick(five.text.toString().toInt())
        }


        six.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[5].toString())
            if(onItemSelected != null) onItemSelected?.onClick(six.text.toString().toInt())
        }

        seven.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[6].toString())
            if(onItemSelected != null) onItemSelected?.onClick(seven.text.toString().toInt())
        }

        eight.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[7].toString())
            if(onItemSelected != null) onItemSelected?.onClick(eight.text.toString().toInt())
        }

        nine.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[8].toString())
            if(onItemSelected != null) onItemSelected?.onClick(nine.text.toString().toInt())
        }

        zero.setOnClickListener {
            pin.text = String.format("%s%s",pin.text.toString(), keyArray[9].toString())
            if(onItemSelected != null) onItemSelected?.onClick(zero.text.toString().toInt())
        }

        delete.setOnClickListener {
            if(pin.text.isNotEmpty()){
                pin.text = pin.text.toString().substring(0, pin.text.length-1)
            }
        }

        try{
            obscured = attributes?.getBoolean(R.styleable.PinPad_obscured, false)
            if(!obscured!!) pin.inputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
        }catch (error: Exception){}

        try {
            validate = attributes?.getBoolean(R.styleable.PinPad_validate,false)
            when(validate){
                true -> disableSubmit()
                else -> enableSubmit()
            }
        }catch (error: Exception){}

        try {
            minLength = attributes?.getInteger(R.styleable.PinPad_minLength,0)
            if(minLength!! > 0) disableSubmit()
        }catch (e : Exception){}

        try {
            title = attributes?.getString(R.styleable.PinPad_title)
            mTitle.text = title
        }
        catch (error : Exception){}

        try {
            desc = attributes?.getString(R.styleable.PinPad_desc)
            mDesc.text = desc
        }
        catch (error : Exception){}

        try {
            pinLength = attributes?.getInteger(R.styleable.PinPad_pinLength, 0)
            pin.filters = arrayOf(InputFilter.LengthFilter(pinLength!!))

        }catch (error : Exception){}

        try{
            randomKeys = attributes?.getBoolean(R.styleable.PinPad_randomiseKeys, false)

            if(randomKeys == true){

                generateKeys()
                one.text = keyArray[0].toString()
                two.text = keyArray[1].toString()
                three.text = keyArray[2].toString()
                four.text = keyArray[3].toString()
                five.text = keyArray[4].toString()
                six.text = keyArray[5].toString()
                seven.text = keyArray[6].toString()
                eight.text = keyArray[7].toString()
                nine.text = keyArray[8].toString()
                zero.text = keyArray[9].toString()

            }
            else keyArray = mutableListOf(1,2,3,4,5,6,7,8,9,0)

            //
        }catch (error : Exception){

        }




    }
}