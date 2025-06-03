package com.muhasib.aliascreation.model

data class UserDetails(
    var userId : Int,
    var userName : String? = null,
    var isActive : Boolean? = null,
    var mobile : String? = null,
    var mailId : String? = null,
    var imageLink : String? = null,
    var fcmToken : String? = null,
    var deviceId : String? = null,
    var iDeviceId : String? = null,
    var recoveryMobile : String? = null,
    var recoveryMail : String? = null,
    var isMobileVerified : Boolean? = null,
    var isMailVerified : Boolean? = null
)
