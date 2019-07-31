package ru.skillbranch.devintensive.models

import androidx.core.text.isDigitsOnly

class Bender(var status:Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion():String = when(question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question

    }

    fun listenAnswer(answer:String): Pair<String, Triple<Int, Int, Int>>{

        val validationResult = question.validateAnswer(answer)

        return if(validationResult.isNullOrEmpty()) {
            if (question.answers.contains(answer.toLowerCase())) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                if (status == Status.CRITICAL) {
                    status = Status.NORMAL
                    question = Question.NAME
                    "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                } else {
                    status = status.nextStatus()
                    "Это неправильный ответ\n${question.question}" to status.color
                }
            }
        } else {
            "${validationResult}\n${question.question}" to status.color
        }
    }


    enum class Status(val color: Triple<Int, Int, Int>){
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus():Status{
            return if (this.ordinal < values().lastIndex)  {
                values()[this.ordinal + 1]
            }else{
                values()[0]
            }
        }

    }

    enum class Question(val question: String, val answers:List<String>){
        NAME("Как меня зовут?", listOf("бендер", "bender")){
            override fun validateAnswer(answer: String):String? {

                return if(answer[0].isUpperCase()){
                    null
                }else{
                    "Имя должно начинаться с заглавной буквы"
                }
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")){
            override fun validateAnswer(answer: String):String? {
                return if(answer[0].isLowerCase()) {
                    null
                }else{
                    "Профессия должна начинаться со строчной буквы"
                }
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")){
            override fun validateAnswer(answer: String):String? {
                return if(answer.none{ it.isDigit() }) {
                   null
                }else{
                    "Материал не должен содержать цифр"
                }
            }
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun validateAnswer(answer: String):String? {
                return if(answer.isDigitsOnly()){
                    null
                }else{
                    "Год моего рождения должен содержать только цифры"
                }
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun validateAnswer(answer: String):String? {
                return if(answer.length == 7 && answer.isDigitsOnly()){
                    null
                }else{
                   "Серийный номер содержит только цифры, и их 7"
                }
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun validateAnswer(answer: String):String? {
                return null
            }
        };

        fun nextQuestion():Question{
            return if (this.ordinal < values().lastIndex)  {
                values()[this.ordinal + 1]
            }else{
                IDLE
            }
        }

        abstract fun validateAnswer(answer: String):String?
    }

}