package net.top.repo.utilities

object Constants {

        var RUN_ON_SERVER = 1 //Prod Server = 1, Stage Server = 2, Dev Server = any number except 1 and 2

        lateinit var BASE_URL : String
        var devServerLink = "https://api.github.com/"
        var stageServerLink = "https://api.github.com/"
        var prodServerLink = "https://api.github.com/"

        const val REQUET_TIMEOUT_DURATION = 10
        const val DEBUG = true
        const val USERNAME = "masum1161"
        const val PASSWORD = "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"


        fun main() {
            if(RUN_ON_SERVER==1){
                BASE_URL = prodServerLink


            }
            else if(RUN_ON_SERVER==2){
                BASE_URL = stageServerLink


            }
            else{
                BASE_URL = devServerLink

            }
        }

    }