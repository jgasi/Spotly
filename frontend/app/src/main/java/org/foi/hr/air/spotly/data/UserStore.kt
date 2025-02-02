package org.foi.hr.air.spotly.data

object UserStore {
    private var currentUser : User? = null

    fun setUser(userData: User) {
        currentUser = userData
    }

    fun getUser(): User? = currentUser

    fun clearUser() {
        currentUser = null
    }
}