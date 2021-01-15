package com.kristantosean.catfactsapp.utils

import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.*

class DateFormatter {
    companion object {
        fun makeFormatter(): DateTimeFormatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
    }
}
