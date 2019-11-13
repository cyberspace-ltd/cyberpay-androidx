package com.cyberspace.cyberpaysdk.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.cyberspace.cyberpaysdk.R
import java.lang.Exception


internal class PinPad constructor(context: Context, attributeSet: AttributeSet?) : LinearLayout (context, attributeSet)
{
    /*
    customizable properties
     */
    var randomKeys : Boolean? = false
    var title : String? = null
    var desc : String ? = null
    var pinLength : Int? = 0

    /*
    pin-pad keys
     */
    private lateinit var one : AppCompatTextView
    private lateinit var two : AppCompatTextView
    private lateinit var three : AppCompatTextView
    private lateinit var four : AppCompatTextView
    private lateinit var five : AppCompatTextView
    private lateinit var six : AppCompatTextView
    private lateinit var seven : AppCompatTextView
    private lateinit var eight : AppCompatTextView
    private lateinit var nine : AppCompatTextView
    private lateinit var zero : AppCompatTextView
    private lateinit var delete : ImageView
    private lateinit var pin : TextView

    private lateinit var mTitle : AppCompatTextView
    private lateinit var  mDesc: AppCompatTextView

    private var keyArray = mutableListOf<Int>()

    private lateinit var btContinue : View

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

        btContinue.setOnClickListener {
            if(onSubmitted != null) onSubmitted?.onSubmit(pin.text.toString())
        }

        /*
        set click listeners
         */
        one.setOnClickListener {
           if(onItemSelected != null) onItemSelected?.onClick(one.text.toString().toInt())
        }

        two.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(two.text.toString().toInt())
        }

        three.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(three.text.toString().toInt())
        }

        four.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(four.text.toString().toInt())
        }

        five.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(five.text.toString().toInt())
        }


        six.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(six.text.toString().toInt())
        }

        seven.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(seven.text.toString().toInt())
        }

        eight.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(eight.text.toString().toInt())
        }

        nine.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(nine.text.toString().toInt())
        }

        zero.setOnClickListener {
            if(onItemSelected != null) onItemSelected?.onClick(zero.text.toString().toInt())
        }




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
            /*

            val filterArray = arrayOfNulls<InputFilter>(1)
            filterArray [0] = InputFilter.LengthFilter(4)
            pin?.filters = filterArray

             */

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

            //
        }catch (error : Exception){

        }




    }
}