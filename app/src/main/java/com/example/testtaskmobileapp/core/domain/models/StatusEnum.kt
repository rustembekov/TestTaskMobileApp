package com.example.testtaskmobileapp.core.domain.models

import com.example.testtaskmobileapp.R

enum class StatusEnum(val messageResId: Int) {
    REGISTERED(R.string.successfully_registered),
    LOGIN(R.string.successfully_logged_in),
    FAILURE_LOGIN(R.string.otp_code_incorrect),
    FAILURE_REGISTER(R.string.invalid_register)
}
