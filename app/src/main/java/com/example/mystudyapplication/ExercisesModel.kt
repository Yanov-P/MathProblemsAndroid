package com.example.mystudyapplication

import java.lang.Exception
import kotlin.random.Random



enum class Operation(val sign: Char) {
    ADDITION('+'),
    SUBTRACTION('-'),
    DIVISION('/'),
    MULTIPLICATION('*')
}

class ExercisesModel(questionQuantity: Int) {

    val questionQuantity = questionQuantity
    var answers = Array(questionQuantity) {i->0}
    var currentAnswer : Int = 0
    var correctAnswers: Float = 0f
    var currentAnswerNum : Int = 0
    var allAnswered : Boolean = false
    var currentQuestion : String = ""
    var isHinted : Boolean = false
    var currentOperation : Operation = Operation.MULTIPLICATION
    var firstOperand : Int = 0
    var secondOperand : Int = 0

    fun restart(){
        answers = Array(questionQuantity) {i->0}
        currentAnswer = 0
        correctAnswers = 0f

        currentAnswerNum = 0
        allAnswered = false
        currentQuestion = ""
        isHinted = false
    }

    fun getHint(): Int{
        isHinted = true
        var s = 0
        s = try {
             currentAnswer.toString().take(1).toInt()
        }
        catch (e:Exception){
            currentAnswer.toString().take(2).takeLast(1).toInt()
        }
        return s
    }

    fun generateQuestion(){
        isHinted = false

        currentOperation = Operation.values()[Random.nextInt(Operation.values().size)]
        firstOperand = Random.nextInt(-100,100)
        secondOperand = Random.nextInt(-100,100)

        if(currentOperation == Operation.DIVISION){
            while (firstOperand % secondOperand != 0 && secondOperand != 0){
                firstOperand = Random.nextInt(-100,100)
                secondOperand = Random.nextInt(-100,100)
            }
        }
        if(currentOperation == Operation.MULTIPLICATION){
            while (Math.abs(firstOperand * secondOperand) > 150){
                firstOperand = Random.nextInt(-100,100)
                secondOperand = Random.nextInt(-100,100)
            }
        }


        currentAnswer = when (currentOperation) {
            Operation.ADDITION -> firstOperand + secondOperand
            Operation.SUBTRACTION -> firstOperand - secondOperand
            Operation.DIVISION -> firstOperand / secondOperand
            Operation.MULTIPLICATION -> firstOperand * secondOperand
        }

    }

    fun processQuestion(ans: Int) : Boolean{
        if (ans == currentAnswer){

            if(isHinted){
                answers[currentAnswerNum++] = 3
                correctAnswers += 0.5f
            }
            else{
                answers[currentAnswerNum++] = 1
                correctAnswers++
            }
            checkForEnd()
            return true
        }
        else{
            answers[currentAnswerNum++] = 2
            checkForEnd()
            return false
        }
    }

    fun checkForEnd(){
        if(currentAnswerNum >= questionQuantity) allAnswered = true
    }
}