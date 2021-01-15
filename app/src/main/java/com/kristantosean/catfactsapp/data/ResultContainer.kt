package com.kristantosean.catfactsapp.data

import java.lang.Exception

data class ResultContainer<T>(val data: T?, val error: Exception?)