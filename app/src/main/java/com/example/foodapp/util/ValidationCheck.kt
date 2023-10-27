package com.example.airmovies.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email can't be empty")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if(password.isEmpty())
        return RegisterValidation.Failed("Password can't be empty")

    if(password.length < 6)
        return RegisterValidation.Failed("Password should contain 6 char.")

    return RegisterValidation.Success
}

fun validateUser(username: String): RegisterValidation {
    if (username.isEmpty())
        return RegisterValidation.Failed("Username can't be empty")

    if (username.length < 3)
        return RegisterValidation.Failed("Username must have 3 characters")

    return RegisterValidation.Success
}

fun validateWeight(weight: String): RegisterValidation {
    if (weight.isEmpty())
        return RegisterValidation.Failed("Can't be empty")
    if (containsNonNumericCharacters(weight))
        return RegisterValidation.Failed("Non numeric char")
    return RegisterValidation.Success
}

fun validateHight(hight: String): RegisterValidation {
    if (hight.isEmpty())
        return RegisterValidation.Failed("Can't be empty")
    if (containsNonNumericCharacters(hight))
        return RegisterValidation.Failed("Non numeric char")
    return RegisterValidation.Success
}

private fun containsNonNumericCharacters(input: String): Boolean {
    val regex = Regex("[^0-9]") // Match any character that is NOT a digit
    return regex.containsMatchIn(input)
}