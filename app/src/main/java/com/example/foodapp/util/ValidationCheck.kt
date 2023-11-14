package com.example.airmovies.util

import android.util.Patterns
import com.example.foodapp.util.Validation

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

fun validateTitle(title: String): Validation {
    if (title.isEmpty()) {
        return Validation.Failed("Title is required!")
    }
    return Validation.Success
}

fun validateAmount(amount: Int): Validation {
    if (amount == null) {
        return Validation.Failed("Amount is required!")
    }
    return Validation.Success
}

fun validateCalories(calories: Int): Validation {
    if (calories == null) {
        return Validation.Failed("Calories are required!")
    }
    return Validation.Success
}

fun validateCarbs(carbs: Double): Validation {
    if (carbs == null) {
        return Validation.Failed("Carbs are required!")
    }
    return Validation.Success
}

fun validateFat(fat: Double): Validation {
    if (fat == null) {
        return Validation.Failed("Fat is required!")
    }
    return Validation.Success
}

fun validateProtein(protein: Double): Validation {
    if (protein == null) {
        return Validation.Failed("Protein is required!")
    }
    return Validation.Success
}

fun validateType(type: Int): Validation {
    if (type == null || type == 0) {
        return Validation.Failed("Something is wrong with the type!")
    }
    return Validation.Success
}