package br.com.prumoapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform