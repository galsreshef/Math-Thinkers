package com.thegalos.maththinkers;

import android.util.Log;

import com.thegalos.maththinkers.objects.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {
        public static Question sumComplexQuestion() {
                Question question = new Question();
                Random rd = new Random();
                List<Integer> wrong = new ArrayList<>();
                int num1 = rd.nextInt(25)+20;
                int num2 = rd.nextInt(15)+10;
                int num3 = rd.nextInt(10)+5;
                int incorrectAnswer;
                for (int i = 0; i < 3; i++) {
                        incorrectAnswer = rd.nextInt(50) + 35;
                        while(incorrectAnswer == num1+num2+num3) {
                                incorrectAnswer = rd.nextInt(50) + 35;
                        }
                        wrong.add(incorrectAnswer);
                }
                question.setQuestion(num1 + " + " + num2 + " + " + num3);
                question.setCorrectAnswer(num1+num2+num3);
                question.setWrongAnswer(wrong);

                Log.d("questions" , "sumComplexQuestion question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                        + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

                return question;
        }
        public static Question subComplexQuestions() {
                Question question = new Question();
                List<Integer> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((50-25)+1)+25;
                int num2 = rd.nextInt((15-10)+1)+10;
                int num3 = rd.nextInt((10-5)+1)+5;
                int incorrectAnswer;
                for(int i = 0; i < 3; i++) {
                        incorrectAnswer = rd.nextInt(25)+10;
                        while(incorrectAnswer == num1-num2-num3) {
                                incorrectAnswer = rd.nextInt(25)+10;
                        }
                        wrong.add(incorrectAnswer);
                }
                question.setQuestion(num1 + " - " + num2 + " - " + num3);
                question.setCorrectAnswer(num1-num2-num3);
                question.setWrongAnswer(wrong);
                Log.d("questions" , "subComplexQuestions question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                        + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

                return question;
        }
        public static Question mulSubQuestion() {
                Question question = new Question();
                List<Integer> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((50-25)+1)+25;
                int num2 = rd.nextInt((15-10)+1)+10;
                int num3 = rd.nextInt((5-1)+1)+1;
                question.setQuestion("(" + num1 + "-" + num2 + ") x " + num3);
                int incorrectAnswer;
                for(int i = 0; i < 3; i++) {
                        incorrectAnswer = rd.nextInt((80-20)+1)+20;
                        while(incorrectAnswer == (num1-num2)*num3) {
                                incorrectAnswer = rd.nextInt((80-20)+1)+20;
                        }
                        wrong.add(incorrectAnswer);
                }
                question.setWrongAnswer(wrong);
                question.setQuestion("(" + num1 + "-" + num2 + ") x " + num3);
                question.setCorrectAnswer((num1-num2)*num3);
                Log.d("questions" , "mulSubQuestion question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                        + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

                return question;
        }
        public static Question mulAddQuestion() {
                Question question = new Question();
                List<Integer> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((25-20)+1)+20;
                int num2 = rd.nextInt((15-10)+1)+10;
                int num3 = rd.nextInt((5-1)+1)+1;
                int incorrectAnswer;
                for(int i = 0; i < 3; i++) {
                        incorrectAnswer = rd.nextInt((150-80)+1)+80;
                        while(incorrectAnswer == (num1+num2)*num3) {
                                incorrectAnswer = rd.nextInt((150-80)+1)+80;
                        }
                        wrong.add(incorrectAnswer);
                }
                question.setQuestion("(" + num1 + "+" + num2 + ") x " + num3);
                question.setCorrectAnswer((num1+num2)*num3);
                question.setWrongAnswer(wrong);

                Log.d("questions" , "mulAddQuestion question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                        + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

                return question;
        }
        public static Question divSubQuestion() {
                Question question = new Question();
                List<Integer> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((50-25)+1)+25;
                int num2 = rd.nextInt((15-10)+1)+10;
                int num3 = rd.nextInt((10-1)+1)+1;

                while((num1-num2) % num3 != 0) {
                        num1 = rd.nextInt((50-25)+1)+25;
                        num2 = rd.nextInt((15-10)+1)+10;
                        num3 = rd.nextInt((10-1)+1)+1;
                }
                int incorrectAnswer;
                for(int i = 0; i < 3; i++) {
                        incorrectAnswer = rd.nextInt((40-15)+1)+15;
                        while(incorrectAnswer == (num1-num2)/num3) {
                                incorrectAnswer = rd.nextInt((40-15)+1)+15;
                        }
                        wrong.add(incorrectAnswer);
                }
                question.setWrongAnswer(wrong);
                question.setQuestion("(" + num1 + "-" + num2 + ") / " + num3);
                question.setCorrectAnswer((num1-num2)/num3);
                Log.d("questions" , "divSubQuestion question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                        + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

                return question;
        }
        public static Question divAddQuestion() {
                Question question = new Question();
                List<Integer> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((25-20)+1)+20;
                int num2 = rd.nextInt((15-10)+1)+10;
                int num3 = rd.nextInt((10-1)+1)+1;

                //Checks for remainder.
                while((num1+num2) % num3 != 0) {
                        num1 = rd.nextInt((50-25)+1)+25;
                        num2 = rd.nextInt((15-10)+1)+10;
                        num3 = rd.nextInt((10-1)+1)+1;
                }
                int incorrectAnswer;

                for(int i = 0; i < 3; i++) {
                        incorrectAnswer = rd.nextInt((40-10)+1)+10;
                        while(incorrectAnswer == (num1+num2)/num3) {
                                incorrectAnswer = rd.nextInt((40-10)+1)+10;
                        }
                        wrong.add(incorrectAnswer);
                }
                question.setWrongAnswer(wrong);
                question.setQuestion("(" + num1 + "+" + num2 + ") / " + num3);
                question.setCorrectAnswer((num1+num2)/num3);

                Log.d("questions" , "divAddQuestion question is: " + question.getQuestion() + "\ncorrect is: " + question.getCorrectAnswer() + "\nwrong 0: " + question.getWrongAnswer().get(0)
                        + "\nwrong 1: " + question.getWrongAnswer().get(1) + "\nwrong 2: " + question.getWrongAnswer().get(2));

                return question;
        }



        ///////////////////////////////////Quick/////////////////////////////////////////////////////
//      generate question object that hold 1 true/false question

        public static Question sumQuestion() {
                Question question = new Question();
                Random rd = new Random();
                int num1 = rd.nextInt((25 - 10) + 1) + 10;
                int num2 = rd.nextInt((25 - 10) + 1) + 10;
//                int incorrectAnswer = rd.nextInt((50-12)+1)+12;
//                while(incorrectAnswer == num1+num2) {
//                        incorrectAnswer = rd.nextInt((50-12)+1)+12;
//                }
                question.setQuestion(num1 + " + " + num2);
//                question.setWrongAnswer(Collections.singletonList(incorrectAnswer));

                //////////////////////////////////////// correct incorrect question
                int num3 = rd.nextInt(10) - 5;
                while (num3 == 0 ) {
                        num3 = rd.nextInt(10) - 5;
                }
                question.setCorrectQuestion(num1 + " + " + num2 + " = " + (num1+num2));
                question.setWrongQuestion(num1 + " + " + num2 + " = " + (num1+num2+num3));

                return question;
        }

        public static Question subQuestion() {
                Question question = new Question();
                Random rd = new Random();
                int num1 = rd.nextInt(25) + 1;
                int num2 = rd.nextInt(10) + num1;

//                int incorrectAnswer = rd.nextInt(20)+1;
//                while(incorrectAnswer == num2-num1) {
//                        incorrectAnswer = rd.nextInt(20)+1;
//                }
                question.setQuestion(num2 + " - " + num1);
//                question.setWrongAnswer(Collections.singletonList(incorrectAnswer));

                //////////////////////////////////////// correct incorrect question
                int num3 = rd.nextInt(10) - 5;
                while (num3 == 0 ) {
                        num3 = rd.nextInt(10) - 5;
                }
                question.setCorrectQuestion(num2 + " - " + num1 + " = " + (num2-num1));
                question.setWrongQuestion(num2 + " - " + num1 + " = " + (num2-num1+num3));

                return question;
        }

        public static Question mulQuestion() {
                Question question = new Question();
                Random rd = new Random();
                List<Integer> wrong = new ArrayList<>();
                int num1 = rd.nextInt((12 - 1) + 1) + 1;
                int num2 = rd.nextInt((12 - 1) + 1) + 1;
                int num3 = rd.nextInt(10) - 5;
                while (num3 == 0 ) {
                        num3 = rd.nextInt(10) - 5;
                }
                int temp1 = num3;
                int temp2 = num3;

                while (num3 == temp1 || temp1 == 0) {
                        temp1 = rd.nextInt(10) - 5;
                }
                while (num3 == temp2 || temp1 == temp2 || temp2 == 0) {
                        temp2 = rd.nextInt(10) - 5;
                }
                wrong.add(num1*num2+num3);
                wrong.add(num1*num2+temp1);
                wrong.add(num1*num2+temp2);

                //////////////////////////////////////// correct incorrect question

                question.setQuestion(num1 + " x " + num2);
                question.setCorrectAnswer(num1*num2);
                question.setWrongAnswer(wrong);

                question.setCorrectQuestion(num1 + " x " + num2 + " = " + (num1*num2));
                question.setWrongQuestion(num1 + " x " + num2 + " = " + ((num1*num2)+num3));

                return question;
        }

        public static Question divQuestion() {
                Question question = new Question();
                List<Integer> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((25 - 10) + 1) + 10;
                int num2 = rd.nextInt((25 - 10) + 1) + 10;
                while(num2 % num1 != 0) {
                        num1 = rd.nextInt(10)+1;
                        num2 = rd.nextInt(10)+num1;
                }
                int num3 = rd.nextInt(10) - 5;
                while (num3 == 0 ) {
                        num3 = rd.nextInt(10) - 5;
                }

                int temp1 = num3;
                int temp2 = num3;
                while (num3 == temp1 || temp1 == 0) {
                        temp1 = rd.nextInt(10) - 5;
                }
                while (num3 == temp2 || temp1 == temp2 || temp2 == 0) {
                        temp2 = rd.nextInt(10) - 5;
                }
                wrong.add((num2/num1)+num3);
                wrong.add((num2/num1)+temp1);
                wrong.add((num2/num1)+temp2);

                question.setQuestion(num2 + " / " + num1);
                question.setCorrectAnswer(num2 / num1);
                question.setWrongAnswer(wrong);



                //////////////////////////////////////// correct incorrect question
//               rd.nextInt(10) - 5;
//                while (num3 == 0 ) {
//                        num3 = rd.nextInt(10) - 5;
//                }
                question.setCorrectQuestion(num2 + " / " + num1 + " = " + (num2/num1));
                question.setWrongQuestion(num2 + " / " + num1 + " = " + ((num2/num1)+num3));
                return question;
        }


        ///////////////////////////////////TimeAttack/////////////////////////////////////////////////////
//      generate question object that hold 1 correct and 3 wrong questions
        public static Question sumListQuestion() {
                Question question = new Question();
                List<String> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((15-4)+1)+4;
                int num2 = rd.nextInt((15-4)+1)+4;
                question.setCorrectQuestion(num1 + " + " + num2 + " = " + (num1+num2));
                for (int i = 0; i < 3; i++) {
                        num1 = rd.nextInt(15)+1;
                        num2 = rd.nextInt(12)+1;
                        int num3 = rd.nextInt(10) - 5;
                        while (num3 == 0 ) {
                                num3 = rd.nextInt(10) - 5;
                        }
                        wrong.add(num1 + " + " + num2 + " = " + (num1+num2+num3));
//                        while(num3 == num1+num2) {
//                                num3 = rd.nextInt(40)+1;
//                        }
//                        wrong.add(num1 + " + " + num2 + " = "+ num3);
                }
                question.setWrongQuestions(wrong);
                Log.d("questions" , "question object:"
                        + "\n Correct is: " + question.getCorrectQuestion() + "\n Wrong1 is: " + question.getWrongQuestions().get(0)
                        + "\n Wrong1 is: " + question.getWrongQuestions().get(1)+ "\n Wrong2 is: " + question.getWrongQuestions().get(2));

                return question;
        }
        public static Question subListQuestion() {
                Question question = new Question();
                List<String> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((15-4)+1)+4;
                int num2 = rd.nextInt((15-num1)+1)+num1;
                question.setCorrectQuestion(num2 + " - " + num1 + " = " + (num2-num1));
                for (int i = 0; i < 3; i++) {
                        num1 = rd.nextInt(15)+1;
                        num2 = rd.nextInt((10-5)+1)+5;
                        int num3 = rd.nextInt(15)+1;
                        while(num3 == num1-num2) {
                                num3 = rd.nextInt(15)+1;
                        }
                        wrong.add(num1 + " - " + num2 + " = " + num3);
                }
                question.setWrongQuestions(wrong);

                Log.d("questions" , "question object:"
                        + "\n Correct is: " + question.getCorrectQuestion() + "\n Wrong1 is: " + question.getWrongQuestions().get(0)
                        + "\n Wrong1 is: " + question.getWrongQuestions().get(1)+ "\n Wrong2 is: " + question.getWrongQuestions().get(2));

                return question;
        }
        public static Question mulListQuestion() {
                Question question = new Question();
                List<String> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt((12-1)+1)+1;
                int num2 = rd.nextInt((12-1)+1)+1;
                question.setCorrectQuestion(num1 + " x " + num2 + " = " + (num1*num2));
                num1 = rd.nextInt(12)+1;
                num2 = rd.nextInt(12)+1;
                int num3 = rd.nextInt(50)+1;
                for (int i = 0; i < 3; i++) {
                        while (num3 == num1 * num2) {
                                num3 = rd.nextInt(50) + 1;
                        }
                        wrong.add(num1 + " x " + num2 + " = " + num3);
                }

                question.setWrongQuestions(wrong);
                Log.d("questions" , "question object:"
                        + "\n Correct is: " + question.getCorrectQuestion() + "\n Wrong1 is: " + question.getWrongQuestions().get(0)
                        + "\n Wrong1 is: " + question.getWrongQuestions().get(1)+ "\n Wrong2 is: " + question.getWrongQuestions().get(2));

                return question;
        }
        public static Question divListQuestion() {
                Question question = new Question();
                List<String> wrong = new ArrayList<>();
                Random rd = new Random();
                int num1 = rd.nextInt(10)+1;
                int num2 = rd.nextInt(50)+1;
                while(num2 % num1 != 0) {
                        num1 = rd.nextInt(10)+1;
                        num2 = rd.nextInt(50)+1;
                }
                question.setCorrectQuestion(num2 + " / " + num1 + " = " + (num2/num1));
                for (int i = 0; i < 3; i++) {
                        num1 = rd.nextInt(25)+1;
                        num2 = rd.nextInt(12)+1;
                        int num3 = rd.nextInt(12)+1;
                        while(num3 == num1/num2) {
                                num3 = rd.nextInt(12)+1;
                        }
                        wrong.add(num1 + " / " + num2 + " = " + num3);

                }
                question.setWrongQuestions(wrong);
                Log.d("questions" , "question object:"
                        + "\n Correct is: " + question.getCorrectQuestion() + "\n Wrong1 is: " + question.getWrongQuestions().get(0)
                        + "\n Wrong1 is: " + question.getWrongQuestions().get(1)+ "\n Wrong2 is: " + question.getWrongQuestions().get(2));

                return question;
        }


}