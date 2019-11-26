package com.cyberspace.cyberpaysdk.data.transaction.remote.response

import java.net.URLEncoder

internal class CardTransaction {
    var reference = ""
    var status = ""
    var redirectUrl = ""
    var message = ""
    var reason = ""
    var responseAction = ""

    //processACS Parameters
    var eciFlag : String? = ""

    var md : String? = ""
        get() {
            return URLEncoder.encode(field, "UTF-8")
        }

    var acsUrl : String? = ""

    var termUrl : String? = ""
        get() {
            return URLEncoder.encode(field, "UTF-8")
        }

    var paReq : String? = ""
        get() {
            return URLEncoder.encode(field, "UTF-8")
        }

    var responseCode : String? = ""
    var amount : Double? = null
    var paymentId : String? = null

}