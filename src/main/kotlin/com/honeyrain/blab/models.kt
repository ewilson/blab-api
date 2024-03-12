package com.honeyrain.blab

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("BLABS")
data class Blab(@Id var id: String?, val title: String, val userId: String)

@Table("USERS")
data class User(@Id var id: String?, val username: String)
