package com.example.exercises

import java.io.File

/*
Уровень 9. Телефонная книга. Написать функцию printUsers(users: List<User>)
которая выводит список пользователей вот так:
A
Алексей Пивоваров 89992223311
Альберт Эйнштейн 89992223311
Д
Дмитрий Сосков  89992223311
...
Пример класса User для задачи
data class User(val id: Int, val firstName: String, val lastName: String, val phone: String)
Если в букве нет пользователей, ее не выводить.
Для задачи желательно использовать методы коллекций.*/

/*val firstNames = File("C:\\Users\\parfe\\Downloads\\names.txt").readText()
    .split(", ")
    .map { it.trim('"') }
val lastNames = File("C:\\Users\\parfe\\Downloads\\surnames.txt").readText()
    .split(", ")
    .map { it.trim('"') }
val usersPhoneBook = getRandomPhoneArray()



fun main() {
    printUsers(usersPhoneBook)
}

fun getRandomFirstNameFromTxt(firstNames: List<String>): String {
    return firstNames[(0..firstNames.size).random()]
}
fun getRandomLastNameFromTxt(lastNames: List<String>): String {
    return lastNames[(0..lastNames.size).random()]
}
fun getRandomNumber(): String {
    var phoneNumber = "89"
    for(i in 1..9){
        phoneNumber += (0..9).random()
    }
    return phoneNumber
}
fun getRandomPhoneArray(): ArrayList<PhoneBookUser> {
    val usersPhoneBook: ArrayList<PhoneBookUser> = arrayListOf()
    for (i in 0..9) {     // тут задаем количесвто людей в книге
        usersPhoneBook.add(PhoneBookUser(getRandomFirstNameFromTxt(firstNames),
            getRandomLastNameFromTxt(lastNames), getRandomNumber()))
    }
    return usersPhoneBook
}
fun printUsers(usersPhoneBook: ArrayList<PhoneBookUser> ) {
    usersPhoneBook.sortWith(
        compareBy(
            { it.firstName },
            { it.lastName },
            { it.phone }
        )
    )

    usersPhoneBook.forEachIndexed {
            index, it ->
        if (index == 0) println(it.firstName[0])
        else if (it.firstName[0] != usersPhoneBook[index - 1].firstName[0]) println(it.firstName[0])

        println(it.firstName + " " + it.lastName + " " + it.phone)
    }
}*/

/*for (i in 0 until phoneBook.size) {
    if (i != 0 && phoneBook[i].name[0] == phoneBook[i - 1].name[0]) {
        println(phoneBook[i].name + " " + phoneBook[i].surname + " " + phoneBook[i].phone)
    } else {
        println(phoneBook[i].name[0])
        println(phoneBook[i].name + " " + phoneBook[i].surname + " " + phoneBook[i].phone)
    }
}*/



