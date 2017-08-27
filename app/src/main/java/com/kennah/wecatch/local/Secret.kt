package com.kennah.wecatch.local

class Secret {

    companion object {

        init {
            System.loadLibrary("secret_lib")
        }

        external fun wcm(): String
        external fun wce(): String
        external fun wca(): String
    }
}