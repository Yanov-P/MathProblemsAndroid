package com.example.mystudyapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.StreamTokenizer
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import kotlinx.android.synthetic.main.marker.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var model : ExercisesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ExercisesModel(5)
        drawQuestion()
        redrawMarkers()

//        editText.setOnEditorActionListener{
//            try{
//                val t = editText.text.toString().toInt()
//                this.model.processQuestion(t)
//                drawQuestion()
//                redrawMarkers()
//            }catch (e:Exception){
//                val toast = Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT)
//                toast.show()
//            }
//
//            if(model.allAnswered){
//                showEndDialog()
//            }
//            return@setOnEditorActionListener true
//        }

        send_button.setOnClickListener{

            try{
                val t = editText.text.toString().toInt()
                this.model.processQuestion(t)
                drawQuestion()
                redrawMarkers()
            }catch (e:Exception){
                val toast = Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT)
                toast.show()
            }
            if(model.allAnswered){
//                model.restart()
                showEndDialog()
            }
            editText.text.clear()
        }

        hint_button.setOnClickListener{
            val toast = Toast.makeText(this,getString(R.string.hint) +
                     " " + model.getHint().toString(),Toast.LENGTH_SHORT)
            toast.show()
        }


    }

    override fun onResume() {
        super.onResume()
        model.restart()
        drawQuestion()
        redrawMarkers()
    }

    fun showEndDialog(){

        val intent = Intent(this@MainActivity,ResultActivity::class.java)
        intent.putExtra("Message",getString(R.string.end_dialog_text) + " " + model.correctAnswers.toString())
        startActivity(intent)

//        val builder = AlertDialog.Builder(this)
//        builder.setTitle(R.string.end_dialog_title)
//        builder.setMessage(getString(R.string.end_dialog_text) + " " + model.correctAnswers.toString())
//
//        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
//            model.restart()
//            redrawMarkers()
//        }
//
//        builder.setOnDismissListener { _ ->
//            model.restart()
//            redrawMarkers()
//        }
//
//        builder.show()
    }

    fun redrawMarkers(){
        var s = ""
        for (inems: Int in model.answers){
            s += inems.toString() + " "
        }
        Log.d("my", "going to draw " + s)

        for (i: Int in 0 until model.answers.size){
            val m = markers_container.getChildAt(i)
            val col = when(model.answers[i]){
                1 -> R.color.correct_answered_marker
                2 -> R.color.incorrect_answered_marker
                3 -> R.color.hinted_marker
                else -> R.color.neutral_marker
            }
            val colorValue = ContextCompat.getColor(this, col)
            m.setBackgroundColor(colorValue)
        }
//        Log.d("my","markers_container.childCount" + markers_container.childCount)
//
//        markers_container.removeAllViews()
//        var s = ""
//        for (inems: Int in model.answers){
//            s += inems.toString() + " "
//        }
//        Log.d("my", "going to draw " + s)
//        for (item: Int in model.answers){
//            Log.d("my", "ans " + item.toString())
//            val vi =
//                applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val m: View = vi.inflate(R.layout.marker, markers_container)
////            markers_container.orientation = LinearLayout.VERTICAL
////            markers_container.addView(m)
//            val col = when(item){
//                1 -> R.color.correct_answered_marker
//                2 -> R.color.incorrect_answered_marker
//                else -> R.color.neutral_marker
//            }
//            val colorValue = ContextCompat.getColor(this, col)
//            m.square.setBackgroundColor(colorValue)
//        }
//        Log.d("my","markers_container.childCount" + markers_container.childCount)

    }

    fun drawQuestion(){
        model.generateQuestion()
        var question = "${model.firstOperand} ${model.currentOperation.sign} ${model.secondOperand} = ?"
        if(model.secondOperand < 0){
            question = "${model.firstOperand} ${model.currentOperation.sign} (${model.secondOperand}) = ?"
        }
        question_text.text = question
    }

}
