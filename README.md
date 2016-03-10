# Question Pool #

This is an application for students who want to test their knowledge on question with provided answers and not provided. Users have the option to shuffle answers and , if questions have provided answers, it shuffles answers too.
The entire application is written in Java programming language (Gradle project). 

### Requirements ###

* Linux/Unix or Windows operating system
* [Java Runtime Environment](https://java.com/en/download/) (preferable version 8+)

### How to use it? ###

* download [QuestionPool.jar](https://bitbucket.org/mightymatth8/questionpooljava/downloads/QuestionPool.jar) and [pitanjaTest.txt](https://bitbucket.org/mightymatth8/questionpooljava/downloads/pitanjaTest.txt) from [Download section](https://bitbucket.org/mightymatth8/questionpooljava/downloads)
* open QuestionPool.jar

![qpStart.png](https://bitbucket.org/repo/7895Re/images/2413767385-qpStart.png)

* click "Choose File" and open file named "pitanjaTest.txt"
* after selecting file click "Start"

![myimage.gif](https://bitbucket.org/repo/7895Re/images/1216042890-myimage.gif)

* this is a sample of question with provided answers

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

* or just look at the [pitanjaTest.txt](https://bitbucket.org/mightymatth8/questionpooljava/downloads/pitanjaTest.txt) and you will understand rules with these examples (all types of questions and answers are covered)

### Locale and file encoding explanation ###

I added feature that converts file encoding if it's different than your default encoding on your system. For example, if you are using Windows OS (e.g. encoding windows_1250) and you are reading a file that somebody made on Linux (e.g. encoding utf-8), you will have a conflict that will cause your locale letters break. 
To fix that, we need to guess encoding of certain file, because we cannot be 100% sure that some file is encoded in, e.g. utf-8. This program uses [ICU4J](http://icu-project.org/apiref/icu4j/) to guess encoding, but this will not always fix an issue. Guessing will only be good method if your file is long enough to detect file encoding.
If you write your file on English, you will not have any problems because you won't have special locale characters.

### Want to make a new feature? ###

* just clone it to your local repository and make a pull request