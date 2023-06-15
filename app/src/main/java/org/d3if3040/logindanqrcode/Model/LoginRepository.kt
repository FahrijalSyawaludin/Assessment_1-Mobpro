package org.d3if3040.logindanqrcode.Model

import android.content.Context

class LoginRepository(context: Context) {
    private val db: DataBaseHeleperLogin =
        DataBaseHeleperLogin(context)

    fun checkLogin(username: String, password: String): Boolean {
        return db.checkLogin(username, password)
    }

    fun upgradeSession(session: String, value: Int): Boolean {
        return db.upgradeSession(session, value)
    }
}