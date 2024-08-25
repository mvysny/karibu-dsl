package com.github.mvysny.karibudsl.v10

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


/**
 * Serializes the object to a byte array
 * @return the byte array containing this object serialized form.
 */
public fun Serializable?.serializeToBytes(): ByteArray = ByteArrayOutputStream().also { ObjectOutputStream(it).writeObject(this) }.toByteArray()

public inline fun <reified T: Serializable> ByteArray.deserialize(): T? = T::class.java.cast(
    ObjectInputStream(inputStream()).readObject())

/**
 * Clones this object by serialization and returns the deserialized clone.
 * @return the clone of this
 */
public fun <T : Serializable> T.cloneBySerialization(): T = javaClass.cast(serializeToBytes().deserialize())
