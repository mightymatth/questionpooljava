# Question Pool #

This is an application for students who want to test their knowledge on question with provided answers and not provided. Users have the option to shuffle answers and , if questions have provided answers, it shuffles answers too.
The entire application is written in Java programming language (Gradle project). 

### Requirements ###

* Linux/Unix or Windows operating system
* [Java Runtime Environment](https://java.com/en/download/) (preferable version 8+)

### How to use it? ###

* download `QuestionPool.jar` from [the latest release](https://github.com/mightymatth/questionpooljava/releases) and [questionsTest.txt](/resources/questionTest.txt)
* open QuestionPool.jar with `java -jar QuestionPool.jar`

![qp_screen1.png](/resources/qp_screen1.png)

* click "Choose File" and open file named "questionsTest.txt"
* after selecting file click "Start"

![qp_screen2.gif](/resources/qp_screen2.gif)

* this is a sample of question with provided answers

### Navigation ###

You have many ways to navigate through the questions:

* **Forward**: right arrow, down arrow, space, page down, enter and mouse click on the button "Forward"

* **Backward**: left arrow, up arrow, backspace, page up and mouse click on the button "Back"

### Do you want to write your own questions? ###

Write your own questions by following these rules:

* every section can contain multiple lines

* sections must be in proper order

* sections must be divided by **one** empty line

**Question Section** -> Begins with character **'#'** (if question has provided answers)
or **'$'** (if question has not provided answers)

**Answer Section** -> *if questions has provided answers*: every line is one answer.
if answer is correct, line must begin with character **'.'** .
*if questions has not provided answers*: answer can contain multiple lines.

**Comment Section** -> comments on answers (can contain multiple lines too)

* or just look at the [questionsTest.txt](https://bitbucket.org/mightymatth8/questionpooljava/downloads/questionsTest.txt) and you will understand rules with these examples (all types of questions and answers are covered)

### Locale and file encoding explanation ###

I added feature that converts file encoding if it's different than your default encoding on your system. For example, if you are using Windows OS (e.g. encoding windows_1250) and you are reading a file that somebody made on Linux (e.g. encoding utf-8), you will have a conflict that will cause your locale letters break. 
To fix that, we need to guess encoding of certain file, because we cannot be 100% sure that some file is encoded in, e.g. utf-8. This program uses [ICU4J](http://site.icu-project.org/home) to guess encoding, but this will not always fix an issue. Guessing will only be good method if your file is long enough to detect file encoding.
If you write your file on English, you will not have any problems because you won't have special locale characters.
