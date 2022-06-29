package com.rezaharisz.competencytest.helper

import com.rezaharisz.competencytest.utils.Event

data class EventClickListener(val clickListener: (data: Event) -> Unit)
